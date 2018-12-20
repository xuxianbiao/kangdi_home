package com.kandi.model;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.kandi.home.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 系统需要持久化保存的设置参数
 * 
 * @author david_gu
 *
 */
public class PresistentData {

	static PresistentData instance;
	Context context;
	
	public static void initInstance(Context appContext) {
		instance = new PresistentData(appContext);
	}

	public static PresistentData getInstance() {
//		if(instance == null) {
//			instance = new PresistentData();
//		}
		 return instance;
	}
	
	private PresistentData() { }
	private PresistentData(Context appContext) {
		this.context = appContext;
		sp = appContext.getSharedPreferences("test", Activity.MODE_PRIVATE); 
	}
	
	private SharedPreferences sp;
	
	final String KEY_DATEFORMAT = "dateformat";
	final String KEY_TIMEFORMAT = "timeformat";
	private String DateFormat;	//日期格式
	private String TimeFormat;	//时间格式
	SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
	SimpleDateFormat sdfDate;
	
	private int MainVolume;		//主音量
	private int BassVolume;		//重低音
	private int BalanceVolume;	//音量平衡
	
	private int ScreenLight;				//屏幕亮度
	private boolean isScreenAutoLight;		//自动屏幕亮度
	
	private float LastPlayedRadioFmFreq;	//最后播放的收音机频率
	
	private int MusicPlayMode;				//音乐播放模式

	private String LastPlayedMusicFile;		//最后播放的音乐
	private int LastPlayedMusicLength;		//最后播放音乐的时长
	
	private String locale = "ch";
	
	//...

	//DATE FORMAT
	public SimpleDateFormat getDateFormatObj() {
		if(sdfDate == null) {
			sdfDate = new SimpleDateFormat(this.getDateFormat());			
		}
		return sdfDate;
	}
	public String getDateFormat() {
		if(DateFormat == null) {
			DateFormat = sp.getString(KEY_DATEFORMAT, context.getString(R.string.year_month_day));
		}
		return DateFormat;
	}
	public void setDateFormat(String dateFormat) {
		DateFormat = dateFormat;
		sdfDate = new SimpleDateFormat(DateFormat);
		sp.edit().putString("dateformat", DateFormat); 
	}
	
	//TIME FORMAT
	public SimpleDateFormat getTimeFormatObj() {
		if(sdfTime == null) {
			sdfTime = new SimpleDateFormat(this.getTimeFormat());			
		}
		return sdfTime;
	}
	public String getTimeFormat() {
		if(TimeFormat == null) {
			TimeFormat = sp.getString(KEY_TIMEFORMAT, "HH:mm");
		}
		return TimeFormat;
	}
	public void setTimeFormat(String timeFormat) {
		TimeFormat = timeFormat;
	}
	
	//TODO：实现以下持久层
	public int getMainVolume() {
		return MainVolume;
	}
	public void setMainVolume(int mainVolume) {
		MainVolume = mainVolume;
	}
	public int getBassVolume() {
		return BassVolume;
	}
	public void setBassVolume(int bassVolume) {
		BassVolume = bassVolume;
	}
	public int getBalanceVolume() {
		return BalanceVolume;
	}
	public void setBalanceVolume(int balanceVolume) {
		BalanceVolume = balanceVolume;
	}
	public int getScreenLight() {
		return ScreenLight;
	}
	public void setScreenLight(int screenLight) {
		ScreenLight = screenLight;
	}
	public boolean isScreenAutoLight() {
		return isScreenAutoLight;
	}
	public void setScreenAutoLight(boolean isScreenAutoLight) {
		this.isScreenAutoLight = isScreenAutoLight;
	}
	public float getLastPlayedRadioFmFreq() {
		return LastPlayedRadioFmFreq;
	}
	public void setLastPlayedRadioFmFreq(float lastPlayedRadioFmFreq) {
		LastPlayedRadioFmFreq = lastPlayedRadioFmFreq;
	}
	public String getLastPlayedMusicFile() {
		return LastPlayedMusicFile;
	}
	public void setLastPlayedMusicFile(String lastPlayedMusicFile) {
		LastPlayedMusicFile = lastPlayedMusicFile;
	}
	public int getLastPlayedMusicLength() {
		return LastPlayedMusicLength;
	}
	public void setLastPlayedMusicLength(int lastPlayedMusicLength) {
		LastPlayedMusicLength = lastPlayedMusicLength;
	}
	
	public int getMusicPlayMode() {
		return MusicPlayMode;
	}
	public void setMusicPlayMode(int musicPlayMode) {
		MusicPlayMode = musicPlayMode;
	}
	
	public void setLang(String locale) {
		this.locale = locale;
		sp.edit().putString("locale", locale).commit(); 
	}
	public String getLang() {
		if(sp.getString("locale", "").equals("")){
			return this.locale;
		}
		return sp.getString("locale", "");
	}

}
