package com.num.myspeedtest.view.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.LinearLayout;

import com.num.myspeedtest.Constants;
import com.num.myspeedtest.R;
import com.num.myspeedtest.controller.helpers.TracerouteInstallHelper;
import com.num.myspeedtest.controller.utils.DeviceUtil;
import com.num.myspeedtest.model.Signal;


public class MainActivity extends ActionBarActivity {

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout throughputButton, latencyButton, tracerouteButton, dataUsageButton,
                configureButton, aboutUsButton;
        activity = this;

        SignalListener listener = new SignalListener();
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        if (!TracerouteInstallHelper.isTracerouteInstalled()) {
            TracerouteInstallHelper.installExecutable(this);
        }

        throughputButton = (LinearLayout) findViewById(R.id.main_button_throughput);
        throughputButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(activity, ThroughputActivity.class);
                startActivity(i);
            }
        });

        latencyButton = (LinearLayout) findViewById(R.id.main_button_latency);
        latencyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(activity, LatencyActivity.class);
                startActivity(i);
            }
        });

        dataUsageButton = (LinearLayout) findViewById(R.id.main_button_data_usage);
        dataUsageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, DataUsageActivity.class);
                startActivity(i);
            }
        });

        tracerouteButton = (LinearLayout) findViewById(R.id.main_button_traceroute);
        tracerouteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(activity, TracerouteActivity.class);
                startActivity(i);
            }
        });

        configureButton = (LinearLayout) findViewById(R.id.main_button_configure);
        configureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, SettingsActivity.class);
                startActivity(i);
            }
        });

        aboutUsButton = (LinearLayout) findViewById(R.id.main_button_about_us);
        aboutUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, AboutUsActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        if (!prefs.contains("accept_conditions") || prefs.getBoolean("accept_conditions", false)) {
            finish();
            Intent myIntent = new Intent(getApplicationContext(), TermsAndConditionsActivity.class);
            startActivity(myIntent);
        }

        /* Check Internet Connection */
        if (!DeviceUtil.getInstance().isInternetAvailable(this)) {
            new AlertDialog.Builder(this)
                    .setTitle("Internet Warning")
                    .setMessage("You are not currently connected to the Internet. Some features may not work.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

    }

    private class SignalListener extends PhoneStateListener {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signal) {
            String signalStrength = "-1";
            if (signal != null) {
                if (signal.isGsm()) {
                    signalStrength = "" + signal.getGsmSignalStrength();
                } else if (signal.getCdmaDbm() > -120) {
                    signalStrength = signal.getCdmaDbm() + "dBm ";
                    signalStrength += signal.getCdmaEcio() + "Ec/Io";
                } else if (signal.getEvdoDbm() > -120) {
                    signalStrength = signal.getEvdoDbm() + "dBm ";
                    signalStrength += signal.getEvdoEcio() + "Ec/Io";
                    signalStrength += signal.getEvdoSnr() + "snr";
                }
            }
            Signal.setSignal(signalStrength);
        }
    }
}
