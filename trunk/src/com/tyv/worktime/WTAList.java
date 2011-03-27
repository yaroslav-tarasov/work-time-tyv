package com.tyv.worktime;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;
import android.widget.TextView;




public class WTAList extends ListActivity {
    private static final String TAG = "WTAList";

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
        getListView().setOnCreateContextMenuListener(this);
        
        // Perform a managed query. The Activity will handle closing and requerying the cursor
        // when needed.
        Cursor cursor = managedQuery(getIntent().getData(), PROJECTION, null, null,
        		WorktimeProvider.DEFAULT_SORT_ORDER);
        



	    
        // Used to map notes entries from the database to views
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.tplist_item, cursor, //R.layout.wtlist_item
                new String[] { WorktimeProvider.KEY_DATE, WorktimeProvider.KEY_DETAILS}, new int[] { R.id.rowDate, R.id.row });
        
        adapter.setViewBinder(new WTABinder());
        
        CursorToStringConverter converter = new CursorToStringConverter() {

            @Override
            public CharSequence convertToString(Cursor cursor) {
               int desiredColumn = 1;
               return cursor.getString(desiredColumn);
            }
         };   
         
         adapter.setCursorToStringConverter(converter);    
        
        setListAdapter(adapter);
    }
    
    private static class WTABinder implements SimpleCursorAdapter.ViewBinder
    {
        public boolean setViewValue(View view, Object data, String textRepresentation)
        {
            if (view.getId() == android.R.id.text1)
            {
                ((TextView)view).setText("Text");// (Integer)data);
                return true;
            }

            return false;
        }

		@Override
		public boolean setViewValue(View arg0, Cursor arg1, int arg2) {
            if (arg0.getId() == R.id.rowDate)//android.R.id.text1
            {
      	        long created = arg1.getLong(arg1.getColumnIndex(WorktimeProvider.KEY_DATE));           	
            	SimpleDateFormat sdf = new SimpleDateFormat("d-MMMM-yyyy  HH.mm");
        	    String dateString = sdf.format(new Date(created));
                ((TextView)arg0).setText(dateString);// (Integer)data);
                return true;
            }
			return false;
		}   
    }   
}
