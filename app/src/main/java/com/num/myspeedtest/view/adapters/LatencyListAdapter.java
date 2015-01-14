package com.num.myspeedtest.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.num.myspeedtest.R;
import com.num.myspeedtest.model.Ping;

public class LatencyListAdapter extends ArrayAdapter<Ping> {
    private Context context;
    private Ping[] values;

    public LatencyListAdapter(Context context, Ping[] values){
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
        TextView rtt = (TextView) rowView.findViewById(R.id.textRTT);
        target.setText(values[pos].getDstIP().getTagName());
        progressBar.setProgress((int)values[pos].getMeasure().getAverage());
        if(values[pos].getMeasure().getAverage()<0) {
            rtt.setText("No Response");
        }
        else {
            rtt.setText((int) values[pos].getMeasure().getAverage() + "ms");
        }
        return rowView;
    }
}