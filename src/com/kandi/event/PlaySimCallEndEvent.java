package com.kandi.event;

public class PlaySimCallEndEvent {
	public static final int SIM_STATE = 1;
	public static final int SIM_NOSTATE = 0;
	public int type; // 0idle 1left 2longleft 3right 4longright
}
