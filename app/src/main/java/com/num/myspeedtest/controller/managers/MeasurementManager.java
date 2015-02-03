package com.num.myspeedtest.controller.managers;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;

import com.num.myspeedtest.Constants;
import com.num.myspeedtest.controller.tasks.MeasurementTask;
import com.num.myspeedtest.controller.utils.Logger;
import com.num.myspeedtest.controller.utils.ServerUtil;
import com.num.myspeedtest.model.Loss;
import com.num.myspeedtest.model.Measurement;
import com.num.myspeedtest.model.Ping;
import com.num.myspeedtest.model.Usage;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MeasurementManager {
    private static ThreadPoolExecutor measurementThreadPool;
    private static BlockingQueue<Runnable> workQueue;
    private Context context;
    private Measurement measurement;


    public MeasurementManager(Context context) {
        workQueue = new LinkedBlockingQueue<>();
        measurementThreadPool = new ThreadPoolExecutor(Constants.CORE_POOL_SIZE,
                Constants.MAX_POOL_SIZE, Constants.KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue);
        this.context = context;
    }

    public void execute(boolean isManual) {
//        MeasurementTask task = new MeasurementTask(context, isManual, parentHandler);
//        measurementThreadPool.execute(task);
//        measurement = new Measurement(context, isManual);
//        Logger.show(measurement.toJSON().toString());
//        LatencyManager latencyManager = new LatencyManager(managerHandler);
//        latencyManager.execute(ServerUtil.getTargets());

//        LatencyManager latencyManager = new LatencyManager(managerHandler);
//        latencyManager.execute(ServerUtil.getTargets());
//
//        DataUsageManager dataUsageManager = new DataUsageManager(managerHandler);
//        dataUsageManager.execute(context);
    }

    public void sendMeasurement(Measurement measurement) {
        Logger.show(measurement.toJSON().toString());
    }

}