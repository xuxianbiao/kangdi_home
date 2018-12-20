package com.kandi.event;

public class PlayFmEvent {
	public static final int FM_TYPE_LEFT = 1;
	public static final int FM_TYPE_LONG_LEFT = 2;
	public static final int FM_TYPE_RIGHT = 3;
	public static final int FM_TYPE_LONG_RIGHT = 4;
	public int type = 0; // 0idle 1left 2longleft 3right 4longright
	public int showDeskMusic = 0;
}
