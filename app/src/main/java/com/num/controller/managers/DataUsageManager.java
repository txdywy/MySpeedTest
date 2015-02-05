package com.num.controller.managers;

import android.content.Context;
import android.os.Handler;

import com.num.Constants;
import com.num.controller.tasks.DataUsageTask;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DataUsageManager {

    private static ThreadPoolExecutor usageThreadPool;
    private static BlockingQueue<Runnable> workQueue;

    private Handler handler;

    public DataUsageManager(Handler handler) {
        workQueue = new LinkedBlockingQueue<>();
        usageThreadPool =
                new ThreadPoolExecutor(Constants.CORE_POOL_SIZE, Constants.MAX_POOL_SIZE, Constants.KEEP_ALIVE_TIME,
                        TimeUnit.SECONDS, workQueue);
        this.handler = handler;
    }

    public void execute(Context context) {
        DataUsageTask task = new DataUsageTask(context, handler);
        usageThreadPool.execute(task);
    }

}
