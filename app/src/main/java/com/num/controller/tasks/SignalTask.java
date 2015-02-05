package com.num.controller.tasks;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import com.num.Constants;
import com.num.controller.utils.Logger;

public class SignalTask implements Runnable {

    private Context context;
    private Handler handler;
    private boolean isRunning = true;
    private String signalStrength = "-1";

    public SignalTask(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }
    @Override
    public void run() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(new SignalListener(), PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        while(isRunning) {
            try {
                Thread.sleep(Constants.NORMAL_SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Bundle bundle = new Bundle();
        bundle.putString("signal", signalStrength);
        Message msg = new Message();
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    private class SignalListener extends PhoneStateListener {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signal) {
            String sig = "-1";
            Logger.show("Signal changed!");
            if (signal != null) {
                if (signal.isGsm()) {
                    sig = "" + signal.getGsmSignalStrength();
                } else if (signal.getCdmaDbm() > -120) {
                    sig = signal.getCdmaDbm() + "dBm ";
                    sig += signal.getCdmaEcio() + "Ec/Io";
                } else if (signal.getEvdoDbm() > -120) {
                    sig = signal.getEvdoDbm() + "dBm ";
                    sig += signal.getEvdoEcio() + "Ec/Io";
                    sig += signal.getEvdoSnr() + "snr";
                }
            }
            signalStrength = sig;
            isRunning = false;
        }
    }
}
