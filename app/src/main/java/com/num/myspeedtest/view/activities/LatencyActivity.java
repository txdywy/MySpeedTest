package com.num.myspeedtest.view.activities;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.num.myspeedtest.R;
import com.num.myspeedtest.controller.helpers.LatencyHelper;
import com.num.myspeedtest.model.Address;
import com.num.myspeedtest.model.Ping;
import com.num.myspeedtest.model.Values;
import com.num.myspeedtest.view.adapters.LatencyListAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class LatencyActivity extends Activity {
    private ListView listView;
    private ProgressBar progressBar;
    private ArrayList<Ping> pings;
    private Context context;
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latency);

        context = this;
        listView = (ListView) findViewById(R.id.latency_list_view);
        progressBar = (ProgressBar) findViewById(R.id.latency_progress);
    }

    @Override
    protected void onResume() {
        pings = new ArrayList<>();
        counter = 0;
        for(Address dst : new Values().getTargets()) {
            if(dst.getType().equals("ping")) {
                PingTask task = new PingTask();
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dst);
                counter++;
            }
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.latency, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    private class PingTask extends AsyncTask<Address, Void, Void> {

        private LatencyListAdapter adapter;

        @Override
        protected Void doInBackground(Address... addresses) {
            pings.add(LatencyHelper.ping(addresses[0]));
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            if(pings.size() >= counter) {
                progressBar.setVisibility(View.INVISIBLE);
            }
            adapter = new LatencyListAdapter(context, pings.toArray(new Ping[pings.size()]));
            listView.setAdapter(adapter);
        }
    }
}