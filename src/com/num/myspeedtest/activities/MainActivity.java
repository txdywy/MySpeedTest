package com.num.myspeedtest.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.num.myspeedtest.R;


public class MainActivity extends ActionBarActivity {
    private Button throughputButton, latencyButton, censorshipButton, httpButton,
            tracerouteButton, dataUsageButton;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;

        throughputButton = (Button) findViewById(R.id.button_throughput);
        throughputButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent i = new Intent(activity, ThroughputActivity.class);
                startActivity(i);
            }
         });

        latencyButton = (Button) findViewById(R.id.button_latency);
        latencyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent i = new Intent(activity, LatencyActivity.class);
                startActivity(i);
            }
        });

        censorshipButton = (Button) findViewById(R.id.button_censorship);
        censorshipButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(activity, CensorshipActivity.class);
				startActivity(i);
				
			}
		});

        //httpButton = (Button) findViewById(R.id.button_http);

        tracerouteButton = (Button) findViewById(R.id.button_traceroute);
        tracerouteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent i = new Intent(activity, TracerouteActivity.class);
                startActivity(i);
            }
        });

        dataUsageButton = (Button) findViewById(R.id.button_data_usage);
        dataUsageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, DataUsageActivity.class);
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(activity, SettingsActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
