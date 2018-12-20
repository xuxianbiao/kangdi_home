package com.yd.manager;

import java.util.regex.Pattern;

/**
 * 
 * @author dw in yd
 *
 */
public class YdUtils {

	private static String regex = "^[a-zA-Z].*$"; 
	
	private static Pattern pattern = Pattern.compile(regex);
	
	public static boolean isStartWithLetter(String str){
		if (str != null) {
			if (pattern.matcher(str).matches()) {
				return true;
			}
		}
		return false;
	}
	
}