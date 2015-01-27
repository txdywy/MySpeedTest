package com.num.myspeedtest.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.num.myspeedtest.R;
import com.num.myspeedtest.controller.helpers.TracerouteInstallHelper;
import com.num.myspeedtest.controller.managers.MeasurementManager;
import com.num.myspeedtest.controller.utils.CommandLineUtil;

public class AboutUsActivity extends Activity {
    private LinearLayout rateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        //MeasurementManager manager = new MeasurementManager();
        //manager.execute(this, true);

        rateButton = (LinearLayout) findViewById(R.id.about_us_button);
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "market://details?id=com.num";
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(i);
            }
        });

        String command = TracerouteInstallHelper.getTraceroutePath();
        command += " -m 20 -q 3 www.google.com";
        String result = new CommandLineUtil().runCommand(command);
        System.out.println(result);
    }
}
