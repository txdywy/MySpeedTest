package com.num.myspeedtest.controller.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Calendar;

public class DeviceUtil {
    private static DeviceUtil sInstance = new DeviceUtil();
    public static DeviceUtil getInstance() {
        return sInstance;
    }

    public boolean isInternetAvailable(Context context) {
        boolean status=false;
        try{
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
                status= true;
            }else {
                netInfo = cm.getNetworkInfo(1);
                if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
                    status= true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return status;

    }

    public int getNextMonth(){
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MONTH, 1);
        System.out.println("Monthly reset set to "+date.get(Calendar.MONTH));
        return date.get(Calendar.MONTH);
    }

    public int getCurrentMonth(){
        Calendar date = Calendar.getInstance();
        return date.get(Calendar.MONTH);
    }
}
