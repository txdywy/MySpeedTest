package com.num.myspeedtest.controller.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import com.num.myspeedtest.controller.utils.DataUsageUtil;
import com.num.myspeedtest.controller.utils.Logger;
import com.num.myspeedtest.db.datasource.DataUsageDataSource;
import com.num.myspeedtest.model.Application;
import com.num.myspeedtest.model.Signal;

import java.util.List;

public class DataUsageService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.show("Starting data Usage service");
        DataUsageUtil.updateOnBoot(this);
        SignalListener listener = new SignalListener();
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    private class SignalListener extends PhoneStateListener {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signal) {
            String signalStrength = "-1";
            if (signal != null) {
                if (signal.isGsm()) {
                    signalStrength = "" + signal.getGsmSignalStrength();
                } else if (signal.getCdmaDbm() > -120) {
                    signalStrength = signal.getCdmaDbm() + "dBm ";
                    signalStrength += signal.getCdmaEcio() + "Ec/Io";
                } else if (signal.getEvdoDbm() > -120) {
                    signalStrength = signal.getEvdoDbm() + "dBm ";
                    signalStrength += signal.getEvdoEcio() + "Ec/Io";
                    signalStrength += signal.getEvdoSnr() + "snr";
                }
            }
            Signal.setSignal(signalStrength);
        }
    }
}
