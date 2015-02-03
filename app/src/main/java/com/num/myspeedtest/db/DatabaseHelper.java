package com.num.myspeedtest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.num.myspeedtest.model.Link;
import com.num.myspeedtest.model.Throughput;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by miseonpark on 12/16/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "myspeedtest.db";
    private static final int DATABASE_VERSION = 1;

    //Tables
    public static final String TABLE_THROUGHPUT = "table_throughput";
    public static final String TABLE_DATA_USAGE = "table_data_usage";
    public static final String TABLE_TRACEROUTE = "table_traceroute";

    //Columns for Throughput
    public static final int NUM_THROUGHPUT_ROWS = 10; //number of throughput records to keep
    public static final String COLUMN_DATE_TIME = "column_date_time";
    public static final String COLUMN_CONNECTION_NAME ="column_connection_name";
    public static final String COLUMN_UPLOAD = "column_upload";
    public static final String COLUMN_DOWNLOAD = "column_download";

    //Columns for DataUsage
//    public static final String COLUMN_APP_ID = "column_app_id";
//    public static final String COLUMN_APP_NAME = "column_app_name";
//    public static final String COLUMN_APP_PKG_NAME = "column_app_pkg_name";
//    public static final String COLUMN_APP_DATA_SENT = "column_app_data_sent";
//    public static final String COLUMN_APP_DATA_RECEIVED = "column_app_data_received";

    //Columns for Traceroute
//    public static final String COLUMN_DATETIME = "column_datetime";
//    public static final String COLUMN_HOP_NUMBER = "column_hop_number";
//    public static final String COLUMN_HOSTNAME = "column_hostname";
//    public static final String COLUMN_IP_ADDRESS = "column_ip_address";
//    public static final String COLUMN_RTT = "column_rtt";


    /* Create Statements */
    //Create Throughput Table
    private static final String CREATE_TABLE_THROUGHPUT = "CREATE TABLE "
            + TABLE_THROUGHPUT + "("
            + COLUMN_DATE_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + COLUMN_CONNECTION_NAME + " TEXT, "
            + COLUMN_DOWNLOAD + " TEXT, "
            + COLUMN_UPLOAD + " TEXT)";

//    private static final String CREATE_TABLE_DATA_USAGE = "CREATE TABLE "
//            + TABLE_DATA_USAGE + "("
//            + COLUMN_APP_ID + " integer primary key, "
//            + COLUMN_APP_NAME + " text not null, "
//            + COLUMN_APP_PKG_NAME + " text not null, "
//            + COLUMN_APP_DATA_SENT + " integer default 0, "
//            + COLUMN_APP_DATA_RECEIVED + " integer default 0)";

//    private static final String CREATE_TABLE_TRACEROUTE = "CREATE TABLE "
//            + TABLE_TRACEROUTE + "("
//            + COLUMN_DATETIME + " text not null, "
//            + COLUMN_HOP_NUMBER + " integer not null, "
//            + COLUMN_HOSTNAME + " text not null, "
//            + COLUMN_IP_ADDRESS + " text not null, "
//            + COLUMN_RTT + " text not null)";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_THROUGHPUT);
//        sqLiteDatabase.execSQL(CREATE_TABLE_DATA_USAGE);
//        sqLiteDatabase.execSQL(CREATE_TABLE_TRACEROUTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_THROUGHPUT);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA_USAGE);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACEROUTE);
        onCreate(sqLiteDatabase);
    }

    public void insertThroughput(Throughput throughput){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE_TIME, throughput.getDatetime());
//        values.put(COLUMN_CONNECTION_NAME, throughput.getConnectionType());
        values.put(COLUMN_DOWNLOAD, throughput.getDownload());
        values.put(COLUMN_UPLOAD, throughput.getUpload());

        db.insert(TABLE_THROUGHPUT, null, values);

        Log.d("DatabaseHelper", "insertThroughput");

        db.close();
    }

    public List<Throughput> getThroughput(){
        List<Throughput> throughputs = new ArrayList<Throughput>();
        String query = "SELECT * FROM " + TABLE_THROUGHPUT
                + " ORDER BY " + COLUMN_DATE_TIME + " DESC "
                + " LIMIT " + NUM_THROUGHPUT_ROWS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Throughput throughput = null;
        if(cursor.moveToFirst()){
            do {
                String date = cursor.getString(0);
                String download = cursor.getString(2);
                String upload = cursor.getString(3);

                throughput = new Throughput(date, download, upload);
                throughputs.add(throughput);
            }while(cursor.moveToNext());
        }

        Log.d("DatabaseHelper", "getThroughput");

        return throughputs;
    }

    public void updateThroughput(){

//        /* First, collect date_time list to delete */
//        String query = " SELECT " + COLUMN_DATE_TIME + " FROM " + TABLE_THROUGHPUT
//                + " ORDER BY " + COLUMN_DATE_TIME + " DESC LIMIT " + NUM_THROUGHPUT_ROWS;
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(query, null);
//
//        ArrayList<String> ids = new ArrayList<String>();
//        if(cursor.moveToFirst()){
//            do{
//                ids.add(cursor.getString(0));
//            }while(cursor.moveToNext());
//        }
//
//        /* Delete the unwanted records */
//        for(int i=0; i<ids.size(); i++){
//            db.delete(TABLE_THROUGHPUT, COLUMN_DATE_TIME+" = ?", new String[]{ids.get(i)});
//        }
//
//        db.close();

        Log.d("DatabaseHelper", "updateThroughput");
    }

    public String getDateTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}
