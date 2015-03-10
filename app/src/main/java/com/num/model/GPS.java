package com.num.model;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import org.json.JSONObject;

public class GPS implements BaseModel {

    private String latitude = "";
    private String longitude = "";
    private String altitude = "";

    public GPS(Context context) {
        boolean gpsEnabled = false;
        boolean networkEnabled = false;
        final LocationManager locationManager =
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

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationManager.removeUpdates(this);
                latitude = "" + location.getLatitude();
                longitude = "" + location.getLongitude();
                altitude = "" + location.getAltitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(gpsEnabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        if(networkEnabled) {

        }

        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, );
    }
    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.putOpt("latitude", latitude);
            json.putOpt("longitude", longitude);
            json.putOpt("altitude", altitude);
        } catch (Exception e) {}
        return json;
    }
}
