package net.tsz.afinal.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtils {
	
	private static final String SHARED_NAME = "kandi_shared_defualt";
	
	@SuppressWarnings("deprecation")
	public static SharedPreferences getSharedPreferences(Context context){
		//外部也可以去读到配置
		SharedPreferences preferences = context.getSharedPreferences(SHARED_NAME, Activity.MODE_WORLD_READABLE);
		return preferences;
	}
	
	public static Editor getEditor(Context context){
		return getSharedPreferences(context).edit();
	}
}

