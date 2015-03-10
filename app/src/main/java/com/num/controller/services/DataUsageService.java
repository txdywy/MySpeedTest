package com.num.controller.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.num.controller.utils.DataUsageUtil;
import com.num.controller.utils.Logger;

public class DataUsageService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.show("Starting data Usage service");
        DataUsageUtil.updateOnBoot(this);
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
