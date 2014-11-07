package com.num.myspeedtest.models;

/**
 * Model for IP address
 */
public class Address {
    private String mIP;
    private String mTagName;
    private String mType;

    public Address(String ip, String tagName, String type) {
        mIP = ip;
        mTagName = tagName;
        mType = type;
    }

    public String getIp() {
        return mIP;
    }

    public void setIp(String ip) {
        mIP = ip;
    }

    public String getTagName() {
        return mTagName;
    }

    public void setTagName(String tagName) {
        mTagName = tagName;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }
}
