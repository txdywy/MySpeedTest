package com.num.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Measure implements BaseModel, Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(max);
        dest.writeDouble(min);
        dest.writeDouble(average);
        dest.writeDouble(stddev);
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public Measure createFromParcel(Parcel source) {
            return new Measure(source);
        }

        @Override
        public Measure[] newArray(int size) {
            return new Measure[size];
        }
    };

    private Measure(Parcel source) {
        max = source.readDouble();
        min = source.readDouble();
        average = source.readDouble();
        stddev = source.readDouble();
    }
}
