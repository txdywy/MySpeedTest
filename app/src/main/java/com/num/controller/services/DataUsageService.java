package com.num.controller.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import com.num.controller.utils.DataUsageUtil;
import com.num.controller.utils.Logger;
import com.num.db.datasource.DataUsageDataSource;
import com.num.model.Application;
import com.num.model.Signal;

import java.util.List;

public class DataUsageService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.show("Starting data Usage service");
        DataUsageUtil.updateOnBoot(this);
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }
}
