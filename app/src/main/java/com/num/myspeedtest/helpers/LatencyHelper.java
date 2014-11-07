package com.num.myspeedtest.helpers;

import android.content.Context;

import com.mobilyzer.MeasurementTask;
import com.mobilyzer.api.API;
import com.mobilyzer.exceptions.MeasurementError;
import com.num.myspeedtest.models.Address;
import com.num.myspeedtest.models.Values;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class LatencyHelper {
    private static ArrayList<String> taskIDList;

    public static void execute(Context c) {
        API mobilyzer = API.getAPI(c, "My Speed Test");
        ArrayList<Address> pingTargets = new Values().getTargets();
        taskIDList = new ArrayList<String>();
        ArrayList<MeasurementTask> taskList = new ArrayList<MeasurementTask>();
        HashMap<String, String> params = new HashMap<String, String>();
        try {
            for (Address a : pingTargets) {
                MeasurementTask task;
                params.put("target", a.getIp());
                try {
                    task = mobilyzer.createTask(API.TaskType.PING, Calendar.getInstance().getTime()
                            , null, 1, 1, MeasurementTask.USER_PRIORITY, 1, params);
                    taskList.add(task);
                    taskIDList.add(task.getTaskId());
                } catch (MeasurementError e) {
                    e.printStackTrace();
                }
            }
            MeasurementTask task = mobilyzer.composeTasks(API.TaskType.PARALLEL,
                    Calendar.getInstance().getTime(), null, 1, 1, MeasurementTask.USER_PRIORITY,
                    1, params, taskList);
            mobilyzer.submitTask(task);
        } catch (MeasurementError e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getTaskIDList() {
        return taskIDList;
    }
}
