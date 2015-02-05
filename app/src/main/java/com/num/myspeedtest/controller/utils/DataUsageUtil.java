package com.num.myspeedtest.controller.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;

import com.num.myspeedtest.Constants;
import com.num.myspeedtest.db.datasource.DataUsageDataSource;
import com.num.myspeedtest.model.Application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataUsageUtil {

    private static SharedPreferences sharedpreferences;
    private static final String MobileData = "mobile_data";
    private static final String MobileRecv = "mobile_recv";
    private static final String MobileSent = "mobile_sent";

    public static List<Application> getApplications(Context context) {
        List<Application> applications = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> appInfo = pm.getInstalledApplications(0);
        HashMap<String, Boolean> runningApps = getRunningApplications(context);
        Set<Integer> uids = new HashSet<Integer>();
        DataUsageDataSource db = new DataUsageDataSource(context);
        db.open();

        for (ApplicationInfo info : appInfo) {
            Integer uid = info.uid;
            if (!uids.contains(uid)) {
                uids.add(uid);
                long recv = TrafficStats.getUidRxBytes(uid);
                long sent = TrafficStats.getUidTxBytes(uid);
                String pkgName = info.packageName;
                String appName = pkgName;
                try {
                    appName = info.loadLabel(pm).toString();
                } catch (Resources.NotFoundException e) {}
                Drawable appIcon = info.loadIcon(pm);
                boolean isForeground = false;
                boolean isRunning = runningApps.keySet().contains(pm.getPackagesForUid(uid)[0]);
                try {
                    isForeground = runningApps.get(pkgName);
                } catch(Exception e) {}
                Application app = new Application(appName, pkgName, appIcon, sent, recv, isRunning, isForeground);
                applications.add(app);
            }
        }
        db.close();
        Collections.sort(applications);
        return applications;
    }

    public static void updateOnBoot(Context context) {
        DataUsageDataSource db = new DataUsageDataSource(context);
        db.open();
        List<Application> applications = DataUsageUtil.getApplications(context);
        for (Application app : applications) {
            db.updateOnBoot(app);
        }
        db.close();
    }

    public static void clearTable(Context context) {
        DataUsageDataSource db = new DataUsageDataSource(context);
        db.open();
        List<Application> applications = DataUsageUtil.getApplications(context);
        for (Application app : applications) {
            db.updateOnReset(app);
        }
        db.close();
    }

    private static HashMap<String, Boolean> getRunningApplications(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes = manager.getRunningAppProcesses();
        HashMap<String, Boolean> runningApplications = new HashMap<>();

        for (ActivityManager.RunningAppProcessInfo p : processes) {
            boolean isForeground = false;
            if(p.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                isForeground = true;
            }
            runningApplications.put(p.processName, isForeground);
        }

        return runningApplications;
    }

    public static void setFirstMonthOfTheMonthFlag(Context context, int month){
        SharedPreferences pref = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(Constants.NEXT_MONTHLY_RESET, month);
    }

    public static void resetMobileData(Context context) {
        if (sharedpreferences == null)
            sharedpreferences = context.getSharedPreferences(MobileData, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();

        long mobile_recv = 0;
        long mobile_sent = 0;
        Logger.show("Mobile sent: " + mobile_sent + " recv: " + mobile_recv);

        editor.putLong(MobileRecv, mobile_recv);
        editor.putLong(MobileSent, mobile_sent);
        editor.commit();
    }


    public static void updateMobileData(Context context) {
        if (sharedpreferences == null)
            sharedpreferences = context.getSharedPreferences(MobileData, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();

        long mobile_recv = TrafficStats.getMobileRxBytes();
        long mobile_sent = TrafficStats.getMobileTxBytes();
        Logger.show("Mobile sent: " + mobile_sent + " recv: " + mobile_recv);

        editor.putLong(MobileRecv, mobile_recv);
        editor.putLong(MobileSent, mobile_sent);
        editor.commit();
    }

    public static long getMobileSent(Context context) {
        if (sharedpreferences == null)
            sharedpreferences = context.getSharedPreferences(MobileData, Context.MODE_PRIVATE);
        long sent = sharedpreferences.getLong(MobileSent, 0);
        return sent;
    }

    public static long getMobileRecv(Context context) {
        if (sharedpreferences == null)
            sharedpreferences = context.getSharedPreferences(MobileData, Context.MODE_PRIVATE);
        long recv = sharedpreferences.getLong(MobileRecv, 0);
        return recv;
    }

    public static String getUsageString(long usage) {
        if(usage >= 1000000000) {
            double d = (double) usage / 1000000000;
            String n = String.format("%.1f", d);
            return n + " GB";
        }
        else if(usage >= 1000000) {
            double d = (double) usage / 1000000;
            String n = String.format("%.1f", d);
            return n + " MB";
        }
        else {
            return "< 1 MB";
        }
    }
}
