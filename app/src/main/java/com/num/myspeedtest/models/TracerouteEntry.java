package com.num.myspeedtest.models;

import org.json.JSONException;
import org.json.JSONObject;

public class TracerouteEntry implements BaseModel, Comparable<TracerouteEntry>{

    String hostname = "";
    String ipAddr = "";
    String rtt = "";
    int hopnumber = -1;

    public TracerouteEntry(String ipAddr, String hostname, String rtt, int hopnumber){
        this.ipAddr = ipAddr;
        this.rtt = rtt + " ms";
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
}
