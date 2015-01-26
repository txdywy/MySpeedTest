package com.num.myspeedtest.controller.managers;

import android.content.Context;
import android.os.Handler;

import com.num.myspeedtest.Constants;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MeasurementManager {
    private static ThreadPoolExecutor measurementThreadPool;
    private static BlockingQueue<Runnable> workQueue;
    private Handler serviceHandler;
    private Handler managerHandler;

    public MeasurementManager(Handler handler) {
        workQueue = new LinkedBlockingQueue<>();
        measurementThreadPool = new ThreadPoolExecutor(Constants.CORE_POOL_SIZE,
                Constants.MAX_POOL_SIZE, Constants.KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue);
        this.serviceHandler = handler;
        this.managerHandler = new MeasurementHandler();
    }

    public void execute(Context context) {
    }

    private class MeasurementHandler extends Handler {

    }
}