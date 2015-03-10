package com.num.view.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.num.R;

public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
