package com.num.myspeedtest.controller.utils;

import android.app.ActivityManager;
import android.content.Context;
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

    public static boolean initialized = false;

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
        initialized = true;
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

    public static boolean isInitialized() {
        return initialized;
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
}
