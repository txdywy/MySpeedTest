package com.num.myspeedtest.models;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Traceroute implements BaseModel {

    public static final int MaxHop = 20;
    private List<TracerouteEntry> traceroutelist = new ArrayList<TracerouteEntry>();
    private int startindex, endindex;

    public Traceroute(int startindex, int endindex) {
        this.startindex = startindex;
        this.endindex = endindex;
    }

    public Traceroute(int startindex) {
        this(startindex,0);
    }

//    public List<TracerouteEntry> getTraceroutelist() {
//        return traceroutelist;
//    }

    public void addToList(TracerouteEntry trace_entry) {
//        endindex++;
        traceroutelist.add(trace_entry);
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();

        try {
            JSONArray array = new JSONArray();
            for(TracerouteEntry entry : traceroutelist) {
                array.put(entry.toJSON());
            }
            obj.put("entries", array);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return obj;
    }

    public List<TracerouteEntry> getDisplayData() {

        Collections.sort(traceroutelist);

        /* Check if there is multiple entries for the last one */
        if(traceroutelist.size()==MaxHop){
            String address = traceroutelist.get(MaxHop-1).getIpAddr();
            for (int i=MaxHop-2; i>0; i--){
                if(address.equals(traceroutelist.get(i).getIpAddr())){
                    traceroutelist.remove(i+1);
                }
            }
        }



        return traceroutelist;

    }

}
