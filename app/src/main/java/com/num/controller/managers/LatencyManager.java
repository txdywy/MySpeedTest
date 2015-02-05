package com.num.controller.managers;

import android.os.Handler;

import com.num.Constants;
import com.num.controller.tasks.LatencyTask;
import com.num.model.Address;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Manager class that handles threaded ping test
 */
public class LatencyManager {

    private static ThreadPoolExecutor latencyThreadPool;
    private static BlockingQueue<Runnable> workQueue;
    private Handler handler;

    /**
     * Initialize a work queue and a thread pool.
     * @param handler Parent defined handler to handle data usage
     */
    public LatencyManager(Handler handler) {
        workQueue = new LinkedBlockingQueue<>();
        latencyThreadPool = new ThreadPoolExecutor(Constants.CORE_POOL_SIZE,
                Constants.MAX_POOL_SIZE, Constants.KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue);
        this.handler = handler;
    }

    /**
     * Execute ping for all defined targets
     * @param targets List of destinations for the ping test
     */
    public void execute(List<Address> targets) {
        for(Address dst : targets) {
            LatencyTask task = new LatencyTask(dst, handler);
            latencyThreadPool.execute(task);
        }
    }
}
