package com.num.controller.managers;

import android.content.Context;

import com.mobilyzer.MeasurementTask;
import com.mobilyzer.api.API;
import com.mobilyzer.exceptions.MeasurementError;
import com.num.model.Throughput;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ThroughputManager {

    public static void execute(Context c) {
        API mobilyzer = API.getAPI(c, "My Speed Test");
        Map<String, String> params = new HashMap<String, String>();
        int priority = MeasurementTask.USER_PRIORITY;
        Date endTime = null;
        int contextIntervalSec = 1;
        MeasurementTask task = null;
        ArrayList<MeasurementTask> taskList = new ArrayList<MeasurementTask>();

        try {
            task = mobilyzer.createTask(API.TaskType.TCPTHROUGHPUT, Calendar.getInstance().getTime(),
                    endTime, 60, 1, priority, contextIntervalSec, params);
            mobilyzer.submitTask(task);

            params.put("dir_up", "true");
            task = mobilyzer.createTask(API.TaskType.TCPTHROUGHPUT, Calendar.getInstance().getTime(),
                    endTime, 60, 1, priority, contextIntervalSec, params);
            mobilyzer.submitTask(task);
        }
        catch (MeasurementError e) {
            e.printStackTrace();
        }
    }

    public static String outputString(long l) {
        if(l <= 0){
            return "0.0 Mbps";
        }
        else if(l >= 1000000) {
            double d = (double) l / 1000000;
            String n = String.format("%.3f", d);
            return n + " Gbps";
        }
        else if(l >= 1000) {
            double d = (double) l / 1000;
            String n = String.format("%.3f", d);
            return n + " Mbps";
        }
        else {
            double d = (double) l;
            String n = String.format("%.3f", d);
            return n + " Kbps";
        }
    }
}
