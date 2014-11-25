package com.num.myspeedtest.models;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommandLine {
    public String runCommand(String cmd, String input, String options) {
        String runtimeInput = "/system/bin/" + cmd + " " + options + " " + input;
        return runCommand(runtimeInput);
    }

    public String runCommand(String cmd) {
        Process process;
        String line;
        String message = "";

        try {
            process = Runtime.getRuntime().exec(cmd);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
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
