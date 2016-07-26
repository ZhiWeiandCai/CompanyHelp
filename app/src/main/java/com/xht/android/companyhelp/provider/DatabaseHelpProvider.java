package com.xht.android.companyhelp.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.SparseArray;

public class DatabaseHelpProvider extends ContentProvider {
	
	private static final String TAG = DatabaseHelpProvider.class.getSimpleName();
	public static final String AUTHORITY = "com.xht.android.companyhelp.DatabaseHelpProvider";
	private static final String DATABASE_NAME = "my.db";
	private static final int DATABASE_VERSION = 1;
	private DatabaseHelper mDatabaseHelper;
	private MyDatabaseManager mMyDatabaseManager;
	private static final int mMytable = 1;
	private static final SparseArray<String> mTableMap = new SparseArray<String>() {
		{
			put(mMytable, MyDatabaseManager.TABLE_NAME);
		}
	};
	private static final UriMatcher mUriMatcher;
	static {
		mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		mUriMatcher.addURI(AUTHORITY, mTableMap.get(mMytable), mMytable);
	}

	@Override
	public boolean onCreate() {
		mDatabaseHelper = new DatabaseHelper(getContext());
		mMyDatabaseManager = new MyDatabaseManager();
		return true;
	}
	
	private AbsManager getAbsManagerByCode(int code) {
		String tableName = mTableMap.get(code);
		if (TextUtils.equals(tableName, MyDatabaseManager.TABLE_NAME)) {
			return mMyDatabaseManager;
		}
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
		Cursor cursor = null;
		int type = mUriMatcher.match(uri);
		AbsManager manager = getAbsManagerByCode(type);
		if (manager != null) {
			cursor = manager.query(database, uri, projection, selection, selectionArgs, sortOrder);
		}
		if (cursor != null) {
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
		}
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		int code = mUriMatcher.match(uri);
		String tableName = mTableMap.get(code);		
		return tableName;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		Uri ret = null;
		int code = mUriMatcher.match(uri);
		AbsManager manager = getAbsManagerByCode(code);
		if (manager != null) {
			ret = manager.insert(db, uri, values);
		}
		if (ret != null) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return ret;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		int count = 0;
		int code = mUriMatcher.match(uri);
		AbsManager manager = getAbsManagerByCode(code);
		if (manager != null) {
			count = manager.delete(db, uri, selection, selectionArgs);
		}
		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		int count = 0;
		int code = mUriMatcher.match(uri);
		AbsManager manager = getAbsManagerByCode(code);
		if (manager != null) {
			count = manager.update(db,  uri, values, selection,
					selectionArgs);
		}
		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}
	
	public static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(MyDatabaseManager.CREATE_SQL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + MyDatabaseManager.TABLE_NAME);
			onCreate(db);
		}
		
	}

}
