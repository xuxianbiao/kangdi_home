package com.kandi.event;

public class MusicPlayerModelEvent {

	public enum EVENTTYPE {ON_ERROR, ON_PLAY,ON_PAUSE,ON_STOP, ON_SEEK, ON_COMPLETION, ON_NEXT, ON_PREVIOUS}

	public EVENTTYPE type;
	
	public MusicPlayerModelEvent() {
		
	}

	public MusicPlayerModelEvent(EVENTTYPE et) {
		type = et;
	}
}
