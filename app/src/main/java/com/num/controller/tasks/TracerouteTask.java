package com.num.controller.tasks;

import android.os.Handler;

import com.num.Constants;
import com.num.controller.utils.TracerouteUtil;
import com.num.model.Traceroute;

import java.util.HashMap;

// TODO Handle interrupts
public class TracerouteTask implements Runnable {

    private String address;
    private Handler handler;
    private int type;

    public TracerouteTask(String address, int type, Handler handler) {
        this.address = address;
        this.handler = handler;
        this.type = type;
    }

    @Override
    public void run(){
        HashMap<String, String> params = new HashMap<>();
        params.put("-n","");
        params.put("-m", "" + Constants.MAX_HOP);
        params.put("-q", "" + Constants.TRACE_COUNT);
        params.put("-p", "" + Constants.TRACE_PORT);
        if(type == Traceroute.ICMP) {
            params.put("-I", "");
        }
        TracerouteUtil.traceroute(address, params, handler);
    }
}
