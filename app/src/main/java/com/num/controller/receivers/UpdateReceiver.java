package com.num.controller.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.num.controller.utils.BootstrapUtil;

public class UpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
            if(intent.getData().getSchemeSpecificPart().equals(context.getPackageName())) {
                BootstrapUtil.bootstrap(context);
            }
        }
        else if(Intent.ACTION_MY_PACKAGE_REPLACED.equals(intent.getAction())) {
            BootstrapUtil.bootstrap(context);
        }
    }
}
