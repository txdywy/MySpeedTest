package com.num.myspeedtest.models;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Telephony;
import android.telephony.TelephonyManager;

import com.num.myspeedtest.helpers.HashHelper;

import org.json.JSONObject;

/**
 * Created by Andrew on 10/7/2014.
 */
public class Device implements BaseModel {
    private String phoneType, phoneNumber, softwareVersion, androidVersion, phoneBrand,
            manufacturer, productName, radioVersion, boardName, deviceDesign, phoneModel,
            networkCountry, networkName;
    private int applicationVersion;

    public Device(Context c) {
        TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        PackageManager pm = c.getPackageManager();

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
        phoneNumber = HashHelper.SHA1(tm.getLine1Number());

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
            applicationVersion = pm.getPackageInfo(c.getPackageName(),0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
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
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
