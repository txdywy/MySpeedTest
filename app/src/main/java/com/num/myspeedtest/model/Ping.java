package com.num.myspeedtest.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.json.JSONException;

public class Ping implements BaseModel, Parcelable {
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
        time = sdf.format(new Date());
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
    public JSONObject toJSON(){
        JSONObject obj = new JSONObject();
        try {
            obj.putOpt("srcIP", srcIP);
            obj.putOpt("dstIP", dstIP.getIp());
            obj.putOpt("time", time);
            obj.putOpt("measure", measure.toJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(srcIP);
        dest.writeParcelable(dstIP, 0);
        dest.writeString(time);
        dest.writeParcelable(measure, 0);
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public Ping createFromParcel(Parcel source) {
            return new Ping(source);
        }

        @Override
        public Ping[] newArray(int size) {
            return new Ping[size];
        }
    };

    private Ping(Parcel source) {
        srcIP = source.readString();
        dstIP = source.readParcelable(Address.class.getClassLoader());
        time = source.readString();
        measure = source.readParcelable(Measure.class.getClassLoader());
    }
}
