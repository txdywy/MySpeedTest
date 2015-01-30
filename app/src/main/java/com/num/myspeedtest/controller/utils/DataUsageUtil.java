package com.num.myspeedtest.controller.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;

import com.num.myspeedtest.db.datasource.DataUsageDataSource;
import com.num.myspeedtest.model.Application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataUsageUtil {

    private static SharedPreferences sharedpreferences;
    private static final String MobileData = "mstmobiledata";
    private static final String MobileRecv = "mobile_recv";
    private static final String MobileSent = "mobile_sent";

    public static List<Application> getApplications(Context context) {
        List<Application> applications = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> appInfo = pm.getInstalledApplications(0);
        List<String> runningApps = getRunningApplications(context);
        Set<Integer> uids = new HashSet<Integer>();

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
                boolean isRunning = runningApps.contains(pm.getPackagesForUid(uid)[0]);

                Application app = new Application(appName, pkgName, appIcon, sent, recv, isRunning);
                applications.add(app);
            }
        }
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
    }

    private static List<String> getRunningApplications(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes = manager.getRunningAppProcesses();
        ArrayList<String> runningApplications = new ArrayList<>();

        for (ActivityManager.RunningAppProcessInfo p : processes) {
            runningApplications.add(p.processName);
        }

        return runningApplications;
    }

    public static void updateMobileData(Context context) {
        if (sharedpreferences == null)
            sharedpreferences = context.getSharedPreferences(MobileData, Context.MODE_PRIVATE);
        long mobile_recv = TrafficStats.getMobileRxBytes();
        long mobile_sent = TrafficStats.getMobileTxBytes();
        Logger.show("Mobile sent: " + mobile_sent + " recv: " + mobile_recv);
        SharedPreferences.Editor editor = sharedpreferences.edit();
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
}
