package com.num.myspeedtest.models;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Andrew on 9/24/2014.
 */
public class Ping implements BaseModel {
    private String srcIP;
    private Address dstIP;
    private String time;
    private Measure measure;

    public Ping(String srcIP, Address dstIP, Measure measure) {
        this.srcIP = srcIP;
        this.dstIP = dstIP;
        this.measure = measure;
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.time = sdf.format(new Date());
    }

    public String getSrcIP() {
        return srcIP;
    }

    public void setSrcIP(String srcIP) {
        this.srcIP = srcIP;
    }

    public Address getDstIP() {
        return dstIP;
    }

    public void setDstIP(Address dstIP) {
        this.dstIP = dstIP;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Measure getMeasure() {
        return measure;
    }

    public void setMeasure(Measure measure) {
        this.measure = measure;
    }

    @Override
    public JSONObject toJSON() {
        return null;
    }
}
