package com.num.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import com.num.controller.utils.HashUtil;
import com.num.controller.utils.Logger;

import org.json.JSONObject;

public class Device implements BaseModel {
    private String phoneType, phoneNumber, softwareVersion, androidVersion, phoneBrand,
            manufacturer, productName, radioVersion, boardName, deviceDesign, phoneModel,
            networkCountry, networkName, emailAddress;
    private int dataCap, applicationVersion;

    public Device(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        PackageManager pm = context.getPackageManager();

        // Get the phone's type
        switch(tm.getPhoneType()) {
            case(TelephonyManager.PHONE_TYPE_CDMA) :
                phoneType = "CDMA";
                break;
            case(TelephonyManager.PHONE_TYPE_GSM) :
                phoneType = "GSM";
                break;
            case(TelephonyManager.PHONE_TYPE_SIP) :
                phoneType = "SIP";
                break;
            case(TelephonyManager.PHONE_TYPE_NONE) :
                phoneType = "NONE";
                break;
            default:
                phoneType = "UNKNOWN";
                break;
        }

        // Read the software version on the phone
        softwareVersion = tm.getDeviceSoftwareVersion();

        //Get the hashed phone number
        phoneNumber = HashUtil.SHA1(tm.getLine1Number());

        // Get device model
        phoneModel = Build.MODEL;

        // Get Android version
        androidVersion = Build.VERSION.RELEASE;

        // Get phone brand name
        phoneBrand = Build.BRAND;

        // Name of the industrial design
        deviceDesign = Build.DEVICE;

        // Manufacturer of the product
        manufacturer = Build.MANUFACTURER;

        // Name of overall product
        productName = Build.PRODUCT;

        // Name of the radio firmware version
        radioVersion = Build.RADIO;

        // Name of underlying board
        boardName = Build.BOARD;

        // Get connected network country ISO code
        networkCountry = tm.getNetworkCountryIso();

        // Get the connected carrier name
        networkName = tm.getNetworkOperatorName();

        // Get the package version
        try {
            applicationVersion = pm.getPackageInfo(context.getPackageName(),0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        emailAddress = prefs.getString("pref_email", "");

        String dCap = prefs.getString("pref_data_cap", "-1");
        dataCap = Integer.parseInt(dCap);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.putOpt("phoneType", phoneType);
            json.putOpt("phoneNumber", phoneNumber);
            json.putOpt("softwareVersion", softwareVersion);
            json.putOpt("phoneModel", phoneModel);
            json.putOpt("androidVersion", androidVersion);
            json.putOpt("phoneBrand", phoneBrand);
            json.putOpt("deviceDesign", deviceDesign);
            json.putOpt("manufacturer", manufacturer);
            json.putOpt("productName", productName);
            json.putOpt("radioVersion", radioVersion);
            json.putOpt("boardName", boardName);
            json.putOpt("networkCountry", networkCountry);
            json.putOpt("networkName", networkName);
            json.putOpt("applicationVersion", applicationVersion);
            json.putOpt("datacap", dataCap);
            json.putOpt("emailAddress", emailAddress);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
