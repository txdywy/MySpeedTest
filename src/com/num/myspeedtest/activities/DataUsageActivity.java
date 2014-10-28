package com.num.myspeedtest.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.num.myspeedtest.R;
import com.num.myspeedtest.adapters.DataUsageListAdapter;
import com.num.myspeedtest.helpers.DataUsageHelper;


public class DataUsageActivity extends ActionBarActivity {
    private ListView lv;
    private ProgressBar pb;
    private Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_usage);
        c = this;
        lv = (ListView) findViewById(R.id.list_view_data);
        pb = (ProgressBar) findViewById(R.id.progress_data_usage);
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class LoadDataUsageTask extends AsyncTask<Void, Void, Void> {
        DataUsageListAdapter adapter;
        @Override
        protected Void doInBackground(Void... voids) {
            adapter = new DataUsageListAdapter(c, DataUsageHelper.getApplications(c));
            return null;
        }

        protected void onPostExecute(Void v) {
            pb.setVisibility(View.INVISIBLE);
            lv.setAdapter(adapter);
        }
    }
}
