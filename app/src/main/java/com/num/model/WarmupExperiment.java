package com.num.model;

import com.num.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WarmupExperiment implements BaseModel {

    private double lowest, highest, timeGap;
    private String version;
    private Address address;
    private int totalCount;
    private List<WarmupPing> sequence;

    public WarmupExperiment() {
        lowest = -1;
        highest = -1;
        version = Constants.PING_SEQUENCE_VERSION;
        timeGap = Constants.PING_WARMUP_SEQUENCE_GAP;
        address = Constants.PING_SEQUENCE_ADDRESS;
        totalCount = Constants.PING_SEQUENCE_TOTAL;
        sequence = new ArrayList<>();
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.putOpt("highest", highest);
            json.putOpt("lowest", lowest);
            json.putOpt("version", version);
            json.putOpt("total_count", totalCount);
            json.putOpt("time_gap", timeGap);
            json.putOpt("dstip", address.getIp());
            JSONArray array = new JSONArray();
            for(WarmupPing ping : sequence) {
                array.put(ping.toJSON());
            }
            json.putOpt("sequence", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private class WarmupPing implements BaseModel {

        private double value;
        private int sequenceCount;
        private double period;

        public WarmupPing(double value, int count, double gap) {
            this.value = value;
            this.sequenceCount = count;
            this.period = (count-1)*gap;
        }

        @Override
        public JSONObject toJSON() {
            JSONObject json = new JSONObject();
            try {
                json.putOpt("value", value);
                json.putOpt("sequence", sequence);
                json.putOpt("period", period);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json;
        }
    }
}
