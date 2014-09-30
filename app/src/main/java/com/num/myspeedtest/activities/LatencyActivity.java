package com.num.myspeedtest.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.mobilyzer.MeasurementResult;
import com.mobilyzer.UpdateIntent;
import com.mobilyzer.measurements.PingTask;
import com.mobilyzer.measurements.PingTask.PingDesc;
import com.num.myspeedtest.R;
import com.num.myspeedtest.adapters.LatencyListAdapter;
import com.num.myspeedtest.models.Ping;

import java.util.Calendar;
import java.util.HashMap;


public class LatencyActivity extends ActionBarActivity {
    private ListView lv;
    private Ping[] pings;
    private float[] latencies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latency);
        lv = (ListView) findViewById(R.id.list_view_latency);
        LatencyListAdapter adapter = new LatencyListAdapter(lv.getContext(), pings);
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

    private class LatencyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Parcelable[] parcels = intent.getParcelableArrayExtra(UpdateIntent.RESULT_PAYLOAD);
            String latencyJSON;
            MeasurementResult[] results;
            PingDesc desc;
            if(parcels != null) {
                results = new MeasurementResult[parcels.length];
                for(int i=0; i<results.length; i++) {
                    results[i] = (MeasurementResult) parcels[i];
                    latencyJSON = results[i].getValues().get("tcp_speed_results");
                    desc = (PingDesc) results[i].getMeasurementDesc();
                }
            }
        }
    }
}