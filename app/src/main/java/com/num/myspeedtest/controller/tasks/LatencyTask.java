package com.num.myspeedtest.controller.tasks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.num.myspeedtest.controller.managers.LatencyManager;
import com.num.myspeedtest.controller.utils.PingUtil;
import com.num.myspeedtest.model.Address;
import com.num.myspeedtest.model.Ping;
import com.num.myspeedtest.view.activities.LatencyActivity;

public class LatencyTask implements Runnable {

    private Address target;
    private Handler handler;

    public LatencyTask(Address target, Handler handler) {
        this.target = target;
        this.handler = handler;
    }

    @Override
    public void run() {
        Ping ping = PingUtil.pingICMP(target, 15);
        Bundle bundle = new Bundle();
        bundle.putParcelable("ping", ping);
        Message msg = new Message();
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
}
