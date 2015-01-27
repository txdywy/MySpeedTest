package com.num.myspeedtest.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.num.myspeedtest.R;
import com.num.myspeedtest.model.Traceroute;

public class TracerouteListAdapter extends ArrayAdapter<Traceroute> {

    private Context context;
    private Traceroute[] values;

    public TracerouteListAdapter(Context context, Traceroute[] values){
        super(context, R.layout.row_traceroute, values);
        this.context = context;
        this.values = values;
    }

    public View getView(int pos, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_traceroute, parent, false);
        TextView hopNumber = (TextView) rowView.findViewById(R.id.traceroute_number);
        TextView address = (TextView) rowView.findViewById(R.id.traceroute_address);
        TextView addressName = (TextView) rowView.findViewById(R.id.traceroute_address_name);
        TextView time = (TextView) rowView.findViewById(R.id.traceroute_time);

        if(values[pos].getAddress().equals("") && values.length<=1){
            hopNumber.setText("1");
            address.setText("Unknown Host");
            addressName.setText("");
            time.setText("");
        }else{
            hopNumber.setText(Integer.toString(values[pos].getHopNumber()));
            address.setText(values[pos].getAddress());
            addressName.setText(values[pos].getHostname());
            time.setText("" + values[pos].getRtt());
        }

        return rowView;
    }

}
