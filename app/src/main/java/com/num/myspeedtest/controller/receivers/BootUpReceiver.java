package com.num.myspeedtest.controller.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.num.myspeedtest.Constants;
import com.num.myspeedtest.controller.services.BackgroundService;
import com.num.myspeedtest.controller.services.DataUsageService;
import com.num.myspeedtest.controller.utils.DataUsageUtil;
import com.num.myspeedtest.controller.utils.DeviceUtil;
import com.num.myspeedtest.db.datasource.DataUsageDataSource;

public class BootUpReceiver extends BroadcastReceiver {

    final static String TAG = "BootUpReceiver";
    private AlarmReceiver alarm = new AlarmReceiver();
    private MonthlyResetAlarmReceiver monthlyAlarm = new MonthlyResetAlarmReceiver();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            context.startService(new Intent(context, BackgroundService.class));
            alarm.setAlarm(context);
        }

        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        //monthly alarm resets the data usage table
//        if(prefs.getInt(Constants.NEXT_MONTHLY_RESET, 0)== (new DeviceUtil().getCurrentMonth())){
            //reset usage data
            DataUsageUtil.resetMobileData(context);
            DataUsageUtil.clearTable(context);
            DataUsageUtil.setFirstMonthOfTheMonthFlag(context, new DeviceUtil().getNextMonth());
            monthlyAlarm.setAlarm(context);
//        }else{
//            monthlyAlarm.setAlarm(context);
//        }

        System.out.println("Monthly boot up receiver");

        context.startService(new Intent(context, DataUsageService.class));
    }
}
