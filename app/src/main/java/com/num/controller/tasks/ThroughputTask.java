package com.num.controller.tasks;

import android.content.Context;
import android.os.Handler;

import edu.berkeley.icsi.netalyzr.tests.NetProbeStats;
import edu.berkeley.icsi.netalyzr.tests.connectivity.BandwidthTestTCP;

public class ThroughputTask implements Runnable {

    private Context context;
    private BandwidthTestTCP bwt;
    private NetProbeStats nps;
    private Handler handler;

    public ThroughputTask(Context context, BandwidthTestTCP bwt, Handler handler) {
        this.context = context;
        this.bwt = bwt;
        this.handler = handler;
    }

    public ThroughputTask(Context context, NetProbeStats nps, Handler handler) {
        this.context = context;
        this.nps = nps;
        this.handler = handler;
    }

    @Override
    public void run() {
        if(bwt != null) {
            bwt.run();
        }
        else if(nps != null) {
            nps.run();
        }
    }
}
