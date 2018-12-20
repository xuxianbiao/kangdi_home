package com.kandi.service;

import java.io.File;
import java.util.Map.Entry;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.kandi.home.R;
import com.kandi.util.FileUtil;
import com.kandi.util.SerializableMap;
import com.kandi.view.MusicListActivity.MusicList;
import com.kandi.view.VideoListActivity.VideoList;

public class CopyFileService extends Service{

	public static CopyFileService instance;
	public static int count=-1;
	public static int total;
	private final int FILE_FULL_ACTION = 1000;
	
	public static String CopyFileLocalToUSB(){
		if(count >= total || count == -1){
			if(count == total){
				return ""+total+"/"+count;
			}else{
				return "";
			}
		}
		return ""+total+"/"+count;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	/**
	 * 添加拷贝文件,进行拷贝
	 * @param 拷贝文件的路径集合
	 */
	@Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
		instance = this;
		count = 1;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Bundle bundle = intent.getExtras();
				SerializableMap serializableMap = (SerializableMap) bundle.get(getString(R.string.map));
				total = serializableMap.getMap().size();
				boolean isFull = false;
				for (Entry<Integer, Boolean> entry : serializableMap.getMap().entrySet()) {
					int key = entry.getKey();
					if((Boolean)serializableMap.getMap().get(key)){
						try {
							File src,tar;
							if(!intent.getBooleanExtra(getString(R.string.isusb), false)){
								String usbpath = getString(R.string.usb_path);
								if(!intent.getBooleanExtra(getString(R.string.choose), false)){
									String videostr = VideoList.getInstance().getLocalVideoList().get(key).get(getString(R.string.moviepath))+"";
									src = new File(videostr);
									tar = new File(usbpath+videostr.substring(videostr.lastIndexOf("/")+1));
								}else{
									String musicstr = MusicList.getInstance().getLocalMusicList().get(key).get(getString(R.string.musicpath))+"";
									src = new File(musicstr);
									tar = new File(usbpath+musicstr.substring(musicstr.lastIndexOf("/")+1));
								}
							}else{
								if(!intent.getBooleanExtra(getString(R.string.choose), false)){
									String usbvideostr = VideoList.getInstance().getUsbVideoList().get(key).get(getString(R.string.moviepath))+"";
									src = new File(usbvideostr);
									tar = new File(getString(R.string.local_movie_path)+usbvideostr.substring(usbvideostr.lastIndexOf("/")+1));
								}else{
									String usbmusicstr = MusicList.getInstance().getUsbMusicList().get(key).get(getString(R.string.musicpath))+"";
									src = new File(usbmusicstr);
									tar = new File(getString(R.string.local_music_path)+usbmusicstr.substring(usbmusicstr.lastIndexOf("/")+1));
								}
							}
							if(!intent.getBooleanExtra(getString(R.string.isusb), false)){
								if(FileUtil.getUsbFreeSize()>10 && FileUtil.getUsbFreeSize()>FileUtil.getFileSize(src)){
									FileUtil.copyFile(src, tar);
								}else{
									isFull = true;
								}
							}else{
								if(FileUtil.getSDFreeSize()>100 && FileUtil.getSDFreeSize()>FileUtil.getFileSize(src)){
									FileUtil.copyFile(src, tar);
								}else{
									isFull = true;
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					count++;
				}
				if(isFull){
					mhandler.sendEmptyMessage(FILE_FULL_ACTION);
				}
				stopSelf();
			}
		}).start();
		return super.onStartCommand(intent, flags, startId);
    }
	
	Handler mhandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FILE_FULL_ACTION:
				Toast.makeText(getApplicationContext(), getString(R.string.sdcard_isfull), Toast.LENGTH_SHORT).show();
				break;
			}
		}
		
	};
	
	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		instance = null;
		count = -1;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return true;
	}

}
