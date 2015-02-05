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

public class AlarmReceiver extends BroadcastReceiver {

    final static String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent arg1) {
        try {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
            wakeLock.acquire();

            DataUsageUtil.updateMobileData(context);

            // run only when background_service is set to run (true)
            SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
            if (prefs.contains("background_service") && prefs.getBoolean("background_service", false)) {
                Log.d(TAG, "background service");
                MeasurementManager manager = new MeasurementManager(context);
                manager.execute();
                try {
                    Thread.sleep(Constants.SHORT_SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            wakeLock.release();
        }catch(Exception e){
            Log.d(TAG, "onReceive");
        }
    }

    public void setAlarm(Context context)
    {
        AlarmManager manager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Constants.UPDATE_INTERVAL, pendingIntent);
    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
    }


}
