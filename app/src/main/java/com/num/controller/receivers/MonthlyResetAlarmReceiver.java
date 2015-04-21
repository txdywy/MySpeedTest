package com.num.controller.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.util.Log;

import com.num.Constants;
import com.num.controller.managers.MeasurementManager;
import com.num.controller.utils.DataUsageUtil;
import com.num.controller.utils.DeviceUtil;
import com.num.db.datasource.DataUsageDataSource;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MonthlyResetAlarmReceiver extends BroadcastReceiver {

    final static String TAG = "MonthlyAlarm";

    @Override
    public void onReceive(Context context, Intent arg1) {
        try {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
            wakeLock.acquire();

            SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

            //monthly alarm resets the data usage table
            if(prefs.getInt(Constants.NEXT_MONTHLY_RESET, 0) == (new DeviceUtil().getCurrentMonth())){
                //reset usage data
                DataUsageUtil.resetMobileData(context);
                DataUsageUtil.clearTable(context);
                DataUsageUtil.setFirstMonthOfTheMonthFlag(context, new DeviceUtil().getNextMonth());
                setAlarm(context);
            }

            Log.d(TAG, "Removed Data");

            wakeLock.release();
        }catch(Exception e){
            Log.d(TAG, "onReceive");
        }
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, MonthlyResetAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
    }

    public void setAlarm(Context context) {
        AlarmManager manager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MonthlyResetAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar date = new GregorianCalendar();

        Calendar today = Calendar.getInstance();

        int month = new DeviceUtil().getNextMonth();
        if(month==0) {
            today.add(Calendar.YEAR, 1);
        }

        date.set(Calendar.YEAR, today.get(Calendar.YEAR));
        date.set(Calendar.MONTH, month);
        date.set(Calendar.DAY_OF_MONTH, 1);

        //today.add(Calendar.MINUTE,2);
        //Log.d(TAG, "Setting Next Alarm: " + today.get(Calendar.YEAR) + " " + today.get(Calendar.MONTH) + " " + today.get(Calendar.HOUR) + " " + today.get(Calendar.MINUTE));

        manager.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), pendingIntent);
        //manager.set(AlarmManager.RTC_WAKEUP, today.getTimeInMillis(), pendingIntent);

    }
}
