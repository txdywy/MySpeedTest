package com.num.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.num.R;
import com.num.model.Ping;

import java.util.List;

public class LatencyListAdapter extends ArrayAdapter<Ping> {
    private Context context;
    private List<Ping> values;

    public LatencyListAdapter(Context context, List<Ping> values){
        super(context, R.layout.row_latency, values);
        this.context = context;
        this.values = values;
    }

    public View getView(int pos, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_latency, parent, false);
        TextView targetTextView = (TextView) rowView.findViewById(R.id.textTarget);
        ProgressBar progressBar = (ProgressBar) rowView.findViewById(R.id.progress_latency);
        TextView rttTextView = (TextView) rowView.findViewById(R.id.textRTT);

        Ping ping = values.get(pos);
        String target = ping.getDstIP().getTagName();
        int progress = (int) ping.getMeasure().getAverage();
        int rtt = (int) ping.getMeasure().getAverage();
        String rttString = (rtt<0) ? "No Response" : rtt + " ms";

        targetTextView.setText(target);
        progressBar.setProgress(progress);
        rttTextView.setText(rttString);

        return rowView;
    }
}