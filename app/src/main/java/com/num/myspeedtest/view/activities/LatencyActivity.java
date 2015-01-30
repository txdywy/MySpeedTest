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
import com.num.myspeedtest.controller.managers.MeasurementManager;
import com.num.myspeedtest.controller.utils.ServerUtil;
import com.num.myspeedtest.model.LastMile;
import com.num.myspeedtest.model.Measurement;
import com.num.myspeedtest.model.Ping;
import com.num.myspeedtest.view.adapters.LatencyListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Latency test activity
 */
public class LatencyActivity extends ActionBarActivity {

    private Context context;
    private ListView listView;
    private ProgressBar progressBar;

    private List<Ping> pings;
    private LatencyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latency);

        context = this;
        progressBar = (ProgressBar) findViewById(R.id.latency_progress);

        pings = new ArrayList<>();
        adapter = new LatencyListAdapter(context, pings);

        listView = (ListView) findViewById(R.id.latency_list_view);
        listView.setAdapter(adapter);

        LatencyHandler handler = new LatencyHandler();
        LatencyManager manager = new LatencyManager(handler);
        manager.execute(ServerUtil.getTargets());
    }

    private class LatencyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            /* Extract ping results */
            Ping ping = msg.getData().getParcelable("ping");

            /* Update the list adapter */
            adapter.add(ping);

            /* Finish up if the test is complete */
            if(ServerUtil.getTargets().size() == pings.size()) {
                progressBar.setVisibility(View.INVISIBLE);
                Measurement measurement = new Measurement(context, true);
                measurement.setPings(pings);
                MeasurementManager measurementManager = new MeasurementManager();
                measurementManager.sendMeasurement(measurement);
            }
        }
    }
}