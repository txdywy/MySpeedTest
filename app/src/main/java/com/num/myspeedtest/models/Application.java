package com.num.myspeedtest.models;

import android.graphics.drawable.Drawable;

import org.json.JSONObject;

/**
 * Created by Andrew on 9/23/2014.
 */
public class Application implements BaseModel, Comparable<Application> {
    private String name;
    private String packageName;
    private long totalSent;
    private long totalRecv;
    private boolean isRunning;
    private Drawable icon;
    private final String DESCRIPTION = "Shows data sent and received for application";

    public Application(String name, String pkg, Drawable icon) {
        this.name = name;
        this.packageName = pkg;
        this.icon = icon;
    }

    public String getName() {
        return this.name;
    }

    public Drawable getIcon() { return this.icon; }

    public void setTotalSent(long i) { this.totalSent = i; }

    public void setTotalRecv(long i) { this.totalRecv = i; }

    public void setIsRunning(boolean b) { this.isRunning = b; }

    public long getTotal() { return totalRecv + totalSent; }

    public String getUsageString() {
        long total = getTotal();
        if(total >= 1000000000) {
            double d = (double) total / 1000000000;
            String n = String.format("%.1f", d);
            return n + " GB";
        }
        else if(total >= 1000000) {
            double d = (double) total / 1000000;
            String n = String.format("%.1f", d);
            return n + " MB";
        }
        else {
            return "< 1 MB";
        }
    }

    public int getPercentage(long total) {
        return (int)(getTotal()*100 / total);
    }

    public String getPercentageString(long total) {
        int percent = getPercentage(total);
        if(percent > 1) {
            return percent + "%";
        }
        else {
            return "< 1%";
        }
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        return json;
    }

    public int compareTo(Application a) {
        if(this.getTotal() > a.getTotal()) return -1;
        else if(this.getTotal() < a.getTotal()) return 1;
        else return 0;
    }
}
