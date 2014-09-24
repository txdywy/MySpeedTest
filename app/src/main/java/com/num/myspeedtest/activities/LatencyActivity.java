package com.num.myspeedtest.activities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mobilyzer.MeasurementTask;
import com.mobilyzer.api.API;
import com.mobilyzer.exceptions.MeasurementError;
import com.num.myspeedtest.R;
import com.num.myspeedtest.adapters.LatencyListAdapter;

import java.util.Calendar;
import java.util.HashMap;


public class LatencyActivity extends ActionBarActivity {
    private ListView lv;
    private String[] targets;
    private float[] latencies;
    private static API mobilyzer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latency);
        executePing();
        lv = (ListView) findViewById(R.id.list_view_latency);
        LatencyListAdapter adapter = new LatencyListAdapter(lv.getContext(), targets);
        lv.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.latency, menu);
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

    private void executePing() {
        //Initialize Mobilyzer
        mobilyzer = API.getAPI(this, getString(R.string.app_name));
        MeasurementTask task = null;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("target", "www.google.com");
        try {
            task = mobilyzer.createTask(API.TaskType.PING, Calendar.getInstance().getTime(),
                    null, 120, 1, MeasurementTask.USER_PRIORITY, 1, params);
            mobilyzer.submitTask(task);
        }
        catch (MeasurementError e) {

        }
    }
}