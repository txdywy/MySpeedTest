package com.num.myspeedtest.controller.managers;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

import com.num.myspeedtest.Constants;
import com.num.myspeedtest.controller.tasks.DataUsageTask;
import com.num.myspeedtest.model.Usage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DataUsageManager {

    private static ThreadPoolExecutor usageThreadPool;
    private static BlockingQueue<Runnable> workQueue;
    private Handler parentHandler;

    public DataUsageManager(Handler handler) {
        workQueue = new LinkedBlockingQueue<>();
        usageThreadPool = new ThreadPoolExecutor(Constants.CORE_POOL_SIZE,
                Constants.MAX_POOL_SIZE, Constants.KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue);
        parentHandler = handler;
    }

    public void execute(Context context) {
        DataUsageTask task = new DataUsageTask(context, parentHandler);
        usageThreadPool.execute(task);
    }

}
