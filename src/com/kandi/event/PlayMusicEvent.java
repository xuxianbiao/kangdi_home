package com.kandi.event;

public class PlayMusicEvent {
	public static final int MUSIC_IDLE = 0;
	public static final int MUSIC_PLAY = 1;
	public static final int MUSIC_PAUSE = 2;
	public static final int MUSIC_STOP = 3;
	
	public int type; // 0idle 1play 2pause 3stop
}
