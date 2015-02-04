package com.num.myspeedtest.controller.utils;

import com.num.myspeedtest.Constants;
import com.num.myspeedtest.model.Address;
import com.num.myspeedtest.model.Measurement;

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
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(url);
            StringEntity se = new StringEntity(measurement.toJSON().toString());
            postRequest.setEntity(se);
            postRequest.setHeader("Accept", "application/json");
            postRequest.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(postRequest);
            String r = EntityUtils.toString(response.getEntity());
            Logger.show(r+"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
