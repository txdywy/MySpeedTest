package com.num.myspeedtest.model;

import com.num.myspeedtest.controller.managers.ThroughputManager;

import org.json.JSONObject;

public class Throughput implements BaseModel{

    private String datetime;
    private String download;
    private String upload;

    private Link downLink;
    private Link upLink;

    public Throughput(Link downLink, Link upLink, String datetime) {
        this.downLink = downLink;
        this.upLink = upLink;
        this.datetime = datetime;
        this.download = ThroughputManager.outputString(downLink.getSpeedInBytes());
        this.upload = ThroughputManager.outputString(upLink.getSpeedInBytes());
    }

    public Throughput(String datetime, String download, String upload){
        this.datetime = datetime;
        this.download = download;
        this.upload = upload;
    }

    public String getDownload(){ return download; }
    public void setDownload(String download){ this.download = download; }

    public String getUpload(){ return upload; }
    public void setUpload(String upload){ this.upload = upload; }

    public String getDatetime(){ return datetime; }
    public void setDatetime(String datetime){ this.datetime = datetime; }

    public Link getDownLink() {
        return downLink;
    }

    public void setDownLink(Link downLink) {
        this.downLink = downLink;
    }

    public Link getUpLink() {
        return upLink;
    }

    public void setUpLink(Link upLink) {
        this.upLink = upLink;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        try {

            obj.put("downLink", downLink.toJSON());
            obj.put("upLink", upLink.toJSON());

        } catch (Exception e) {
            obj = new JSONObject();
        }

        return obj;
    }
}