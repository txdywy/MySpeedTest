package com.num.myspeedtest.db.datasource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.num.myspeedtest.db.DatabaseOutput;
import com.num.myspeedtest.db.mapping.DataUsageMapping;
import com.num.myspeedtest.controller.helpers.Logger;
import com.num.myspeedtest.model.Application;
import com.num.myspeedtest.model.BaseModel;
import com.num.myspeedtest.model.GraphPoint;
import com.num.myspeedtest.model.BaseModel;


public class DataUsageDataSource extends DataSource {
    private final String GRAPH_TYPE = "data";
    private final String Y_AXIS_UNITS = "bytes";

	private final String[] MODES = {"normal","aggregate"};

	public DataUsageDataSource(Context context) {
		super(context);
		setDBHelper(new DataUsageMapping(context));
	}

    public void updateOnBoot(Application app) {
        if (!database.isOpen()) {
            database = dbHelper.getWritableDatabase();
        }

        String[] SELECT = new String[]{app.getPackageName()};
        open();

        Cursor cursor = database.query(dbHelper.getTableName(), getColumns(),
                DataUsageMapping.COLUMN_NAME + " = ?", SELECT, null, null, "_id");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Map<String, String> dataStore = dbHelper.getDatabaseColumns().getDataStore(cursor);
//            Logger.show("Existing: " + dataStore.toString());
            cursor.moveToNext();
        }
        cursor.close();

        ContentValues value = new ContentValues();
        value.put(DataUsageMapping.COLUMN_NAME, app.getPackageName());
        value.put(DataUsageMapping.COLUMN_TIME, getTime());

        cursor = database.query(dbHelper.getTableName(), new String[]{DataUsageMapping.COLUMN_PREV_RECV},
                DataUsageMapping.COLUMN_NAME + " = ?", SELECT, null, null, "_id");
        cursor.moveToFirst();
        int prev_recv = cursor.getInt(cursor.getPosition());
        cursor.close();

        cursor = database.query(dbHelper.getTableName(), new String[]{DataUsageMapping.COLUMN_PREV_SENT},
                DataUsageMapping.COLUMN_NAME + " = ?", SELECT, null, null, "_id");
        cursor.moveToFirst();
        int prev_sent = cursor.getInt(cursor.getPosition());
        cursor.close();

        value.put(DataUsageMapping.COLUMN_PREV_RECV, app.getTotalRecv());
        value.put(DataUsageMapping.COLUMN_PREV_SENT, app.getTotalSent());

        value.put(DataUsageMapping.COLUMN_BOOT_RECV, prev_recv);
        value.put(DataUsageMapping.COLUMN_BOOT_SENT, prev_sent);

        try{
            database.update(dbHelper.getTableName(), value, DataUsageMapping.COLUMN_NAME + " = ?", SELECT);
        }catch(Exception e){
            Logger.show("db", e.getLocalizedMessage());
        }
    }

    private void addRow(Application app) {
        if (!database.isOpen()) {
            database = dbHelper.getWritableDatabase();
        }

        String[] SELECT = new String[]{app.getPackageName()};
        boolean exist = false;

        open();
        Cursor cursor = database.query(dbHelper.getTableName(), getColumns(),
                DataUsageMapping.COLUMN_NAME + " = ?", SELECT, null, null, "_id");
        cursor.moveToFirst();
        while (!exist && !cursor.isAfterLast()) {
            Map<String, String> dataStore = dbHelper.getDatabaseColumns().getDataStore(cursor);
            Logger.show("Existing: " + dataStore.toString());
            cursor.moveToNext();
            exist = true;
        }
        cursor.close();

        ContentValues value = new ContentValues();
        value.put(DataUsageMapping.COLUMN_NAME, app.getPackageName());
        value.put(DataUsageMapping.COLUMN_TIME, getTime());

        if (!exist) { // first install
            value.put(DataUsageMapping.COLUMN_RECV, app.getTotalRecv());
            value.put(DataUsageMapping.COLUMN_SENT, app.getTotalSent());
            value.put(DataUsageMapping.COLUMN_PREV_RECV, app.getTotalRecv());
            value.put(DataUsageMapping.COLUMN_PREV_SENT, app.getTotalSent());
            try {
                Logger.show("first install (adding): " + app.toString());
                database.insert(dbHelper.getTableName(), null, value);
            } catch (Exception e) {
                Logger.show("db", e.getLocalizedMessage());
            }
        } else { // either rebooting or normal case
            cursor = database.query(dbHelper.getTableName(), new String[]{DataUsageMapping.COLUMN_PREV_RECV},
                    DataUsageMapping.COLUMN_NAME + " = ?", SELECT, null, null, "_id");
            cursor.moveToFirst();
            int prev_recv = cursor.getInt(cursor.getPosition());
            cursor.close();
            cursor = database.query(dbHelper.getTableName(), new String[]{DataUsageMapping.COLUMN_PREV_SENT},
                    DataUsageMapping.COLUMN_NAME + " = ?", SELECT, null, null, "_id");
            cursor.moveToFirst();

            int prev_sent = cursor.getInt(cursor.getPosition());

            if (prev_recv > app.getTotalRecv() || prev_sent > app.getTotalSent()) {
                Logger.show("Rebooting case: " + prev_recv + " " + prev_sent + " " + app.toString());
                app.setTotalRecv(prev_recv + app.getTotalRecv());
                app.setTotalSent(prev_sent + app.getTotalSent());
            } else {
                value.put(DataUsageMapping.COLUMN_RECV, app.getTotalRecv());
                value.put(DataUsageMapping.COLUMN_SENT, app.getTotalSent());
                value.put(DataUsageMapping.COLUMN_PREV_RECV, app.getTotalRecv());
                value.put(DataUsageMapping.COLUMN_PREV_SENT, app.getTotalSent());
                Logger.show("Normal case: " + prev_recv + " " + prev_sent + " " + app.toString());
            }

            try{
                database.update(dbHelper.getTableName(), value, DataUsageMapping.COLUMN_NAME + " = ?", SELECT);
            }catch(Exception e){
                Logger.show("db", e.getLocalizedMessage());
            }
        }
    }

    private BaseModel addBaseModel(Application app) {
        if (!database.isOpen()) {
            database = dbHelper.getWritableDatabase();
        }

        String[] SELECT = new String[]{app.getPackageName()};
        boolean exist = false;
        open();

        Cursor cursor = database.query(dbHelper.getTableName(), getColumns(),
                DataUsageMapping.COLUMN_NAME + " = ?", SELECT, null, null, "_id");
        cursor.moveToFirst();
        while (!exist && !cursor.isAfterLast()) {
            Map<String, String> dataStore = dbHelper.getDatabaseColumns().getDataStore(cursor);
//            Logger.show("Existing: " + dataStore.toString());
            cursor.moveToNext();
            exist = true;
        }
        cursor.close();

        ContentValues value = new ContentValues();
        value.put(DataUsageMapping.COLUMN_NAME, app.getPackageName());
        value.put(DataUsageMapping.COLUMN_TIME, getTime());

        if (!exist) { // first install
            value.put(DataUsageMapping.COLUMN_RECV, app.getTotalRecv());
            value.put(DataUsageMapping.COLUMN_SENT, app.getTotalSent());
            value.put(DataUsageMapping.COLUMN_PREV_RECV, app.getTotalRecv());
            value.put(DataUsageMapping.COLUMN_PREV_SENT, app.getTotalSent());
            try {
                Logger.show("first install (adding): " + app.toString());
                database.insert(dbHelper.getTableName(), null, value);
                return app;
            } catch (Exception e) {
                Logger.show("db", e.getLocalizedMessage());
            }
        } else { // either rebooting or normal case
            cursor = database.query(dbHelper.getTableName(), new String[]{DataUsageMapping.COLUMN_PREV_RECV},
                    DataUsageMapping.COLUMN_NAME + " = ?", SELECT, null, null, "_id");
            cursor.moveToFirst();
            int prev_recv = cursor.getInt(cursor.getPosition());
            cursor.close();

            cursor = database.query(dbHelper.getTableName(), new String[]{DataUsageMapping.COLUMN_PREV_SENT},
                    DataUsageMapping.COLUMN_NAME + " = ?", SELECT, null, null, "_id");
            cursor.moveToFirst();
            int prev_sent = cursor.getInt(cursor.getPosition());
            cursor.close();

            cursor = database.query(dbHelper.getTableName(), new String[]{DataUsageMapping.COLUMN_BOOT_RECV},
                    DataUsageMapping.COLUMN_NAME + " = ?", SELECT, null, null, "_id");
            cursor.moveToFirst();
            int boot_recv = cursor.getInt(cursor.getPosition());
            cursor.close();

            cursor = database.query(dbHelper.getTableName(), new String[]{DataUsageMapping.COLUMN_BOOT_SENT},
                    DataUsageMapping.COLUMN_NAME + " = ?", SELECT, null, null, "_id");
            cursor.moveToFirst();
            int boot_sent = cursor.getInt(cursor.getPosition());
            cursor.close();

            if (prev_recv > app.getTotalRecv() || prev_sent > app.getTotalSent()) {
                Logger.show("Rebooting case: " + prev_recv + " " + prev_sent + " " + app.toString());
                value.put(DataUsageMapping.COLUMN_PREV_RECV, app.getTotalRecv());
                value.put(DataUsageMapping.COLUMN_PREV_SENT, app.getTotalSent());
                app.setTotalRecv(prev_recv + app.getTotalRecv());
                app.setTotalSent(prev_sent + app.getTotalSent());
                value.put(DataUsageMapping.COLUMN_RECV, app.getTotalRecv());
                value.put(DataUsageMapping.COLUMN_SENT, app.getTotalSent());
            } else {
                Logger.show("Normal case: " + prev_recv + " " + prev_sent + " " + app.toString());
                value.put(DataUsageMapping.COLUMN_RECV, app.getTotalRecv());
                value.put(DataUsageMapping.COLUMN_SENT, app.getTotalSent());
                value.put(DataUsageMapping.COLUMN_PREV_RECV, app.getTotalRecv());
                value.put(DataUsageMapping.COLUMN_PREV_SENT, app.getTotalSent());
            }

            app.setTotalRecv(app.getTotalRecv() + boot_recv);
            app.setTotalSent(app.getTotalSent() + boot_sent);

            try{
                database.update(dbHelper.getTableName(), value, DataUsageMapping.COLUMN_NAME + " = ?", SELECT);
            }catch(Exception e){
                Logger.show("db", e.getLocalizedMessage());
            }

            return app;
        }
        return null;
    }


    protected void insertModel(BaseModel model) {
        try {
            addRow((Application)model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected BaseModel insertBaseModel(BaseModel model) {
        try {
            return addBaseModel((Application) model);
        } catch (Exception e) {
            e.getStackTrace();
        }
        return null;
    }


    public DatabaseOutput getOutput() {

		List<Map<String,String>> allData = getDataStores();

		int totalUpload = 0;
		int totalDownload = 0;
		int countUpload = 0;
		int countDownload = 0;


		for (Map<String,String> data : allData) {
            Logger.show(data.toString());
		}

		if(countDownload==0) countDownload++;
		if(countUpload==0) countUpload++;

		DatabaseOutput output = new DatabaseOutput();

//		output.add("avg_upload", ""+totalUpload/countUpload);
//		output.add("avg_download", ""+totalDownload/countDownload);

		return output;

	}

	public HashMap<String, ArrayList<GraphPoint>> getGraphData() {
        return null;
//		return getGraphData(DeviceUtil.getNetworkInfo(context), "Atlanta, GA");
	}

	public HashMap<String, ArrayList<GraphPoint>> getGraphData(String currentConnectionType, String destination) {

		List<Map<String,String>> allData = getDataStores();

		ArrayList<GraphPoint> roundtripPoints = new ArrayList<GraphPoint>();
		ArrayList<GraphPoint> firsthopPoints = new ArrayList<GraphPoint>();

//		for (Map<String,String> data : allData) {
//
//			if(!data.get(LatencyMapping.COLUMN_CONNECTION).equals(currentConnectionType)) {
//				continue;
//			}
//
//			if(!data.get(LatencyMapping.COLUMN_DSTIP).equals(destination)) {
//				continue;
//			}
//
//
//
//			if(data.get(ThroughputMapping.COLUMN_TYPE).equals("ping")){
//				try {
//					roundtripPoints.add(new GraphPoint(roundtripPoints.size(),extractValue(data),extractTime(data)));
//				} catch (Exception e) {
//					continue;
//				}
//			} else if(data.get(ThroughputMapping.COLUMN_TYPE).equals("firsthop")){
//				try {
//					firsthopPoints.add(new GraphPoint(firsthopPoints.size(),extractValue(data),extractTime(data)));
//				} catch (Exception e) {
//					continue;
//				}
//			}
//		}

		HashMap collection = new HashMap<String,ArrayList<GraphPoint>>();

		collection.put("ping", roundtripPoints);
		collection.put("firsthop", firsthopPoints);

		return collection;

	}

	@Override
	public int extractValue(Map<String, String> data) {
        return 0;
//		return  (int)Double.parseDouble(data.get(DataUsageMapping.COLUMN_VALUE));
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

	@Override
	public String getGraphType() {
		return GRAPH_TYPE;
	}

	@Override
	public String getYAxisLabel() {
		return Y_AXIS_UNITS;
	}

	@Override
	public void aggregatePoints(GraphPoint oldP, GraphPoint newP) {
		oldP.incrementCount();
		oldP.y+=newP.y;
	}

	@Override
	public String[] getModes() {
		return MODES;
	}
}
