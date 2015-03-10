package com.num.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONObject;
import org.json.JSONException;

/**
 * Model for IP address
 */
public class Address implements BaseModel, Parcelable {
    private String ip;
    private String tagName;

    public Address(String ip, String tagName) {
        this.ip = ip;
        this.tagName = tagName;
    }

    public String getIp() {
        return ip;
    }

    public String getTagName() {
        return tagName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ip);
        dest.writeString(tagName);
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    private Address(Parcel source) {
        ip = source.readString();
        tagName = source.readString();
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        try {
            obj.putOpt("ip",ip);
            obj.putOpt("tagName",tagName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
