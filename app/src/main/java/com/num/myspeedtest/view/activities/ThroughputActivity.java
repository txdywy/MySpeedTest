package com.num.myspeedtest.view.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mobilyzer.MeasurementResult;
import com.mobilyzer.UpdateIntent;
import com.mobilyzer.api.API;
import com.mobilyzer.measurements.TCPThroughputTask.TCPThroughputDesc;
import com.num.myspeedtest.R;
import com.num.myspeedtest.controller.helpers.ThroughputHelper;

public class ThroughputActivity extends ActionBarActivity {

    private TextView downSpeed, upSpeed, percentage;
    private TextView startButtonTxt;
    private ProgressBar progressBar;
    private LinearLayout startButton;
    private ImageView startButtonImage;
    private CountDownTimer countDownTimer;
    private API mobilyzer;
    private Context context;
    private BroadcastReceiver br;
    private final long progressLength = 60000; //1 mins
    private final long progressInterval = 1000; //.5 seconds
    private boolean isRunningUp;
    private boolean isRunningDown;

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

        isRunningUp = false;
        isRunningDown = false;
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        percentage = (TextView) findViewById(R.id.text_pvalue);
        downSpeed = (TextView) findViewById(R.id.text_dvalue);
        upSpeed = (TextView) findViewById(R.id.text_uvalue);
        startButton = (LinearLayout) findViewById(R.id.button_start);
        startButtonImage = (ImageView) findViewById(R.id.button_start_image);
        startButtonTxt = (TextView) findViewById(R.id.button_start_txt);

        countDownTimer = new CountDownTimer(progressLength,progressInterval) {
            @Override
            public void onTick(long millisUntilFinished_) {
//                System.out.println("Throughput Activity Timer: " + millisUntilFinished_);
                if(millisUntilFinished_>progressInterval && (isRunningUp || isRunningDown)) {
                    int percentage = 100 - (int)(millisUntilFinished_/600);
                    System.out.println("Throughput Activity Timer: " + percentage);
                    progressBar.setProgress(percentage);
                }
            }

            @Override
            public void onFinish() {
                // wait
            }
        };

        startButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isRunningUp && !isRunningDown) {
                    isRunningUp = true;
                    isRunningDown = true;
                    startButtonImage.setImageResource(R.drawable.ic_action_stop);
                    startButtonTxt.setText("Running");
                    startButton.setClickable(false);
                    percentage.setText("In progress...");
                    ThroughputHelper.execute(context);
                    countDownTimer.start();
                }else{
                    isRunningUp = false;
                    isRunningDown = false;
                    unregisterReceiver(br);
                }
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
        this.unregisterReceiver(br);
        super.onDestroy();
    }

    private class ThroughputReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Parcelable[] parcels = intent.getParcelableArrayExtra(UpdateIntent.RESULT_PAYLOAD);
            MeasurementResult[] results;
            TCPThroughputDesc desc;
            System.out.println("Throughput Activity Received");
            if(parcels != null) {
                results = new MeasurementResult[parcels.length];
                for(int i=0; i<results.length; i++) {
                    results[i] = (MeasurementResult) parcels[i];
                    String throughputJSON = results[i].getValues().get("tcp_speed_results");
                    desc = (TCPThroughputDesc) results[i].getMeasurementDesc();
                    long tp = (long) (desc.calMedianSpeedFromTCPThroughputOutput(throughputJSON));
//                    System.out.println("Throughput Activity: " + desc.dir_up + " tp: " + ThroughputHelper.outputString(tp) + " timeout: " + desc.tcp_timeout_sec + "down: " + desc.data_limit_mb_down + "up: " + desc.data_limit_mb_up);
                    if(!desc.dir_up) {
                        System.out.println("Throughput Activity Down");
                        downSpeed.setText(ThroughputHelper.outputString(tp));
                        isRunningDown = false;
                    }else {
                        System.out.println("Throughput Activity Up");
                        upSpeed.setText(ThroughputHelper.outputString(tp));
                        isRunningUp = false;
                    }
                    if(!isRunningDown && !isRunningUp){
                        startButtonImage.setImageResource(R.drawable.ic_action_replay);
                        startButtonTxt.setText("Start");
                        startButton.setClickable(true);
                        progressBar.setProgress(100);
                        percentage.setText("Test Complete");
                    }
                }
            }
        }
    }
}
