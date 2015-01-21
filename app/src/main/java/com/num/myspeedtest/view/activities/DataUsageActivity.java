package com.num.myspeedtest.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.num.myspeedtest.R;
import com.num.myspeedtest.view.adapters.DataUsageListAdapter;
import com.num.myspeedtest.controller.helpers.DataUsageHelper;
import com.num.myspeedtest.model.Application;


public class DataUsageActivity extends ActionBarActivity {
    private ListView listView;
    private ProgressBar progressBar;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_usage);
        context = this;
        listView = (ListView) findViewById(R.id.data_usage_list);
        progressBar = (ProgressBar) findViewById(R.id.data_usage_progress);
    }

    @Override
    protected void onResume() {
        new LoadDataUsageTask().execute();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.data_usage, menu);
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

    private class LoadDataUsageTask extends AsyncTask<Void, Void, Void> {

        private DataUsageListAdapter adapter;

        @Override
        protected Void doInBackground(Void... voids) {
            adapter = new DataUsageListAdapter(context, DataUsageHelper.getApplications(context));
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            progressBar.setVisibility(View.INVISIBLE);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Application application = (Application) adapterView.getItemAtPosition(i);
                    Intent intent = new Intent(context, ApplicationDetailActivity.class);
                    intent.putExtra("package", application.getPackageName());
                    startActivity(intent);
                }
            });
        }
    }
}
