package com.num.myspeedtest.controller.utils;

import com.num.myspeedtest.model.Address;
import com.num.myspeedtest.model.Measure;
import com.num.myspeedtest.model.Ping;
import com.num.myspeedtest.model.Traceroute;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.Socket;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;

public class PingUtil {

    public static Ping pingICMP(Address address, HashMap<String, String> params) {
        String src = getSrcIp();
        String output = ping(address.getIp(), params);
        Measure measure = parsePingResult(output);
        return new Ping(src, address, measure);
    }

    public static Traceroute traceroute(String address, HashMap<String, String> params) {
        int hop = Integer.parseInt(params.get("-t"));
        String output = ping(address, params);
        Traceroute traceroute = parseTracerouteResult(output, hop);
        output = ping(traceroute.getHostname(), params);
        return parseTracerouteResult(output, hop);
    }

    public static String ping(String address, HashMap<String, String> params){
        CommandLineUtil cmdLine = new CommandLineUtil();
        String cmd = "ping";
        String options = "";
        for(String flag : params.keySet()){
            options += (flag + " " + params.get(flag) + " ");
        }
        String dst = address;
        return cmdLine.runCommand(cmd, dst, options);
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

    private static Traceroute parseTracerouteResult(String result, int index){
        boolean found = false;
        String hostName = "";
        String hostAddress = "";

        String[] output = result.split("\n");
        int count = 0;
        if(output.length<=1){
            return new Traceroute("","", "", index);
        }

        String secondLine = output[1];
        if(secondLine.length()>0) {
            String[] split = secondLine.split(" ");
            if (secondLine.contains("Time to live exceeded")) {
                if (split[2].contains("(") && split[2].contains(")")) {
                    hostName = split[1];
                    hostAddress = split[2].substring(1, split[2].length() - 2);
                } else {
                    hostName = split[1].substring(0, split[1].length() - 1);
                    hostAddress = split[1].substring(0, split[1].length() - 1);
                }
                found = true;
            } else if (secondLine.contains("bytes from")) {
                if (split[4].contains("(") && split[4].contains(")")) {
                    hostName = split[3];
                    hostAddress = split[4].substring(1, split[4].length() - 2);
                } else {
                    hostName = split[3].substring(0, split[3].length() - 1);
                    hostAddress = split[3].substring(0, split[3].length() - 1);
                }
                found = true;
            }
        }

        String lastLine = output[output.length-1];
        NumberFormat nf = NumberFormat.getInstance();

        double avg=0;
        if(lastLine.contains("rtt")) {
            lastLine = lastLine.substring(23,lastLine.length()-3);
            String[] split = lastLine.split("/");
            try {
                Number n = nf.parse(split[1]);
                avg = n.doubleValue();
                found = true;
            } catch (ParseException e) {
                avg = 0;
            }
        }

        if(found==true)
        {
            return new Traceroute(hostAddress,hostName, avg + " ms", index);
        }
        else
        {
            return new Traceroute("***","*", "*", index);
        }
    }

}
