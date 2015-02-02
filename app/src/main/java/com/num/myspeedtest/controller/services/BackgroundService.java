package com.num.myspeedtest.controller.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import com.num.myspeedtest.model.Signal;

public class BackgroundService extends IntentService {

    private Context context;

    public BackgroundService() {
        super(BackgroundService.class.getName());
        System.out.println("Background Service created");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        context = this;
    }


}
