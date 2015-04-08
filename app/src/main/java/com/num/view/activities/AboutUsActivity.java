package com.num.view.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.python27.BackgroundScriptService;
import com.android.python27.ScriptService;
import com.android.python27.config.GlobalConstants;
import com.android.python27.support.Utils;
import com.num.Constants;
import com.num.R;
import com.num.controller.managers.CensorshipManager;
import com.num.controller.managers.MeasurementManager;
import com.num.controller.utils.CommandLineUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Activity describing the application and asking users to rate the app
 */
public class AboutUsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        /**
         * Set up rate our app button to send the user to the store
         */
        LinearLayout rateButton = (LinearLayout) findViewById(R.id.about_us_button);
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "market://details?id=com.num";
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(i);
            }
        });
    }
}
