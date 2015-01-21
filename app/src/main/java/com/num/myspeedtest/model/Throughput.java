package com.num.myspeedtest.model;

import org.json.JSONObject;

public class Throughput implements BaseModel{

    private String download;
    private String upload;
    private String datetime;
    private String connectionType;

    private Link downLink;
    private Link upLink;
    private boolean isComplete = false;

    public Throughput() {
        Link downLink = new Link();
        Link upLink = new Link();
    }

    public Throughput(String datetime, String download, String upload){
        this.datetime = datetime;
//        this.connectionType = connectionType;
        this.download = download;
        this.upload = upload;
    }

    public String getDownload(){ return download; }
    public void setDownload(String download){ this.download = download; }

    public String getUpload(){ return upload; }
    public void setUpload(String upload){ this.upload = upload; }

    public String getDatetime(){ return datetime; }
    public void setDatetime(String datetime){ this.datetime = datetime; }

    public String getConnectionType(){ return connectionType; }
    public void setConnectionType(String connectionType){ this.connectionType = connectionType; }

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