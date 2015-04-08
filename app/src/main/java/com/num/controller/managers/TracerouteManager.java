package com.num.controller.managers;

import android.content.Context;
import android.os.Handler;

import com.num.Constants;
import com.num.controller.tasks.TracerouteTask;
import com.num.controller.utils.TracerouteUtil;
import com.num.model.Address;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Manager class that handles threaded traceroute test
 */
public class  TracerouteManager {

    private static ThreadPoolExecutor tracerouteThreadPool;
    private static BlockingQueue<Runnable> workQueue;
    private Handler handler;

    /**
     * Initialize a work queue and a thread pool.
     * @param handler Parent defined handler to handle data usage
     */
    public TracerouteManager(Context context, Handler handler){
        workQueue = new LinkedBlockingQueue<>();
        tracerouteThreadPool = new ThreadPoolExecutor(Constants.CORE_POOL_SIZE,
                Constants.MAX_POOL_SIZE, Constants.KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue);
        this.handler = handler;
        if(!TracerouteUtil.isInstalled()) {
            TracerouteUtil.installExecutable(context);
        }
    }

    /**
     * Execute traceroute for all defined targets
     * @param address List of destinations for the traceroute test
     * @param type UDP or ICMP version of traceroute
     */
    public void execute(List<Address> address, int type) {
        for(Address a : address) {
            TracerouteTask task = new TracerouteTask(a.getIp(), type, handler);
            tracerouteThreadPool.execute(task);
        }
    }

    /**
     * Execute traceroute for a single target
     * @param address Destination for the traceroute test
     * @param type UDP or ICMP version of traceroute
     */
    public void execute(String address, int type) {
        TracerouteTask task = new TracerouteTask(address, type, handler);
        tracerouteThreadPool.execute(task);
    }
}
