package com.tyv.worktime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;




public class WTList extends Activity { // Áûë ListActivity
    private static final String TAG = "WTList";
    
    private ArrayList<TimePoint> tpItems;
    private ListView myListView;
    private EditText myEditText;   
    private TimePointAdapter aa;

    // Menu item ids
    public static final int MENU_ITEM_DELETE = Menu.FIRST;
    public static final int MENU_ITEM_INSERT = Menu.FIRST + 1;

    /**
     * The columns we are interested in from the database
     */
    private static final String[] PROJECTION = new String[] {
            WorktimeProvider.KEY_ID, // 0
            WorktimeProvider.KEY_DATE, // 1
            WorktimeProvider.KEY_DETAILS, // 2
    };

    /** The index of the title column */
    private static final int COLUMN_INDEX_TITLE = 1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main2);
        
        setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);

        // If no data was given in the intent (because we were started
        // as a MAIN activity), then use our default content provider.

        final Intent intent = getIntent();

        // Do some setup based on the action being performed.

        final String action = intent.getAction();
//        if (Intent.ACTION_VIEW.equals(action)) {
//            // Requested to edit: set that state, and the data being edited.
//            // mState = STATE_EDIT;
//            Uri mUri = intent.getData();
//        } else {
//            // Whoops, unknown action!  Bail.
//            Log.e(TAG, "Unknown action, exiting");
//            finish();
//            return;
//        }
        
        Uri mUri = intent.getData();
        
        // Inform the list we provide context menus for items
//        getListView().setOnCreateContextMenuListener(this);
        
        // Perform a managed query. The Activity will handle closing and requerying the cursor
        // when needed.
        Cursor cursor = managedQuery(getIntent().getData(), PROJECTION, null, null,
        		WorktimeProvider.DEFAULT_SORT_ORDER);

//        // Used to map notes entries from the database to views
//        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.wtlist_item, cursor,
//                new String[] { WorktimeProvider.KEY_DATE, WorktimeProvider.KEY_DETAILS}, new int[] { android.R.id.text1,android.R.id.text2 });
//        
//        CursorToStringConverter converter = new CursorToStringConverter() {
//
//            @Override
//            public CharSequence convertToString(Cursor cursor) {
//               int desiredColumn = 1;
//               long date = cursor.getLong(desiredColumn);
//	       	   SimpleDateFormat sdf = new SimpleDateFormat("d-MMMM-yyyy  HH.mm");
//	    	   String dateString = sdf.format(date);	      
//	    	    
//               return dateString;
//            }
//         };   
//         
//         adapter.setCursorToStringConverter(converter);    
//        
//        setListAdapter(adapter);
        
        myListView = (ListView)findViewById(R.id.myListView);
        myEditText = (EditText)findViewById(R.id.myEditText);

        tpItems = new ArrayList<TimePoint>();
        int resID = R.layout.tplist_item;
        aa = new TimePointAdapter(this, resID, tpItems);
        myListView.setAdapter(aa);        
      
      //cursor.requery();

	  //tpItems.clear();
	    
	  if (cursor.moveToFirst())
	    do { 
	      String task = cursor.getString(cursor.getColumnIndex(WorktimeProvider.KEY_DETAILS));
	      long created = cursor.getLong(cursor.getColumnIndex(WorktimeProvider.KEY_DATE));

	      TimePoint newItem = new TimePoint( new Date(created),task);
	      tpItems.add(0, newItem);
	    } while(cursor.moveToNext());
	  
	   aa.notifyDataSetChanged();       
        
    }
    
}