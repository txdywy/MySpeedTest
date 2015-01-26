package com.num.myspeedtest.view.activities;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.num.myspeedtest.R;
import com.num.myspeedtest.model.Application;
import com.num.myspeedtest.model.Usage;

public class ApplicationDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_detail);

        ImageView icon = (ImageView) findViewById(R.id.icon_application_detail);
        TextView name = (TextView) findViewById(R.id.individual_app_page_name);
        TextView total = (TextView) findViewById(R.id.individual_app_page_total_data_used_by_app_value);
        TextView send = (TextView) findViewById(R.id.individual_app_page_sent_data_used_by_app_value);
        TextView recv = (TextView) findViewById(R.id.individual_app_page_recv_data_used_by_app_value);
        TextView percent = (TextView) findViewById(R.id.individual_app_page_percentage_used_by_app_value);
        ProgressBar progress = (ProgressBar) findViewById(R.id.individual_app_page_value);

        Bundle extras = getIntent().getExtras();
        PackageManager pm = getPackageManager();
        Application application = extras.getParcelable("application");
        Drawable appIcon = null;
        try {
            ApplicationInfo info = pm.getApplicationInfo(application.getPackageName(), 0);
            appIcon = info.loadIcon(pm);
            application.setIcon(appIcon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return;
        }


        long recvTraffic = application.getTotalRecv();
        long sentTraffic = application.getTotalSent();
        long totalTraffic = application.getTotal();
        long globalTraffic = Usage.getTotalUsage();
        long globalMax = Usage.getMaxUsage();
        int percentValue = (int) (totalTraffic*100/globalTraffic);
        int progressValue = (int) (totalTraffic*100/globalMax);

        icon.setImageDrawable(application.getIcon());
        name.setText(application.getName());
        total.setText(getUsageString(totalTraffic));
        send.setText(getUsageString(sentTraffic));
        recv.setText(getUsageString(recvTraffic));
        percent.setText(percentValue+"%");
        progress.setProgress(progressValue);

    }

    private String getUsageString(long usage) {
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
