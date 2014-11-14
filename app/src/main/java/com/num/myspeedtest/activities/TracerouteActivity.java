package com.num.myspeedtest.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mobilyzer.MeasurementResult;
import com.mobilyzer.UpdateIntent;
import com.mobilyzer.api.API;
import com.mobilyzer.measurements.PingTask;
import com.mobilyzer.measurements.TracerouteTask;
import com.num.myspeedtest.R;
import com.num.myspeedtest.adapters.LatencyListAdapter;
import com.num.myspeedtest.adapters.TracerouteListAdapter;
import com.num.myspeedtest.helpers.TracerouteHelper;
import com.num.myspeedtest.models.Address;
import com.num.myspeedtest.models.Measure;
import com.num.myspeedtest.models.Ping;
import com.num.myspeedtest.models.Traceroute;
import com.num.myspeedtest.models.TracerouteEntry;
import com.num.myspeedtest.models.Values;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Map;

public class TracerouteActivity extends ActionBarActivity {

    private ListView lv;
    private EditText address;
    private Button enter;
    private ProgressBar progressBar;

    private Context context;

    private String default_address = "www.google.com";
    private BroadcastReceiver broadcastReceiver;
    private Traceroute traceroute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        setContentView(R.layout.activity_traceroute);

        /* setup for receiver */
        broadcastReceiver = new TracerouteReceiver();
        API mobilyzer = API.getAPI(this, "My Speed Test");
        IntentFilter filter = new IntentFilter();
        filter.addAction(mobilyzer.userResultAction);
        this.registerReceiver(broadcastReceiver, filter);

        /* setup for UI */
        address = (EditText) findViewById(R.id.editText_traceroute);
        address.setText(default_address);
        enter = (Button) findViewById(R.id.button_traceroute);
        progressBar = (ProgressBar) findViewById(R.id.traceroute_progress);
        lv = (ListView) findViewById(R.id.list_view_traceroute);

        address.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    //hide keyboard
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    //perform traceroute on this address
                    TracerouteHelper.execute(context, address.getText().toString());
                    return true;
                }

                return false;
            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                TracerouteHelper.execute(context, address.getText().toString());
            }
        });

    }

    @Override
    protected void onResume() {
        TracerouteHelper.execute(context, address.getText().toString());
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.traceroute, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class TracerouteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Parcelable[] parcels = intent.getParcelableArrayExtra(UpdateIntent.RESULT_PAYLOAD);
            if (parcels == null) {
                return;
            }
            MeasurementResult[] results = new MeasurementResult[parcels.length];

            try {
                for (int i = 0; i < results.length; i++) {
                    results[i] = (MeasurementResult) parcels[i];
                    Map<String, String> values = results[i].getValues();
                    TracerouteTask.TracerouteDesc desc = (TracerouteTask.TracerouteDesc) results[i].getMeasurementDesc();

                    String n = values.get("num_hops");
                    int num_hops = Integer.parseInt(n);
                    int hostIdx = 1;
                    String addr[] = new String[num_hops];
                    String rttms[] = new String[num_hops];

                    traceroute = new Traceroute(1,num_hops);

                    for(int j=0; j<num_hops; j++){
                        addr[j] = values.get("hop_" + j + "_addr_" + hostIdx++);
                        rttms[j] = values.get("hop_" + j + "_rtt_ms");

                        TracerouteEntry entry = new TracerouteEntry(addr[j], "host", rttms[j], j);
                        traceroute.addToList(entry);
                    }
                }
                TracerouteListAdapter adapter = new TracerouteListAdapter(context,
                        traceroute.getDisplayData());
                lv.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }

            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
