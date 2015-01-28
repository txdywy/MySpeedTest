package com.num.myspeedtest.controller.tasks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.num.myspeedtest.Constants;
import com.num.myspeedtest.controller.utils.PingUtil;
import com.num.myspeedtest.controller.utils.TracerouteUtil;
import com.num.myspeedtest.model.Traceroute;

import java.util.HashMap;

public class TracerouteTask implements Runnable {

    private String address;
    private Handler handler;

    public TracerouteTask(String address, Handler handler) {
        this.address = address;
        this.handler = handler;
    }

    @Override
    public void run(){
        HashMap<String, String> params = new HashMap<>();
        params.put("-n","");
        params.put("-m", ""+ Constants.MAX_HOP);
        params.put("-q", ""+1);
        params.put("-p", ""+33434);

        TracerouteUtil.traceroute(address, params, handler);
    }
}
