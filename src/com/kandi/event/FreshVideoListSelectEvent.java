package com.kandi.event;

import java.util.List;
import java.util.Map;

/**
 * 音乐播放时选择的path
 */
public class FreshVideoListSelectEvent {
	private int videoIndex;
	private int video_src;
	List<Map<String, Object>> usbVideoList;
	private boolean location;

	int resultCode = 1;
	public void setVideoSrc(int type){
		video_src=type;
	}
	
	public int getVideoSrc(){
		return video_src;
	}
	
	public int getVideoIndex() {
		return videoIndex;
	}

	public void setVideoIndex(int videoIndex) {
		this.videoIndex = videoIndex;
	}

	public List<Map<String, Object>> getUsbVideoList() {
		return usbVideoList;
	}

	public void setUsbVideoList(List<Map<String, Object>> usbVideoList) {
		this.usbVideoList = usbVideoList;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public boolean isLocation() {
		return location;
	}

	public void setLocation(boolean location) {
		this.location = location;
	}
}
