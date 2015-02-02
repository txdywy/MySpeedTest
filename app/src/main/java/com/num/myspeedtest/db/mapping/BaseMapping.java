package com.num.myspeedtest.db.mapping;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.num.myspeedtest.db.DatabaseColumns;

public abstract class BaseMapping extends SQLiteOpenHelper {
	
	private String TABLE_NAME;
	private int DATABASE_VERSION;

	public BaseMapping(Context context,String tableName, int version) {
		super(context, tableName+".db", null, version);
		TABLE_NAME = tableName;
		DATABASE_VERSION = version;
	}
	
	public DatabaseColumns columns;

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(getCreateText());
	}

	// Method is called during an upgrade of the database,
	// e.g. if you increase the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(BaseMapping .class.getName(), "Upgrading database " + getDBName()+" from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + getTableName());
		onCreate(database);
	}
	
	public String getCreateText() {
		return getDatabaseColumns().getDatabaseCreateText();
	}
	
	public DatabaseColumns getDatabaseColumns() {
		setColumnMap();
		return columns;
	}

	public abstract void setColumnMap();
	
	public String getTableName() {
		return TABLE_NAME;
	}
	
	public String getDBName() {
		return getTableName()+".db";
	}
	
}