package com.num.myspeedtest.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.mobilyzer.measurements.PingTask.PingDesc;
import com.num.myspeedtest.R;
import com.num.myspeedtest.adapters.LatencyListAdapter;
import com.num.myspeedtest.helpers.LatencyHelper;
import com.num.myspeedtest.models.Address;
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
    private BroadcastReceiver broadcastReceiver;
    private HashSet<String> displayTargets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latency);

        pings = new ArrayList<Ping>();
        displayTargets = new HashSet<String>();
        broadcastReceiver = new LatencyReceiver();
        IntentFilter filter = new IntentFilter();
        this.registerReceiver(broadcastReceiver, filter);
        listView = (ListView) findViewById(R.id.latency_list_view);
        progressBar = (ProgressBar) findViewById(R.id.latency_progress);
    }

    @Override
    protected void onResume() {
        LatencyHelper.execute(this);
        super.onResume();
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

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    private class LatencyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Parcelable[] parcels = intent.getParcelableArrayExtra(UpdateIntent.RESULT_PAYLOAD);
            if (parcels == null) {
                return;
            }
            MeasurementResult[] results = new MeasurementResult[parcels.length];
            NumberFormat nf = NumberFormat.getNumberInstance();
            String src = "";
            for (int i = 0; i < results.length; i++) {
                results[i] = (MeasurementResult) parcels[i];
                Map<String, String> values = results[i].getValues();
                PingDesc desc = (PingDesc) results[i].getMeasurementDesc();
                try {
                    Number n;
                    n = nf.parse(values.get("min_rtt_ms"));
                    double min = n.doubleValue();
                    n = nf.parse(values.get("max_rtt_ms"));
                    double max = n.doubleValue();
                    n = nf.parse(values.get("stddev_rtt_ms"));
                    double stddev = n.doubleValue();
                    if (values.get("filtered_mean_rtt_ms") == null) {
                        n = nf.parse(values.get("mean_rtt_ms"));
                    } else {
                        n = nf.parse(values.get("filtered_mean_rtt_ms"));
                    }
                    double average = n.doubleValue();
                    Values session = new Values();
                    Address dst = session.getAddress(desc.target);
                    Measure m = new Measure(min, max, average, stddev);
                    Ping p = new Ping(src, dst, m);
                    if (!displayTargets.contains(dst.getTagName())) {
                        pings.add(p);
                        displayTargets.add(dst.getTagName());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            LatencyListAdapter adapter = new LatencyListAdapter(context, pings.toArray(new Ping[pings.size()]));
            progressBar.setVisibility(View.INVISIBLE);
            listView.setAdapter(adapter);
        }
    }
}