package com.num.myspeedtest.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Screen implements BaseModel {

    private String time, localtime;
    private boolean on;

    public Screen(String time, String localtime, boolean on) {
        this.time = time;
        this.localtime = localtime;
        this.on = on;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.putOpt("time", time);
            json.putOpt("localtime", localtime);
            if(on) {
                json.putOpt("isOn", 1);
            }
            else {
                json.putOpt("isOn", 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
