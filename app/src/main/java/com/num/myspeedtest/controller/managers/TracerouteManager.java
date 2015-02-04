package com.num.myspeedtest.controller.managers;

import android.os.Handler;

import com.num.myspeedtest.Constants;
import com.num.myspeedtest.controller.tasks.TracerouteTask;
import com.num.myspeedtest.model.Address;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TracerouteManager {

    private static ThreadPoolExecutor tracerouteThreadPool;
    private static BlockingQueue<Runnable> workQueue;

    private Handler handler;

    public TracerouteManager(Handler handler){
        workQueue = new LinkedBlockingQueue<>();
        tracerouteThreadPool = new ThreadPoolExecutor(Constants.CORE_POOL_SIZE,
                Constants.MAX_POOL_SIZE, Constants.KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue);
        this.handler = handler;
    }

    public void execute(List<Address> address, int type) {
        for(Address a : address) {
            TracerouteTask task = new TracerouteTask(a.getIp(), type, handler);
            tracerouteThreadPool.execute(task);
        }
    }

    public void execute(String address, int type) {
        TracerouteTask task = new TracerouteTask(address, type, handler);
        tracerouteThreadPool.execute(task);
    }
}
