package com.num.myspeedtest.model;

import android.net.TrafficStats;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.List;

public class Usage implements BaseModel, Parcelable {

    private List<Application> applications;
    private static long totalSent;
    private static long totalRecv;
    private static long mobileSent;
    private static long mobileRecv;
    private static long maxUsage;

    public Usage(List<Application> applications) {
        this.applications = applications;
        totalSent = TrafficStats.getTotalTxBytes();
        totalRecv = TrafficStats.getTotalRxBytes();
        mobileSent = TrafficStats.getMobileTxBytes();
        mobileRecv = TrafficStats.getMobileRxBytes();
        maxUsage = findMaxUsage();
    }

    public static long getTotalUsage() {
        return totalSent + totalRecv;
    }

    public Application[] getApplications() {
        return applications.toArray(new Application[applications.size()]);
    }

    public long getTotalSent() {
        return totalSent;
    }

    public long getTotalRecv() {
        return totalRecv;
    }

    public long getMobileSent() {
        return mobileSent;
    }

    public long getMobileRecv() {
        return mobileRecv;
    }

    public static long getMaxUsage() {
        return maxUsage;
    }

    @Override
    public JSONObject toJSON() {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(applications);
        dest.writeLong(totalSent);
        dest.writeLong(totalRecv);
        dest.writeLong(mobileSent);
        dest.writeLong(mobileRecv);
        dest.writeLong(maxUsage);
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public Usage createFromParcel(Parcel source) {
            return new Usage(source);
        }

        @Override
        public Usage[] newArray(int size) {
            return new Usage[size];
        }
    };

    private Usage(Parcel source) {
        totalSent = source.readLong();
        totalRecv = source.readLong();
        mobileSent = source.readLong();
        mobileRecv = source.readLong();
        maxUsage = source.readLong();
    }

    private long findMaxUsage() {
        long max = 0;
        if(applications!=null) {
            for(Application app : applications) {
                if(app.getTotal()>max) {
                    max = app.getTotal();
                }
            }
        }
        return max;
    }
}
