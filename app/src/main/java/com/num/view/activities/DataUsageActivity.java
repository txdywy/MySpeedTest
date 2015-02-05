package com.num.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.num.R;
import com.num.controller.managers.DataUsageManager;
import com.num.db.datasource.DataUsageDataSource;
import com.num.model.Application;
import com.num.model.Usage;
import com.num.view.adapters.DataUsageListAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class DataUsageActivity extends ActionBarActivity {

    private Context context;
    private ListView listView;
    private ProgressBar progressBar;
    private List<Application> applications;
    private DataUsageListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_usage);

        context = this;
        progressBar = (ProgressBar) findViewById(R.id.data_usage_progress);

        applications = new ArrayList<>();
        adapter = new DataUsageListAdapter(context, applications);

        listView = (ListView) findViewById(R.id.data_usage_list);
        listView.setAdapter(adapter);

        DataUsageHandler handler = new DataUsageHandler();
        DataUsageManager manager = new DataUsageManager(handler);
        manager.execute(context);
    }

    private class DataUsageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Usage usage = msg.getData().getParcelable("usage");
            adapter.addAll(usage.getApplications());
            progressBar.setVisibility(View.INVISIBLE);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Application application = (Application) adapterView.getItemAtPosition(i);
                    Intent intent = new Intent(context, ApplicationDetailActivity.class);
                    intent.putExtra("application", application);
                    startActivity(intent);
                }
            });
        }
    }
}
