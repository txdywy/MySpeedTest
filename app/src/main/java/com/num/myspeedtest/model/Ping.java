package com.num.myspeedtest.model;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.json.JSONException;

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
    public JSONObject toJSON(){
        JSONObject obj = new JSONObject();
        try {
            obj.putOpt("mSrcIP", mSrcIP);
            obj.putOpt("mDstIP", mDstIP.getIp());
            obj.putOpt("mTime", mTime);
            obj.putOpt("mMeasure", mMeasure.toJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
