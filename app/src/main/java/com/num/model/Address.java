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
    private String type;

    public Address(String ip, String tagName, String type) {
        this.ip = ip;
        this.tagName = tagName;
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ip);
        dest.writeString(tagName);
        dest.writeString(type);
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
        type = source.readString();
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        try {
            obj.putOpt("ip",ip);
            obj.putOpt("tagName",tagName);
            obj.putOpt("type", type);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
