package com.num.myspeedtest.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.num.myspeedtest.R;
import com.num.myspeedtest.model.Traceroute;

import java.util.List;

public class TracerouteListAdapter extends ArrayAdapter<Traceroute> {

    private Context context;
    private List<Traceroute> values;

    public TracerouteListAdapter(Context context, List<Traceroute> values){
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

        Traceroute traceroute = values.get(pos);
        String hop = traceroute.getHopNumber() + "";
        String address = traceroute.getAddress();
        String hostname = traceroute.getHostname();
        String rtt = (traceroute.getRtt() < 0) ? "" : traceroute.getRtt() + " ms";

        hopNumberTextView.setText(hop);
        addressTextView.setText(address);
        hostnameTextView.setText(hostname);
        rttTextView.setText(rtt);

        return rowView;
    }

}
