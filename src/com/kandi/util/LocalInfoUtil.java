package com.kandi.util;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalInfoUtil {
	public static String getValueFromSP(Context context,String SPKey,String key){
		SharedPreferences sp = context.getSharedPreferences(SPKey, 0);
		return sp.getString(key, "");
	}
	public static void saveValue(Context context ,String SPKey,String key,String val){
		SharedPreferences sp = context.getSharedPreferences(SPKey, 0);
		sp.edit().putString(key, val).commit();
	}
	public static void clearValues(Context context,String SPKey){
		SharedPreferences sp = context.getSharedPreferences(SPKey, 0);
		sp.edit().clear().commit();
	}
}
