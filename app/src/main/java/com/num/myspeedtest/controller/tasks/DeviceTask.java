package com.num.myspeedtest.controller.tasks;

import android.os.Handler;

public class DeviceTask implements Runnable {

    private Handler handler;

    public DeviceTask(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {

    }
}
