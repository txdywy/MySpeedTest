package com.num.myspeedtest.controller.tasks;

import android.os.Handler;

import com.num.myspeedtest.Constants;
import com.num.myspeedtest.controller.utils.TracerouteUtil;

import java.util.HashMap;

// TODO Handle interrupts
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
        params.put("-m", "" + Constants.MAX_HOP);
        params.put("-q", "" + Constants.TRACE_COUNT);
        params.put("-p", "" + Constants.TRACE_PORT);

        TracerouteUtil.traceroute(address, params, handler);
    }
}
