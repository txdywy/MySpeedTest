package com.num.myspeedtest.helpers;

import android.content.Context;
import android.content.IntentFilter;

import com.mobilyzer.MeasurementTask;
import com.mobilyzer.api.API;
import com.mobilyzer.exceptions.MeasurementError;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew on 9/30/2014.
 */
public class ThroughputHelper {
    private static API mobilyzer;

    public static void execute(Context c) {
        mobilyzer = API.getAPI(c, "My Speed Test");
        Map<String, String> params = new HashMap<String, String>();
        int priority = MeasurementTask.USER_PRIORITY;
        Date endTime = null;
        int contextIntervalSec = 1;
        MeasurementTask task = null;
        ArrayList<MeasurementTask> taskList = new ArrayList<MeasurementTask>();
        try {
            params.put("dir_up", "false");
            task = mobilyzer.createTask(API.TaskType.TCPTHROUGHPUT, Calendar.getInstance().getTime(),
                    endTime, 120, 1, priority, contextIntervalSec, params);
            mobilyzer.submitTask(task);

            params.put("dir_up", "true");
            task = mobilyzer.createTask(API.TaskType.TCPTHROUGHPUT, Calendar.getInstance().getTime(),
                    endTime, 120, 1, priority, contextIntervalSec, params);
            mobilyzer.submitTask(task);
        }
        catch (MeasurementError e) {
            e.printStackTrace();
        }
    }

    public static String outputString(long l) {
        if(l >= 1000000) {
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
            String n = String.format("%.3f", l);
            return n + " Kbps";
        }
    }
}