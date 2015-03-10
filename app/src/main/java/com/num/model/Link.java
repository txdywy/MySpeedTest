package com.num.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Link implements BaseModel{

    private long count = -1;
    private long messageSize = -1; //in bytes
    private double time = -1; // milliseconds
    private String dstIp = "";
    private String dstPort = "";

    public Link(long count, long messageSize, double time, String dstIp, String dstPort) {
        this.count = count;
        this.messageSize = messageSize;
        this.time = time;
        this.dstIp = dstIp;
        this.dstPort = dstPort;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public long getSpeedInBytes() {
        return messageSize;
    }

    public long getSpeedInBits() {
        return messageSize/8;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        try {
            obj.putOpt("count", count);
            obj.putOpt("message_size",messageSize);
            obj.putOpt("time", time);
            obj.putOpt("speedInBits", messageSize);
            obj.put("dstIp", dstIp);
            obj.put("dstPort", dstPort);
        } catch (JSONException e) {
            obj = new JSONObject();
        }

        return obj;
    }
}