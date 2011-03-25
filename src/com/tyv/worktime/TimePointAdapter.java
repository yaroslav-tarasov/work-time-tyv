package com.tyv.worktime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;



public class TimePointAdapter extends ArrayAdapter<TimePoint> {
	  int resource;

	  public TimePointAdapter(Context _context, 
	                             int _resource, 
	                             List<TimePoint> _items) {
	    super(_context, _resource, _items);
	    resource = _resource;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LinearLayout tpView;

	    TimePoint item = getItem(position);

	    String taskString = item.getDetails().equals("Enter")?"Вход":"Выход";
	    Date createdDate = item.getDate();
	    SimpleDateFormat sdf = new SimpleDateFormat("HH.mm dd-MMMM-yyyy");
	    String dateString = sdf.format(createdDate);

	    if (convertView == null) {
	    	tpView = new LinearLayout(getContext());
	      String inflater = Context.LAYOUT_INFLATER_SERVICE;
	      LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
	      vi.inflate(resource, tpView, true);
	    } else {
	    	tpView = (LinearLayout) convertView;
	    }

	    TextView dateView = (TextView)tpView.findViewById(R.id.rowDate);
	    TextView taskView = (TextView)tpView.findViewById(R.id.row);
	      
	    dateView.setText(dateString);
	    taskView.setText(taskString);

	    return tpView;
	  }
}
