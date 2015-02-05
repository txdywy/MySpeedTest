package com.num.db.datasource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.num.db.mapping.BaseMapping;
import com.num.db.mapping.ApplicationMapping;
import com.num.db.mapping.LatencyMapping;

public abstract class DataSource {
	// Database fields
	public SQLiteDatabase database;
	public Context context;
	public BaseMapping dbHelper;
	public static boolean inTransaction = false;
	private final boolean IS_PURGE_ALLOWED = true;
	public int currentMode = 0;

	public DataSource(Context context) {
		this.context = context;
	}

	public boolean isPurgeAllowed() {
		return IS_PURGE_ALLOWED;
	}

	public void delay() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void waitForTransaction() {

		while (inTransaction) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void open() throws SQLException {
		inTransaction = true;
        database = dbHelper.getWritableDatabase();

	}

	public void close() {
		inTransaction = false;
		dbHelper.close();
	}

	public String getTime() {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
		return sdf.format(new Date());
	}

	public String[] getColumns() {
		return dbHelper.getDatabaseColumns().getColumnNames();
	}

	public void setDBHelper(BaseMapping helper) {
		dbHelper = helper;
		if (isPurgeAllowed()) {
			purgeOldData(5, 10);

			//Random random = new Random();
			//int lower = random.nextInt(10) * 5 + 10;
			//int higher = lower + 5;

			//purgeOldData(lower, higher);

		}
	}

	public ArrayList<String> getDistinctValues(String column) {
		open();
		String[] columns = new String[1];
		columns[0] = column;
		Cursor cursor = database.query(true, dbHelper.getTableName(), columns,
				null, null, null, null, null, null);
		ArrayList<String> ret = new ArrayList<String>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {

			ret.add(cursor.getString(0));
			cursor.moveToNext();
		}
		cursor.close();
		close();
		System.out.println("fetch Distinct Values " + column + " DONE");
		return ret;
	}

	private void purgeOldData(int startdays, int enddays) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1 * enddays);
		Date endDelete = cal.getTime();
		cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1 * startdays);
		Date onDelete = cal.getTime();

		open();

		while (onDelete.after(endDelete)) {
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
			String s = sdf.format(onDelete);
			
			database.delete(dbHelper.getTableName(),
					ApplicationMapping.COLUMN_TIME + " LIKE '%" + s + "%'",
					null);
			
			cal.add(Calendar.DAY_OF_MONTH, -1);
			onDelete = cal.getTime();
		}

		close();

	}

	protected List<Map<String, String>> getDataStores(
			HashMap<String, String> filter) {
		open();

		List<Map<String, String>> dataStores = new ArrayList<Map<String, String>>();

		String selection = "";
		String[] arguments = new String[filter.size()];

		int count = 0;

		Iterator<String> iter = filter.keySet().iterator();
		while (iter.hasNext()) {

			String key = iter.next();
			String value = filter.get(key);

			selection += key + " = ? and ";
			arguments[count++] = value;
		}

		if (selection.length() > 5) {
			selection = selection.substring(0, selection.length() - 4);
		}

		Cursor cursor = database.query(dbHelper.getTableName(), getColumns(),
				selection, arguments, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {

			Map<String, String> dataStore = dbHelper.getDatabaseColumns()
					.getDataStore(cursor);
			dataStores.add(dataStore);

			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();

		close();

		return dataStores;

	}

	public List<Map<String, String>> getDataStores() {

		open();
		List<Map<String, String>> dataStores = new ArrayList<Map<String, String>>();

		Cursor cursor = database.query(dbHelper.getTableName(), getColumns(),
				null, null, null, null, "_id");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Map<String, String> dataStore = dbHelper.getDatabaseColumns()
					.getDataStore(cursor);
			dataStores.add(dataStore);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		close();

		return dataStores;
	}

	public abstract Date extractTime(Map<String, String> data);

	public String extractDate(Map<String, String> data) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
		String dateString = data.get(LatencyMapping.COLUMN_TIME);
		try {
			Date d = df.parse(dateString);
			return (d.getMonth() + 1) + "-" + d.getDate();
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date().toString();
		}
	}

	public Date extractDate(String dateString) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
		try {
			return df.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}

}
