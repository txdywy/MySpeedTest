package com.num.myspeedtest.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.num.myspeedtest.R;

public class ApplicationDetailActivity extends ActionBarActivity {
    private ImageView icon;
    private TextView name, desc, total, send, recv, percent;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_detail);
        icon = (ImageView) findViewById(R.id.icon_application_detail);
        name = (TextView) findViewById(R.id.individual_app_page_name);
        desc = (TextView) findViewById(R.id.individual_app_page_description);
        total = (TextView) findViewById(R.id.individual_app_page_total_data_used_by_app_value);
        send = (TextView) findViewById(R.id.individual_app_page_sent_data_used_by_app_value);
        recv = (TextView) findViewById(R.id.individual_app_page_recv_data_used_by_app_value);
        percent = (TextView) findViewById(R.id.individual_app_page_percentage_used_by_app_value);
        pb = (ProgressBar) findViewById(R.id.individual_app_page_value);
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
