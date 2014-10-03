package com.num.myspeedtest.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;

import com.mobilyzer.MeasurementResult;
import com.mobilyzer.MeasurementTask;
import com.mobilyzer.UpdateIntent;
import com.mobilyzer.api.API;
import com.mobilyzer.exceptions.MeasurementError;
import com.mobilyzer.measurements.TCPThroughputTask.TCPThroughputDesc;
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

    public static void execute(Context c) {
        mobilyzer = API.getAPI(c, "My Speed Test");
        IntentFilter filter = new IntentFilter();
        filter.addAction(mobilyzer.userResultAction);
        ArrayList<Address> pingTargets = (ArrayList<Address>) new Values().getTargets();
        ArrayList<MeasurementTask> taskList = new ArrayList<MeasurementTask>();
        HashMap<String, String> params = new HashMap<String, String>();
        for(Address a : pingTargets) {
            MeasurementTask task = null;
            params.put("target", a.getIp());
            try {
                task = mobilyzer.createTask(API.TaskType.PING, Calendar.getInstance().getTime()
                        , null, 1, 1, MeasurementTask.USER_PRIORITY, 1, params);
                taskList.add(task);
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
        }
*/
    }
}
