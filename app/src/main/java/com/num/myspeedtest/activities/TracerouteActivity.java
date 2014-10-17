package com.num.myspeedtest.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.num.myspeedtest.R;
import com.num.myspeedtest.adapters.TracerouteListAdapter;
import com.num.myspeedtest.helpers.TracerouteHelper;

public class TracerouteActivity extends ActionBarActivity {

    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traceroute);
        lv = (ListView) findViewById(R.id.list_view_traceroute);
        TracerouteListAdapter adapter = new TracerouteListAdapter(this,
                TracerouteHelper.getTracerouteResult(this));
        lv.setAdapter(adapter);
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
}
