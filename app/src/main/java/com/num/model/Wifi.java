package com.num.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.num.controller.utils.HashUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Wifi implements BaseModel {

    private boolean isWifi = false;
    private boolean isPreferred = false;
    private int strength = -1;
    private int ipAddress = -1;
    private int speed = -1;
    private int networkId = -1;
    private int rssi = -1;
    private String macAddress = "";
    private String ssid = "";
    private String detailedInfo = "";
    private String units = "";
    private List<WifiNeighbor> neighbors;
    private List<WifiPreference> preference;

    public Wifi(Context context) {
        String serviceName = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(serviceName);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        preference = new ArrayList<>();
        neighbors = new ArrayList<>();

        if(activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
            isWifi = true;
        }

        if(isWifi) {
            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wm.getConnectionInfo();
            SupplicantState supplicantState = info.getSupplicantState();

            if(info.getBSSID() != null) {
                strength = WifiManager.calculateSignalLevel(info.getRssi(), 11);
                ipAddress = info.getIpAddress();
                speed = info.getLinkSpeed();
                networkId = info.getNetworkId();
                rssi = info.getRssi();
                macAddress = info.getMacAddress();
                ssid = info.getSSID();
                detailedInfo = supplicantState.toString();
                units = WifiInfo.LINK_SPEED_UNITS;
                isPreferred = false;
            }

            List<WifiConfiguration> configs = wm.getConfiguredNetworks();

            for(int i=0; i<configs.size(); i++) {
                WifiPreference wifiPreference = new WifiPreference();
                String ssid = configs.get(i).SSID;
                if(ssid.equalsIgnoreCase(this.ssid)) {
                    wifiPreference.setConnected(true);
                    isPreferred = true;
                }
                else {
                    wifiPreference.setConnected(false);
                }
                wifiPreference.setAuthAlgorithms(configs.get(i).allowedAuthAlgorithms.toString());
                wifiPreference.setBssid(configs.get(i).BSSID);
                wifiPreference.setGroupCiphers(configs.get(i).allowedGroupCiphers.toString());
                wifiPreference.setPairwiseCiphers(configs.get(i).allowedPairwiseCiphers.toString());
                wifiPreference.setPriority(configs.get(i).priority);
                wifiPreference.setSsid(ssid);
                wifiPreference.setProtocols(configs.get(i).allowedProtocols.toString());
                wifiPreference.setNetworkId(configs.get(i).networkId);
                wifiPreference.setStatus(configs.get(i).status);

                preference.add(wifiPreference);
            }
        }
    }


    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.putOpt("strength", "" + strength);
            json.putOpt("ipAddress", "" + ipAddress);
            json.putOpt("speed", "" + speed);
            json.putOpt("networkId", "" + networkId);
            json.putOpt("rssi", "" + rssi);
            json.putOpt("macAddress", HashUtil.SHA1(macAddress));
            json.putOpt("ssid", HashUtil.SHA1(ssid));
            json.putOpt("detailedInfo", "" + detailedInfo);
            json.putOpt("units", "" + units);
            json.putOpt("isPreferred", "" + isPreferred);
            JSONArray neighbor = new JSONArray();
            JSONArray prefer = new JSONArray();
            for(WifiNeighbor wn : neighbors) {
                neighbor.put(wn.toJSON());
            }
            for(WifiPreference wp : preference) {
                prefer.put(wp.toJSON());
            }
            json.putOpt("wifiNeighbors", neighbor);
            json.putOpt("wifiPreference", prefer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private class WifiNeighbor implements BaseModel {

        private String ssid = "";
        private String macAddress = "";
        private int signalLevel = -1;
        private int frequency = -1;
        private String capability = "";
        private boolean isConnected = false;
        private boolean isPreferred = false;

        public String getSsid() {
            return ssid;
        }

        public void setSsid(String ssid) {
            this.ssid = ssid;
        }

        public String getMacAddress() {
            return macAddress;
        }

        public void setMacAddress(String macAddress) {
            this.macAddress = macAddress;
        }

        public int getSignalLevel() {
            return signalLevel;
        }

        public void setSignalLevel(int signalLevel) {
            this.signalLevel = signalLevel;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        public String getCapability() {
            return capability;
        }

        public void setCapability(String capability) {
            this.capability = capability;
        }

        public boolean isConnected() {
            return isConnected;
        }

        public void setConnected(boolean isConnected) {
            this.isConnected = isConnected;
        }

        public boolean isPreferred() {
            return isPreferred;
        }

        public void setPreferred(boolean isPreferred) {
            this.isPreferred = isPreferred;
        }

        @Override
        public JSONObject toJSON() {
            JSONObject json = new JSONObject();
            try {
                json.putOpt("ssid", "" + HashUtil.SHA1(ssid));
                json.putOpt("macAddress", HashUtil.SHA1(macAddress));
                json.putOpt("signalLevel", "" + signalLevel);
                json.putOpt("frequency", "" + frequency);
                json.putOpt("capability", "" + capability);
                json.putOpt("isConnected", "" + isConnected);
                json.putOpt("isPreferred", "" + isPreferred);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return json;
        }
    }

    private class WifiPreference implements BaseModel {

        private String id = "";
        private String protocols = "";
        private String ssid = "";
        private String pairwiseCiphers = "";
        private String authAlgorithms = "";
        private String bssid = "";
        private String keyMgmt = "";
        private String groupCiphers = "";
        private int priority = -1;
        private int networkId = -1;
        private int status = -1;
        private boolean isConnected = false;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProtocols() {
            return protocols;
        }

        public void setProtocols(String protocols) {
            this.protocols = protocols;
        }

        public String getSsid() {
            return ssid;
        }

        public void setSsid(String ssid) {
            this.ssid = ssid;
        }

        public String getPairwiseCiphers() {
            return pairwiseCiphers;
        }

        public void setPairwiseCiphers(String pairwiseCiphers) {
            this.pairwiseCiphers = pairwiseCiphers;
        }

        public String getGroupCiphers() {
            return groupCiphers;
        }

        public void setGroupCiphers(String groupCiphers) {
            this.groupCiphers = groupCiphers;
        }

        public String getAuthAlgorithms() {
            return authAlgorithms;
        }

        public void setAuthAlgorithms(String authAlgorithms) {
            this.authAlgorithms = authAlgorithms;
        }

        public String getBssid() {
            return bssid;
        }

        public void setBssid(String bssid) {
            this.bssid = bssid;
        }

        public String getKeyMgmt() {
            return keyMgmt;
        }

        public void setKeyMgmt(String keyMgmt) {
            this.keyMgmt = keyMgmt;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public int getNetworkId() {
            return networkId;
        }

        public void setNetworkId(int networkId) {
            this.networkId = networkId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public boolean isConnected() {
            return isConnected;
        }

        public void setConnected(boolean isConnected) {
            this.isConnected = isConnected;
        }

        @Override
        public JSONObject toJSON() {
            JSONObject json = new JSONObject();
            try {
                json.putOpt("id", "" + id);
                json.putOpt("protocols", "" + protocols);
                json.putOpt("ssid", "" + HashUtil.SHA1(ssid));
                json.putOpt("priority", "" + priority);
                json.putOpt("networkid", "" + networkId);
                json.putOpt("status", "" + status);
                json.putOpt("pairwiseCiphers", "" + pairwiseCiphers);
                json.putOpt("groupCiphers", "" + groupCiphers);
                json.putOpt("authAlgorithms", "" + authAlgorithms);
                json.putOpt("bssid", "" + bssid);
                json.putOpt("keyMgmt", "" + keyMgmt);
                json.putOpt("isConnected", "" + isConnected);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return json;
        }
    }
}
