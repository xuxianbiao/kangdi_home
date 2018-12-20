package com.kandi.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kandi.adapter.MusicListAdapter;
import com.kandi.adapter.Usbadapter;
import com.kandi.application.BaseApplication;
import com.kandi.event.FinishMusicListEvent;
import com.kandi.event.FreshMusicListSelectEvent;
import com.kandi.event.MusicListSelectEvent;
import com.kandi.event.UsbMediaStatesEvent;
import com.kandi.home.R;
import com.kandi.model.MusicPlayerModel;
import com.kandi.service.CopyFileService;
import com.kandi.util.CommonUtils;
import com.kandi.util.FileUtil;
import com.kandi.util.SerializableMap;
import com.yd.manager.YdUtils;

import de.greenrobot.event.EventBus;

public class MusicListActivity extends Activity implements Callback{
	
	private ListView linearListView;
	
	public static class MusicList {
		private List<Map<String, Object>> musicList = new ArrayList<Map<String,Object>>();
		private List<Map<String, Object>> usbmusicList = new ArrayList<Map<String,Object>>();
		
		static private MusicList _list; 
		static private boolean isDirty=true;
		static public MusicList getInstance() {
			if(_list == null) {
				_list = new MusicList();
			}
			return _list;
		}
		
		private MusicList() {
		}
		public List<Map<String, Object>> getLocalMusicList() {
			return musicList;
		}
		public List<Map<String, Object>> getUsbMusicList() {
			return usbmusicList;
		}
		
		public boolean isDirty() {
			return isDirty;
		}
		
		public void setDirtyFlag() {
			isDirty = true;
		}
		
		public void resetDirtyFlag() {
			isDirty = false;
		}
	}
	

	private RelativeLayout bgview;
	private LinearLayout mainview;
	private ImageButton localmusicbtn;
	private ImageButton refreshfilebtn;
	private String newpath;
	private ImageButton usbmusicbtn;
	private Handler handler;
	private Usbadapter usbadapter;
	private ImageView iv_music_nextline;
	private boolean isusb; //判断是不是usb的数据
	private getmStorageManager storageManager;
	private String[] musicpath;
	private ProgressBar pb_music_usbload;
	private Handler usbhandler;
	
	MusicListAdapter mla;
	public Map<Integer, Boolean> recodeStatu = new HashMap<Integer, Boolean>();
	private Button bt_cancel;
	private Button bt_delete_select;
	private Button bt_import_select;
	private TextView txt_progress;
	private Button bt_select_all;
	private final int REFRESH_PROGRESS_ACTION=1000;
	private int count = 0;
	
	

	//add by dw in yd
	
	private static final int UI_REFRESH = 0;
	private static final int SORT = 1;
	private static boolean isScaning = false;
	private int tempListSize = 0;
	
	private static boolean isMediaPluged = true;
	
	private MyHandler myHandler = null;
	private List<Map<String, Object>> tempList = new ArrayList<Map<String,Object>>();
	
	
	class MyHandler extends Handler {
		public MyHandler() {
		}

		public MyHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
		}
	}
	
	
	private Handler mYdHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UI_REFRESH:
				MusicList.getInstance().getUsbMusicList().clear();
				MusicList.getInstance().getUsbMusicList().addAll(tempList);
				linearListView.setVisibility(View.VISIBLE);
				pb_music_usbload.setVisibility(View.GONE);
				usbadapter.notifyDataSetChanged();
				MusicList.getInstance().resetDirtyFlag();
				break;
			case SORT:
				MusicList.getInstance().getUsbMusicList().clear();
				MusicList.getInstance().getUsbMusicList().addAll(tempList);
				usbadapter.notifyDataSetChanged();
				listSort();
				break;

			default:
				break;
			}
		};
	};
	
	private void listSort() {
//		Collections.sort(MusicList.getInstance().getUsbMusicList(), new Comparator<Map<String, Object>>() {
//
//			@Override
//			public int compare(Map<String, Object> map1,
//					Map<String, Object> map2) {
//				Collator collator = Collator.getInstance(java.util.Locale.ENGLISH);
//				return collator.getCollationKey(map1.get("name")+"").compareTo(collator.getCollationKey(map2.get("name")+""));
//			}
//		});
		Collections.sort(MusicList.getInstance().getUsbMusicList(), new Comparator<Map<String, Object>>() {

			@Override
			public int compare(Map<String, Object> map1,
					Map<String, Object> map2) {
				Collator collator = Collator.getInstance(java.util.Locale.CHINA);
				String name1 = map1.get("name")+"";
				String name2 = map2.get("name")+"";
				if (YdUtils.isStartWithLetter(name1)) {
					name1 = "9" + name1;
				}
				if (YdUtils.isStartWithLetter(name2)) {
					name2 = "9" + name2;
				}
				return collator.getCollationKey(name1).compareTo(collator.getCollationKey(name2));
			}
		});
		Log.i("info", "--- MusicListActivity_sortfinish --- ");
	}
	
	
	private void loadUSBFilesYd(){
		linearListView.setAdapter(usbadapter);	
		MusicList.getInstance().getUsbMusicList().clear();
		tempList.clear();
		myHandler.post(getPathYdRunnable);
	}
	
	private Runnable getPathYdRunnable = new Runnable() {
		
		@Override
		public void run() {
			isScaning = true;
			tempListSize = 0;
			getPathYd(storageManager.getVolumePaths()[2]);
			Log.i("info", "--- MusicListActivity_scanfinish --- ");
			mYdHandler.sendEmptyMessage(UI_REFRESH);
			mYdHandler.sendEmptyMessage(SORT);
			isScaning = false;
		}
	};
	 
	public void getPathYd(String path) {
		File f=new File(path);
		File[] fs=f.listFiles();
		if(fs != null){
			for (int i = 0; i < fs.length; i++) {
				
				if(fs[i].getName().trim().toLowerCase().endsWith(".mp3")
						|| fs[i].getName().trim().toLowerCase().endsWith(".amr")
						|| fs[i].getName().trim().toLowerCase().endsWith(".flac")
						|| fs[i].getName().trim().toLowerCase().endsWith(".m4a")
						|| fs[i].getName().trim().toLowerCase().endsWith(".m4r")
						|| fs[i].getName().trim().toLowerCase().endsWith(".wav")
						|| fs[i].getName().trim().toLowerCase().endsWith(".aac")) {										
					Map<String, Object> map=new HashMap<String, Object>();				
					map.put("name", fs[i].getName());
					map.put("musicpath", fs[i].getAbsolutePath());
//					tempList.add(map);
					
					if (isMediaPluged) {
						tempList.add(map);
					}
				}
				if (fs[i].isDirectory()) {
					newpath = fs[i].getAbsolutePath();
					getPathYd(newpath);
				}
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (tempList.size() > 0 && tempListSize < tempList.size() && tempList.size() % 20 >= 0) {
			tempListSize = tempList.size();
			if (tempList.size() > 20) {
				mYdHandler.sendEmptyMessage(UI_REFRESH);
			}
		}
	}
	
	//add end
	
	
	
	private void loadFiles(){
		///musicList.clear();
		
		MusicList.getInstance().getLocalMusicList().clear();
		
		List<String> fileList = FileUtil.GetFiles2("sdcard", "mp3,amr,m4a,m4r,wav,aac");
		for(int i =0;i<fileList.size();i++){
			Map<String, Object> map = new HashMap<String, Object>();
			String musicPath = fileList.get(i)+"";
			if(!musicPath.contains("._")){
				map.put("musicpath", fileList.get(i));
				///musicList.add(map);
				MusicList.getInstance().getLocalMusicList().add(map);
			}
		}
		
		//add by dw in yd
		Collections.sort(MusicList.getInstance().getLocalMusicList(), new Comparator<Map<String, Object>>() {
			
			@Override
			public int compare(Map<String, Object> map1,
					Map<String, Object> map2) {
				Collator collator = Collator.getInstance(java.util.Locale.CHINA);
				String music1 = map1.get("musicpath")+"";
				String music2 = map2.get("musicpath")+"";
				String v1 = music1.substring(music1.lastIndexOf("/") + 1);
				String v2 = music2.substring(music2.lastIndexOf("/") + 1);
				if (YdUtils.isStartWithLetter(v1)) {
					v1 = "9" + v1;
				}
				if (YdUtils.isStartWithLetter(v2)) {
					v2 = "9" + v2;
				}
				return collator.getCollationKey(v1).compareTo(collator.getCollationKey(v2));
			}
		});
//		if (Settings.System.getInt(getContentResolver(), "system_kandi_key", 0) == 1) {
//		}else {
//			Collections.sort(MusicList.getInstance().getLocalMusicList(), new Comparator<Map<String, Object>>() {
//
//				@Override
//				public int compare(Map<String, Object> map1,
//						Map<String, Object> map2) {
//					Collator collator = Collator.getInstance();
//					String music1 = map1.get("musicpath")+"";
//					String music2 = map2.get("musicpath")+"";
//					String v1 = music1.substring(music1.lastIndexOf("/"));
//					String v2 = music2.substring(music2.lastIndexOf("/"));
//					return collator.getCollationKey(v1).compareTo(collator.getCollationKey(v2));
//				}
//			});
//		}
		
		//add end
		
		
		linearListView.setVisibility(View.VISIBLE);
		pb_music_usbload.setVisibility(View.GONE);
	}
	public void getpath(String path)
	{
//		MusicList.getInstance().getUsbMusicList().clear();
		File f=new File(path);
		File[] fs=f.listFiles();
		if(fs != null){
			for (int i = 0; i < fs.length; i++) {				
				if(fs[i].getName().trim().toLowerCase().endsWith(".mp3")
						|| fs[i].getName().trim().toLowerCase().endsWith(".amr")
						|| fs[i].getName().trim().toLowerCase().endsWith(".m4a")
						|| fs[i].getName().trim().toLowerCase().endsWith(".m4r")
						|| fs[i].getName().trim().toLowerCase().endsWith(".wav")
						|| fs[i].getName().trim().toLowerCase().endsWith(".aac"))
				{										
					Map<String, Object> map=new HashMap<String, Object>();				
					map.put("name", fs[i].getName());
					map.put("musicpath", fs[i].getAbsolutePath());
					MusicList.getInstance().getUsbMusicList().add(map);
				}
				
				if(fs[i].isDirectory())
				{
					newpath=fs[i].getAbsolutePath();
					getpath(newpath);
				}
				
			}
		}
		linearListView.setVisibility(View.VISIBLE);
		pb_music_usbload.setVisibility(View.GONE);
		usbadapter.notifyDataSetChanged();
		MusicList.getInstance().resetDirtyFlag();
		
	}
public void UsbMediaStatesEvent(UsbMediaStatesEvent event) {
		
		switch(event.getUsbState()) {
		case MEDIA_PLUGED:
			{	 
				//Toast.makeText(MusicActivity.this, "U盘已插入", 0).show();
				MusicList.getInstance().getUsbMusicList().clear();
				iv_music_nextline.setPadding(150, 0, 0, 0);
				isusb=true;		
				linearListView.setVisibility(View.GONE);
				pb_music_usbload.setVisibility(View.VISIBLE);
				
					
			// add by dw in yd
			// MusicListActivity.this.loadUSBFiles();

//			Log.i("info", "--- MusicListActivity_UsbMediaStatesEvent_Settings --- " + Settings.System.getInt(getContentResolver(), "system_kandi_key", 0));
			myHandler.removeCallbacks(getPathYdRunnable);
			isMediaPluged = true;
			loadUSBFilesYd();
//			if (Settings.System.getInt(getContentResolver(), "system_kandi_key", 0) == 1) {
//			} else {
//				MusicListActivity.this.loadUSBFiles();
//			}
			// add end
			}
			break;
		case MEDIA_UNPLUGED:
			{
				//Toast.makeText(MusicActivity.this, "U盘已拔出", 0).show();
						
				//	Toast.makeText(MusicListActivity.this, "U盘已拔出", 0).show();
					MusicList.getInstance().setDirtyFlag();
					MusicList.getInstance().getUsbMusicList().clear();
					
				//add by dw in yd
//					usbadapter.notifyDataSetChanged();
					
					myHandler.removeCallbacks(getPathYdRunnable);
					isMediaPluged = false;
					tempList.clear();
					MusicList.getInstance().getUsbMusicList().clear();
					usbadapter.notifyDataSetChanged();
				//add end
			}
			break;
		case UNKNOWN:
		default:
		}
	}


	 class getmStorageManager {

			private Activity mActivity;
			private StorageManager mStorageManager;
			private Method mMethodGetPaths;

			public getmStorageManager(Activity activity) {
			mActivity = activity;
			if (mActivity != null) {
			mStorageManager = (StorageManager)mActivity
			.getSystemService(Activity.STORAGE_SERVICE);
			try {
			mMethodGetPaths = mStorageManager.getClass()
			.getMethod("getVolumePaths");
			} catch (NoSuchMethodException e) {
			e.printStackTrace();
			}
			}
			}

			public String[] getVolumePaths() {
			String[] paths = null;
			try {
			paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
			} catch (IllegalArgumentException e) {
			e.printStackTrace();
			} catch (IllegalAccessException e) {
			e.printStackTrace();
			} catch (InvocationTargetException e) {
			e.printStackTrace();
			}
			return paths;
			}
			}
	
	private void loadUSBFiles(){
		linearListView.setAdapter(usbadapter);	
		getpath(storageManager.getVolumePaths()[2]);
		Collections.sort(MusicList.getInstance().getUsbMusicList(), new Comparator<Map<String, Object>>() {

			@Override
			public int compare(Map<String, Object> map1,
					Map<String, Object> map2) {
				Collator collator = Collator.getInstance(java.util.Locale.CHINA);;
				String music1 = map1.get("musicpath")+"";
				String music2 = map2.get("musicpath")+"";
				String v1 = music1.substring(music1.lastIndexOf("/"));
				String v2 = music2.substring(music2.lastIndexOf("/"));
				return collator.getCollationKey(v1).compareTo(collator.getCollationKey(v2));
			}
		});
	}
	
	private void cancleAll() {
		mla.isShow = false;
		usbadapter.isShow = false;
		recodeStatu.clear();//本地
//		recodeStatu.clear();//usb这个参数定义可能要替换
		bt_import_select.setVisibility(View.GONE);
		bt_delete_select.setVisibility(View.GONE);
		bt_cancel.setVisibility(View.GONE);
		bt_select_all.setVisibility(View.GONE);
		if(isusb){
			usbadapter.notifyDataSetChanged();
		}else{
			mla.notifyDataSetChanged();
		}
	}
	
	private void cancle() {
		bt_import_select.setVisibility(View.GONE);
		bt_delete_select.setVisibility(View.GONE);
		bt_cancel.setVisibility(View.GONE);
		bt_select_all.setVisibility(View.GONE);
		if(isusb){
			usbadapter.isShow = false;
			recodeStatu.clear();//这个参数定义可能要替换
			usbadapter.notifyDataSetChanged();
		}else{
			mla.isShow = false;
			recodeStatu.clear();
			mla.notifyDataSetChanged();
		}
	}
	
	private void initView(){
//		shoots_bg_01 = (ImageView) findViewById(R.id.shoots_bg_01);
//		shoots_bg_01.setOnClickListener(null);
		txt_progress = (TextView) findViewById(R.id.txt_progress);
		
		bt_select_all = (Button) findViewById(R.id.bt_select_all);
		bt_select_all.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(isusb){
					for(int i=0;i<MusicList.getInstance().getUsbMusicList().size();i++){
						recodeStatu.put(i, true);
						usbadapter.notifyDataSetChanged();
					}
				}else{
					for(int i=0;i<MusicList.getInstance().getLocalMusicList().size();i++){
						recodeStatu.put(i, true);
						mla.notifyDataSetChanged();
					}
				}
			}
		});
		
		bt_cancel = (Button) findViewById(R.id.bt_cancel);
		bt_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				cancle();
			}
		});
		
		bt_import_select = (Button) findViewById(R.id.bt_import_select);
		bt_import_select.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(isusb){
					if(CopyFileService.instance == null){
						Intent service = new Intent();
						service.setAction("com.kandi.copyfile");
						SerializableMap myMap=new SerializableMap();
                        myMap.setMap(recodeStatu);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("map", myMap);
                        service.putExtras(bundle);
                        service.putExtra("isusb", true);
                        service.putExtra("choose", true);//true：音乐；false：视频
                        startService(service);
                        cancle();
					}
				}else{
					if(CopyFileService.instance == null){
						Intent service = new Intent();
						service.setAction("com.kandi.copyfile");
						SerializableMap myMap=new SerializableMap();
                        myMap.setMap(recodeStatu);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("map", myMap);
                        service.putExtras(bundle);
                        service.putExtra("isusb", false);
                        service.putExtra("choose", true);//true：音乐；false：视频
                        startService(service);
                        cancle();
					}
				}
			}
		});
		
		bt_delete_select = (Button) findViewById(R.id.bt_delete_select);
		bt_delete_select.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!isusb){
					boolean flag = mPlayerModel.isPlaying();
					List<Map<String, Object>> list = null;
					try {
						list = deepCopy(MusicList.getInstance().getLocalMusicList());
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					boolean location = false;
					for (Entry<Integer, Boolean> entry : recodeStatu.entrySet()) {
						int key = entry.getKey();
						if((Boolean)recodeStatu.get(key)){
							if(mPlayerModel.getCurrentMusicIndex() == key){
								location = true;
							}
							FileUtil.deleteFile(new File(list.get(key).get("musicpath")+""));
							refreshfilebtn.performClick();
						}
					}
					if(flag){
						DeletePlayFresh(location);
					}else{
						DeleteDisPlayFresh();
					}
					cancle();
					finish();
				}
			}
		});
		
		refreshfilebtn = (ImageButton) findViewById(R.id.refreshfilebtn);
		refreshfilebtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				linearListView.setVisibility(View.GONE);
//				handler.postDelayed(new Runnable() {
//					
//					@Override
//					public void run() {
//						linearListView.setVisibility(View.VISIBLE);
//					}
//				}, 5000);
				if(!CommonUtils.isFastDoubleClick()){
					if(isusb)
					{
						//add by dw in yd
						if (!isScaning) {
							MusicList.getInstance().getUsbMusicList().clear();
							tempList.clear();
							linearListView.setVisibility(View.GONE);
							pb_music_usbload.setVisibility(View.VISIBLE);
							usbhandler.sendEmptyMessageDelayed(0, 150);
						}
//						if (Settings.System.getInt(getContentResolver(), "system_kandi_key", 0) == 1) {
//						}else {
//							MusicList.getInstance().getUsbMusicList().clear();
//							tempList.clear();
//							linearListView.setVisibility(View.GONE);
//							pb_music_usbload.setVisibility(View.VISIBLE);
//							usbhandler.sendEmptyMessageDelayed(0, 150);
//						}
						
					}
					else
					{
						linearListView.setVisibility(View.GONE);
						pb_music_usbload.setVisibility(View.VISIBLE);
						usbhandler.sendEmptyMessageDelayed(1, 150);
					}
				}else{
					return;
				}
				
			}
		});
		
		localmusicbtn = (ImageButton) findViewById(R.id.localmusicbtn);
		localmusicbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				iv_music_nextline.setPadding(0, 0, 0, 0);	
				isusb=false;
				String[] musicpath = getIntent().getStringArrayExtra("musicpath");
				mla = new MusicListAdapter(MusicListActivity.this, MusicList.getInstance().getLocalMusicList(), linearListView,musicpath);
				linearListView.setAdapter(mla);
				MusicListActivity.this.loadFiles();
				cancleAll();
			}
		});
		usbmusicbtn = (ImageButton) findViewById(R.id.usbmusicbtn);
		usbmusicbtn.setOnClickListener(new View.OnClickListener() {
			

			@Override
			public void onClick(View arg0) {
				
				iv_music_nextline.setPadding(150, 0, 0, 0);
				isusb = true;	
				if(MusicList.getInstance().isDirty())
				{
					MusicList.getInstance().getUsbMusicList().clear();
					tempList.clear();
					linearListView.setVisibility(View.GONE);
					pb_music_usbload.setVisibility(View.VISIBLE);
					usbhandler.sendEmptyMessage(0);
				}
				else
				{
					linearListView.setAdapter(usbadapter);	
				}
				cancleAll();
			}
		});
	
		storageManager=new getmStorageManager(MusicListActivity.this);
		iv_music_nextline = (ImageView)findViewById(R.id.iv_music_nextline);
		pb_music_usbload = (ProgressBar)findViewById(R.id.pb_music_usbload);
		linearListView = (ListView) findViewById(R.id.linearListView);
		usbhandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					
					//add by dw in yd
//					MusicListActivity.this.loadUSBFiles();
					
//					Log.i("info", "--- MusicListActivity_handleMessage_Settings --- " + Settings.System.getInt(getContentResolver(), "system_kandi_key", 0));
					myHandler.removeCallbacks(getPathYdRunnable);
					loadUSBFilesYd();
//					if (Settings.System.getInt(getContentResolver(), "system_kandi_key", 0) == 1) {
//					}else {
//						MusicListActivity.this.loadUSBFiles();
//					}
					//add end
					
					
				//	pb_music_usbload.setVisibility(View.GONE);
					break;
				case 1:
					MusicListActivity.this.loadFiles();
					break;
				case REFRESH_PROGRESS_ACTION:
					txt_progress.setText(CopyFileService.CopyFileLocalToUSB());
					break;
				}
			}
		};
		usbadapter = new Usbadapter(MusicList.getInstance().getUsbMusicList(), MusicListActivity.this,musicpath);
		
		linearListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(isusb){
					if(usbadapter.isShow){
						CheckBox cb = (CheckBox) arg1.findViewById(R.id.check);
						boolean isCheck = !cb.isChecked();
						if(isCheck){
							recodeStatu.put(arg2, isCheck);
						}else{
							recodeStatu.remove(arg2);
						}
						cb.setChecked(isCheck);
					}else{
						MusicListSelectEvent event = new MusicListSelectEvent();
						event.setMusicIndex(arg2);
						event.setResultCode(0x02);
						event.setMusicSrc(2);
						event.setUsbmusicList(MusicList.getInstance().getUsbMusicList());
						EventBus.getDefault().post(event);
						finish();
					}
				}else{
					if(mla.isShow){
						CheckBox cb = (CheckBox) arg1.findViewById(R.id.check);
						boolean isCheck = !cb.isChecked();
						if(isCheck){
							recodeStatu.put(arg2, isCheck);
						}else{
							recodeStatu.remove(arg2);
						}
						cb.setChecked(isCheck);
					}else{
						MusicListSelectEvent event = new MusicListSelectEvent();
						event.setMusicIndex(arg2);
						event.setMusicSrc(1);
						event.setResultCode(0x01);
						EventBus.getDefault().post(event);
						finish();
					}
				}
			}
		});
		bgview = (RelativeLayout) findViewById(R.id.bgview);
		bgview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
				//bgview.setVisibility(View.GONE);
			}
		});
		mainview = (LinearLayout) findViewById(R.id.mainview);
		mainview.setOnClickListener(null);
	}
	
	/**
     * 深层拷贝对象
     * 
     * @param src
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("rawtypes")
    public List deepCopy(List src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);
 
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        List dest = (List) in.readObject();
        return dest;
    }
	
	private void DeletePlayFresh(boolean location){
		if(mPlayerModel.music_src==1){
			int keyvalue = 0;
			MusicListActivity.this.loadFiles();
			for(int i=0;i<MusicList.getInstance().getLocalMusicList().size();i++){
				if(MusicList.getInstance().getLocalMusicList().get(i).get("musicpath").equals(mPlayerModel.getCurrentMusicFilePath())){
					keyvalue = i;
					break;
				}
			}
			FreshMusicListSelectEvent event = new FreshMusicListSelectEvent();
			event.setMusicIndex(keyvalue);
			event.setMusicSrc(1);
			event.setResultCode(0x01);
			event.setLocation(location);
			EventBus.getDefault().post(event);
		}
	}
	
	private void DeleteDisPlayFresh(){
		if(mPlayerModel.music_src==1){
			int keyvalue = 0;
			MusicListActivity.this.loadFiles();
			for(int i=0;i<MusicList.getInstance().getLocalMusicList().size();i++){
				if(MusicList.getInstance().getLocalMusicList().get(i).get("musicpath").equals(mPlayerModel.getCurrentMusicFilePath())){
					keyvalue = i;
					break;
				}
			}
			FreshMusicListSelectEvent event = new FreshMusicListSelectEvent();
			event.setMusicIndex(keyvalue);
			event.setMusicSrc(1);
			event.setResultCode(0x03);
			EventBus.getDefault().post(event);
		}
	}
	
	private List<Map<String, Object>> GetLocalFiles() {

		List<Map<String, Object>> musicList = new ArrayList<Map<String, Object>>();
		List<String> fileList = FileUtil.GetFiles2("sdcard", "mp3,amr,m4a,m4r,wav,aac");
		for (int i = 0; i < fileList.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			String musicPath = fileList.get(i) + "";
			if (!musicPath.contains("._")) {
				map.put("musicpath", fileList.get(i));
				musicList.add(map);
			}
		}
		Collections.sort(musicList, new Comparator<Map<String, Object>>() {

			@Override
			public int compare(Map<String, Object> map1,
					Map<String, Object> map2) {
				Collator collator = Collator.getInstance(java.util.Locale.CHINA);
				String music1 = map1.get("musicpath")+"";
				String music2 = map2.get("musicpath")+"";
				String v1 = music1.substring(music1.lastIndexOf("/"));
				String v2 = music2.substring(music2.lastIndexOf("/"));
				return collator.getCollationKey(v1).compareTo(collator.getCollationKey(v2));
			}
		});

		return musicList;
	}
	
	private void initEvent(){
		EventBus.getDefault().register(this,"FinishMusicListEvent",FinishMusicListEvent.class);
		EventBus.getDefault().register(this,"UsbMediaStatesEvent",UsbMediaStatesEvent.class);
	}
	public void FinishMusicListEvent(
			FinishMusicListEvent event) {
		finish();
		overridePendingTransition(R.anim.stay, R.anim.slide_out_up);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//add by dw in yd
		Log.i("info", "--- MusicListActivity_onCreate --- ");
		
		HandlerThread thread = new HandlerThread("MyThread");
		thread.start();
		myHandler = new MyHandler(thread.getLooper());
		Message msg = myHandler.obtainMessage();
		msg.sendToTarget();
		
		//add end
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.initEvent();
		setContentView(R.layout.music_file_layout);
		musicpath = getIntent().getStringArrayExtra("musicpath");	
		this.initView();		
		this.loadFiles();
		mPlayerModel = MusicPlayerModel.getInstance();
		MusicList.getInstance().setDirtyFlag();
		mla = new MusicListAdapter(this, MusicList.getInstance().getLocalMusicList(), linearListView,musicpath);		
		linearListView.setAdapter(mla);	
		linearListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(CopyFileService.instance == null){
					if(!isusb){
						mla.isShow = true;
						mla.notifyDataSetChanged();
						bt_import_select.setVisibility(View.VISIBLE);
						bt_import_select.setText(getString(R.string.txt_export));
						bt_delete_select.setVisibility(View.VISIBLE);
						bt_cancel.setVisibility(View.VISIBLE);
						bt_select_all.setVisibility(View.VISIBLE);
					}else{
						usbadapter.isShow = true;
						usbadapter.notifyDataSetChanged();
						bt_import_select.setVisibility(View.VISIBLE);
						bt_import_select.setText(getString(R.string.txt_import));
						bt_delete_select.setVisibility(View.GONE);
						bt_cancel.setVisibility(View.VISIBLE);
						bt_select_all.setVisibility(View.VISIBLE);
					}
				}
				return true;
			}
		});
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(isActivityAtTop("MusicListActivity")){
					try {
						if(count==5){
							usbhandler.sendEmptyMessage(REFRESH_PROGRESS_ACTION);
							count=0;
						}
						count++;
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	MusicPlayerModel mPlayerModel;
	 
	public List<String> GetFiles(String Path, String Extension,boolean IsIterative) {
		List<String> usbFiles =new ArrayList<String>(); 
	    File[] files =new File(Path).listFiles();
	    for (int i =0; i < files.length; i++){
	        File f = files[i];
	        if (f.isFile()){
	            if (f.getPath().substring(f.getPath().length() - Extension.length()).equals(Extension)) 
	            	usbFiles.add(f.getPath());
	            if (!IsIterative)
	                break;
	        }else if (f.isDirectory() && f.getPath().indexOf("/.") == -1)
	            GetFiles(f.getPath(), Extension, IsIterative);
	    }
	    return usbFiles;
	}
	@Override
	public boolean handleMessage(Message arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	@Override
	protected void onPause() {
		super.onStop();
		finish();
	}

	@Override
	protected void onStop() {
		super.onStop();
//		finish();
	}
	
	@Override
	protected void onDestroy() {
		cancleAll();
		EventBus.getDefault().unregister(this,FinishMusicListEvent.class);
		EventBus.getDefault().unregister(this,UsbMediaStatesEvent.class);
		
		//add by dw in yd
		myHandler.removeCallbacks(getPathYdRunnable);
		//add end
		
		super.onDestroy();
	}
	
	private boolean isActivityAtTop(String activityName) {
        ActivityManager am = (ActivityManager) BaseApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        if (cn.getClassName().contains(activityName)) {
            return true;
        }
        return false;
    }
	
}

