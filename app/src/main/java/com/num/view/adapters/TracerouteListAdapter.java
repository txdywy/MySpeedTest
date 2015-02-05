package com.num.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.num.R;
import com.num.model.Hop;
import com.num.model.Traceroute;

import java.util.List;

public class TracerouteListAdapter extends ArrayAdapter<Hop> {

    private Context context;
    private List<Hop> values;

    public TracerouteListAdapter(Context context, List<Hop> values){
        super(context, R.layout.row_traceroute, values);
        this.context = context;
        this.values = values;
    }

    public View getView(int pos, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_traceroute, parent, false);
        TextView hopNumberTextView = (TextView) rowView.findViewById(R.id.traceroute_number);
        TextView addressTextView = (TextView) rowView.findViewById(R.id.traceroute_address);
        TextView hostnameTextView = (TextView) rowView.findViewById(R.id.traceroute_address_name);
        TextView rttTextView = (TextView) rowView.findViewById(R.id.traceroute_time);

        Hop hop = values.get(pos);
        String hopNumber = hop.getHopNumber() + "";
        String address = hop.getAddress();
        String hostname = hop.getHostname();
        String rtt = (hop.getRtt() < 0) ? "" : hop.getRtt() + " ms";

        hopNumberTextView.setText(hopNumber);
        addressTextView.setText(address);
        hostnameTextView.setText(hostname);
        rttTextView.setText(rtt);

        return rowView;
    }

}
