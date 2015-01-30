package com.num.myspeedtest.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Traceroute implements BaseModel, Parcelable {

    public static final int ICMP = 0;
    public static final int UDP = 0;

    private String hostname = "";
    private String address = "";
    private String asn = "";
    private double rtt = -1;
    private int hopNumber = -1;

    public Traceroute(String address, String hostname, String asn, double rtt, int hopNumber){
        this.address = address;
        this.asn = asn;
        this.rtt = rtt;
        this.hostname= hostname;
        this.hopNumber = hopNumber;
    }

    public String getHostname() {
        return hostname;
    }
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public double getRtt() {
        return rtt;
    }
    public void setRtt(double rtt) {
        this.rtt = rtt;
    }
    public int getHopNumber() {
        return hopNumber;
    }
    public void setHopNumber(int hopNumber) {
        this.hopNumber = hopNumber;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        try {

            obj.putOpt("address",  address);
            obj.putOpt("asn", asn);
            obj.putOpt("hostname", hostname);
            obj.putOpt("hopNumber", hopNumber);
            obj.putOpt("rtt", rtt);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    public String toString(){
        return "IP Address: " + address + " Hostname: " + hostname + " RTT: " + rtt + " Hop Number: " + hopNumber;
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
        public Traceroute createFromParcel(Parcel source) {
            return new Traceroute(source);
        }

        @Override
        public Traceroute[] newArray(int size) {
            return new Traceroute[size];
        }
    };

    private Traceroute(Parcel source) {
        hostname = source.readString();
        address = source.readString();
        rtt = source.readDouble();
        hopNumber = source.readInt();
    }

}
