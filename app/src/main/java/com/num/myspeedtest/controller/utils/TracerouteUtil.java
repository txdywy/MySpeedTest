package com.num.myspeedtest.controller.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.num.myspeedtest.model.Hop;
import com.num.myspeedtest.model.Traceroute;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

public class TracerouteUtil {

    private static String traceroutePath = "/data/data/com.num.myspeedtest/traceroute";

    public static boolean installExecutable(Context context) {
        try
        {
            InputStream inputstream = context.getAssets().open("traceroute");
            int i = inputstream.available();
            byte buf[] = new byte[i];
            inputstream.read(buf);
            FileOutputStream fileoutputstream = new FileOutputStream(new File(traceroutePath));
            fileoutputstream.write(buf, 0, i);
            inputstream.close();
            fileoutputstream.close();
            Process process = Runtime.getRuntime().exec((new StringBuilder()).append("chmod 755 ")
                    .append(traceroutePath).toString());
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

    public static boolean isTracerouteInstalled()
    {
        try {
            Process process = Runtime.getRuntime().exec(traceroutePath);
            process.waitFor();
            process.destroy();
        }
        catch (Exception exception) {
            return false;
        }
        return true;
    }

    public static void traceroute(String hostname, HashMap<String, String> params, Handler handler) {
        String cmd = traceroutePath + " ";
        String options = "";
        int traceType = (params.containsKey("-I")) ? Traceroute.ICMP : Traceroute.UDP;
        String srcIp = PingUtil.getSrcIp();
        try {
            InetAddress ip = InetAddress.getByName(hostname);
            for(String flag : params.keySet()){
                options += (flag + " " + params.get(flag) + " ");
            }
            BufferedReader reader = CommandLineUtil.runBufferedCommand(cmd + options + ip.getHostAddress());
            String line = reader.readLine();
            if(line == null || line.contains("No address associated")) {
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

        for(int i=2; i<split.length; i++) {
            if(!split[i].equals("*")) {
                sum += Double.parseDouble(split[i]);
            }
        }
        double rtt = -1;
        if(split.length-2 != 0) {
            rtt = sum / (split.length-2);
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
