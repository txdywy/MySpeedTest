package com.num.myspeedtest.controller.managers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

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
    private Handler activityHandler;
    private Handler managerHandler;

    public LatencyManager(Handler handler) {
        pingList = new ArrayList<>();
        workQueue = new LinkedBlockingQueue<>();
        latencyThreadPool = new ThreadPoolExecutor(10, 10, 30, TimeUnit.SECONDS, workQueue);
        activityHandler = handler;
        managerHandler = new ManagerHandler();
    }

    /**
     * Ping all targets at once
     * @param targets Targets to ping
     */
    public void execute(List<Address> targets) {
        for(Address dst : targets) {
            LatencyTask task = new LatencyTask(dst, managerHandler);
            latencyThreadPool.execute(task);
        }
    }

    private class ManagerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Ping ping = msg.getData().getParcelable("ping");
            pingList.add(ping);
            Ping[] pingArray = pingList.toArray(new Ping[pingList.size()]);

            Bundle bundle = new Bundle();
            bundle.putParcelableArray("pingArray", pingArray);
            Message activityMsg = new Message();
            activityMsg.setData(bundle);
            activityHandler.sendMessage(activityMsg);
        }
    }
}
