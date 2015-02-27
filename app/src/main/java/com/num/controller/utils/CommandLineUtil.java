package com.num.controller.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandLineUtil {

    public static String runCommand(String cmd) {
        Process process;
        String line;
        String message = "";
        try {
            process = Runtime.getRuntime().exec(cmd);
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = bufferedReader.readLine()) != null) {
                message += line + "\n";
            }
            process.waitFor();
        } catch (Exception e) {}
        return message;
    }

    public static BufferedReader runBufferedCommand(String cmd) throws IOException {
        Process process = Runtime.getRuntime().exec(cmd);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        return reader;
    }
}
