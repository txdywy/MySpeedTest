package com.num.myspeedtest.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.num.myspeedtest.R;
import com.num.myspeedtest.db.DatabaseHelper;
import com.num.myspeedtest.model.Application;
import com.num.myspeedtest.model.Throughput;
import com.num.myspeedtest.view.adapters.ThroughputHistoryListAdapter;

import java.util.List;


public class ThroughputHistoryActivity extends Activity {

    private ListView listView;
    private ThroughputHistoryListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_throughput_history);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<Throughput> values = dbHelper.getThroughput();
        if(values.size()==0){
            Throughput throughput = new Throughput("No History", "Download", "Upload");
            values.add(throughput);
        }
        adapter = new ThroughputHistoryListAdapter(getApplicationContext(), values);
        listView = (ListView) findViewById(R.id.list_view_throughput_history);
        listView.setAdapter(adapter);
        dbHelper.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.throughput_history, menu);
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
}
