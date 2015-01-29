package com.num.myspeedtest.controller.managers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.num.myspeedtest.Constants;
import com.num.myspeedtest.controller.tasks.LatencyTask;
import com.num.myspeedtest.model.Address;
import com.num.myspeedtest.model.LastMile;
import com.num.myspeedtest.model.Ping;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LatencyManager {

    private static ThreadPoolExecutor latencyThreadPool;
    private static BlockingQueue<Runnable> workQueue;
    private int count;
    private Handler handler;

    public LatencyManager(Handler handler) {
        workQueue = new LinkedBlockingQueue<>();
        latencyThreadPool = new ThreadPoolExecutor(Constants.CORE_POOL_SIZE,
                Constants.MAX_POOL_SIZE, Constants.KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue);
        this.handler = handler;
    }

    public void execute(List<Address> targets) {
        for(Address dst : targets) {
            LatencyTask task = new LatencyTask(dst, handler);
            latencyThreadPool.execute(task);
        }
    }
}
