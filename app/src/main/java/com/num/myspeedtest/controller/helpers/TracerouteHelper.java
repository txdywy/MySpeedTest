package com.num.myspeedtest.controller.helpers;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.mobilyzer.MeasurementTask;
import com.mobilyzer.api.API;
import com.mobilyzer.exceptions.MeasurementError;
import com.num.myspeedtest.controller.managers.TracerouteManager;
import com.num.myspeedtest.controller.utils.CommandLineUtil;
import com.num.myspeedtest.model.Traceroute;
import com.num.myspeedtest.model.TracerouteEntry;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class TracerouteHelper {

    private Handler handler;

    public TracerouteHelper(Handler handler){
        this.handler = handler;
    }

    public void execute(String address){
        TracerouteManager tracerouteManager = new TracerouteManager(handler);
        tracerouteManager.execute(address);
    }

    public static TracerouteEntry tracerouteHelper(String address, int index) {
        CommandLineUtil cmdLine = new CommandLineUtil();
        String cmd = "ping";
        String options = "-c " + 1 + " -t " + index;
        String output = "";
        output = cmdLine.runCommand(cmd, address, options);
        TracerouteEntry hop = parseResult(output, index);
        return hop;
    }

    static TracerouteEntry parseResult(String result, int index)
    {
        boolean found = false;
        String hostName = "";
        String hostAddress = "";

        Log.d("TraceHelp", "index: " + index);
        String[] output = result.split("\n");
        int count = 0;
        for(String a: output){
            Log.d("TraceHelp", count + " : " + a);
            count++;
        }

        String secondLine = output[1];
        if(secondLine.length()>0) {
            String[] split = secondLine.split(" ");
            if (secondLine.contains("Time to live exceeded")) {
                if (split[2].contains("(") && split[2].contains(")")) {
                    hostName = split[1];
                    hostAddress = split[2].substring(1, split[2].length() - 2);
                } else {
                    hostName = split[1].substring(0, split[1].length() - 1);
                    hostAddress = split[1].substring(0, split[1].length() - 1);
                }
                found = true;
            } else if (secondLine.contains("bytes from")) {
                if (split[2].contains("(") && split[2].contains(")")) {
                    hostName = split[3];
                    hostAddress = split[4].substring(1, split[4].length() - 2);
                } else {
                    hostName = split[3].substring(0, split[3].length() - 1);
                    hostAddress = split[3].substring(0, split[3].length() - 1);
                }
                found = true;
            }
        }

        String lastLine = output[output.length-1];
        NumberFormat nf = NumberFormat.getInstance();

        double avg=0;
        if(lastLine.contains("rtt")) {
            lastLine = lastLine.substring(23,lastLine.length()-3);
            String[] split = lastLine.split("/");
            try {
                Number n = nf.parse(split[1]);
                avg = n.doubleValue();
                found = true;
            } catch (ParseException e) {
                avg = 0;
            }
        }

        if(found==true)
        {
//            return new TracerouteEntry(parsedResult,parsedResult, ""+endTime, index);
            return new TracerouteEntry(hostAddress,hostName, ""+avg, index);
        }
        else
        {
            return new TracerouteEntry("***","*", "*", index);
        }
    }

}
