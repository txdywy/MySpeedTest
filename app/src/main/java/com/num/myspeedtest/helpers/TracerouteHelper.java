package com.num.myspeedtest.helpers;

import android.content.Context;
import com.num.myspeedtest.models.Traceroute;
import com.num.myspeedtest.models.TracerouteEntry;

import java.util.List;

public class TracerouteHelper {

    public static List<TracerouteEntry> getTracerouteResult(Context context){

        //HARD CODED JUST FOR NOW; FOR TESTING PURPOSES
        Traceroute traceroute = new Traceroute(1, 3);

        TracerouteEntry entry1 = new TracerouteEntry("111.111.111.111", "some.src.com", "1.111 ms", 1);
        TracerouteEntry entry2 = new TracerouteEntry("222.222.222.222", "some.place.com", "2.222 ms", 2);
        TracerouteEntry entry3 = new TracerouteEntry("333.333.333.333", "some.dst.com", "3.333 ms", 3);

        traceroute.addToList(entry1);
        traceroute.addToList(entry2);
        traceroute.addToList(entry3);

        return traceroute.getDisplayData(context);
    }
}
