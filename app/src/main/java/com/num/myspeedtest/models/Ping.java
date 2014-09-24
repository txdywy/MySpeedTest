package com.num.myspeedtest.models;

import org.json.JSONObject;

/**
 * Created by Andrew on 9/24/2014.
 */
public class Ping implements BaseModel {
    private String srcIP;
    private String dstIP;
    private String time;

    @Override
    public JSONObject toJSON() {
        return null;
    }
}
