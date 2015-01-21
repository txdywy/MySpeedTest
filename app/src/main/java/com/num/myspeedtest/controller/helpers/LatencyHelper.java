package com.num.myspeedtest.controller.helpers;

import android.os.Handler;

import com.num.myspeedtest.controller.managers.LatencyManager;
import com.num.myspeedtest.controller.utils.PingUtil;
import com.num.myspeedtest.controller.utils.ServerUtil;
import com.num.myspeedtest.model.Address;
import com.num.myspeedtest.model.Ping;

import java.util.ArrayList;
import java.util.List;


public class LatencyHelper {

    private Handler handler;

    public LatencyHelper(Handler handler) {
        this.handler = handler;
    }

    public void execute() {
        List<Address> targets = ServerUtil.getTargets();
        LatencyManager latencyManager = new LatencyManager(handler);
        latencyManager.execute(targets);
    }

    /**
     * Mobilyzer version of ping test
     * @param c Context that executed the measurement
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
    } */
}
