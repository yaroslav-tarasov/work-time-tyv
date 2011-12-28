package com.tyv.worktime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import java.lang.StringBuilder;

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
        
        myListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
              // When clicked, show a toast with the TextView text
              // Toast.makeText(getApplicationContext(), ((TextView) ((RelativeLayout)((LinearLayout) view).getChildAt(0)).getChildAt(0) ).getText() + "  " + String.valueOf(id) ,Toast.LENGTH_SHORT).show();
 
            Toast.makeText(getApplicationContext(), tpItems.get((int) id).toString() + String.valueOf(id) ,Toast.LENGTH_SHORT).show();
            //Cursor cursor = managedQuery(getIntent().getData(), PROJECTION, null, new String[] {"date = " + tpItems.get((int) id).getDate()},
            //		WorktimeProvider.DEFAULT_SORT_ORDER);
              
              
            }
          });      
      //cursor.requery();

	  //tpItems.clear();
        
	String outext = "";
	Date now = new Date(); 
	long wdtime = 0;
	long starttp = 0;
	long stoptp = now.getTime(); 
	Date dt_old = null;
	long created = 0;
	SimpleDateFormat sdf = new SimpleDateFormat("HH.mm");
	TimeZone  timeZone = TimeZone.getDefault();
	int offset = timeZone.getOffset( System.currentTimeMillis() );

if (cursor.moveToFirst())
	do { 
		String task = cursor.getString(cursor.getColumnIndex(WorktimeProvider.KEY_DETAILS));
		created = cursor.getLong(cursor.getColumnIndex(WorktimeProvider.KEY_DATE));
	      
		Date dt = new Date(created);
		
		if(dt_old == null )
		{
			dt_old = dt;
		}

		if(dt_old.getDate() != dt.getDate())
		{
			outext += "Время за день: " + sdf.format(wdtime - offset );  //+ 1*60*60*1000
			TimePoint newItem = new TimePoint( dt_old,outext);
			tpItems.add(0, newItem);
			wdtime = 0;
			outext = "";
			dt_old = dt;
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
	
	  outext += "Время за день: " + sdf.format(wdtime - offset);  //3 * 60 * 60 * 1000
	  TimePoint newItem = new TimePoint( new Date(created),outext);
	  tpItems.add(0, newItem);	  
	  
	  aa.notifyDataSetChanged();       
        
    }
}



