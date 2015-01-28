package com.num.myspeedtest.model;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.num.myspeedtest.controller.utils.HashUtil;
import com.num.myspeedtest.controller.utils.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Measurement implements BaseModel {

    private String deviceId;
    private Device device;
    private Battery battery;
    private Network network;
    private Sim sim;
    private Usage usage;
    private Wifi wifi;
    private String time;
    private String localTime;
    private List<Ping> pings;
    private List<LastMile> lastMiles;
    private Throughput throughput;
    private Loss loss;
    private Ipdv ipdv;
    private WarmupExperiment warmupExperiment;
    private GPS gps;
    private State state;
    private boolean isComplete;
    private ArrayList<Screen> screens;
    private boolean isManual;
    private Context context;

    public Measurement(Context context, boolean isManual) {
        this.context = context;

        this.time = findTime();
        Logger.show("Time", time);

        this.localTime = findLocalTime();
        Logger.show("Local Time", localTime);

        this.deviceId = findDeviceId();
        Logger.show("Device ID", deviceId);

        this.device = new Device(context);
        Logger.show("Device", device.toJSON().toString());

        this.network = new Network(context);
        Logger.show("Network", network.toJSON().toString());

        this.sim = new Sim(context);
        Logger.show("Sim", sim.toJSON().toString());

        this.state = new State(deviceId, time, localTime, device, network);
        Logger.show("State", state.toJSON().toString());

        this.wifi = new Wifi(context);
        Logger.show("Wifi", wifi.toJSON().toString());

        this.battery = new Battery(context);
        Logger.show("Battery",battery.toJSON().toString());

        this.gps = new GPS(context);
//        Logger.show("GPS", gps.toJSON().toString());

        this.isComplete = false;
        this.isManual = isManual;

    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Battery getBattery() {
        return battery;
    }

    public void setBattery(Battery battery) {
        this.battery = battery;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public Sim getSim() {
        return sim;
    }

    public void setSim(Sim sim) {
        this.sim = sim;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    public Wifi getWifi() {
        return wifi;
    }

    public void setWifi(Wifi wifi) {
        this.wifi = wifi;
    }

    public List<Ping> getPings() {
        return pings;
    }

    public void setLastMiles(List<LastMile> lastMiles) {
        this.lastMiles = lastMiles;
    }

    public List<LastMile> getLastMiles() {
        return lastMiles;
    }

    public void setPings(List<Ping> pings) {
        this.pings = pings;
    }

    public Throughput getThroughput() {
        return throughput;
    }

    public void setThroughput(Throughput throughput) {
        this.throughput = throughput;
    }

    public Loss getLoss() {
        return loss;
    }

    public void setLoss(Loss loss) {
        this.loss = loss;
    }

    public Ipdv getIpdv() {
        return ipdv;
    }

    public void setIpdv(Ipdv ipdv) {
        this.ipdv = ipdv;
    }

    public WarmupExperiment getWarmupExperiment() {
        return warmupExperiment;
    }

    public void setWarmupExperiment(WarmupExperiment warmupExperiment) {
        this.warmupExperiment = warmupExperiment;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public ArrayList<Screen> getScreens() {
        return screens;
    }

    public void setScreens(ArrayList<Screen> screens) {
        this.screens = screens;
    }

    public boolean isManual() {
        return isManual;
    }

    public void setManual(boolean isManual) {
        this.isManual = isManual;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();

        try {
            json.putOpt("time", time);
            json.putOpt("localtime", localTime);
            json.putOpt("deviceid", HashUtil.SHA1(deviceId));

            if(pings != null) {
                for (Ping p : pings) {
                    array.put(p.toJSON());
                }
                json.putOpt("pings", array);
            }

            if(lastMiles != null) {
                array = new JSONArray();
                for (LastMile lm : lastMiles) {
                    array.put(lm.toJSON());
                }
                json.putOpt("lastmiles", array);
            }

            if(screens != null) {
                array = new JSONArray();
                for(Screen s : screens) {
                    array.put(s.toJSON());
                }
                json.putOpt("screens", array);
            }

            if(device != null) {
                json.putOpt("device", device.toJSON());
            }

            if(throughput != null) {
                json.putOpt("throughput", throughput.toJSON());
            }

            if(gps != null) {
                json.putOpt("gps", gps.toJSON());
            }

            if(battery != null) {
                json.putOpt("battery", battery.toJSON());
            }

            if(usage != null) {
                json.putOpt("usage", usage.toJSON());
            }

            if(network != null) {
                json.putOpt("network", network.toJSON());
            }

            if(warmupExperiment != null) {
                json.putOpt("warmup_experiment", warmupExperiment.toJSON());
            }

            if(sim != null) {
                json.putOpt("sim", sim.toJSON());
            }

            if(wifi != null) {
                json.putOpt("wifi", wifi.toJSON());
            }

            if(state != null) {
                json.putOpt("state", state.toJSON());
            }

            if(isManual) {
                json.putOpt("isManual", 1);
            }
            else {
                json.putOpt("isManual", 0);
            }

            if(loss != null) {
                json.putOpt("loss", loss.toJSON());
            }

            if(ipdv != null) {
                json.putOpt("delay_variation", ipdv.toJSON());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private String findDeviceId() {
        String serviceName = Context.TELEPHONY_SERVICE;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(serviceName);
        if(tm.getDeviceId() != null) {
            return HashUtil.SHA1(tm.getDeviceId());
        }
        else {
            return Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
    }

    private String findTime() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault().getTimeZone("UTC"));
        String utcTime = sdf.format(new Date());
        return utcTime;
    }

    private String findLocalTime() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String localTime = sdf.format(new Date());
        return localTime;
    }
}
