package com.tyv.worktime;

import android.content.*;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;


public class WorktimeProvider extends ContentProvider {
	
	public static final Uri CONTENT_URI = Uri.parse("content://com.tyv.provider.WT/worktime");
	
	
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
	    int count;
	    
	    switch (uriMatcher.match(uri)) {
	      case WORKTIME:
	        count = worktimeDB.delete(WORKTIME_TABLE, where, whereArgs);
	        break;

	      case WORKTIME_ID:
	        String segment = uri.getPathSegments().get(1);
	        count = worktimeDB.delete(WORKTIME_TABLE, KEY_ID + "="
	                                    + segment
	                                    + (!TextUtils.isEmpty(where) ? " AND (" 
	                                    + where + ')' : ""), whereArgs);
	        break;

	      default: throw new IllegalArgumentException("Unsupported URI: " + uri);
	    }

	    getContext().getContentResolver().notifyChange(uri, null);
	    return count;
	}



	@Override
	public Uri insert(Uri _uri, ContentValues _values) {
	    // Insert the new row, will return the row number if 
	    // successful.
	    long rowID = worktimeDB.insert(WORKTIME_TABLE, null, _values);
	          
	    // Return a URI to the newly inserted row on success.
	    if (rowID > 0) {
	      Uri uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
	      getContext().getContentResolver().notifyChange(uri, null);
	      return uri;
	    }
	    throw new SQLException("Failed to insert row into " + _uri);
	}

	@Override
	public boolean onCreate() {
	    Context context = getContext();

	    WTDatabaseHelper dbHelper = new WTDatabaseHelper(context,  
	                                                                   DATABASE_NAME, 
	                                                                   null, 
	                                                                   DATABASE_VERSION);
	    worktimeDB = dbHelper.getWritableDatabase();
	    return (worktimeDB == null) ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
	   
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

	    qb.setTables(WORKTIME_TABLE);

	    // If this is a row query, limit the result set to the passed in row. 
	    switch (uriMatcher.match(uri)) {
	      case WORKTIME_ID: qb.appendWhere(KEY_ID + "=" + uri.getPathSegments().get(1));
	                     break;
	      default      : break;
	    }

	    // If no sort order is specified sort by date / time
	    String orderBy;
	    if (TextUtils.isEmpty(sortOrder)) {
	      orderBy = KEY_DATE;
	    } else {
	      orderBy = sortOrder;
	    }

	    // Apply the query to the underlying database.
	    Cursor c = qb.query(worktimeDB, 
	                        projection, 
	                        selection, selectionArgs, 
	                        null, null, 
	                        orderBy);

	    // Register the contexts ContentResolver to be notified if
	    // the cursor result set changes. 
	    c.setNotificationUri(getContext().getContentResolver(), uri);
	    
	    // Return a cursor to the query result.
	    return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
	    int count;
	    switch (uriMatcher.match(uri)) {
	      case WORKTIME: count = worktimeDB.update(WORKTIME_TABLE, values, 
	    		  selection, selectionArgs);
	                   break;

	      case WORKTIME_ID: String segment = uri.getPathSegments().get(1);
	                     count = worktimeDB.update(WORKTIME_TABLE, values, KEY_ID 
	                             + "=" + segment 
	                             + (!TextUtils.isEmpty(selection) ? " AND (" 
	                             + selection + ')' : ""), selectionArgs);
	                     break;

	      default: throw new IllegalArgumentException("Unknown URI " + uri);
	    }

	    getContext().getContentResolver().notifyChange(uri, null);
	    return count;
	}

	@Override
	public String getType(Uri uri) {
	    switch (uriMatcher.match(uri)) {
	      case WORKTIME: return "vnd.android.cursor.dir/vnd.tyv.WT";
	      case WORKTIME_ID: return "vnd.android.cursor.item/vnd.tyv.WT";
	      default: throw new IllegalArgumentException("Unsupported URI: " + uri);
	    }
	}
	
	// Create the constants used to differentiate between the different URI 
	  // requests.
	  private static final int WORKTIME = 1;
	  private static final int WORKTIME_ID = 2;

	  private static final UriMatcher uriMatcher;

	  // Allocate the UriMatcher object, where a URI ending in 'earthquakes' will
	  // correspond to a request for all earthquakes, and 'earthquakes' with a 
	  // trailing '/[rowID]' will represent a single earthquake row.
	  static {
	   uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	   uriMatcher.addURI("com.tyv.provider.WT", "worktime", WORKTIME);
	   uriMatcher.addURI("com.tyv.provider.WT", "worktime/#", WORKTIME_ID);
	  }	
	  //The underlying database
	  private SQLiteDatabase worktimeDB;

	  private static final String TAG = "WorktimeProvider";
	  private static final String DATABASE_NAME = "worktime.db";
	  private static final int DATABASE_VERSION = 1;
	  private static final String WORKTIME_TABLE = "worktime";	
	  
	  // Column Names
	  public static final String KEY_ID = "_id";
	  public static final String KEY_DATE = "date";
	  public static final String KEY_DETAILS = "details";
      /**
       * The default sort order for this table
       */
      public static final String DEFAULT_SORT_ORDER = "date DESC";
      
	  // Column indexes
	  public static final int DATE_COLUMN = 1;
	  public static final int DETAILS_COLUMN = 2;

	  
	  // Helper class for opening, creating, and managing database version control
	  private static class WTDatabaseHelper extends SQLiteOpenHelper {
	    private static final String DATABASE_CREATE =
	      "create table " + WORKTIME_TABLE + " (" 
	      + KEY_ID + " integer primary key autoincrement, "
	      + KEY_DATE + " INTEGER, "
	      + KEY_DETAILS + " TEXT);";
	        
	    public WTDatabaseHelper(Context context, String name,
	                                    CursorFactory factory, int version) {
	      super(context, name, factory, version);
	    }

	    @Override
	    public void onCreate(SQLiteDatabase db) {
	      db.execSQL(DATABASE_CREATE);           
	    }

	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	      Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
	                  + newVersion + ", which will destroy all old data");
	              
	      db.execSQL("DROP TABLE IF EXISTS " + WORKTIME_TABLE);
	      onCreate(db);
	    }
	  }	
	
	
	
	
	
}