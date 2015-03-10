package com.num.model;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import org.json.JSONException;
import org.json.JSONObject;

public class Battery implements BaseModel {

    private boolean isPresent = false;
    private String technology = "";
    private int plugged = -1;
    private int scale = -1;
    private int health = -1;
    private int voltage = -1;
    private int level = -1;
    private int temperature = -1;
    private int status = -1;

    public Battery(Context context) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, filter);

        isPresent = batteryStatus.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
        technology = batteryStatus.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
        plugged = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        health = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
        voltage = batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
        int rawLevel = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        temperature = batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
        status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, 0);

        if(isPresent) {
            if(rawLevel >= 0 && scale > 0) {
                level = (rawLevel*100)/scale;
            }
        }
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            if(isPresent) {
                json.putOpt("isPresent", 1);
            }
            else {
                json.putOpt("isPresent", 0);
            }
            json.putOpt("technology", technology);
            json.putOpt("plugged", plugged);
            json.putOpt("scale", scale);
            json.putOpt("health", health);
            json.putOpt("voltage", voltage);
            json.putOpt("level", level);
            json.putOpt("temperature", temperature);
            json.putOpt("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
