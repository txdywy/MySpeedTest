package com.num.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Loss implements BaseModel, Parcelable {

    private int total, lost;
    private double lossPercentage;

    private Loss(int total, int lost, double lossPercentage) {
        this.total = total;
        this.lost = lost;
        this.lossPercentage = lossPercentage;
    }
    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.putOpt("total", total);
            json.putOpt("lost", lost);
            json.putOpt("losspercentage", lossPercentage);
        } catch (JSONException e) {
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

    }
}
