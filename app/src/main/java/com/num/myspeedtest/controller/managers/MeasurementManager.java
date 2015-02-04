package com.num.myspeedtest.controller.managers;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.num.myspeedtest.Constants;
import com.num.myspeedtest.controller.utils.ServerUtil;
import com.num.myspeedtest.model.Hop;
import com.num.myspeedtest.model.Measurement;
import com.num.myspeedtest.model.Ping;
import com.num.myspeedtest.model.Traceroute;
import com.num.myspeedtest.model.Usage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MeasurementManager {
    private static ThreadPoolExecutor measurementThreadPool;
    private static BlockingQueue<Runnable> workQueue;
    private Context context;
    private int count;
    private List<Ping> pings;
    private List<Traceroute> traceroutes;
    private HashMap<String,List<Hop>> traceMap;
    private Usage usage;
    private Measurement measurement;

    public MeasurementManager(Context context) {
        workQueue = new LinkedBlockingQueue<>();
        measurementThreadPool = new ThreadPoolExecutor(Constants.CORE_POOL_SIZE,
                Constants.MAX_POOL_SIZE, Constants.KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue);
        this.context = context;
        pings = new ArrayList<>();
        traceroutes = new ArrayList<>();
        traceMap = new HashMap<>();
    }

    public void execute() {
        measurement = new Measurement(context, false);
        count = ServerUtil.getTargets().size();

        LatencyHandler latencyHandler = new LatencyHandler();
        LatencyManager latencyManager = new LatencyManager(latencyHandler);
        latencyManager.execute(ServerUtil.getTargets());

        UsageHandler usageHandler = new UsageHandler();
        DataUsageManager dataUsageManager = new DataUsageManager(usageHandler);
        dataUsageManager.execute(context);

        TracerouteHandler tracerouteHandler = new TracerouteHandler();
        TracerouteManager tracerouteManager = new TracerouteManager(tracerouteHandler);
        tracerouteManager.execute(ServerUtil.getTargets(), Traceroute.ICMP);
        tracerouteManager.execute(ServerUtil.getTargets(), Traceroute.UDP);
    }

    public void sendMeasurement(final Measurement measurement) {
        int maxLogStringSize = 1000;
        String veryLongString = measurement.toJSON().toString();
        for(int i = 0; i <= veryLongString.length() / maxLogStringSize; i++) {
            int start = i * maxLogStringSize;
            int end = (i+1) * maxLogStringSize;
            end = end > veryLongString.length() ? veryLongString.length() : end;
            Log.d("DEBUG", veryLongString.substring(start, end));
        }
        Runnable task = new Runnable() {
            @Override
            public void run() {
                ServerUtil.sendMeasurement(measurement);
            }
        };
        measurementThreadPool.execute(task);
    }

    private class LatencyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            String type = msg.getData().getString("type");
            Ping ping = msg.getData().getParcelable("ping");
            pings.add(ping);
            onComplete();
        }
    }

    private class TracerouteHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            String srcIp = msg.getData().getString("srcIp");
            String dstIp = msg.getData().getString("dstIp");
            String hostname = msg.getData().getString("hostname");
            Hop hop = msg.getData().getParcelable("hop");

            if(traceMap.containsKey(dstIp)) {
                if(hop != null) {
                    traceMap.get(dstIp).add(hop);
                }
            } else {
                if(hop != null) {
                    List<Hop> hops = new ArrayList<>();
                    hops.add(hop);
                    traceMap.put(dstIp, hops);
                }
            }
            if(msg.getData().getBoolean("isDone")) {
                int traceType = msg.getData().getInt("traceType");
                Traceroute traceroute = new Traceroute(hostname, srcIp, dstIp, traceType);
                traceroute.setHops(traceMap.get(dstIp));
                traceroutes.add(traceroute);
            }
            onComplete();
        }
    }

    private class UsageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            usage = msg.getData().getParcelable("usage");
            onComplete();
        }
    }

    private void onComplete() {
        if(count <= pings.size() && count*2 <= traceroutes.size() && usage != null) {
            measurement.setUsage(usage);
            measurement.setPings(pings);
            measurement.setTraceroutes(traceroutes);
            sendMeasurement(measurement);
        }
    }
}