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

    private static String pingPath = "/data/data/com.num/ping";

    public static Ping ping(Address address, HashMap<String, String> params) {
        String src = getSrcIp();
        String output = executePing(address.getIp(), params);
        Measure measure = parsePingResult(output);
        return new Ping(src, address, measure);
    }

    public static String executePing(String address, HashMap<String, String> params){
        String cmd = "/system/bin/ping ";
        String options = "";
        for(String flag : params.keySet()){
            options += (flag + " " + params.get(flag) + " ");
        }
        String result = CommandLineUtil.runCommand(cmd + options + address);
        String[] output = result.split("\n");
        String lastLine = output[output.length - 1];
        if(lastLine.contains("rtt")) {
            return CommandLineUtil.runCommand(cmd + options + address);
        } else {
            cmd = pingPath + " ";
            return CommandLineUtil.runCommand(cmd + options + address);
        }
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
                //e1.printStackTrace();
            }
        }
        return ip.replace("\n","").replace("\r","");
    }

    private static Measure parsePingResult(String result) {
        String[] output = result.split("\n");
        String lastLine = output[output.length - 1];
        if (lastLine.contains("rtt")) {
            NumberFormat nf = NumberFormat.getInstance();
            double min, avg, max, dev;
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
                return new Measure(-1, -1, -1, -1);
            }
            return new Measure(min, max, avg, dev);
        } else {
            return new Measure(-1, -1, -1, -1);
        }
    }

    public static boolean isPingInstalled() {
        try {
            Process process = Runtime.getRuntime().exec(pingPath);
            process.waitFor();
            process.destroy();
        }
        catch (Exception exception) {
            return false;
        }
        return true;
    }

    public static boolean installExecutable(Context context) {
        try
        {
            InputStream inputstream = context.getAssets().open("ping");
            int i = inputstream.available();
            byte buf[] = new byte[i];
            inputstream.read(buf);
            FileOutputStream fileoutputstream = new FileOutputStream(new File(pingPath));
            fileoutputstream.write(buf, 0, i);
            inputstream.close();
            fileoutputstream.close();
            Process process = Runtime.getRuntime().exec((new StringBuilder()).append("chmod 755 ")
                    .append(pingPath).toString());
            process.waitFor();
            process.destroy();
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
            return false;
        }
        catch (InterruptedException interruptedexception)
        {
            interruptedexception.printStackTrace();
            return false;
        }
        return true;
    }
}
