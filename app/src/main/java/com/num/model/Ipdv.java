package com.num.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Ipdv implements BaseModel {

    private List<IpdvUnit> ipdvList;

    public Ipdv() {
        ipdvList = new ArrayList<>();
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            JSONArray array = new JSONArray();
            for(IpdvUnit entry : ipdvList) {
                array.put(entry.toJSON());
            }
            json.put("ipdvlist", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private class IpdvUnit implements BaseModel {
        private int sequenceNumber;
        private long ipdv;

        public IpdvUnit(int sequenceNumber, long ipdv) {
            this.sequenceNumber = sequenceNumber;
            this.ipdv = ipdv;
        }

        @Override
        public JSONObject toJSON() {
            JSONObject json = new JSONObject();
            try {
                json.putOpt("sequence", sequenceNumber);
                json.putOpt("ipdv", ipdv);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
