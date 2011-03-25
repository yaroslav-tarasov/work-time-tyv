package com.tyv.worktime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ListView;


public class ReportList extends Activity {
    private static final String TAG = "ReportList";
    
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

        
        Uri mUri = intent.getData();
        
        // Inform the list we provide context menus for items
        
        // Perform a managed query. The Activity will handle closing and requerying the cursor
        // when needed.
        Cursor cursor = managedQuery(getIntent().getData(), PROJECTION, null, null,
        		WorktimeProvider.DEFAULT_SORT_ORDER);


        
        myListView = (ListView)findViewById(R.id.myListView);
        myEditText = (EditText)findViewById(R.id.myEditText);

        tpItems = new ArrayList<TimePoint>();
        int resID = R.layout.tplist_item;
        aa = new TimePointAdapter(this, resID, tpItems);
        myListView.setAdapter(aa);        
      
      //cursor.requery();

	  //tpItems.clear();
        
String outext = "";
Date now = new Date(); 
long wdtime = 0;
long starttp = 0;
long stoptp = now.getTime(); 
Date dt_old = null;
SimpleDateFormat sdf = new SimpleDateFormat("HH.mm");

	  if (cursor.moveToFirst())
	    do { 
	      String task = cursor.getString(cursor.getColumnIndex(WorktimeProvider.KEY_DETAILS));
	      long created = cursor.getLong(cursor.getColumnIndex(WorktimeProvider.KEY_DATE));
	      
Date dt = new Date(created);

if(dt_old == null )
{
	dt_old = dt;
}

if(dt_old != dt )
{
	outext += "Время за день: " + sdf.format(wdtime - 3 * 60 * 60 * 1000);  
	TimePoint newItem = new TimePoint( new Date(created),outext);
	tpItems.add(0, newItem);
	wdtime = 0;
}	
   	if(task.contentEquals("Enter"))
   	{
   		starttp = dt.getTime();
   		wdtime += stoptp - starttp;
   	}
   	else
   	{
   		stoptp = dt.getTime();

   	}

    } while(cursor.moveToNext());
	  
	   aa.notifyDataSetChanged();       
        
    }
}



