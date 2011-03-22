package com.tyv.worktime;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimePoint {
	  private Date date;
	  private String details;


	  public Date getDate() { return date; }
	  public String getDetails() { return details; }

	  
	  public TimePoint(Date _d, String _det) {
	    date = _d;
	    details = _det;
	  }

	  @Override
	  public String toString() {
	    SimpleDateFormat sdf = new SimpleDateFormat("d-MMMM-yyyy  HH.mm");
	    String dateString = sdf.format(date);	    
	    return dateString + "  " + details;
	  }
}
