package com.num.myspeedtest.controller.tasks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.num.myspeedtest.controller.managers.LatencyManager;
import com.num.myspeedtest.controller.utils.PingUtil;
import com.num.myspeedtest.model.Address;
import com.num.myspeedtest.model.LastMile;
import com.num.myspeedtest.model.Ping;
import com.num.myspeedtest.view.activities.LatencyActivity;

import java.util.HashMap;

public class LatencyTask implements Runnable {

    private Address target;
    private Handler handler;

    public LatencyTask(Address target, Handler handler) {
        this.target = target;
        this.handler = handler;
    }

    @Override
    public void run() {
        String type = target.getType();
        HashMap<String, String> params = new HashMap<>();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        Message msg = new Message();

        if(type.equals("ping")) {
            params.put("-c", "15");
            params.put("-i", "0.5");
            Ping ping = PingUtil.ping(target, params);
            bundle.putParcelable("ping", ping);
        }
        else if(type.equals("firsthop")) {
            params.put("-n","");
            params.put("-s", ""+56);
            params.put("-c", ""+1);
            LastMile lastMile = PingUtil.lastMile(target, params);
            bundle.putParcelable("lastmile", lastMile);
        }
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
}
