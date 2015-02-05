package com.num.view.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.num.R;
import com.num.model.Application;
import com.num.model.Usage;

import java.util.List;

public class DataUsageListAdapter extends ArrayAdapter<Application>{
    private Context context;
    private List<Application> values;

    public DataUsageListAdapter(Context context, List<Application> values) {
        super(context, R.layout.row_data_usage, values);
        this.context = context;
        this.values = values;
    }

    public View getView(int pos, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_data_usage, parent, false);
        TextView nameTextView = (TextView) rowView.findViewById(R.id.text_app_name);
        ImageView iconImageView = (ImageView) rowView.findViewById(R.id.icon_data_usage);
        ProgressBar usageBar = (ProgressBar) rowView.findViewById(R.id.progress_data_usage);
        TextView usageTextView = (TextView) rowView.findViewById(R.id.text_data);
        TextView percentTextView = (TextView) rowView.findViewById(R.id.text_percent);

        Application application = values.get(pos);
        long totalUsage = Usage.totalSent + Usage.totalRecv;
        long maxUsage = Usage.maxUsage;
        String name = application.getName();
        Drawable icon = application.getIcon();
        String usage = application.getUsageString();
        String percent = application.getPercentageString(totalUsage);
        int usageProgress = application.getPercentage(maxUsage);

        nameTextView.setText(name);
        iconImageView.setImageDrawable(icon);
        usageTextView.setText(usage);
        percentTextView.setText(percent);
        usageBar.setProgress(usageProgress);

        return rowView;
    }
}
