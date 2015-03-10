package com.num.controller.managers;

import android.content.Context;
import android.os.Handler;

import com.num.Constants;
import com.num.controller.tasks.DataUsageTask;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Manager class that handles threaded data usage retrieval
 */
public class DataUsageManager {

    private static ThreadPoolExecutor usageThreadPool;
    private static BlockingQueue<Runnable> workQueue;

    private Handler handler;

    /**
     * Initialize a work queue and a thread pool.
     * @param handler Parent defined handler to handle data usage
     */
    public DataUsageManager(Handler handler) {
        workQueue = new LinkedBlockingQueue<>();
        usageThreadPool =
                new ThreadPoolExecutor(Constants.CORE_POOL_SIZE, Constants.MAX_POOL_SIZE,
                        Constants.KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue);
        this.handler = handler;
    }

    /**
     * Execute a single task to retrieve data usage
     * @param context Context needed to extract data usage
     */
    public void execute(Context context) {
        DataUsageTask task = new DataUsageTask(context, handler);
        usageThreadPool.execute(task);
    }

}
