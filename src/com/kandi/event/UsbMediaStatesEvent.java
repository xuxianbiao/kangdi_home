package com.kandi.event;

public class UsbMediaStatesEvent {
	public enum MEDIASTATE{UNKNOWN, MEDIA_PLUGED, MEDIA_UNPLUGED};
	
	private MEDIASTATE _usbstate;
	
	private UsbMediaStatesEvent() {
	}
	
	public UsbMediaStatesEvent(MEDIASTATE usbstate) {
		_usbstate = usbstate;
	}
	
	public MEDIASTATE getUsbState() {
		return _usbstate;
	}
}
