package com.num.myspeedtest.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import org.json.JSONObject;

/**
 * Created by Andrew on 10/7/2014.
 */
public class Network implements BaseModel {

    private String networkOperatorId, networkType, connectionType, mobileNetworkInfo,
        wifiState, cellId, cellLac, dataState, dataActivity, signalStrength, cellType,
        baseStationLat, baseStationLong, networkId, systemId;

    public Network(Context c) {
        TelephonyManager tm = (TelephonyManager) c.getSystemService(c.TELEPHONY_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(c.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if(activeNetwork != null) {

        }
    }
    @Override
    public JSONObject toJSON() {
        return null;
    }
}
