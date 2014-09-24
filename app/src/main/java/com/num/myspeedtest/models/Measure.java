package com.num.myspeedtest.models;

import org.json.JSONObject;

/**
 * Created by Andrew on 9/24/2014.
 */
public class Measure implements BaseModel {
    private double min_rtt;
    private double max_rtt;
    private double mean_rtt;
    private double stddev;

    @Override
    public JSONObject toJSON() {
        return null;
    }
}
