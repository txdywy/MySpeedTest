package com.num.myspeedtest.view.activities;

import android.app.Activity;
import android.os.Bundle;

import com.num.myspeedtest.R;
import com.num.myspeedtest.controller.managers.MeasurementManager;

public class MeasurementActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        MeasurementManager manager = new MeasurementManager();
        manager.execute(this, true);
    }
}
