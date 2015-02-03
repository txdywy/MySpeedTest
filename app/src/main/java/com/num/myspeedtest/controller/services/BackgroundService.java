package com.num.myspeedtest.controller.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import com.num.myspeedtest.controller.utils.Logger;
import com.num.myspeedtest.model.Signal;

public class BackgroundService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        SignalListener listener = new SignalListener();
        tm.listen(listener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class SignalListener extends PhoneStateListener {

        @Override
        public void onSignalStrengthsChanged(SignalStrength signal) {
            super.onSignalStrengthsChanged(signal);
            String sig = "-1";
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
            Signal.signal = sig;
        }
    }

}
