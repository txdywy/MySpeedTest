package com.num.myspeedtest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.num.myspeedtest.R;

/**
 * Created by Andrew on 9/23/2014.
 */

public class LatencyListAdapter extends ArrayAdapter<String> {
    private Context context;
    private String[] values;

    public LatencyListAdapter(Context context, String[] values){
        super(context, R.layout.row_latency, values);
        this.context = context;
        this.values = values;
    }

    public View getView(int pos, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_latency, parent, false);
        TextView target = (TextView) rowView.findViewById(R.id.textTarget);
        ProgressBar progressBar = (ProgressBar) rowView.findViewById(R.id.progress_latency);
        target.setText(values[pos]);
        return rowView;
    }
}