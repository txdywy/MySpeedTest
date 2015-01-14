package com.num.myspeedtest.model;

import org.json.JSONObject;

public class Throughput implements BaseModel{

    private Link downLink;
    private Link upLink;
    private boolean isComplete = false;

    public Throughput() {
        Link downLink = new Link();
        Link upLink = new Link();

    }

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