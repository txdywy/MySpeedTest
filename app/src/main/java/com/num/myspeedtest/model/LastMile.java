package com.num.myspeedtest.model;

import org.json.JSONException;
import org.json.JSONObject;

public class LastMile extends Ping {

    private int hopCount;
    private String firstHop;

    public LastMile(String src, Address dst, Measure measure, int hopCount, String firstHop) {
        super(src,dst,measure);
        this.hopCount = hopCount;
        this.firstHop = firstHop;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        try {
            json.putOpt("hopcount", hopCount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
