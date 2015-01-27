package com.num.myspeedtest.controller.tasks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.num.myspeedtest.controller.utils.PingUtil;
import com.num.myspeedtest.controller.utils.TracerouteUtil;
import com.num.myspeedtest.model.Traceroute;

import java.util.HashMap;

public class TracerouteTask implements Runnable {

    private String address;
    private int index;
    private Handler handler;

    public TracerouteTask(String address, int index, Handler handler){
        this.address = address;
        this.index = index;
        this.handler = handler;
    }

    @Override
    public void run(){
        HashMap<String, String> params = new HashMap<>();
        params.put("-c", "1");
        params.put("-t", index+"");
        Traceroute[] traceroutes = TracerouteUtil.traceroute(address, params);

        Bundle bundle = new Bundle();
        bundle.putString("type", "traceroute");
        bundle.putParcelableArray("traceroute", traceroutes);

        Message msg = new Message();
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
}
