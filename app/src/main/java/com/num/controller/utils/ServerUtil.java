package com.num.controller.utils;

import android.util.Log;

import com.num.Constants;
import com.num.model.Address;
import com.num.model.Measure;
import com.num.model.Measurement;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ServerUtil {

    //TODO Make this dynamic
    public static List<Address> getTargets() {

        List<Address> pingTargets = new ArrayList<>();
        pingTargets.add(new Address("www.google.com", "Google", "ping"));
        pingTargets.add(new Address("www.facebook.com", "Facebook", "ping"));
        pingTargets.add(new Address("www.twitter.com", "Twitter", "ping"));
        pingTargets.add(new Address("www.youtube.com", "YouTube", "ping"));
        pingTargets.add(new Address("www.bing.com", "Bing", "ping"));
        pingTargets.add(new Address("www.wikipedia.com", "Wikipedia", "ping"));
        pingTargets.add(new Address("143.215.131.173", "GATech", "ping"));

        return pingTargets;
    }

    public static void sendMeasurement(Measurement measurement) {
        String url = Constants.API_SERVER_ADDRESS + "measurement_v2";
        Measurement currentMeasurement = measurement;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(url);
            postRequest.setHeader("Accept", "application/json");
            postRequest.setHeader("Content-type", "application/json");
            Measurement.unsentStack.push(currentMeasurement);
            while(!Measurement.unsentStack.isEmpty()) {
                currentMeasurement = Measurement.unsentStack.pop();
                StringEntity se = new StringEntity(currentMeasurement.toJSON().toString());
                postRequest.setEntity(se);
                HttpResponse response = httpClient.execute(postRequest);
                String r = EntityUtils.toString(response.getEntity());
                Log.d("DEBUG", r + "");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Measurement.unsentStack.push(currentMeasurement);
            Log.d("DEBUG", "Postponing measurement");
        }
    }
}
