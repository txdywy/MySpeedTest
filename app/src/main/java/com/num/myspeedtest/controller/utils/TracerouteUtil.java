package com.num.myspeedtest.controller.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.num.myspeedtest.model.Traceroute;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
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
        try {
            InetAddress ip = InetAddress.getByName(hostname);
            for(String flag : params.keySet()){
                options += (flag + " " + params.get(flag) + " ");
            }
            BufferedReader reader = CommandLineUtil.runBufferedCommand(cmd + options + ip.getHostAddress());
            String line = reader.readLine();
            while((line=reader.readLine())!=null) {
                Traceroute traceroute = parseTracerouteResult(line);
                Bundle bundle = new Bundle();
                bundle.putParcelable("traceroute", traceroute);
                Message msg = new Message();
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        } catch (UnknownHostException e) {
        } catch (IOException e) {
        }
    }

    private static Traceroute parseTracerouteResult(String result) {
        String line = result.replaceAll(" +", " ").replaceAll(" ms", ", ").replaceAll(" \\*", " *,").trim()
                .replaceAll(",$", "");
        String[] split = line.split(" ");
        int hop = Integer.parseInt(split[0]);
        String address = split[1];
        if(address.equals("*")) {
            return new Traceroute(address, "*", "*", -1, hop);
        }
        double rtt = Double.parseDouble(split[2]);
        String hostname = address;
        try {
            hostname = InetAddress.getByName(address).getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String asn = DNSUtil.getAsnByIp(address);
        return new Traceroute(address, hostname, asn, rtt, hop);
    }

}
