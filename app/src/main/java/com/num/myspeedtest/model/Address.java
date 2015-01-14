package com.num.myspeedtest.model;

/**
 * Model for IP address
 */
public class Address {
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
}
