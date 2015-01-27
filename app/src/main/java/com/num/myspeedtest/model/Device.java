package com.num.myspeedtest.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

import com.num.myspeedtest.controller.utils.HashUtil;

import org.json.JSONObject;

public class Device implements BaseModel, Parcelable {
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

        dataCap = Integer.parseInt(prefs.getString("pref_data_cap", "-1"));
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getPhoneBrand() {
        return phoneBrand;
    }

    public void setPhoneBrand(String phoneBrand) {
        this.phoneBrand = phoneBrand;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRadioVersion() {
        return radioVersion;
    }

    public void setRadioVersion(String radioVersion) {
        this.radioVersion = radioVersion;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getDeviceDesign() {
        return deviceDesign;
    }

    public void setDeviceDesign(String deviceDesign) {
        this.deviceDesign = deviceDesign;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getNetworkCountry() {
        return networkCountry;
    }

    public void setNetworkCountry(String networkCountry) {
        this.networkCountry = networkCountry;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public int getDataCap() {
        return dataCap;
    }

    public void setDataCap(int dataCap) {
        this.dataCap = dataCap;
    }

    public int getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(int applicationVersion) {
        this.applicationVersion = applicationVersion;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
