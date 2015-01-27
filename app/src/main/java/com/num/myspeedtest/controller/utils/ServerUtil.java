package com.num.myspeedtest.controller.utils;

import com.num.myspeedtest.model.Address;

import java.util.ArrayList;
import java.util.List;

public class ServerUtil {

    //TODO Make this dynamic
    public static List<Address> getTargets() {

        List<Address> pingTargets = new ArrayList<>();
        pingTargets.add(new Address("www.google.com", "Google", "ping"));
        pingTargets.add(new Address("www.facebook.com", "Facebook", "ping"));
        pingTargets.add(new Address("www.twitter.com", "Twitter", "ping"));
        pingTargets.add(new Address("www.youtube.com", "YouTube", "ping"));
        pingTargets.add(new Address("www.bing.com", "Bing", "ping"));
        pingTargets.add(new Address("www.wikipedia.com", "Wikipedia", "ping"));
        pingTargets.add(new Address("143.215.131.173", "GATech", "ping"));

/*      pingTargets.add(new Address("www.google.com", "Google", "firsthop"));
        pingTargets.add(new Address("www.facebook.com", "Facebook", "firsthop"));
        pingTargets.add(new Address("www.twitter.com", "Twitter", "firsthop"));
        pingTargets.add(new Address("www.youtube.com", "YouTube", "firsthop"));
        pingTargets.add(new Address("www.bing.com", "Bing", "firsthop"));
        pingTargets.add(new Address("www.wikipedia.com", "Wikipedia", "firsthop"));
        pingTargets.add(new Address("143.215.131.173", "GATech", "firsthop"));*/

        return pingTargets;
    }
}
