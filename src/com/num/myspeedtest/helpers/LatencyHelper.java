package com.num.myspeedtest.helpers;

import android.content.Context;
import android.content.IntentFilter;
import com.mobilyzer.MeasurementTask;
import com.mobilyzer.api.API;
import com.mobilyzer.exceptions.MeasurementError;
import com.num.myspeedtest.models.Address;
import com.num.myspeedtest.models.Values;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Andrew on 9/30/2014.
 */
public class LatencyHelper {
    private static API mobilyzer;
    private static ArrayList<String> taskIDList;

    public static void execute(Context c) {
        mobilyzer = API.getAPI(c, "My Speed Test");
        IntentFilter filter = new IntentFilter();
        filter.addAction(mobilyzer.userResultAction);
        ArrayList<Address> pingTargets = (ArrayList<Address>) new Values().getTargets();
        taskIDList = new ArrayList<String>();
        ArrayList<MeasurementTask> taskList = new ArrayList<MeasurementTask>();
        HashMap<String, String> params = new HashMap<String, String>();
        for(Address a : pingTargets) {
            MeasurementTask task = null;
            params.put("target", a.getIp());
            try {
                task = mobilyzer.createTask(API.TaskType.PING, Calendar.getInstance().getTime()
                        , null, 1, 1, MeasurementTask.USER_PRIORITY, 1, params);
                taskList.add(task);
                taskIDList.add(task.getTaskId());
                mobilyzer.submitTask(task);
            }
            catch (MeasurementError e) {
            }
        }
/*        try {
            MeasurementTask task = mobilyzer.composeTasks(API.TaskType.PARALLEL,
                    Calendar.getInstance().getTime(), null, 1, 1, MeasurementTask.USER_PRIORITY,
                    1, params, taskList);
            mobilyzer.submitTask(task);
        }
        catch (MeasurementError e) {
            e.printStackTrace();
        }*/
    }

    public static ArrayList<String> getTaskIDList() {
        return taskIDList;
    }
}
