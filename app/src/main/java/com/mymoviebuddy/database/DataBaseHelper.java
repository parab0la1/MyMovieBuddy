package com.example.joelnilsson6.finproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by joel.nilsson6 on 2016-02-19.
 *
 * The DataBaseHelper class is used for creating, updating and clearing the
 * SQLLitedatabase.
 *
 */
public class DataBaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "Actor.db";
	public static final int CURRENT_VERSION = 1;
	public static final String TABLE_NAME = "actor_table";

	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, CURRENT_VERSION);
	}

	public boolean insertData(String firstName, String lastName) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();

		contentValues.put("FIRSTNAME", firstName);
		contentValues.put("LASTNAME", lastName);

		long result = db.insert(TABLE_NAME, null, contentValues);

		if (result == -1) {
			return false;
		} else {
			return true;
		}
	}

	public Cursor getAllData() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor result = db.rawQuery("select * from " + TABLE_NAME, null);

		return result;
	}

	public void clearDb() {

		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from " + TABLE_NAME);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + TABLE_NAME
				+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT, FIRSTNAME TEXT, LASTNAME TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
}
