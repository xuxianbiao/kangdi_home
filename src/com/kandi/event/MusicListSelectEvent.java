package com.kandi.event;

import java.util.List;
import java.util.Map;

/**
 * 音乐播放时选择的path
 */
public class MusicListSelectEvent {
	private int musicIndex;
	private int music_src;
	List<Map<String, Object>> usbmusicList;
	
	int resultCode = 1;

	public void setMusicSrc(int type){
		 music_src=type;
	}
	
	public int getMusicSrc(){
		return music_src;
	}
	
	public int getMusicIndex() {
		return musicIndex;
	}

	public void setMusicIndex(int musicIndex) {
		this.musicIndex = musicIndex;
	}

	public List<Map<String, Object>> getUsbmusicList() {
		return usbmusicList;
	}

	public void setUsbmusicList(List<Map<String, Object>> usbmusicList) {
		this.usbmusicList = usbmusicList;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
}
