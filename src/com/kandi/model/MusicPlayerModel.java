package com.kandi.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.kandi.event.MusicPlayerModelEvent;
import com.kandi.event.PlayMusicEvent;

import de.greenrobot.event.EventBus;
import android.media.MediaPlayer;

public class MusicPlayerModel {
	public enum PLAYSTATE{IDEL, PLAYING, PAUSE, STOP, ERROR};
	public enum PLAYMODE{PLAY_ALL, PLAY_SINGLE, PLAY_RANDOM};
	public int music_src=1;		//1表示本地音乐，2表示USB音乐
	private static MusicPlayerModel _module;
	public static MusicPlayerModel getInstance() {
		if(_module == null) {
			_module = new MusicPlayerModel();
		}
		return _module;
	}

	public interface IMusicPlayerModelListener {
		public void onCompletion(MusicPlayerModel mpm);
	}
	///////////////////
	
	private IMusicPlayerModelListener _listener =null;
	
	public void setListener(IMusicPlayerModelListener listener) {
		this._listener = listener;
	}
	
	
	private MediaPlayer musicPlayer = new MediaPlayer();
	private String mediaPath = "";
	private List<Map<String, Object>> musicList = new ArrayList<Map<String,Object>>();
	private int currentPlayListIndex = 0;

	PLAYMODE playMode = PLAYMODE.PLAY_ALL;
	PLAYSTATE playState = PLAYSTATE.IDEL;
	
	public PLAYSTATE getPlayState() {
		return playState;
	}

	private MusicPlayerModel() {
		musicPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer arg0) {

				if(playState==PLAYSTATE.STOP) {
					playState=PLAYSTATE.IDEL;
					musicPlayer.reset();
					return;
				}
				
				playState=PLAYSTATE.IDEL;
				musicPlayer.reset();
				
				if(next() == false) return;
				if(play() == false) return;
				
				EventBus.getDefault().postSticky(new MusicPlayerModelEvent(MusicPlayerModelEvent.EVENTTYPE.ON_COMPLETION));

				if(_listener != null) {
					_listener.onCompletion(MusicPlayerModel.this);
				}
			}
		});
	}
	
//	public MediaPlayer getMediaPlayer() {
//		return musicPlayer;
//	}
	
	public void setMediaPath(String path) {
		this.mediaPath = path;
	}
	
	public String getMediaPath() {
		return this.mediaPath;
	}
	
	public List<Map<String, Object>> getMusicList() {
		return this.musicList;
	}
	public void setMusicList(List<Map<String, Object>> musicList) {
		this.musicList = musicList;
	}

	public int getCurrentMusicIndex() {
		return currentPlayListIndex;
	}

	
	public boolean setCurrentMusic(int playListIndex) {
		if(playListIndex < 0 || playListIndex >= musicList.size()) {
			return false;
		}

		if(musicList == null) {
			currentPlayListIndex=0;
			mediaPath = null;
			return false;
		}

		String path = musicList.get(playListIndex).get("musicpath")+"";

		if(path.isEmpty()) {
			return false;
		}

		this.currentPlayListIndex = playListIndex;
		this.mediaPath = path;
		return true;
	}
	
	public boolean init() {
		playMode = PLAYMODE.PLAY_ALL;
		playState = PLAYSTATE.IDEL;
		musicPlayer.reset();
		return true;
	}
	
	public void loadPlayList(List<Map<String, Object>> musicList) {
		stop();
		this.musicList = musicList;
		
		if(musicList == null || musicList.isEmpty()) {
			currentPlayListIndex=0;
			mediaPath = null;
		}
		else {
			currentPlayListIndex=0;
			mediaPath = musicList.get(currentPlayListIndex).get("musicpath")+"";
			
		}
	}
	
	public void loadPlayListSec(List<Map<String, Object>> musicList) {
		this.musicList = musicList;
		
		if(musicList == null || musicList.isEmpty()) {
			currentPlayListIndex=0;
			mediaPath = null;
		}
		else {
			currentPlayListIndex=0;
			mediaPath = musicList.get(currentPlayListIndex).get("musicpath")+"";
			
		}
	}

	public boolean prepare() {
		if(playState==PLAYSTATE.IDEL){
				
			if(musicList.size()!=0 && mediaPath !=null)
			{

		        try{
		        	musicPlayer.setDataSource(mediaPath);
		            musicPlayer.prepare();
		            //musicPlayer.start(); 
		            //playState=PLAYSTATE.PLAYING;
		        }catch(Exception e){
		        	e.printStackTrace();
		        	return false;
		        }
			}
		}
		
		return true;
	}

	public boolean play() {
		if(playState==PLAYSTATE.IDEL || playState==PLAYSTATE.STOP){
				
			if(musicList.size()!=0)
			{
				mediaPath = musicList.get(currentPlayListIndex).get("musicpath")+"";
				if(!"".equals(mediaPath)){
					try{
						musicPlayer.reset();
						musicPlayer.setDataSource(musicList.get(currentPlayListIndex).get("musicpath")+"");
						musicPlayer.prepare();
						musicPlayer.start(); 
						playState=PLAYSTATE.PLAYING;
						EventBus.getDefault().postSticky(new MusicPlayerModelEvent(MusicPlayerModelEvent.EVENTTYPE.ON_PLAY));
						postPlayMusicEvent();
					}catch(Exception e){
						e.printStackTrace();
						return false;
					}
				}
			}
			else {
				return false;
			}
//		}else if(playState==PLAYSTATE.PLAYING){	//播放中
//			try{
//				musicPlayer.pause(); 
//				playState = PLAYSTATE.PAUSE;	//切换到暂停
//	        }catch(Exception e){
//	        	e.printStackTrace();
//	        	return false;
//	        }
		}else if(playState==PLAYSTATE.PAUSE){	//暂停
			try{
				musicPlayer.start(); 
				playState = PLAYSTATE.PLAYING;		//切换到播放
				EventBus.getDefault().postSticky(new MusicPlayerModelEvent(MusicPlayerModelEvent.EVENTTYPE.ON_PLAY));
				postPlayMusicEvent();
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
		}
		
		return true;
	}
	
	
	private void postPlayMusicEvent() {
		// 通知Fm和Video暂停播放
		PlayMusicEvent event = new PlayMusicEvent();
		event.type = PlayMusicEvent.MUSIC_PLAY;
		EventBus.getDefault().post(event);
	}

	public boolean pause() {

		if(playState==PLAYSTATE.PLAYING){	//播放中
			try{
				musicPlayer.pause(); 
				playState = PLAYSTATE.PAUSE;	//切换到暂停
				EventBus.getDefault().postSticky(new MusicPlayerModelEvent(MusicPlayerModelEvent.EVENTTYPE.ON_PAUSE));
	        }catch(Exception e){
	        	e.printStackTrace();
	        	return false;
	        }
		}
		
		PlayMusicEvent event = new PlayMusicEvent();
		event.type = PlayMusicEvent.MUSIC_PAUSE;
		EventBus.getDefault().post(event);
		
		return true;
	}
	
	public boolean stop() {
		if(playState==PLAYSTATE.PLAYING || playState==PLAYSTATE.PAUSE){	//播放中或暂停
			try{
				musicPlayer.stop(); 
				playState = PLAYSTATE.STOP;	//停止
				EventBus.getDefault().postSticky(new MusicPlayerModelEvent(MusicPlayerModelEvent.EVENTTYPE.ON_STOP));

//	        	musicPlayer.reset();
//				playState = PLAYSTATE.IDEL;	//
	        }catch(Exception e){
	        	e.printStackTrace();
	        	return false;
	        }
		}

		PlayMusicEvent event = new PlayMusicEvent();
		event.type = PlayMusicEvent.MUSIC_STOP;
		EventBus.getDefault().post(event);

		return true;
	}
	
	public boolean isPlaying() {
		/*modify by florg yd*/
		return playState==PLAYSTATE.PLAYING?true:false;
		//return musicPlayer.isPlaying();
		/*modify by florg yd*/
	}
	
	public boolean previous() {
		if(musicList == null || musicList.isEmpty()) {
			currentPlayListIndex=0;
			mediaPath = null;
			return false;
		}
		else {
			switch(this.playMode) {
			case PLAY_ALL:
				currentPlayListIndex = (--currentPlayListIndex) % musicList.size();
				if(currentPlayListIndex < 0) currentPlayListIndex=0;
				break;
			case PLAY_SINGLE:
				break;
			case PLAY_RANDOM:
				currentPlayListIndex = new Random().nextInt(musicList.size());
			}
			mediaPath = musicList.get(currentPlayListIndex).get("musicpath")+"";
		}

		if(this.isPlaying()) {
			stop();
			play();
		}
		else {
			stop();
		}
		
		EventBus.getDefault().postSticky(new MusicPlayerModelEvent(MusicPlayerModelEvent.EVENTTYPE.ON_PREVIOUS));
		
		return true;
	}

	public boolean next() {

		if(musicList == null || musicList.isEmpty()) {
			currentPlayListIndex=0;
			mediaPath = null;
			return false;
		}
		else {
			switch(this.playMode) {
			case PLAY_ALL:
				currentPlayListIndex = (++currentPlayListIndex) % musicList.size();
				break;
			case PLAY_SINGLE:
				break;
			case PLAY_RANDOM:
				if(musicList.size()<=1){
					currentPlayListIndex = 0;
				}else{
					currentPlayListIndex = new Random().nextInt(musicList.size());
				}
			}
			mediaPath = musicList.get(currentPlayListIndex).get("musicpath")+"";
		}

		if(this.isPlaying()) {
			stop();
			play();
		}
		else {
			stop();
		}

		EventBus.getDefault().postSticky(new MusicPlayerModelEvent(MusicPlayerModelEvent.EVENTTYPE.ON_NEXT));

		return true;
	}

	public String getCurrentMusicFilePath() {
		return mediaPath;
	}

	public String getCurrentMusicFileName() {
		if(mediaPath == null) {
			return "";
		}
		String fileName = mediaPath.substring(mediaPath.lastIndexOf("/")+1);
		
		return fileName;
	}

	public int getCurrentMusicDuration() {
		int duration = this.musicPlayer.getDuration();
		if(duration > 1000000000 ) {	//U盘拔出后获取duration值为 1797496648
			duration = 0;
		}
		return duration;
	}

	public int getCurrentPosition() {
		return this.musicPlayer.getCurrentPosition();
	}
	
	/**
	 * 
	 * @return
	 */
	public PLAYMODE getPlayMode() {
		return playMode;
	}

	/**
	 * 
	 */
	public void setPlayMode(PLAYMODE mode) {
		playMode = mode;
	}

	/**
	 * 
	 * @return
	 */
	public PLAYMODE switchPlayMode() {
		switch(playMode) {
		case PLAY_ALL:
			playMode = PLAYMODE.PLAY_SINGLE;
			break;
		case PLAY_SINGLE:
			playMode = PLAYMODE.PLAY_RANDOM;
			break;
		case PLAY_RANDOM:
			playMode = PLAYMODE.PLAY_ALL;
			break;
		}
		
		return playMode;
	}

	public void seekTo(int time) {
		this.musicPlayer.seekTo(time);
		EventBus.getDefault().postSticky(new MusicPlayerModelEvent(MusicPlayerModelEvent.EVENTTYPE.ON_SEEK));
	}
	
	
	static public String sec2Time(int secv){
		int hour = secv/3600;
		int min = (secv/60)%60;
		int sec = (secv%3600)%60;
		///System.out.println(hour+":"+min+":"+sec);
		String hourStr = hour<10?"0"+hour:hour+"";
		String minStr = min<10?"0"+min:min+"";
		String secStr = sec<10?"0"+sec:sec+"";
		if(hour==0){
			
			return minStr+":"+secStr;
		}else{
			return hourStr+":"+minStr+":"+secStr;
		}
	}
	
}
