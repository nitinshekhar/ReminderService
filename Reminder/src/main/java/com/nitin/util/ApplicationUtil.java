package com.nitin.util;

import java.util.Collection;

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
}
