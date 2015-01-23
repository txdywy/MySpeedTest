package com.num.myspeedtest.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Traceroute implements BaseModel, Parcelable {

    private String hostname = "";
    private String ipAddr = "";
    private String rtt = "";
    private int hopnumber = -1;

    public Traceroute(String ipAddr, String hostname, String rtt, int hopnumber){
        this.ipAddr = ipAddr;
        this.rtt = rtt;
        this.hostname= hostname;
        this.hopnumber = hopnumber;
    }

    public String getHostname() {
        return hostname;
    }
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
    public String getIpAddr() {
        return ipAddr;
    }
    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }
    public String getRtt() {
        return rtt;
    }
    public void setRtt(String rtt) {
        this.rtt = rtt;
    }
    public int getHopnumber() {
        return hopnumber;
    }
    public void setHopnumber(int hopnumber) {
        this.hopnumber = hopnumber;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        try {

            obj.putOpt("ipAddr",  ipAddr);
            obj.putOpt("hopnumber", hopnumber);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }

    public int compareTo(TracerouteEntry another) {
        if(this.hopnumber > another.hopnumber) return 1;
        if(this.hopnumber == another.hopnumber) return 0;
        return -1;

    }

    @Override
    public String toString(){
        return "IP Address: " + ipAddr + " Hostname: " + hostname + " RTT: " + rtt + " Hop Number: " + hopnumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hostname);
        dest.writeString(ipAddr);
        dest.writeString(rtt);
        dest.writeInt(hopnumber);
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
        ipAddr = source.readString();
        rtt = source.readString();
        hopnumber = source.readInt();
    }

}
