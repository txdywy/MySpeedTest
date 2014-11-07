package com.num.myspeedtest.models;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Ping implements BaseModel {
    private String mSrcIP;
    private Address mDstIP;
    private String mTime;
    private Measure mMeasure;

    public Ping(String srcIP, Address dstIP, Measure measure) {
        mSrcIP = srcIP;
        mDstIP = dstIP;
        mMeasure = measure;
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        mTime = sdf.format(new Date());
    }

    public String getSrcIP() {
        return mSrcIP;
    }

    public void setSrcIP(String mSrcIP) {
        this.mSrcIP = mSrcIP;
    }

    public Address getDstIP() {
        return mDstIP;
    }

    public void setDstIP(Address mDstIP) {
        this.mDstIP = mDstIP;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String mTime) {
        this.mTime = mTime;
    }

    public Measure getMeasure() {
        return mMeasure;
    }

    public void setMeasure(Measure mMeasure) {
        this.mMeasure = mMeasure;
    }

    @Override
    public JSONObject toJSON() {
        return null;
    }
}
