package com.num.myspeedtest.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Hop implements BaseModel, Parcelable {
    private String hostname = "";
    private String address = "";
    private double rtt = -1;
    private int hopNumber = -1;

    public Hop(String address, String hostname, double rtt, int hopNumber){
        this.address = address;
        this.rtt = rtt;
        this.hostname= hostname;
        this.hopNumber = hopNumber;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.putOpt("address", address);
            json.putOpt("hostname", hostname);
            json.putOpt("hopNumber", hopNumber);
            json.putOpt("rtt", rtt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public String getHostname() {
        return hostname;
    }

    public String getAddress() {
        return address;
    }

    public double getRtt() {
        return rtt;
    }

    public int getHopNumber() {
        return hopNumber;
    }

    @Override
    public String toString(){
        return "IP Address: " + address + " Hostname: " + hostname + " RTT: " + rtt + " Hop Number: " +
                hopNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hostname);
        dest.writeString(address);
        dest.writeDouble(rtt);
        dest.writeInt(hopNumber);
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public Hop createFromParcel(Parcel source) {
            return new Hop(source);
        }

        @Override
        public Hop[] newArray(int size) {
            return new Hop[size];
        }
    };

    private Hop(Parcel source) {
        hostname = source.readString();
        address = source.readString();
        rtt = source.readDouble();
        hopNumber = source.readInt();
    }
}
