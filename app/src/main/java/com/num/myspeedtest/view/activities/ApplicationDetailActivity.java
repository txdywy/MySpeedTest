package com.num.myspeedtest.view.activities;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.num.myspeedtest.R;
import com.num.myspeedtest.controller.helpers.DataUsageHelper;

public class ApplicationDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_detail);

        Bundle extras = getIntent().getExtras();
        String packageName = extras.getString("package");
        PackageManager pm = this.getPackageManager();

        ImageView icon = (ImageView) findViewById(R.id.icon_application_detail);
        TextView name = (TextView) findViewById(R.id.individual_app_page_name);
        TextView total = (TextView) findViewById(R.id.individual_app_page_total_data_used_by_app_value);
        TextView send = (TextView) findViewById(R.id.individual_app_page_sent_data_used_by_app_value);
        TextView recv = (TextView) findViewById(R.id.individual_app_page_recv_data_used_by_app_value);
        TextView percent = (TextView) findViewById(R.id.individual_app_page_percentage_used_by_app_value);
        ProgressBar progress = (ProgressBar) findViewById(R.id.individual_app_page_value);

        try {
            ApplicationInfo info = this.getPackageManager().getApplicationInfo(packageName, 0);
            int uid = info.uid;
            long recvTraffic = TrafficStats.getUidRxBytes(uid);
            long sentTraffic = TrafficStats.getUidTxBytes(uid);
            long totalTraffic = recvTraffic + sentTraffic;
            long globalTraffic = DataUsageHelper.getTotalUsage();
            long globalMax = DataUsageHelper.getMaxUsage();
            int percentValue = (int) (totalTraffic*100/globalTraffic);
            int progressValue = (int) (totalTraffic*100/globalMax);

            if(progressValue==0){
                progressValue = 1;
            }

            icon.setImageDrawable(info.loadIcon(pm));

            name.setText(info.loadLabel(pm));

            total.setText(getUsageString(totalTraffic));

            send.setText(getUsageString(sentTraffic));

            recv.setText(getUsageString(recvTraffic));

            percent.setText(percentValue+"%");

            progress.setProgress(progressValue);

        } catch (PackageManager.NameNotFoundException e) {
            name.setText("Unknown");
            total.setText("0");
            send.setText("0");
            recv.setText("0");
            percent.setText("0%");
            progress.setProgress(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.application_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
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
