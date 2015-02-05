package com.num.view.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.num.R;
import com.num.controller.managers.TracerouteManager;
import com.num.controller.utils.TracerouteUtil;
import com.num.model.Hop;
import com.num.model.Traceroute;
import com.num.view.adapters.TracerouteListAdapter;

import java.util.ArrayList;
import java.util.List;

public class TracerouteActivity extends ActionBarActivity {

    private Context context;
    private ListView listView;
    private EditText address;
    private Button enter;
    private ProgressBar progressBar;
    private RadioGroup traceType;
    private List<Hop> hops;
    private TracerouteListAdapter adapter;
    private int type;
    private final String DEFAULT_ADDRESS = "www.google.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        setContentView(R.layout.activity_traceroute);

        if(!TracerouteUtil.isTracerouteInstalled()) {
            TracerouteUtil.installExecutable(this);
        }

        /* setup for UI */
        progressBar = (ProgressBar) findViewById(R.id.traceroute_progress);
        progressBar.setVisibility(View.INVISIBLE);

        hops = new ArrayList<>();
        adapter = new TracerouteListAdapter(context, hops);

        listView = (ListView) findViewById(R.id.list_view_traceroute);
        listView.setAdapter(adapter);

        type = Traceroute.UDP;
        traceType = (RadioGroup) findViewById(R.id.radio_group_traceroute);
        traceType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_button_icmp) {
                    type = Traceroute.ICMP;
                }
            }
        });

        address = (EditText) findViewById(R.id.editText_traceroute);
        address.setText(DEFAULT_ADDRESS);
        address.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    performTraceroute(v);
                    return true;
                }
                return false;
            }
        });

        enter = (Button) findViewById(R.id.button_traceroute);
        enter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                performTraceroute(v);
            }
        });
    }

    private void performTraceroute(View v) {
        //hide keyboard
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

        adapter.clear();
        progressBar.setVisibility(View.VISIBLE);

        TracerouteHandler handler = new TracerouteHandler();
        TracerouteManager manager = new TracerouteManager(handler);
        String ip = address.getText().toString();
        manager.execute(ip, type);
    }

    private class TracerouteHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Hop hop = msg.getData().getParcelable("hop");
            if(hop == null || msg.getData().getBoolean("isDone")) {
                progressBar.setVisibility(View.INVISIBLE);
                return;
            }
            adapter.add(hop);
        }

    }


}
