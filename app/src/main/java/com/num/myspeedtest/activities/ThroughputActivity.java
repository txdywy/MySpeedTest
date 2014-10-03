package com.num.myspeedtest.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import android.content.IntentFilter;
import android.view.View.OnClickListener;
import android.view.*;

import com.mobilyzer.*;
import com.mobilyzer.api.*;
import com.mobilyzer.MeasurementResult;
import com.mobilyzer.MeasurementTask;
import com.mobilyzer.UpdateIntent;
import com.mobilyzer.api.API;
import com.mobilyzer.exceptions.MeasurementError;
import com.mobilyzer.measurements.PingTask;
import com.mobilyzer.measurements.TCPThroughputTask;
import com.mobilyzer.measurements.TCPThroughputTask.TCPThroughputDesc;

import com.num.myspeedtest.R;
import com.num.myspeedtest.helpers.ThroughputHelper;

import org.json.JSONObject;

public class ThroughputActivity extends ActionBarActivity {
    private TextView downSpeed, upSpeed, percentage;
    private ProgressBar progressBar;
    private Button startButton;
    private API mobilyzer;
    private Context context;
    private BroadcastReceiver br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_throughput);

        this.context = this;

        API mobilyzer = API.getAPI(this, "My Speed Test");
        br = new ThroughputReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(mobilyzer.userResultAction);
        this.registerReceiver(br, filter);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        percentage = (TextView) findViewById(R.id.text_pvalue);
        downSpeed = (TextView) findViewById(R.id.text_dvalue);
        upSpeed = (TextView) findViewById(R.id.text_uvalue);
        startButton = (Button) findViewById(R.id.button_start);
        startButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ThroughputHelper.execute(context);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.throughput, menu);
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

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(br
        );
        super.onDestroy();
    }

    private class ThroughputReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Parcelable[] parcels = intent.getParcelableArrayExtra(UpdateIntent.RESULT_PAYLOAD);
            MeasurementResult[] results;
            TCPThroughputDesc desc;
            if(parcels != null) {
                results = new MeasurementResult[parcels.length];
                for(int i=0; i<results.length; i++) {
                    results[i] = (MeasurementResult) parcels[i];
                    String throughputJSON = results[i].getValues().get("tcp_speed_results");
                    desc = (TCPThroughputDesc) results[i].getMeasurementDesc();
                    long tp = (long) (desc.calMedianSpeedFromTCPThroughputOutput(throughputJSON));
                    if(desc.parameters.get("dir_up").equals("false"))
                        downSpeed.setText(ThroughputHelper.outputString(tp));
                    else
                        upSpeed.setText(ThroughputHelper.outputString(tp));
                }
            }
        }
    }
}
