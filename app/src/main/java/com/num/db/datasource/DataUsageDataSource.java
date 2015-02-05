package com.num.db.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.num.db.mapping.DataUsageMapping;
import com.num.model.Application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


public class DataUsageDataSource extends DataSource {

    public DataUsageDataSource(Context context) {
        super(context);
        setDBHelper(new DataUsageMapping(context));
    }

    public void updateOnBoot(Application app) {
        if (!database.isOpen()) {
            database = dbHelper.getWritableDatabase();
        }
        open();

        String[] PACKAGE = new String[]{app.getPackageName()};
        int recv = getInt(PACKAGE, DataUsageMapping.COLUMN_RECV);
        int sent = getInt(PACKAGE, DataUsageMapping.COLUMN_SENT);

        ContentValues value = new ContentValues();
        value.put(DataUsageMapping.COLUMN_NAME, app.getPackageName());
        value.put(DataUsageMapping.COLUMN_TIME, getTime());
        value.put(DataUsageMapping.COLUMN_BOOT_RECV, recv);
        value.put(DataUsageMapping.COLUMN_BOOT_SENT, sent);
        value.put(DataUsageMapping.COLUMN_RESET_RECV, 0);
        value.put(DataUsageMapping.COLUMN_RESET_SENT, 0);
        value.put(DataUsageMapping.COLUMN_RECV, recv);
        value.put(DataUsageMapping.COLUMN_SENT, sent);
        try {
            database.insertWithOnConflict(dbHelper.getTableName(), null, value, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateOnReset(Application app){
        if (!database.isOpen()){
            database = dbHelper.getWritableDatabase();
        }
        open();

        ContentValues value = new ContentValues();
        value.put(DataUsageMapping.COLUMN_NAME, app.getPackageName());
        value.put(DataUsageMapping.COLUMN_TIME, getTime());
        value.put(DataUsageMapping.COLUMN_BOOT_RECV, 0);
        value.put(DataUsageMapping.COLUMN_BOOT_SENT, 0);
        value.put(DataUsageMapping.COLUMN_RESET_RECV, app.getTotalRecv());
        value.put(DataUsageMapping.COLUMN_RESET_SENT, app.getTotalSent());
        value.put(DataUsageMapping.COLUMN_RECV, 0);
        value.put(DataUsageMapping.COLUMN_SENT, 0);
        try {
            database.insertWithOnConflict(dbHelper.getTableName(), null, value, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Application updateOnGet (Application app) {
        if(!database.isOpen()) {
            database = dbHelper.getWritableDatabase();
        }
        open();
        String[] PACKAGE = new String[]{app.getPackageName()};
        ContentValues value = new ContentValues();

        int boot_recv = getInt(PACKAGE, DataUsageMapping.COLUMN_BOOT_RECV);
        int boot_sent = getInt(PACKAGE, DataUsageMapping.COLUMN_BOOT_SENT);
        int reset_recv = getInt(PACKAGE, DataUsageMapping.COLUMN_RESET_RECV);
        int reset_sent = getInt(PACKAGE, DataUsageMapping.COLUMN_RESET_SENT);
        int recv = (int) app.getTotalRecv() + boot_recv - reset_recv;
        int sent = (int) app.getTotalSent() + boot_sent - reset_sent;

        value.put(DataUsageMapping.COLUMN_NAME, app.getPackageName());
        value.put(DataUsageMapping.COLUMN_TIME, getTime());
        value.put(DataUsageMapping.COLUMN_BOOT_RECV, boot_recv);
        value.put(DataUsageMapping.COLUMN_BOOT_SENT, boot_sent);
        value.put(DataUsageMapping.COLUMN_RESET_RECV, reset_recv);
        value.put(DataUsageMapping.COLUMN_RESET_SENT, reset_sent);
        value.put(DataUsageMapping.COLUMN_RECV, recv);
        value.put(DataUsageMapping.COLUMN_SENT, sent);
        try {
            database.insertWithOnConflict(dbHelper.getTableName(), null, value, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Application newApp = app;
        newApp.setTotalRecv(recv);
        newApp.setTotalSent(sent);
        recv = getInt(PACKAGE, DataUsageMapping.COLUMN_RECV);
        sent = getInt(PACKAGE, DataUsageMapping.COLUMN_SENT);
        return newApp;
    }

    private int getInt(String[] PACKAGE, String column) {
        int result = 0;
        Cursor cursor = database.query(dbHelper.getTableName(), new String[]{column},
                DataUsageMapping.COLUMN_NAME + " = ?", PACKAGE, null, null, null);
        if(cursor.moveToFirst()) {
            result = cursor.getInt(cursor.getPosition());
        }
        cursor.close();
        return result;
    }

    @Override
    public Date extractTime(Map<String, String> data) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString =  data.get(DataUsageMapping.COLUMN_TIME);
        try {
            return df.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }
}