package com.num.myspeedtest.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.num.myspeedtest.R;
import com.num.myspeedtest.db.datasource.DataUsageDataSource;
import com.num.myspeedtest.model.Application;
import com.num.myspeedtest.model.Usage;

public class DataUsageListAdapter extends ArrayAdapter<Application>{
    private Context context;
    private Usage usage;
    private Application[] applications;
    private DataUsageDataSource db;

    public DataUsageListAdapter(Context context, Usage usage) {
        super(context, R.layout.row_data_usage, usage.getApplications());
        this.context = context;

        this.usage = usage;
        applications = usage.getApplications();
        db = new DataUsageDataSource(context);
        db.open();
        Application[] temp_apps = new Application[applications.length];

        int counter = 0;
        for (Application app : applications) {
            temp_apps[counter++] = (Application) db.insertBaseModelandReturn(app);
        }

        this.applications = temp_apps;

    }

    public View getView(int pos, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);
        long totalUsage = usage.getTotalRecv() + usage.getTotalSent();
        long maxUsage = usage.getMaxUsage();
        Application[] applications = usage.getApplications();
        View rowView = inflater.inflate(R.layout.row_data_usage, parent, false);
        TextView appName = (TextView) rowView.findViewById(R.id.text_app_name);
        ImageView appIcon = (ImageView) rowView.findViewById(R.id.icon_data_usage);
        ProgressBar usageBar = (ProgressBar) rowView.findViewById(R.id.progress_data_usage);
        TextView appUsage = (TextView) rowView.findViewById(R.id.text_data);
        TextView appPercent = (TextView) rowView.findViewById(R.id.text_percent);
        appName.setText(applications[pos].getName());
        appIcon.setImageDrawable(applications[pos].getIcon());
        appUsage.setText(applications[pos].getUsageString());
        appPercent.setText(applications[pos].getPercentageString(totalUsage));
        usageBar.setProgress(applications[pos].getPercentage(maxUsage));
        return rowView;
    }
}
