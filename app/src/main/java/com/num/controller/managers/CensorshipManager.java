package com.num.controller.managers;

import android.content.Context;
import android.os.Handler;

import com.num.Constants;
import com.num.controller.tasks.LatencyTask;
import com.num.controller.utils.PingUtil;
import com.num.controller.utils.PythonUtil;
import com.num.model.Address;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CensorshipManager {
    private static ThreadPoolExecutor censorshipThreadPool;
    private static BlockingQueue<Runnable> workQueue;
    private Handler handler;

    public CensorshipManager(Context context, Handler handler) {
        workQueue = new LinkedBlockingQueue<>();
        censorshipThreadPool = new ThreadPoolExecutor(Constants.CORE_POOL_SIZE,
                Constants.MAX_POOL_SIZE, Constants.KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue);
        this.handler = handler;
        if(!PythonUtil.isInstalled()) {
            PythonUtil.installExecutable(context);
        }
    }

    public void execute() {
    }
}
