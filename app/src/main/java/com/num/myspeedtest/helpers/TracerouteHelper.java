package com.num.myspeedtest.helpers;

import android.content.Context;

import com.mobilyzer.MeasurementTask;
import com.mobilyzer.api.API;
import com.mobilyzer.exceptions.MeasurementError;
import com.num.myspeedtest.models.Address;
import com.num.myspeedtest.models.Traceroute;
import com.num.myspeedtest.models.TracerouteEntry;
import com.num.myspeedtest.models.Values;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class TracerouteHelper {

//    private static String address;
//
//    public TracerouteHelper(String address){
//        this.address = address;
//    }

    public static void execute(Context c, String address) {
        API mobilyzer = API.getAPI(c, "My Speed Test");

        HashMap<String, String> params = new HashMap<String, String>();

        try{
            params.put("target", address);
            params.put("max_hop_count", "15"); //for now
            MeasurementTask task = mobilyzer.createTask(API.TaskType.TRACEROUTE, Calendar.getInstance().getTime(),
                    null, 1, 1, MeasurementTask.USER_PRIORITY, 1, params);
            mobilyzer.submitTask(task);
        }catch(MeasurementError e){
            e.printStackTrace();
        }
    }

    public static List<TracerouteEntry> getTracerouteResult(Context context){

        //HARD CODED JUST FOR NOW; FOR TESTING PURPOSES
        Traceroute traceroute = new Traceroute(1, 3);

        TracerouteEntry entry1 = new TracerouteEntry("111.111.111.111", "some.src.com", "1.111 ms", 1);
        TracerouteEntry entry2 = new TracerouteEntry("222.222.222.222", "some.place.com", "2.222 ms", 2);
        TracerouteEntry entry3 = new TracerouteEntry("333.333.333.333", "some.dst.com", "3.333 ms", 3);

        traceroute.addToList(entry1);
        traceroute.addToList(entry2);
        traceroute.addToList(entry3);

        return traceroute.getDisplayData();
    }
}
