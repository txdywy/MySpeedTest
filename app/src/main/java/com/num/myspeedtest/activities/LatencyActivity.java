package com.num.myspeedtest.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mobilyzer.MeasurementResult;
import com.mobilyzer.UpdateIntent;
import com.mobilyzer.api.API;
import com.mobilyzer.measurements.PingTask.PingDesc;
import com.num.myspeedtest.R;
import com.num.myspeedtest.adapters.LatencyListAdapter;
import com.num.myspeedtest.helpers.LatencyHelper;
import com.num.myspeedtest.models.Address;
import com.num.myspeedtest.models.CommandLine;
import com.num.myspeedtest.models.Measure;
import com.num.myspeedtest.models.Ping;
import com.num.myspeedtest.models.Values;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class LatencyActivity extends ActionBarActivity {
    private ListView listView;
    private ProgressBar progressBar;
    private ArrayList<Ping> pings;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latency);
        context = this;
        pings = new ArrayList<Ping>();
        listView = (ListView) findViewById(R.id.latency_list_view);
        progressBar = (ProgressBar) findViewById(R.id.latency_progress);

    }

    @Override
    protected void onResume() {
        super.onResume();
        for(Address dst : new Values().getTargets()) {
            if(dst.getType().equals("ping")) {
                System.out.println("Adding: " + dst);
                new PingTask().execute(dst);
            }
        }
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
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    private class PingTask extends AsyncTask<Address, Void, Void> {

        private LatencyListAdapter adapter;

        @Override
        protected Void doInBackground(Address... addresses) {
            pings.add(LatencyHelper.pingIcmp(addresses[0], 15));
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            progressBar.setVisibility(View.INVISIBLE);
            adapter = new LatencyListAdapter(context, pings.toArray(new Ping[pings.size()]));
            listView.setAdapter(adapter);
        }
    }
}