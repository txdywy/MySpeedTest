package com.num.myspeedtest.view.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.num.myspeedtest.R;

public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
