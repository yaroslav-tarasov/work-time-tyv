package com.machine.worktime;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;

import org.json.JSONStringer;

public class WT extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
      
        
        final Button button = (Button) findViewById(R.id.refrbutton);
         button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) // клик на кнопку
            {
                 RefreshTemper(v);
            }
         });    
         final Button button2 = (Button) findViewById(R.id.refrbutton2);
         button2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) // клик на кнопку
            {
                 RefreshTemper(v);
            }
         });  
         
         File imgDir = Environment.getExternalStorageDirectory();
         extStoragePath = imgDir.getAbsolutePath();
    }
    public void RefreshTemper(View v)
    { 
        final TextView tTemper = (TextView) findViewById(R.id.temper);
         String bashtemp = "";
         // bashtemp = GetTemper("http://www.bashkirenergo.ru/weather/ufa/index.asp");
        Date dt = new Date();
        int btnnum = 0;
        if (v.getId() == R.id.refrbutton2)
        {
        	btnnum = 2;
        }
        else
        {
        	btnnum = 1;        	
        }
        tTemper.setText(btnnum + " Время: " + dt.toLocaleString() + "\n" + extStoragePath); // bashtemp.concat("°")// отображение температуры
    };    
    
    private String extStoragePath;

}