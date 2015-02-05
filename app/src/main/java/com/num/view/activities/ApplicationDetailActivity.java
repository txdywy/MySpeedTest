package com.num.view.activities;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.num.R;
import com.num.controller.utils.DataUsageUtil;
import com.num.model.Application;
import com.num.model.Usage;

/**
 * Activity showing the data usage of individual applications.
 */
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

        long recvTraffic = application.getTotalRecv();
        long sentTraffic = application.getTotalSent();
        long totalTraffic = application.getTotal();
        long globalTraffic = Usage.totalRecv + Usage.totalSent;
        long globalMax = Usage.maxUsage;
        int percentValue = (int) (totalTraffic*100/globalTraffic);
        int progressValue = (int) (totalTraffic*100/globalMax);
        Drawable appIcon = null;
        try {
            appIcon = pm.getApplicationIcon(application.getPackageName());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        icon.setImageDrawable(appIcon );
        name.setText(application.getName());
        total.setText(DataUsageUtil.getUsageString(totalTraffic));
        send.setText(DataUsageUtil.getUsageString(sentTraffic));
        recv.setText(DataUsageUtil.getUsageString(recvTraffic));
        percent.setText(percentValue+"%");
        progress.setProgress(progressValue);
    }
}
