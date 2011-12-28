package com.tyv.worktime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.TimeZone;

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
                newTimePoint(v);
            }
        });       
        final Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) // клик на кнопку
            {
                newTimePoint(v);
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
       MenuItem item1 = menu.add(1,base,base,"Подробный отчет")
       //.setShortcut('1', 'd')
       .setIcon(android.R.drawable.ic_dialog_info);
       base++;
       MenuItem item2 = menu.add(1,base,base,"Суммарно за день")
       //.setShortcut('1', 'd')
       .setIcon(android.R.drawable.ic_dialog_info);    
       
       base++;
       MenuItem item3 = menu.add(2,base,base,"Подробный отчет3")
       //.setShortcut('1', 'd')
       .setIcon(android.R.drawable.ic_dialog_info);  
       
       return true; 
    } 
     
    @Override 
    public boolean onOptionsItemSelected(MenuItem item)   { 
       if (item.getItemId() == 1)       { 
          //IntentUtils.tryOneOfThese(this); 
           // Uri uri = ContentUris.withAppendedId(getIntent().getData(), item.getItemId());
    	   Uri uri = getIntent().getData();
           // String action = getIntent().getAction();
           /// startActivity(new Intent(Intent.ACTION_VIEW, uri));
    	   
    	   Intent di = new Intent(Intent.ACTION_VIEW, uri,this, WTList.class);
    	   this.startActivity(di);
       } 
       else if (item.getItemId() == 2)       { 
          //IntentUtils.tryOneOfThese(this); 
           // Uri uri = ContentUris.withAppendedId(getIntent().getData(), item.getItemId());
    	   Uri uri = getIntent().getData();
           // String action = getIntent().getAction();
           /// startActivity(new Intent(Intent.ACTION_VIEW, uri));
    	   
    	   Intent di = new Intent(Intent.ACTION_VIEW, uri,this, ReportList.class);
    	   this.startActivity(di);
       } 
       else if (item.getItemId() == 3)       { 
           //IntentUtils.tryOneOfThese(this); 
            // Uri uri = ContentUris.withAppendedId(getIntent().getData(), item.getItemId());
     	   Uri uri = getIntent().getData();
            // String action = getIntent().getAction();
            /// startActivity(new Intent(Intent.ACTION_VIEW, uri));
     	   
     	   Intent di = new Intent(Intent.ACTION_VIEW, uri,this, WTAList.class);
     	   this.startActivity(di);
        }
       else { 
         return super.onOptionsItemSelected(item); 
       } 
       return true; 
    }    
    public void newTimePoint(View v)
    { 
    	
        final Button button1 = (Button) findViewById(R.id.button1);
        final Button button2 = (Button) findViewById(R.id.button2);      

        if (v.getId()==R.id.button1)
        {
			TimePoint tp = new TimePoint(new Date(),"Enter") ;
			button1.setEnabled(false); 
			button2.setEnabled(true);          
			addNewEntry(tp);
        }
        else
        {
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
        	if(dt.getDate() == now.getDate() && dt.getMonth() == now.getMonth() && dt.getYear() == now.getYear())
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
	    TimeZone  timeZone = TimeZone.getDefault();
	    int offset = timeZone.getOffset( System.currentTimeMillis() );
        outext += "Время за день: " + sdf.format(wdtime - offset ); //+ 1*60*60*1000 
        tTextOut.setText(outext);
        return ret;
    };
    
    private void addNewEntry(TimePoint _tp) {
  	  ContentResolver cr = getContentResolver();

  	  String w = WorktimeProvider.KEY_DATE + " = " + _tp.getDate().getTime();

  	  if (cr.query(WorktimeProvider.CONTENT_URI, null, w, null, null).getCount()==0){
  	    ContentValues values = new ContentValues();    

  	    values.put(WorktimeProvider.KEY_DATE, _tp.getDate().getTime());
  	    values.put(WorktimeProvider.KEY_DETAILS, _tp.getDetails());

  	    cr.insert(WorktimeProvider.CONTENT_URI, values);

 	    addTimePointToArray(_tp);
  	  }
  	}

 	
  	private void loadTimePointsFromProvider() {


  		
        ContentResolver cr = getContentResolver();


        Cursor c = cr.query(WorktimeProvider.CONTENT_URI, null, null, null, null);
  		 
        if (c.moveToFirst()) {
          do { 
            Long datems = c.getLong(WorktimeProvider.DATE_COLUMN);
            String details = c.getString(WorktimeProvider.DETAILS_COLUMN);

            Date date = new Date(datems);

            TimePoint q = new TimePoint(date, details);
            addTimePointToArray(q);
          } while(c.moveToNext());
        }
      }    

  	private void addTimePointToArray(TimePoint _tp) {
  		timepoints.add(_tp);


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