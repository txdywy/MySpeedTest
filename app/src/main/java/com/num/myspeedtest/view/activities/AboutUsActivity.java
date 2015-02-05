package com.num.myspeedtest.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.num.myspeedtest.R;
import com.num.myspeedtest.controller.managers.MeasurementManager;

/**
 * Activity describing the application and asking users to rate the app
 */
public class AboutUsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

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
