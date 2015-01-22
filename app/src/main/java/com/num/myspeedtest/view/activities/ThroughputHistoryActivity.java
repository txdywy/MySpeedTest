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

}
