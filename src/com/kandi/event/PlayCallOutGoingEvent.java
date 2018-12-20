package com.kandi.event;

public class PlayCallOutGoingEvent {
	public static final int CALLOUTGOING_STATE = 1;
	public static final int CALLOUTGOING_NOSTATE = 0;
	public int type; // 0idle 1left 2longleft 3right 4longright
}
