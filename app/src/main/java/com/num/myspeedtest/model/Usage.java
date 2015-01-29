package com.num.myspeedtest.model;

import android.net.TrafficStats;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Usage implements BaseModel, Parcelable {

    private List<Application> applications;
    public static long totalSent = 0;
    public static long totalRecv = 0;
    public static long mobileSent = 0;
    public static long mobileRecv = 0;
    public static long maxUsage = 0;

    public Usage(List<Application> applications) {
        this.applications = applications;
    }

    public List<Application> getApplications() {
        return applications;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        for(Application app : applications) {
            array.put(app.toJSON());
        }
        try {
            json.putOpt("applications", array);
            json.putOpt("total_sent", totalSent);
            json.putOpt("total_recv", totalRecv);
            json.putOpt("mobile_sent", mobileSent);
            json.putOpt("mobile_recv", mobileRecv);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
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
