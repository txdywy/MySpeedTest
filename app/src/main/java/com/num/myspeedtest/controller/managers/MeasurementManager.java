package com.num.myspeedtest.controller.managers;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.telephony.SignalStrength;

import com.num.myspeedtest.Constants;
import com.num.myspeedtest.controller.tasks.BatteryTask;
import com.num.myspeedtest.controller.tasks.DeviceTask;
import com.num.myspeedtest.controller.tasks.LossTask;
import com.num.myspeedtest.controller.tasks.SignalTask;
import com.num.myspeedtest.controller.utils.ServerUtil;
import com.num.myspeedtest.model.Battery;
import com.num.myspeedtest.model.Device;
import com.num.myspeedtest.model.Loss;
import com.num.myspeedtest.model.Measure;
import com.num.myspeedtest.model.Measurement;
import com.num.myspeedtest.model.Network;
import com.num.myspeedtest.model.Ping;
import com.num.myspeedtest.model.Usage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MeasurementManager {
    private static ThreadPoolExecutor measurementThreadPool;
    private static BlockingQueue<Runnable> workQueue;
    private Handler parentHandler;
    private Handler managerHandler;
    private Measurement measurement;
    private boolean latencyDone;


    public MeasurementManager() {
        workQueue = new LinkedBlockingQueue<>();
        measurementThreadPool = new ThreadPoolExecutor(Constants.CORE_POOL_SIZE,
                Constants.MAX_POOL_SIZE, Constants.KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue);
        //this.parentHandler = handler;
        this.managerHandler = new ManagerHandler();
    }

    public void execute(Context context, boolean isManual) {
        measurement = new Measurement(context, isManual);

        LatencyManager latencyManager = new LatencyManager(managerHandler);
        latencyManager.execute(ServerUtil.getTargets());

        DataUsageManager dataUsageManager = new DataUsageManager(managerHandler);
        dataUsageManager.execute(context);
    }

    public void sendMeasurement(Measurement measurement) {

    }

    private class ManagerHandler extends Handler {
        
        public ManagerHandler() {
            latencyDone = false;
        }

        @Override
        public void handleMessage(Message msg) {
            String type = msg.getData().getString("type");

            switch (type) {
                case "warmup":
                    handleWarmup(msg);
                    break;
                case "usage":
                    handleUsage(msg);
                    break;
                case "loss":
                    handleLoss(msg);
                    break;
                case "latency":
                    handleLatency(msg);
                    break;
            }

        }

    }

    private void handleWarmup(Message msg) {

    }

    private void handleUsage(Message msg) {
        Usage usage = msg.getData().getParcelable("usage");
        measurement.setUsage(usage);
    }

    private void handleLoss(Message msg) {
        Loss loss = msg.getData().getParcelable("loss");
        measurement.setLoss(loss);
    }

    private void handleLatency(Message msg) {
        if(msg.getData().getBoolean("isDone")) {
            Parcelable[] parcelables = msg.getData().getParcelableArray("pings");
            Ping[] pings = Arrays.copyOf(parcelables, parcelables.length, Ping[].class);
            measurement.setPings(Arrays.asList(pings));
        }
    }

}