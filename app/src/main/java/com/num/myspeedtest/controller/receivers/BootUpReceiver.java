package com.num.myspeedtest.controller.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.num.myspeedtest.controller.services.BackgroundService;
import com.num.myspeedtest.controller.services.DataUsageService;

public class BootUpReceiver extends BroadcastReceiver {

    final static String TAG = "BootUpReceiver";
    private AlarmReceiver alarm = new AlarmReceiver();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            context.startService(new Intent(context, BackgroundService.class));
            alarm.setAlarm(context);
        }
        context.startService(new Intent(context, DataUsageService.class));
    }
}
