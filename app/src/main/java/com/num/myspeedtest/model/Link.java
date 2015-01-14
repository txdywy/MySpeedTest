package com.num.myspeedtest.model;

import org.json.JSONObject;

public class Link implements BaseModel{

    private long count;
    private long message_size; //in bytes
    private double time; // milliseconds
    private String dstIp;
    private String dstPort;

    public Link() {
        count = -1;
        message_size = -1;
        time = -1;
        dstIp = "";
        dstPort = "";
    }

    public String showData() {
        return String.format("%.3f Mbps", (double)speedInBits()/1000);
    }

    public String getDstIp() {
        return dstIp;
    }

    public void setDstIp(String dstIp) {
        this.dstIp = dstIp;
    }

    public String getDstPort() {
        return dstPort;
    }

    public void setDstPort(String dstPort) {
        this.dstPort = dstPort;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count2) {
        this.count = count2;
    }

    public long getMessage_size() {
        return message_size;
    }

    public void setMessage_size(long message_size) {
        this.message_size = message_size;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double speedInBytes(){
        return ((double)count*message_size)/time;
    }

    public double speedInBits(){
        return speedInBytes()*8;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        try {
            obj.putOpt("count", count);
            obj.putOpt("message_size",message_size);
            obj.putOpt("time", time);
            obj.putOpt("speedInBits", speedInBits());
            obj.put("dstIp", dstIp);
            obj.put("dstPort", dstPort);
        } catch (Exception e) {
            obj = new JSONObject();
        }

        return obj;
    }
}