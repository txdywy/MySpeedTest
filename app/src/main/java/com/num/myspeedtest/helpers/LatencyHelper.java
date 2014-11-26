package com.num.myspeedtest.helpers;

import android.content.Context;

import com.mobilyzer.MeasurementTask;
import com.mobilyzer.api.API;
import com.mobilyzer.exceptions.MeasurementError;
import com.num.myspeedtest.models.Address;
import com.num.myspeedtest.models.CommandLine;
import com.num.myspeedtest.models.Measure;
import com.num.myspeedtest.models.Ping;
import com.num.myspeedtest.models.Values;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LatencyHelper {

    public static Ping pingIcmp(Address address, int count) {
        CommandLine cmdLine = new CommandLine();
        double timeGap = 0.5;
        String cmd = "ping";
        String options = "-c" + count + " -i" + timeGap;
        String src = "";
        String dst = address.getIp();
        String output = cmdLine.runCommand(cmd, dst, options);
        Measure measure = parsePingResult(output);
        return new Ping(src, address, measure);
    }

    public static Measure parsePingResult(String p) {
        String[] output = p.split("\n");
        String lastLine = output[output.length-1];
        System.out.println(lastLine);
        if(lastLine.contains("rtt")) {
            NumberFormat nf = NumberFormat.getInstance();
            double min, avg, max, dev;
            lastLine = lastLine.substring(23,lastLine.length()-3);
            String[] result = lastLine.split("/");
            try {
                Number n = nf.parse(result[0]);
                min = n.doubleValue();
                n = nf.parse(result[1]);
                avg = n.doubleValue();
                n = nf.parse(result[2]);
                max = n.doubleValue();
                n = nf.parse(result[3]);
                dev = n.doubleValue();
            } catch (ParseException e) {
                return new Measure(-1,-1,-1,-1);
            }
            return new Measure(min, max, avg, dev);
        }
        else {
            return new Measure(-1,-1,-1,-1);
        }
    }

    /**
     * Mobilyzer version of ping test
     * @param c Context that executed the measurement
     */
    public static void execute(Context c) {
        API mobilyzer = API.getAPI(c, "My Speed Test");
        ArrayList<Address> pingTargets = new Values().getTargets();
        ArrayList<MeasurementTask> taskList = new ArrayList<MeasurementTask>();
        HashMap<String, String> params = new HashMap<String, String>();
        try {
            for (Address a : pingTargets) {
                params.put("target", a.getIp());
                MeasurementTask task = mobilyzer.createTask(API.TaskType.PING, Calendar.getInstance().getTime()
                        , null, 1, 1, MeasurementTask.USER_PRIORITY, 1, params);
                taskList.add(task);
            }
            MeasurementTask task = mobilyzer.composeTasks(API.TaskType.PARALLEL,
                    Calendar.getInstance().getTime(), null, 1, 1, MeasurementTask.USER_PRIORITY,
                    1, params, taskList);
            mobilyzer.submitTask(task);
        } catch (MeasurementError e) {
            e.printStackTrace();
        }
    }
}
