package com.num.myspeedtest.activities;

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
import com.mobilyzer.measurements.TCPThroughputTask;
import com.mobilyzer.measurements.TCPThroughputTask.TCPThroughputDesc;

import com.num.myspeedtest.R;


public class ThroughputActivity extends ActionBarActivity {
    private Button button;
    private static API mobilyzer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_throughput);
        button = (Button) findViewById(R.id.button_start);

        mobilyzer = API.getAPI(getApplicationContext(), "My Speed Test");
        IntentFilter filter = new IntentFilter();
        filter.addAction(mobilyzer.userResultAction);

        button.setOnClickListener(new OnClickListener(){
            public void onClick(View v){

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
}
