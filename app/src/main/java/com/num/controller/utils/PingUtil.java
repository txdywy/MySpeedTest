package com.num.controller.utils;

import android.content.Context;

import com.num.model.Address;
import com.num.model.Measure;
import com.num.model.Ping;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;

public class PingUtil {

    private static String path = "/data/data/com.num/ping";
    private static String executable = "ping";

    public static Ping ping(Address address, HashMap<String, String> params) {
        String src = getSrcIp();
        String output = executePing(address.getIp(), params);
        Measure measure = parsePingResult(output);
        return new Ping(src, address, measure);
    }

    public static String executePing(String address, HashMap<String, String> params){
        try {
            String options = "";
            for (String flag : params.keySet()) {
                options += (flag + " " + params.get(flag) + " ");
            }
            String cmd = "/system/bin/ping ";
            String result = CommandLineUtil.runCommand(cmd + options + address);
            String[] output = result.split("\n");
            String lastLine = output[output.length - 1];
            if (lastLine.contains("rtt")) {
                return result;
            } else {
                cmd = path + " ";
                return CommandLineUtil.runCommand(cmd + options + address);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getSrcIp() {
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
        return ip.replace("\n","").replace("\r","");
    }

    private static Measure parsePingResult(String result) {
        double min = -1, avg = -1, max = -1, dev = -1;
        String[] output = result.split("\n");
        String lastLine = output[output.length - 1];
        if (lastLine.contains("rtt")) {
            NumberFormat nf = NumberFormat.getInstance();
            lastLine = lastLine.substring(23, lastLine.length() - 3);
            String[] split = lastLine.split("/");
            try {
                Number n = nf.parse(split[0]);
                min = n.doubleValue();
                n = nf.parse(split[1]);
                avg = n.doubleValue();
                n = nf.parse(split[2]);
                max = n.doubleValue();
                n = nf.parse(split[3]);
                dev = n.doubleValue();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return new Measure(min, max, avg, dev);
    }

    public static boolean isInstalled() {
        return FileUtil.exists(path);
    }

    public static void installExecutable(Context context) {
        FileUtil.copyFromAsset(context, executable, path);
        FileUtil.chmod(path, "755");
    }
}
