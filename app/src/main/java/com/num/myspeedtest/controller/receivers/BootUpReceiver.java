package com.num.myspeedtest.controller.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.num.myspeedtest.controller.helpers.Logger;
import com.num.myspeedtest.controller.services.DataUsageService;

/**
 * Created by miseonpark on 12/16/14.
 * Modified by Joseph H. Lee on 28/01/15.
 */
public class BootUpReceiver extends BroadcastReceiver {

    final static String TAG = "BootUpReceiver";

    @Override
    public void onReceive(Context context, Intent arg1) {
        // place the service to start at boot here
        context.startService(new Intent(context, DataUsageService.class));
    }
}
