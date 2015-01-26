package com.num.myspeedtest.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.num.myspeedtest.R;
import com.num.myspeedtest.controller.managers.DataUsageManager;
import com.num.myspeedtest.model.Application;
import com.num.myspeedtest.model.Usage;
import com.num.myspeedtest.view.adapters.DataUsageListAdapter;


public class DataUsageActivity extends ActionBarActivity {

    private Context context;
    private ListView listView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_usage);

        context = this;
        listView = (ListView) findViewById(R.id.data_usage_list);
        progressBar = (ProgressBar) findViewById(R.id.data_usage_progress);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataUsageHandler handler = new DataUsageHandler();
        DataUsageManager manager = new DataUsageManager(handler);
        manager.execute(context);
    }

    private class DataUsageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            final Usage usage = msg.getData().getParcelable("usage");
            DataUsageListAdapter adapter = new DataUsageListAdapter(context, usage);
            progressBar.setVisibility(View.INVISIBLE);
            listView.setAdapter(adapter);
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
