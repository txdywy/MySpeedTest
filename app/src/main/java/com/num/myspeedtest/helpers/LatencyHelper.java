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

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Andrew on 9/30/2014.
 */
public class LatencyHelper {
    private API mobilyzer;

    public LatencyHelper(Context c) {
        mobilyzer = API.getAPI(c, "My Speed Test");
    }

    public void executePing() {
        MeasurementTask task = null;
        HashMap<String, String> params = new HashMap<String, String>();
        IntentFilter filter = new IntentFilter();
        filter.addAction(mobilyzer.userResultAction);
        params.put("target", "www.google.com");
        try {
            task = mobilyzer.createTask(API.TaskType.PING, Calendar.getInstance().getTime()
            , null, 120, 1, MeasurementTask.USER_PRIORITY, 1, params);
            mobilyzer.submitTask(task);
        }
        catch (MeasurementError e) {
        }
    }
}
