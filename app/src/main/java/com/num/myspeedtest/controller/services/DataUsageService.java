package com.num.myspeedtest.controller.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.num.myspeedtest.controller.utils.DataUsageUtil;
import com.num.myspeedtest.db.datasource.DataUsageDataSource;
import com.num.myspeedtest.model.Application;

import java.util.List;

public class DataUsageService extends Service {

    private Context context = this;
    private static boolean initialized = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateOnBoot();
        initialized = true;
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    private void updateOnBoot() {
        DataUsageDataSource db = new DataUsageDataSource(context);
        db.open();
        List<Application> applications = DataUsageUtil.getApplications(context);
        for (Application app : applications) {
            db.updateOnBoot(app);
        }
    }

}
