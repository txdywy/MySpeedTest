package com.num.myspeedtest.view.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.num.myspeedtest.R;
import com.num.myspeedtest.controller.helpers.LatencyHelper;
import com.num.myspeedtest.controller.managers.LatencyManager;
import com.num.myspeedtest.model.Ping;
import com.num.myspeedtest.view.adapters.LatencyListAdapter;

import java.util.Arrays;
import java.util.List;

public class LatencyActivity extends ActionBarActivity {

    private Context context;
    private ListView listView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latency);

        context = this;
        listView = (ListView) findViewById(R.id.latency_list_view);
        progressBar = (ProgressBar) findViewById(R.id.latency_progress);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LatencyHandler handler = new LatencyHandler();
        LatencyHelper helper = new LatencyHelper(handler);
        helper.execute();
    }

    private class LatencyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Parcelable[] parcelables = msg.getData().getParcelableArray("pingArray");
            Ping[] pingArray = Arrays.copyOf(parcelables, parcelables.length, Ping[].class);
            LatencyListAdapter adapter = new LatencyListAdapter(context, pingArray);
            listView.setAdapter(adapter);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}