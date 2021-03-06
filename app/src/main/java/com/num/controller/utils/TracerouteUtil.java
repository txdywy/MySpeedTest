package com.num.controller.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.num.model.Hop;
import com.num.model.Traceroute;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

public class TracerouteUtil {

    private static String path = "/data/data/com.num/traceroute";
    private static String executable = "traceroute";

    public static boolean isInstalled() {
        return FileUtil.exists(path);
    }

    public static void installExecutable(Context context) {
        FileUtil.copyFromAsset(context, executable, path);
        FileUtil.chmod(path, "755");
    }

    public static void traceroute(String hostname, HashMap<String, String> params, Handler handler) {
        String cmd = path + " ";
        String options = "";
        int traceType = (params.containsKey("-I")) ? Traceroute.ICMP : Traceroute.UDP;
        String srcIp = PingUtil.getSrcIp();
        try {
            InetAddress ip = null;
            InetAddress[] ipArray = Inet4Address.getAllByName(hostname);
            for(InetAddress i: ipArray) {
                if(i instanceof Inet4Address) {
                    ip = i;
                }
            }
            for(String flag : params.keySet()){
                options += (flag + " " + params.get(flag) + " ");
            }
            BufferedReader reader = CommandLineUtil.runBufferedCommand(cmd + options + ip.getHostAddress());
            String line = reader.readLine();
            if(line == null) {
                Hop hop = new Hop("Unsupported traceroute type", "", 0, 0);

                Bundle bundle = new Bundle();
                bundle.putString("hostname", hostname);
                bundle.putString("srcIp", srcIp);
                bundle.putString("dstIp", ip.getHostAddress());
                bundle.putInt("traceType", traceType);
                bundle.putParcelable("hop", hop);
                bundle.putBoolean("isDone", true);

                Message msg = new Message();
                msg.setData(bundle);
                handler.sendMessage(msg);
                return;
            }
            else if(line.contains("No address associated")) {
                Hop hop = new Hop("Unknown Host", "", 0, 0);

                Bundle bundle = new Bundle();
                bundle.putString("hostname", hostname);
                bundle.putString("srcIp", srcIp);
                bundle.putString("dstIp", ip.getHostAddress());
                bundle.putInt("traceType", traceType);
                bundle.putParcelable("hop", hop);
                bundle.putBoolean("isDone", true);

                Message msg = new Message();
                msg.setData(bundle);
                handler.sendMessage(msg);
                return;
            }
            while((line=reader.readLine())!=null) {
                Log.d("TracerouteUtil", line);
                Hop hop = parseTracerouteResult(line);

                Bundle bundle = new Bundle();
                bundle.putString("hostname", hostname);
                bundle.putString("srcIp", srcIp);
                bundle.putString("dstIp", ip.getHostAddress());
                bundle.putInt("traceType", traceType);
                bundle.putParcelable("hop", hop);
                bundle.putBoolean("isDone", false);

                Message msg = new Message();
                msg.setData(bundle);
                handler.sendMessage(msg);
            }

            Bundle bundle = new Bundle();
            bundle.putString("hostname", hostname);
            bundle.putString("srcIp", srcIp);
            bundle.putString("dstIp", ip.getHostAddress());
            bundle.putInt("traceType", traceType);
            bundle.putBoolean("isDone", true);

            Message msg = new Message();
            msg.setData(bundle);
            handler.sendMessage(msg);
        } catch (UnknownHostException e) {
            Hop hop = new Hop("Unknown Host", "", 0, 0);

            Bundle bundle = new Bundle();
            bundle.putString("hostname", hostname);
            bundle.putString("srcIp", srcIp);
            bundle.putString("dstIp", "");
            bundle.putInt("traceType", traceType);
            bundle.putParcelable("hop", hop);
            bundle.putBoolean("isDone", true);

            Message msg = new Message();
            msg.setData(bundle);
            handler.sendMessage(msg);
        } catch (IOException e) {
            Hop hop = new Hop("Unknown Host", "", 0, 0);

            Bundle bundle = new Bundle();
            bundle.putString("hostname", hostname);
            bundle.putString("srcIp", srcIp);
            bundle.putString("dstIp", "");
            bundle.putInt("traceType", traceType);
            bundle.putParcelable("hop", hop);
            bundle.putBoolean("isDone", true);

            Message msg = new Message();
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
    }

    // TODO Improve IPv6 reverse lookup
    private static Hop parseTracerouteResult(String result) {
        String line = result.replaceAll("ms", "").replaceAll("\\*", "*").replaceAll(" +", " ").trim()
                .replaceAll(",$", "");
        String[] split = line.split(" ");
        int hop = Integer.parseInt(split[0]);
        String address = split[1];
        double sum = 0.0;
        int count = split.length-2;
        for(int i=2; i<split.length; i++) {
            try {
                sum += Double.parseDouble(split[i]);
            }
            catch (Exception e) {
                count--;
            }
        }
        double rtt = -1;
        if(count > 0) {
            rtt = sum / (count);
        }
        String hostname = address;
        try {
            if(!address.equals("*")) {
                hostname = InetAddress.getByName(address).getCanonicalHostName();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return new Hop(address, hostname, rtt, hop);
    }
}
