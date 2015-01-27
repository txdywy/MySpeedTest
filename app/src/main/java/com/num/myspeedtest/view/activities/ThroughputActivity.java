package com.num.myspeedtest.view.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import com.num.myspeedtest.controller.utils.DeviceUtil;
import com.num.myspeedtest.db.DatabaseHelper;
import com.num.myspeedtest.model.Throughput;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ThroughputActivity extends ActionBarActivity {

    private DatabaseHelper dbHelper;
    private TextView downSpeed, upSpeed, percentage;
    private TextView startButtonTxt;
    private ProgressBar progressBar;
    private LinearLayout startButton, historyButton;
    private ImageView startButtonImage;
    private CountDownTimer countDownTimer;
    private API mobilyzer;
    private Context context;
    private BroadcastReceiver br;
    private final long progressLength = 60000; //1 mins
    private final long progressInterval = 1000; //.5 seconds
    private boolean isRunningUp;
    private boolean isRunningDown;
    private boolean testSuccessful;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_throughput);

        this.context = this;

        dbHelper = new DatabaseHelper(this);

        API mobilyzer = API.getAPI(this, "My Speed Test");
        br = new ThroughputReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(mobilyzer.userResultAction);
        this.registerReceiver(br, filter);

        isRunningUp = false;
        isRunningDown = false;
        testSuccessful = false;
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        percentage = (TextView) findViewById(R.id.text_pvalue);
        downSpeed = (TextView) findViewById(R.id.text_dvalue);
        upSpeed = (TextView) findViewById(R.id.text_uvalue);
        startButton = (LinearLayout) findViewById(R.id.button_start);
        startButtonImage = (ImageView) findViewById(R.id.button_start_image);
        startButtonTxt = (TextView) findViewById(R.id.button_start_txt);
        historyButton = (LinearLayout) findViewById(R.id.button_history);

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
                progressBar.setProgress(100);
                if(!testSuccessful) {
                    percentage.setText("Connection Failed");
                    startButtonImage.setImageResource(R.drawable.ic_action_play);
                    startButtonTxt.setText("Start");
                    startButton.setClickable(true);
                    downSpeed.setText("0.0 Mbps");
                    upSpeed.setText("0.0 Mbps");
                }else{
                    testSuccessful = false;
                }
                /* add result to database */
                String dateTime = dbHelper.getDateTime();
                Throughput throughput = new Throughput(dateTime, downSpeed.getText().toString(), upSpeed.getText().toString());
                dbHelper.insertThroughput(throughput);
            }
        };

        startButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!DeviceUtil.getInstance().isInternetAvailable(context)) {
                    percentage.setText("No Connection");
                }else {
                    if (!isRunningUp && !isRunningDown) {
                        isRunningUp = true;
                        isRunningDown = true;
                        startButtonImage.setImageResource(R.drawable.ic_action_stop);
                        startButtonTxt.setText("Running");
                        startButton.setClickable(false);
                        percentage.setText("In progress...");
                        downSpeed.setText("Running");
                        upSpeed.setText("Running");
                        ThroughputHelper.execute(context);
                        countDownTimer.start();
                    } else {
                        isRunningUp = false;
                        isRunningDown = false;
                        unregisterReceiver(br);
                    }
                }
            }
        });

        historyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ThroughputHistoryActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onDestroy() {
        /* update database */
        dbHelper.updateThroughput();
        dbHelper.close();
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
                    if(desc.dir_up) {
                        upSpeed.setText(ThroughputHelper.outputString(tp));
                        isRunningUp = false;
                    }else {
                        downSpeed.setText(ThroughputHelper.outputString(tp));
                        isRunningDown = false;
                    }
                    if(!isRunningDown && !isRunningUp){
                        testSuccessful = true;
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
