package com.num.myspeedtest.controller.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

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
