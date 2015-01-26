package com.num.myspeedtest.view.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.num.myspeedtest.R;
import com.num.myspeedtest.controller.managers.LatencyManager;
import com.num.myspeedtest.controller.utils.ServerUtil;
import com.num.myspeedtest.model.Ping;
import com.num.myspeedtest.view.adapters.LatencyListAdapter;

import java.util.Arrays;

/**
 * Ping test activity
 */
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
        LatencyManager manager = new LatencyManager(handler);
        manager.execute(ServerUtil.getTargets());
    }

    private class LatencyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Parcelable[] parcelables = msg.getData().getParcelableArray("pings");
            Ping[] pings = Arrays.copyOf(parcelables, parcelables.length, Ping[].class);
            LatencyListAdapter adapter = new LatencyListAdapter(context, pings);
            listView.setAdapter(adapter);
            if(msg.getData().getBoolean("isDone")) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }
}