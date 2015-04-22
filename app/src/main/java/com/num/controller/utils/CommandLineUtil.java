package com.num.controller.utils;

import android.nfc.Tag;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandLineUtil {
    final static String TAG = "CommandLineUtil";
    public static String runCommand(String cmd) throws IOException, InterruptedException {
        Log.d(TAG, "Executing: " + cmd);
        Process process;
        process = Runtime.getRuntime().exec(cmd);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String message = new String();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            message += line + "\n";
        }
        process.waitFor();
        process.destroy();
        return message;
    }

    public static BufferedReader runBufferedCommand(String cmd) throws IOException {
        Log.d(TAG, "Executing: " + cmd);
        Process process = Runtime.getRuntime().exec(cmd);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        return reader;
    }
}
