package com.xht.android.companyhelp.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

public class MyDatabaseManager implements AbsManager {
	
	public static final String TABLE_NAME = "mytable";
	public static final String CREATE_SQL;
	
	public static class MyDbColumns implements BaseColumns{
		public static final Uri CONTENT_URI = Uri.parse("content://" + DatabaseHelpProvider.AUTHORITY
				+ "/" + TABLE_NAME);
		/**
		 * id
		 */
		public static final String UID = "id";
		/**
		 * Name
		 */
		public static final String NAME = "name";
		
		public static final String PHONE = "phone";
		
		public static final String DEFAULT_SORT_ORDER = UID + " desc";// ASC
	}
	
	static {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("CREATE TABLE ");
		stringBuilder.append(TABLE_NAME);
		stringBuilder.append(" (");
		stringBuilder.append(MyDbColumns._ID + " INTEGER PRIMARY KEY,");
		stringBuilder.append(MyDbColumns.UID + " INTEGER,");
		stringBuilder.append(MyDbColumns.NAME + " TEXT,");
		stringBuilder.append(MyDbColumns.PHONE + " INTEGER");
		stringBuilder.append(")");
		CREATE_SQL = stringBuilder.toString();
	}

	@Override
	public Cursor query(SQLiteDatabase db, Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		String orderByString;
		if (TextUtils.isEmpty(sortOrder)) {
			orderByString = MyDbColumns.DEFAULT_SORT_ORDER;
		} else {
			orderByString = sortOrder;
		}
		Cursor c = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, orderByString);
		return c;
	}

	@Override
	public Uri insert(SQLiteDatabase database, Uri uri, ContentValues cValues) {
		long rowId = database.insert(TABLE_NAME, null, cValues);
		if (rowId > 0) {
			Uri ret = ContentUris.withAppendedId(uri, rowId);
			return ret;
		}
		return null;
	}

	@Override
	public int delete(SQLiteDatabase db, Uri uri, String where,
			String[] whereArgs) {
		int count;
		count = db.delete(TABLE_NAME, where, whereArgs);
		return count;
	}

	@Override
	public int update(SQLiteDatabase db, Uri uri, ContentValues values,
			String where, String[] whereArgs) {
		int count;
		count = db.update(TABLE_NAME, values, where, whereArgs);
		return count;
	}
	
	public static void saveMytable(Context context,int id, String name, int phoneNum) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(MyDbColumns.UID, id);
		contentValues.put(MyDbColumns.NAME, name);
		contentValues.put(MyDbColumns.PHONE, phoneNum);
		String whereClause = MyDbColumns.UID + " = ?";
		String[] whereArgs = new String[] { ""+id };
		int size = context.getContentResolver().update(MyDbColumns.CONTENT_URI, contentValues, whereClause, whereArgs);
		if (size <= 0) {
			context.getContentResolver().insert(MyDbColumns.CONTENT_URI, contentValues);
		}
	}

}
