package com.num.myspeedtest.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.num.myspeedtest.controller.utils.PingUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Traceroute implements BaseModel, Parcelable {

    public static final int UDP = 0;
    public static final int ICMP = 1;

    private String srcIp;
    private String dstIp;
    private String hostname;
    private int type;
    private List<Hop> hops;

    public Traceroute(String hostname, String srcIp, String dstIp, int type) {
        this.hostname = hostname;
        this.srcIp = srcIp;
        this.dstIp = dstIp;
        this.type = type;
    }

    public void setHops(List<Hop> hops) {
        this.hops = hops;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            String t = (type == UDP) ? "UDP":"ICMP";
            json.putOpt("hostname", hostname);
            json.putOpt("srcIp", srcIp);
            json.putOpt("dstIp", dstIp);
            json.putOpt("type", t);
            JSONArray array = new JSONArray();
            for(Hop hop : hops) {
                array.put(hop.toJSON());
            }
            json.putOpt("hops", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(srcIp);
        dest.writeString(dstIp);
        dest.writeInt(type);
    }
}
