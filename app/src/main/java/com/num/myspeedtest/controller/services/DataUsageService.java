package com.num.myspeedtest.controller.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.num.myspeedtest.controller.managers.DataUsageManager;
import com.num.myspeedtest.controller.utils.DataUsageUtil;
import com.num.myspeedtest.db.datasource.DataUsageDataSource;
import com.num.myspeedtest.model.Application;
import com.num.myspeedtest.model.Usage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataUsageService extends Service {

    private Context context;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this;
        updateOnBoot();
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
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
