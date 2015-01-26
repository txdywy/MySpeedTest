package com.num.myspeedtest.model;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Measurement implements BaseModel {
    private List<Ping> pings;
    private Device device;
    private Network network;
    private Sim sim;
    private Throughput throughput;
    private Loss loss;
    private Ipdv ipdv;
    private WarmupExperiment warmupExperiment;
    private boolean isComplete;
    private ArrayList<Screen> screens;
    private boolean isManual;

    public Measurement(boolean isManual) {
        isComplete = false;
        this.isManual = isManual;
        this.pings = pings;
        screens = new ArrayList<>();
    }


    @Override
    public JSONObject toJSON() {
        return null;
    }
}
