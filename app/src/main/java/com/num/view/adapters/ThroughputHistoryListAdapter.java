package com.num.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.num.R;
import com.num.model.Throughput;

import java.util.List;

public class ThroughputHistoryListAdapter extends ArrayAdapter<Throughput> {
    private Context context;
    private List<Throughput> values;

    public ThroughputHistoryListAdapter(Context context, List<Throughput> values){
        super(context, R.layout.row_throughput_history, values);
        this.context = context;
        this.values = values;
    }

    public View getView(int pos, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_throughput_history, parent, false);
        TextView date = (TextView) rowView.findViewById(R.id.throughput_history_date);
        TextView download = (TextView) rowView.findViewById(R.id.throughput_history_download);
        TextView upload = (TextView) rowView.findViewById(R.id.throughput_history_upload);

        date.setText(values.get(pos).getDatetime());
        download.setText(values.get(pos).getDownload());
        upload.setText(values.get(pos).getUpload());
        return rowView;
    }
}
