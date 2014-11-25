package com.num.myspeedtest.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Measure implements BaseModel {
    private double min;
    private double max;
    private double average;
    private double stddev;

    public Measure(double min, double max, double average, double stddev) {
        this.min = min;
        this.max = max;
        this.average = average;
        this.stddev = stddev;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public double getStddev() {
        return stddev;
    }

    public void setStddev(double stddev) {
        this.stddev = stddev;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.putOpt("max", max);
            json.putOpt("min", min);
            json.putOpt("average", average);
            json.putOpt("stddev", stddev);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
