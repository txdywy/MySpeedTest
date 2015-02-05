package com.num.model;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.num.controller.utils.HashUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class Sim implements BaseModel {
    private String simNetworkCountry, simState, simOperatorName, simOperatorCode,
            simSerialNumber;

    public Sim(Context context) {
        String serviceName = Context.TELEPHONY_SERVICE;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(serviceName);

        int state = tm.getSimState();
        switch (state) {
            case (TelephonyManager.SIM_STATE_ABSENT):
                simState = "ABSENT";
                break;
            case (TelephonyManager.SIM_STATE_NETWORK_LOCKED):
                simState = "NETWORK_LOCKED";
                break;
            case (TelephonyManager.SIM_STATE_PIN_REQUIRED):
                simState = "PIN_REQUIRED";
                break;
            case (TelephonyManager.SIM_STATE_PUK_REQUIRED):
                simState = "PUK_REQUIRED";
                break;
            case (TelephonyManager.SIM_STATE_UNKNOWN):
                simState = "UNKNOWN";
                break;
            case (TelephonyManager.SIM_STATE_READY):
                simState = "READY";
                simNetworkCountry = tm.getSimCountryIso();
                simOperatorCode = tm.getSimOperator();
                simOperatorName = tm.getSimOperatorName();
                simSerialNumber = tm.getSimSerialNumber();
                break;
        }
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.putOpt("networkCountry", simNetworkCountry);
            json.putOpt("state", simState);
            json.putOpt("operatorName", simOperatorName);
            json.putOpt("operatorCode", simOperatorCode);
            json.putOpt("serialNumber",  HashUtil.SHA1(simSerialNumber));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
