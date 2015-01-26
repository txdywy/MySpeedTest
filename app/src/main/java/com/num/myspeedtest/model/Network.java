package com.num.myspeedtest.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import org.json.JSONObject;
import org.json.JSONException;

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
        JSONObject obj = new JSONObject();
        try {
            obj.putOpt("networkOperatorId", networkOperatorId);
            obj.putOpt("networkType", networkType);
            obj.putOpt("connectionType", connectionType);
            obj.putOpt("wifiState", wifiState);
            obj.putOpt("cellType",cellType );
            obj.putOpt("cellId", cellId);
            obj.putOpt("cellLac", cellLac);
            obj.putOpt("mobileNetworkInfo",mobileNetworkInfo );
            obj.putOpt("baseStationLong", baseStationLong);
            obj.putOpt("baseStationLat", baseStationLat);
            obj.putOpt("networkId",networkId );
            obj.putOpt("systemId", systemId);
            obj.putOpt("dataState", dataState);
            obj.putOpt("dataActivity", dataActivity);
            obj.putOpt("signalStrength", signalStrength);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
