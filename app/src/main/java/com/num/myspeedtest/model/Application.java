package com.num.myspeedtest.model;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Application implements BaseModel, Comparable<Application>, Parcelable {
    private String name;
    private String packageName;
    private long totalSent;
    private long totalRecv;
    private boolean isRunning;
    private Drawable icon;

    public Application(String name, String pkg, Drawable icon,
                       long sent, long recv, boolean isRunning) {
        this.name = name;
        this.packageName = pkg;
        this.icon = icon;
        this.totalSent = sent;
        this.totalRecv = recv;
        this.isRunning = isRunning;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getName() {
        return this.name;
    }

    public long getTotal() {
        return totalRecv + totalSent;
    }

    public long getTotalSent() { return totalSent; }

    public void setTotalSent(long totalSent) {
        this.totalSent = totalSent;
    }

    public long getTotalRecv() { return totalRecv; }

    public void setTotalRecv(long totalRecv) {
        this.totalRecv = totalRecv;
    }

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
        if(total == 0) {
            return 0;
        }
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

    public Drawable getIcon() {
        return this.icon;
    }

    public void setIcon(Drawable drawable) {
        this.icon = drawable;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.putOpt("name", name);
            json.putOpt("packageName", packageName);
            json.putOpt("total_sent", totalSent);
            json.putOpt("total_recv", totalRecv);
            if(isRunning) {
                json.putOpt("isRunning", 1);
            }
            else {
                json.putOpt("isRunning", 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    @Override
    public int compareTo(Application a) {
        if(this.getTotal() > a.getTotal()) return -1;
        else if(this.getTotal() < a.getTotal()) return 1;
        else return 0;
    }

    @Override
    public String toString() {
        return name + " (" + packageName + ") => recv: " + totalRecv +", send: " + totalSent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(packageName);
        dest.writeLong(totalSent);
        dest.writeLong(totalRecv);
        dest.writeValue(new Boolean(isRunning));
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public Application createFromParcel(Parcel source) {
            return new Application(source);
        }

        @Override
        public Application[] newArray(int size) {
            return new Application[size];
        }
    };

    private Application(Parcel source) {
        name = source.readString();
        packageName = source.readString();
        totalSent = source.readLong();
        totalRecv = source.readLong();
        isRunning = (Boolean) source.readValue(getClass().getClassLoader());
    }
}