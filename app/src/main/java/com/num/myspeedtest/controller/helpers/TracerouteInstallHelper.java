package com.num.myspeedtest.controller.helpers;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TracerouteInstallHelper {
    private static String traceroutePath = "/data/data/com.num.myspeedtest/traceroute";

    public static String getTraceroutePath() {
        return traceroutePath;
    }

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
            Process process = Runtime.getRuntime().exec((new StringBuilder()).append("chmod 755 ").append(traceroutePath).toString());
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
}
