package com.num.controller.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.num.controller.utils.BootstrapUtil;
import com.num.controller.utils.DataUsageUtil;

public class BootUpReceiver extends BroadcastReceiver {

    final static String TAG = "BootUpReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Bootstrapping on boot");
        BootstrapUtil.bootstrap(context);

        // Initialize data usage database
        DataUsageUtil.updateOnBoot(context);
    }
}
