package com.num.myspeedtest.model;

import android.content.Context;
import android.location.LocationManager;

import org.json.JSONObject;

public class GPS implements BaseModel {

    private String latitude = "";
    private String longitude = "";
    private String altitude = "";

    public GPS(Context context) {
        boolean gpsEnabled = false;
        boolean networkEnabled = false;
        LocationManager locationManager =
                (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, );
    }
    @Override
    public JSONObject toJSON() {
        return null;
    }
}
