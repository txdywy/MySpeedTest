package com.num.myspeedtest.model;

import org.json.JSONException;
import org.json.JSONObject;

public class State implements BaseModel {

    private String cellId = "";
    private String time = "";
    private String localTime = "";
    private String deviceId = "";
    private String networkType = "";

    public State(String deviceId, String time, String localTime, Device device, Network network) {
        this.time = time;
        this.localTime = localTime;
        this.deviceId = deviceId;
        networkType = network.getConnectionType();
        if(networkType.contains("Mobile")) {
            cellId = network.getCellId();
        }
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.putOpt("cellId", cellId);
            json.putOpt("time", time);
            json.putOpt("localtime", localTime);
            json.putOpt("deviceid", deviceId);
            json.putOpt("networkType", networkType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
