package com.num.myspeedtest.controller.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.num.myspeedtest.controller.utils.DataUsageUtil;
import com.num.myspeedtest.controller.utils.Logger;
import com.num.myspeedtest.db.datasource.DataUsageDataSource;
import com.num.myspeedtest.model.Application;

import java.util.List;

public class DataUsageService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.show("Starting data Usage service");
        if(!DataUsageUtil.isInitialized()) {
            DataUsageUtil.updateOnBoot(this);
        }
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }


}
