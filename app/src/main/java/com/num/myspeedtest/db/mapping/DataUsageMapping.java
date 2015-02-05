package com.num.myspeedtest.db.mapping;

import android.content.Context;

import com.num.myspeedtest.db.DatabaseColumns;

public class DataUsageMapping  extends BaseMapping {

    public static String TABLE_NAME = "data_usage";
    public static int DATABASE_VERSION = 1;

    public DataUsageMapping(Context context) {
        super(context,TABLE_NAME,DATABASE_VERSION);
    }

//    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_RECV = "recv";
    public static final String COLUMN_SENT = "sent";
    public static final String COLUMN_RESET_RECV = "reset_recv";
    public static final String COLUMN_RESET_SENT = "reset_sent";
    public static final String COLUMN_BOOT_RECV = "boot_recv";
    public static final String COLUMN_BOOT_SENT = "boot_sent";


    public void setColumnMap() {
        columns = new DatabaseColumns(getTableName());
//        columns.add(COLUMN_ID,"integer primary key autoincrement");
        columns.add(COLUMN_NAME,"text not null primary key");
        columns.add(COLUMN_TIME,"text not null");
        columns.add(COLUMN_RECV,"integer");
        columns.add(COLUMN_SENT,"integer");
        columns.add(COLUMN_RESET_RECV,"integer");
        columns.add(COLUMN_RESET_SENT,"integer");
        columns.add(COLUMN_BOOT_RECV,"integer");
        columns.add(COLUMN_BOOT_SENT,"integer");
    }

}