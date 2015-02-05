package com.num.db.mapping;


import android.content.Context;

import com.num.db.DatabaseColumns;

public class LatencyMapping extends BaseMapping {
	
	public static String TABLE_NAME = "latency";
	public static int DATABASE_VERSION = 1;

	public LatencyMapping(Context context) {
		super(context,TABLE_NAME,DATABASE_VERSION);
	}

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TIME = "time";
	public static final String COLUMN_VALUE = "value";
	public static final String COLUMN_MEASUREMENT = "measurement";
	public static final String COLUMN_SRCIP = "srcip";
	public static final String COLUMN_DSTIP = "dstip";
	public static final String COLUMN_CONNECTION = "connection";
	public static final String COLUMN_TYPE = "type";
	
	public void setColumnMap() {
		columns = new DatabaseColumns(getTableName());
		columns.add(COLUMN_ID,"integer primary key autoincrement");
		columns.add(COLUMN_TIME,"text not null");
		columns.add(COLUMN_TYPE,"text not null");
		columns.add(COLUMN_VALUE,"real");
		columns.add(COLUMN_MEASUREMENT,"text not null");		
		columns.add(COLUMN_SRCIP,"text not null");
		columns.add(COLUMN_DSTIP,"text not null");
		columns.add(COLUMN_CONNECTION,"text not null");
				
	}
	
}