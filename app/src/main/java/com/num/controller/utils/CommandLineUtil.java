package com.num.controller.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandLineUtil {

    public static String runCommand(String cmd) throws IOException, InterruptedException {
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
        Process process = Runtime.getRuntime().exec(cmd);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        return reader;
    }
}
