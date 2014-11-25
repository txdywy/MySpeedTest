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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class LatencyHelper {

    public ArrayList<Ping> execute() {
        ArrayList<Ping> results = new ArrayList<Ping>();
        ArrayList<Address> pingTargets = new Values().getTargets();
        for(Address dst : pingTargets) {
            new PingTask(dst,15).run();
        }
        return results;
    }

    private static Measure parsePingResult(String p) {
        String[] result = p.split("\n");
        String lastLine = result[result.length-1];
        System.out.println(lastLine);
        if(lastLine.contains("rtt")) {
            return null;
        }
        else {
            return new Measure(-1,-1,-1,-1);
        }
    }

    private class PingTask implements Runnable {

        private Address dst;
        private int count;

        public PingTask(Address dst, int count) {
            this.dst = dst;
            this.count = count;
        }

        @Override
        public void run() {
            pingIcmp(dst,count);
        }

        private Ping pingIcmp(Address address, int count) {
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
