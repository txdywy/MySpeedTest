package com.num.myspeedtest.helpers;

import android.content.Context;
import android.util.Log;

import com.mobilyzer.MeasurementTask;
import com.mobilyzer.api.API;
import com.mobilyzer.exceptions.MeasurementError;
import com.num.myspeedtest.models.Address;
import com.num.myspeedtest.models.CommandLine;
import com.num.myspeedtest.models.Measure;
import com.num.myspeedtest.models.Ping;
import com.num.myspeedtest.models.Traceroute;
import com.num.myspeedtest.models.TracerouteEntry;
import com.num.myspeedtest.models.Values;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.NumberFormat;
import java.text.ParseException;
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

    /**
     * For use with Mobilyzer API
     * @param c
     * @param address address to be traceroute to
     */
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

    public static TracerouteEntry tracerouteHelper(String address, int index) {
        CommandLine cmdLine = new CommandLine();
        double timeGap = 0.5;
        String cmd = "ping";
        String options = "-c " + 1 + " -i " + timeGap + " -t " + index;
//        String options = "-c " + 3 + " -t " + index;
        String output = "";
        long startTime = System.nanoTime();
        output = cmdLine.runCommand(cmd, address, options);
        float endTime = (System.nanoTime() - startTime) / 1000000.0f;
        TracerouteEntry hop = parseResult(output, index, endTime);
        return hop;
    }

    static TracerouteEntry parseResult(String result, int index, float endTime)
    {
        String ipAddr = "";
        String ipName="";
        String ipBits = "";
        boolean found = false;
        String parsedResult= "";
        int pos;
        //int hop = startindex;
        for(pos=0; pos<result.length(); pos++)
        {
            parsedResult = "";
            if(result.charAt(pos)=='F')
            {
                found = true;
            }
            if(found==true){
                pos+=5;

                while(result.charAt(pos)!=' '&&result.charAt(pos)!=':')
                {
                    parsedResult += result.charAt(pos);
                    pos++;
                }
                break;
            }
        }
        if(found==true)
        {
//            Log.d("TraceHelp", "Parsed result " + parsedResult + " endTime: " + endTime + " index: " + index);
            return new TracerouteEntry(parsedResult,parsedResult, ""+endTime, index);
        }
        else
        {
            return new TracerouteEntry("***","*", "*", index);
        }
    }

    /**
     * For testing purposes
     * @param context
     * @return
     */
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
