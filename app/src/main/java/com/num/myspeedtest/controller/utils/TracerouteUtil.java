package com.num.myspeedtest.controller.utils;

import android.content.Context;

import com.num.myspeedtest.model.Traceroute;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public static Traceroute[] traceroute(String address, HashMap<String, String> params) {
        String output = executeTraceroute(address, params);
        Traceroute[] traceroutes = parseTracerouteResult(output);
        return traceroutes;
    }

    public static String executeTraceroute(String address, HashMap<String, String> params) {
        String cmd = traceroutePath + " ";
        String options = "";
        for(String flag : params.keySet()){
            options += (flag + " " + params.get(flag) + " ");
        }
        return CommandLineUtil.runCommand(cmd + options + address);
    }

    private static Traceroute[] parseTracerouteResult(String result) {
        String[] output = result.split("\n");
        return null;
    }
}
