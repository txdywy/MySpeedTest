package com.num.myspeedtest.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.num.myspeedtest.R;

public class TermsAndConditionsActivity extends ActionBarActivity {

    private WebView textView;
    private TextView accept;
    private TextView reject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        textView = (WebView) findViewById(R.id.terms_and_conditions_text);
        textView.loadUrl("file:///android_asset/terms_and_conditions_text.html");
        accept = (TextView) findViewById(R.id.terms_and_conditions_accept);
        reject = (TextView) findViewById(R.id.terms_and_conditions_reject);

        accept.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                SharedPreferences sharedpreferences = getSharedPreferences("pref_key_terms_and_conditions", Context.MODE_PRIVATE);
                SharedPreferences.Editor e = sharedpreferences.edit();
                e.putBoolean("accept", true);
                e.commit();
                finish();
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);
            }
        });

        reject.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
