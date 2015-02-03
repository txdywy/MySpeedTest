package com.num.myspeedtest.controller.tasks;

import android.content.Context;
import android.os.Handler;

import com.num.myspeedtest.model.Measurement;

public class MeasurementTask implements Runnable {

    private boolean isManual;
    private Context context;
    private Handler handler;

    public MeasurementTask(Context context, boolean isManual, Handler handler) {
        this.context = context;
        this.isManual = isManual;
        this.handler = handler;
    }

    @Override
    public void run() {
        Measurement measurement = new Measurement(context, isManual);

    }
}
