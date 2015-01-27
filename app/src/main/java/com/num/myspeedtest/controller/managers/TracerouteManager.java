package com.num.myspeedtest.controller.managers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.num.myspeedtest.Constants;
import com.num.myspeedtest.controller.tasks.TracerouteTask;
import com.num.myspeedtest.model.Traceroute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TracerouteManager {

    private static ThreadPoolExecutor tracerouteThreadPool;
    private static BlockingQueue<Runnable> workQueue;
    private final HashMap<Integer, Traceroute> tracerouteHashMap;

    private Handler parentHandler;
    private Handler managerHandler;


    public TracerouteManager(Handler handler){
        tracerouteHashMap = new HashMap<>();
        workQueue = new LinkedBlockingQueue<>();
        tracerouteThreadPool = new ThreadPoolExecutor(Constants.CORE_POOL_SIZE,
                Constants.MAX_POOL_SIZE, Constants.KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue);
        this.parentHandler = handler;
        this.managerHandler = new ManagerHandler();
    }

    public void execute(String address){
        for(int i=1; i<= Constants.MAX_HOP; i++){
            TracerouteTask task = new TracerouteTask(address, i, managerHandler);
            tracerouteThreadPool.execute(task);
        }
    }

    private boolean isDone() {
        if(tracerouteHashMap.size() >= Constants.MAX_HOP-1) {
            return true;
        }
        return false;
    }

    /* return sorted traceroute array */
    private Traceroute[] convertToArray(){
        List<Traceroute> traceroutes = new ArrayList<>();
        Integer[] hops = tracerouteHashMap.keySet().toArray(new Integer[tracerouteHashMap.size()]);
        Arrays.sort(hops);
        int i = 1;
        int hopCount = 1;
        String prevIp = "";
        for(Integer hop : hops){
            if(hop==1){
                prevIp = tracerouteHashMap.get(hop).getAddress();
                traceroutes.add(tracerouteHashMap.get(hop));
            }else {
                if (hop == i) {
                    String currentIp = tracerouteHashMap.get(hop).getAddress();
                    if ((!prevIp.equals(currentIp)
                            && !currentIp.equals("")) || currentIp.equals("***")) {
                        hopCount++;
                        prevIp = currentIp;
                        Traceroute traceroute = tracerouteHashMap.get(hop);
                        traceroute.setHopNumber(hopCount);
                        traceroutes.add(traceroute);
                    }
                }else{
                    break;
                }
            }
            i++;
        }
        return traceroutes.toArray(new Traceroute[traceroutes.size()]);
    }

    private class ManagerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Traceroute traceroute = msg.getData().getParcelable("traceroute");
            tracerouteHashMap.put(traceroute.getHopNumber(), traceroute);
            Traceroute[] traceroutes = convertToArray();

            Bundle bundle = new Bundle();
            bundle.putParcelableArray("tracerouteArray", traceroutes);
            bundle.putBoolean("isDone", isDone());
            Message activityMsg = new Message();
            activityMsg.setData(bundle);
            parentHandler.sendMessage(activityMsg);
        }
    }

}
