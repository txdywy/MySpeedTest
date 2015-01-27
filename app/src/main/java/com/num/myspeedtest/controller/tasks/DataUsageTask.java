package com.num.myspeedtest.controller.tasks;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.num.myspeedtest.model.Application;
import com.num.myspeedtest.model.Usage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataUsageTask implements Runnable {

    private Context context;
    private Handler handler;

    public DataUsageTask(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    public void run() {
        List<Application> applications = getApplicationUsage();
        Usage usage = new Usage(applications);

        Bundle bundle = new Bundle();
        bundle.putString("type", "usage");
        bundle.putParcelable("usage", usage);

        Message msg = new Message();
        msg.setData(bundle);
        handler.sendMessage(msg);

    }

    private List<Application> getApplicationUsage() {
        List<Application> applications = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> appInfo = pm.getInstalledApplications(0);
        List<String> runningApps = getRunningApplications();
        Set<Integer> uids = new HashSet<Integer>();

        for (ApplicationInfo info : appInfo) {
            Integer uid = info.uid;
            if (!uids.contains(uid)) {
                uids.add(uid);
                long recv = TrafficStats.getUidRxBytes(uid);
                long sent = TrafficStats.getUidTxBytes(uid);
                if (recv > 0 || sent > 0) {
                    String appName = info.loadLabel(pm).toString();
                    String pkgName = info.packageName;
                    Drawable appIcon = info.loadIcon(pm);
                    boolean isRunning = runningApps.contains(pm.getPackagesForUid(uid)[0]);

                    Application app = new Application(appName, pkgName, appIcon, sent, recv, isRunning);
                    applications.add(app);
                }
            }
        }
        Collections.sort(applications);
        return applications;
    }

    private List<String> getRunningApplications() {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes = manager.getRunningAppProcesses();
        ArrayList<String> runningApplications = new ArrayList<>();

        for(ActivityManager.RunningAppProcessInfo p: processes) {
            runningApplications.add(p.processName);
        }

        return runningApplications;
    }
}
