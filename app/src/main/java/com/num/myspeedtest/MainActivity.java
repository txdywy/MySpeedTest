package com.num.myspeedtest;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


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

        censorshipButton = (Button) findViewById(R.id.button_censorship);

        httpButton = (Button) findViewById(R.id.button_http);

        tracerouteButton = (Button) findViewById(R.id.button_traceroute);

        dataUsageButton = (Button) findViewById(R.id.button_data_usage);
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
