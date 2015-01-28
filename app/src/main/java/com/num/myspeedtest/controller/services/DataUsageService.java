package com.num.myspeedtest.controller.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.num.myspeedtest.controller.helpers.Logger;
import com.num.myspeedtest.controller.managers.DataUsageManager;

/**
 * Created by joseph on 1/28/15.
 */
public class DataUsageService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DataUsageManager.updateOnBoot(this);
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }
}
