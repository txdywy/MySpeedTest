package com.num.myspeedtest.controller.managers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.num.myspeedtest.Constants;
import com.num.myspeedtest.controller.tasks.LatencyTask;
import com.num.myspeedtest.model.Address;
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
    private final List<Ping> pingList;
    private int count;
    private Handler parentHandler;
    private Handler managerHandler;

    public LatencyManager(Handler handler) {
        pingList = new ArrayList<>();
        workQueue = new LinkedBlockingQueue<>();
        latencyThreadPool = new ThreadPoolExecutor(Constants.CORE_POOL_SIZE,
                Constants.MAX_POOL_SIZE, Constants.KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue);
        parentHandler = handler;
        managerHandler = new ManagerHandler();
    }

    /**
     * Ping all targets at once
     * @param targets Targets to ping
     */
    public void execute(List<Address> targets) {
        count = targets.size();
        for(Address dst : targets) {
            LatencyTask task = new LatencyTask(dst, managerHandler);
            latencyThreadPool.execute(task);
        }
    }

    private boolean isDone() {
        if(pingList.size() >= count) {
            return true;
        }
        return false;
    }

    private class ManagerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Ping ping = msg.getData().getParcelable("ping");
            pingList.add(ping);
            Ping[] pings = pingList.toArray(new Ping[pingList.size()]);

            Bundle bundle = new Bundle();
            bundle.putParcelableArray("pings", pings);
            bundle.putBoolean("isDone", isDone());
            Message activityMsg = new Message();
            activityMsg.setData(bundle);
            parentHandler.sendMessage(activityMsg);
        }
    }
}
