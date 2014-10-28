package com.num.myspeedtest.models;

/**
 * Created by Andrew on 10/1/2014.
 */
public class Address {
    private String ip;
    private String tagname;
    private String type;

    public Address(String ip, String tagname, String type) {
        this.ip = ip;
        this.tagname = tagname;
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
