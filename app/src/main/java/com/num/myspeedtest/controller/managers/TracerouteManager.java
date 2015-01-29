package com.num.myspeedtest.controller.managers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.num.myspeedtest.Constants;
import com.num.myspeedtest.controller.tasks.TracerouteTask;
import com.num.myspeedtest.model.Traceroute;

import java.util.ArrayList;
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

    public void execute(String address) {
        TracerouteTask task = new TracerouteTask(address, handler);
        tracerouteThreadPool.execute(task);
    }

    private boolean isDone() {
        return false;
    }

}
