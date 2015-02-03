package com.num.myspeedtest.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import com.num.myspeedtest.Constants;
import com.num.myspeedtest.controller.tasks.SignalTask;
import com.num.myspeedtest.controller.utils.Logger;

import org.json.JSONObject;
import org.json.JSONException;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Network implements BaseModel {

    private String networkOperatorId = "";
    private String networkType = "";
    private String connectionType = "";
    private String mobileNetworkInfo = "";
    private String wifiState = "";
    private String cellId = "";
    private String cellLac = "";
    private String dataState = "";
    private String dataActivity = "";
    private String signalStrength = "-1";
    private String cellType = "";
    private String baseStationLat = "";
    private String baseStationLong = "";
    private String networkId = "";
    private String systemId = "";

    private TelephonyManager tm;
    private ConnectivityManager cm;
    private NetworkInfo activeNetwork;
    private Context context;

    public Network(Context context) {
        tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
        this.context = context;

        getNetworkInfo();
        getDataInfo();
        getCellInfo();
        getSignal();
    }

    private void getNetworkInfo() {
        networkOperatorId = tm.getNetworkOperator();
        if(activeNetwork == null) {
            mobileNetworkInfo = "No Network";
        }
        else {
            int connectionType = activeNetwork.getType();
            int networkType = tm.getNetworkType();

            this.connectionType = parseConnectionType(connectionType);
            this.networkType = parseNetworkType(networkType);

            if(connectionType == ConnectivityManager.TYPE_MOBILE) {
                String networkLevelType = parseNetworkLevelType(networkType);
                this.connectionType += ": " + networkLevelType;
            }
        }

        if(!this.connectionType.equals("Wifi")) {
                int network = ConnectivityManager.TYPE_MOBILE;
                NetworkInfo mobileNetwork = cm.getNetworkInfo(network);
                this.mobileNetworkInfo = mobileNetwork.toString();
        }
    }

    private void getDataInfo() {
        int dataActivity = tm.getDataActivity();
        int dataState = tm.getDataState();

        this.dataActivity = parseDataActivity(dataActivity);
        this.dataState = parseDataState(dataState);
    }

    private void getCellInfo() {
        CellLocation cellLocation = tm.getCellLocation();
        if(cellLocation instanceof CdmaCellLocation) {
            CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cellLocation;
            int baseStationId = cdmaCellLocation.getBaseStationId();
            int baseStationLat = cdmaCellLocation.getBaseStationLatitude();
            int baseStationLong = cdmaCellLocation.getBaseStationLongitude();
            int networkId = cdmaCellLocation.getNetworkId();
            int systemId = cdmaCellLocation.getSystemId();
            this.cellType = "CDMA";
            this.cellId = "" + baseStationId;
            this.baseStationLat = "" + baseStationLat;
            this.baseStationLong = "" + baseStationLong;
            this.networkId = "" + networkId;
            this.systemId = "" + systemId;
        }
        else if(cellLocation instanceof GsmCellLocation) {
            GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
            int cid = gsmCellLocation.getCid() & 0xffff;
            int lac = gsmCellLocation.getLac() & 0xffff;

            this.cellId = "" + cid;
            this.cellLac = "" + lac;
            this.cellType = "GSM";
        }
        else {
            this.cellId = Constants.UNAVAILABLE_CELLID;
            this.cellLac = Constants.UNAVAILABLE_CELLLAC;
        }
    }

    private void getSignal() {
        signalStrength = Signal.signal;
    }

    private String parseNetworkType(int type) {
        switch (type) {
            case (TelephonyManager.NETWORK_TYPE_1xRTT):
                return "1xRTT";
            case (TelephonyManager.NETWORK_TYPE_CDMA):
                return "CMDA";
            case (TelephonyManager.NETWORK_TYPE_EDGE):
                return "EDGE";
            case (TelephonyManager.NETWORK_TYPE_EVDO_0):
                return "EVDO_0";
            case (TelephonyManager.NETWORK_TYPE_EVDO_A):
                return "EVDO_A";
            case (TelephonyManager.NETWORK_TYPE_EVDO_B):
                return "EVDO_B";
            case (TelephonyManager.NETWORK_TYPE_IDEN):
                return "IDEN";
            case (TelephonyManager.NETWORK_TYPE_GPRS):
                return "GPRS";
            case (TelephonyManager.NETWORK_TYPE_HSDPA):
                return "HSDPA";
            case (TelephonyManager.NETWORK_TYPE_HSUPA):
                return "HSUPA";
            case (TelephonyManager.NETWORK_TYPE_HSPA):
                return "HSPA";
            case (TelephonyManager.NETWORK_TYPE_UMTS):
                return "UMTS";
            case (TelephonyManager.NETWORK_TYPE_LTE):
                return "LTE";
            case (TelephonyManager.NETWORK_TYPE_UNKNOWN):
                return "UNKNOWN";
            default:
                return "";
        }
    }

    private String parseNetworkLevelType(int type) {
        switch(type) {
            case (TelephonyManager.NETWORK_TYPE_1xRTT):
                return "3G";
            case (TelephonyManager.NETWORK_TYPE_CDMA):
                return "2G/3G";
            case (TelephonyManager.NETWORK_TYPE_EDGE):
                return "2G";
            case (TelephonyManager.NETWORK_TYPE_EVDO_0):
                return "3G";
            case(TelephonyManager.NETWORK_TYPE_EVDO_A):
                return "3G";
            case(TelephonyManager.NETWORK_TYPE_EVDO_B):
                return "3G";
            case(TelephonyManager.NETWORK_TYPE_IDEN):
                return "PDT";
            case(TelephonyManager.NETWORK_TYPE_GPRS):
                return "2G";
            case(TelephonyManager.NETWORK_TYPE_HSDPA):
                return "3G";
            case(TelephonyManager.NETWORK_TYPE_HSPA):
                return "3G";
            case(TelephonyManager.NETWORK_TYPE_HSUPA):
                return "3G";
            case(TelephonyManager.NETWORK_TYPE_UMTS):
                return "3G";
            case(TelephonyManager.NETWORK_TYPE_LTE):
                return "4G";
            case(TelephonyManager.NETWORK_TYPE_UNKNOWN):
                return "2G";
            default:
                return "";
        }
    }

    private String parseConnectionType(int type) {
        switch (type) {
            case ConnectivityManager.TYPE_WIFI:
                return "Wifi";
            case ConnectivityManager.TYPE_MOBILE:
                return "Mobile";
            case ConnectivityManager.TYPE_WIMAX:
                return "WIMAX";
            default:
                return "";
        }
    }

    private String parseDataActivity(int activity) {
        switch(activity) {
            case TelephonyManager.DATA_ACTIVITY_IN:
                return "DATA_ACTIVITY_IN";
            case TelephonyManager.DATA_ACTIVITY_OUT:
                return "DATA_ACTIVITY_OUT";
            case TelephonyManager.DATA_ACTIVITY_INOUT:
                return "DATA_ACTIVITY_INOUT";
            case TelephonyManager.DATA_ACTIVITY_NONE:
                return "DATA_ACTIVITY_NONE";
            case TelephonyManager.DATA_ACTIVITY_DORMANT:
                return "DATA_ACTIVITY_DORMANT";
            default:
                return "";
        }
    }

    private String parseDataState(int state) {
        switch (state) {
            case TelephonyManager.DATA_CONNECTED :
                return "DATA_CONNECTED";
            case TelephonyManager.DATA_CONNECTING :
                return "DATA_CONNECTING";
            case TelephonyManager.DATA_DISCONNECTED :
                return "DATA_DISCONNECTED";
            case TelephonyManager.DATA_SUSPENDED :
                return "DATA_SUSPENDED";
            default:
                return "";
        }
    }

    public String getConnectionType() {
        return connectionType;
    }

    public String getCellId() {
        return cellId;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        try {
            obj.putOpt("networkOperatorId", networkOperatorId);
            obj.putOpt("networkType", networkType);
            obj.putOpt("connectionType", connectionType);
            obj.putOpt("mobileNetworkInfo", mobileNetworkInfo);
            obj.putOpt("wifiState", wifiState);
            obj.putOpt("cellType",cellType );
            obj.putOpt("cellId", cellId);
            obj.putOpt("cellLac", cellLac);
            obj.putOpt("baseStationLong", baseStationLong);
            obj.putOpt("baseStationLat", baseStationLat);
            obj.putOpt("networkid",networkId );
            obj.putOpt("systemid", systemId);
            obj.putOpt("dataState", dataState);
            obj.putOpt("dataActivity", dataActivity);
            obj.putOpt("signalStrength", signalStrength);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}