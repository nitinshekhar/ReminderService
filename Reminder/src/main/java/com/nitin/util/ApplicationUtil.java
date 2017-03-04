package com.nitin.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class ApplicationUtil {

	public static boolean isObjectEmpty(Object object) {
		if(object == null) return true;
		else if(object instanceof String) {
			if (((String)object).trim().length() == 0) {
				return true;
			}
		} else if(object instanceof Collection) {
			return isCollectionEmpty((Collection<?>)object);
		}
		return false;
	}

	private static boolean isCollectionEmpty(Collection<?> object) {
		if (object == null || object.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public static Date convertToDate(String date) {
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		Date mydate = null;
		try {
			mydate = df.parse(date);
		} catch (Exception e){
			//Do something
		}
		System.out.println("Before Conversion Date is : "+ date);
		System.out.println("After Conversion Date is : "+ mydate.toString());
		return mydate;
	}
	
	public static int compareDate(Date startDate, Date endDate) {
		return startDate.compareTo(endDate);
	}
}
