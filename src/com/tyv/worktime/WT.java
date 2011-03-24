package com.tyv.worktime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;

import com.tyv.worktime.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class WT extends Activity {
	
// 	ListView WTListView;
//  ArrayAdapter<TimePoint> aa;
	ArrayList<TimePoint> timepoints = new ArrayList<TimePoint>();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) // клик на кнопку
            {
                RefreshTemper(v);
            }
        });       
        final Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) // клик на кнопку
            {
                RefreshTemper(v);
            }
        });   
        
        // If no data was given in the intent (because we were started
        // as a MAIN activity), then use our default content provider.
        Intent intent = getIntent();
        if (intent.getData() == null) {
            intent.setData(WorktimeProvider.CONTENT_URI);
        }
        
//        WTListView = (ListView)this.findViewById(R.id.WTListView);

//        WTListView.setOnItemClickListener(new OnItemClickListener() {
//          public void onItemClick(AdapterView _av, View _v, int _index, long arg3) {
//            //selectedQuake = earthquakes.get(_index);
//            //showDialog(QUAKE_DIALOG);
//          }
//        });

//        int layoutID = android.R.layout.simple_list_item_1;
//         aa = new ArrayAdapter<TimePoint>(this, layoutID , timepoints);
//         WTListView.setAdapter(aa);
        
        // getSharedPreferences(MY_PREFS, mode);
        
        loadTimePointsFromProvider();
        String ret = RefreshScrollView(); 
        if ( ret.contentEquals("Enter"))
        {
        	button1.setEnabled(false); 
        	button2.setEnabled(true);   
        }
        else 
        {
        	button1.setEnabled(true); 
        	button2.setEnabled(false);     	
        }
        
        
    }
    
    /*
     * handles key events in the game. Update the direction our snake is traveling
     * based on the DPAD. Ignore events that would cause the snake to immediately
     * turn back on itself.
     * 
     * (non-Javadoc)
     * 
     * @see android.view.View#onKeyDown(int, android.os.KeyEvent)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {

        if (keyCode == KeyEvent.KEYCODE_MENU) {
        } 
        
        if (keyCode == KeyEvent.KEYCODE_SEARCH) {
        } 
        
        return super.onKeyDown(keyCode, msg);
    }   
    
    @Override 
    public boolean onCreateOptionsMenu(Menu menu)    { 
       super.onCreateOptionsMenu(menu); 
       int base=Menu.FIRST; // value is 1 
       MenuItem item1 = menu.add(base,base,base,"Подробный отчет")
       //.setShortcut('1', 'd')
       .setIcon(android.R.drawable.ic_dialog_info);
       base++;
       MenuItem item2 = menu.add(base,base,base,"Подробный отчет2")
       //.setShortcut('1', 'd')
       .setIcon(android.R.drawable.ic_input_get);       
       
       return true; 
    } 
     
    @Override 
    public boolean onOptionsItemSelected(MenuItem item)   { 
       if (item.getItemId() == 1)       { 
          //IntentUtils.tryOneOfThese(this); 
           // Uri uri = ContentUris.withAppendedId(getIntent().getData(), item.getItemId());
    	   // Uri uri = getIntent().getData();
           // String action = getIntent().getAction();
           /// startActivity(new Intent(Intent.ACTION_VIEW, uri));
    	   
    	   Intent di = new Intent(this, WTList.class);
    	   this.startActivity(di);
       } 
       else { 
         return super.onOptionsItemSelected(item); 
       } 
       return true; 
    }    
    public void RefreshTemper(View v)
    { 
    	
        final Button button1 = (Button) findViewById(R.id.button1);
        final Button button2 = (Button) findViewById(R.id.button2);      
        //final TextView tTemper = (TextView) findViewById(R.id.textView1);
        //final TextView tTemper2 = (TextView) findViewById(R.id.textView2);
        //String bashtemp = "";
        //bashtemp = GetTemper("http://www.bashkirenergo.ru/weather/ufa/index.asp");
        if (v.getId()==R.id.button1)
        {
			//tTemper.setText("Point1"); // отображение температуры
			//tTemper.setVisibility(1);
			TimePoint tp = new TimePoint(new Date(),"Enter") ;
			button1.setEnabled(false); 
			button2.setEnabled(true);          
			addNewEntry(tp);
        }
        else
        {
			//tTemper2.setText("Point2"); // отображение температуры
			//tTemper2.setVisibility(1);
			TimePoint tp = new TimePoint(new Date(),"Exit") ;
			button1.setEnabled(true); 
			button2.setEnabled(false);             
			addNewEntry(tp);
        }

        
        RefreshScrollView(); 
    };
    
    public String RefreshScrollView()
    { 
        final TextView tTextOut = (TextView) findViewById(R.id.textOut);       
        String ret = "none";
        String outext = "";
        Date now = new Date(); 
        long wdtime = 0;
        long starttp = 0;
        long stoptp = now.getTime();        
        //Iterator<TimePoint> it = timepoints.iterator();
      
        for (ListIterator<TimePoint> iterator = timepoints.listIterator(timepoints.size()); iterator.hasPrevious();) {
        	TimePoint tp = iterator.previous();
        	Date dt = tp.getDate();
        	if(dt.getDay() == now.getDay())
        	{
        		outext += tp.toString()  +  "\n"; //+ " wdtime= " +wdtime
        		if(ret.contentEquals("none"))
        			ret = tp.getDetails();
        		
               	if(tp.getDetails().contentEquals("Enter"))
               	{
               		starttp = tp.getDate().getTime();
               		wdtime += stoptp - starttp;
               	}
               	else
               	{
               		stoptp = tp.getDate().getTime();

               	}
        	}
        }  
        //outext +=  " wdtime= " +wdtime +  "\n"; 
        
	    SimpleDateFormat sdf = new SimpleDateFormat("HH.mm");
	    
        outext += "Время за день: " + sdf.format(wdtime - 3 * 60 * 60 * 1000);  
        tTextOut.setText(outext);
        return ret;
    };
    
    private void addNewEntry(TimePoint _tp) {
  	  ContentResolver cr = getContentResolver();
  	  // Construct a where clause to make sure we donХt already have this 
  	  // earthquake in the provider.
  	  String w = WorktimeProvider.KEY_DATE + " = " + _tp.getDate().getTime();

  	  // If the earthquake is new, insert it into the provider.
  	  if (cr.query(WorktimeProvider.CONTENT_URI, null, w, null, null).getCount()==0){
  	    ContentValues values = new ContentValues();    

  	    values.put(WorktimeProvider.KEY_DATE, _tp.getDate().getTime());
  	    values.put(WorktimeProvider.KEY_DETAILS, _tp.getDetails());

// 	    double lat = _quake.getLocation().getLatitude();
// 	    double lng = _quake.getLocation().getLongitude();
// 	    values.put(EarthquakeProvider.KEY_LOCATION_LAT, lat);
// 	    values.put(EarthquakeProvider.KEY_LOCATION_LNG, lng);


  	    cr.insert(WorktimeProvider.CONTENT_URI, values);
// 	    timepoints.add(_tp);

 	    addTimePointToArray(_tp);
  	  }
  	}

// 	private void addQuakeToArray(Quake _quake) {
// 	  if (_quake.getMagnitude() > minimumMagnitude) {
// 	    // Add the new quake to our list of earthquakes.
//	    earthquakes.add(_quake);
//
//	    // Notify the array adapter of a change.
// 	    aa.notifyDataSetChanged();
//  	  }
//  	}
  	
  	private void loadTimePointsFromProvider() {
        // Clear the existing earthquake array
//        earthquakes.clear();

        ContentResolver cr = getContentResolver();

        // Return all the saved earthquakes
        Cursor c = cr.query(WorktimeProvider.CONTENT_URI, null, null, null, null);
  		 
        if (c.moveToFirst()) {
          do { 
            // Extract the quake details.
            Long datems = c.getLong(WorktimeProvider.DATE_COLUMN);
            String details = c.getString(WorktimeProvider.DETAILS_COLUMN);


//            Location location = new Location("dummy");
//            location.setLongitude(lng);
//            location.setLatitude(lat);

            Date date = new Date(datems);

            TimePoint q = new TimePoint(date, details);
            addTimePointToArray(q);
          } while(c.moveToNext());
        }
      }    

  	private void addTimePointToArray(TimePoint _tp) {
		  //if (_quake.getMagnitude() > minimumMagnitude) {
		    // Add the new quake to our list of earthquakes.
  		timepoints.add(_tp);

		    // Notify the array adapter of a change.
//		 aa.notifyDataSetChanged();
		  //}
		} 
  	
    @Override
    protected void onPause() {
        super.onPause();
        //finish();
    } 	
    
    @Override
    protected void onStop() {
        super.onStop();
        //finish();
    } 	  	
}