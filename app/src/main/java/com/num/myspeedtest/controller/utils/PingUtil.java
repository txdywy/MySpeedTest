package com.num.myspeedtest.controller.utils;

import com.num.myspeedtest.model.Address;
import com.num.myspeedtest.model.Measure;
import com.num.myspeedtest.model.Ping;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.NumberFormat;
import java.text.ParseException;

public class PingUtil {

    public static Ping pingICMP(Address address, int count) {
        CommandLineUtil cmdLine = new CommandLineUtil();
        double timeGap = 0.5;
        String cmd = "ping";
        String options = "-c" + count + " -i" + timeGap;
        String src = getSrcIp();
        String dst = address.getIp();
        String output = cmdLine.runCommand(cmd, dst, options);
        Measure measure = parsePingResult(output);
        return new Ping(src, address, measure);
    }

    private static String getSrcIp() {
        String ip = "";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet("http://icanhazip.com/");
            HttpResponse response = httpClient.execute(getRequest);
            response.getStatusLine().getStatusCode();
            ip = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            Socket conn;
            try {
                conn = new Socket("www.google.com", 80);
                ip = conn.getLocalAddress().toString();
                conn.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return ip;
    }

    private static Measure parsePingResult(String p) {
        String[] output = p.split("\n");
        String lastLine = output[output.length - 1];
        System.out.println(lastLine);
        if (lastLine.contains("rtt")) {
            NumberFormat nf = NumberFormat.getInstance();
            double min, avg, max, dev;
            lastLine = lastLine.substring(23, lastLine.length() - 3);
            String[] result = lastLine.split("/");
            try {
                Number n = nf.parse(result[0]);
                min = n.doubleValue();
                n = nf.parse(result[1]);
                avg = n.doubleValue();
                n = nf.parse(result[2]);
                max = n.doubleValue();
                n = nf.parse(result[3]);
                dev = n.doubleValue();
            } catch (ParseException e) {
                return new Measure(-1, -1, -1, -1);
            }
            return new Measure(min, max, avg, dev);
        } else {
            return new Measure(-1, -1, -1, -1);
        }
    }

}
