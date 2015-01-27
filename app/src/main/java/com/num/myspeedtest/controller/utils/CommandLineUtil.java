package com.num.myspeedtest.controller.utils;

import java.io.BufferedReader;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }
}
