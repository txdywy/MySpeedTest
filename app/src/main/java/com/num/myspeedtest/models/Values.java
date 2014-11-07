package com.num.myspeedtest.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Values {
    private static Map<String, Address> pingTargets = new HashMap<String, Address>();

    public Values() {
        pingTargets = new HashMap<String, Address>();
        pingTargets.put("www.google.com", new Address("www.google.com", "Google", "ping"));
        pingTargets.put("www.facebook.com", new Address("www.facebook.com", "Facebook", "ping"));
        pingTargets.put("www.twitter.com", new Address("www.twitter.com", "Twitter", "ping"));
        pingTargets.put("www.youtube.com", new Address("www.youtube.com", "YouTube", "ping"));
        pingTargets.put("www.bing.com", new Address("www.bing.com", "Bing", "ping"));
        pingTargets.put("www.wikipedia.com", new Address("www.wikipedia.com", "Wikipedia", "ping"));
        pingTargets.put("143.215.131.173", new Address("143.215.131.173", "GATech", "ping"));
    }

    public ArrayList<Address> getTargets() {
        ArrayList<Address> targets = new ArrayList<Address>();
        for(String key : pingTargets.keySet()) {
            targets.add(pingTargets.get(key));
        }
        return targets;
    }

    public String getLabel(String ip) {
        return pingTargets.get(ip).getTagName();
    }

    public Address getAddress(String ip) {
        return pingTargets.get(ip);
    }
}
