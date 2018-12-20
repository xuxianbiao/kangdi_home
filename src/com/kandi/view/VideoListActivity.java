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

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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

import com.kandi.adapter.MovieListAdapter;
import com.kandi.adapter.MovieUsbadapter;
import com.kandi.application.BaseApplication;
import com.kandi.event.FinishMovieListEvent;
import com.kandi.event.FreshVideoListSelectEvent;
import com.kandi.event.UsbMediaStatesEvent;
import com.kandi.event.VideoListSelectEvent;
import com.kandi.fragment.VideoFragment;
import com.kandi.home.R;
import com.kandi.service.CopyFileService;
import com.kandi.util.CommonUtils;
import com.kandi.util.FileUtil;
import com.kandi.util.SerializableMap;
import com.kandi.view.MusicListActivity.MusicList;
import com.kandi.view.MusicListActivity.MyHandler;
import com.yd.manager.YdUtils;

import de.greenrobot.event.EventBus;

public class VideoListActivity extends Activity implements Callback{
	private ImageView shoots_bg_01;
	private ListView linearListView;
	private RelativeLayout bgview;
	private LinearLayout mainview;
	private String[] videopath;
	private ImageButton refreshfilebtn;
	private Handler handler;	
	private MovieListAdapter mla;
	private ImageButton ib_video_usb;
	private ImageView iv_video_nextline;
	private MovieUsbadapter videousb;
	private boolean isusb=false;
	private ProgressBar pb_video_usbload;
	private ImageButton ib_video_local;
	private getmStorageManager storageManager;
	public Map<Integer, Boolean> recodeStatu = new HashMap<Integer, Boolean>();
	private Button bt_cancel;
	private Button bt_delete_select;
	private Button bt_import_select;
	private Button bt_select_all;
	private TextView txt_progress;
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
				VideoList.getInstance().getUsbVideoList().clear();
				VideoList.getInstance().getUsbVideoList().addAll(tempList);
				linearListView.setVisibility(View.VISIBLE);
				pb_video_usbload.setVisibility(View.GONE);
				videousb.notifyDataSetChanged();
				VideoList.getInstance().resetDirtyFlag();
				break;
			case SORT:
				VideoList.getInstance().getUsbVideoList().clear();
				VideoList.getInstance().getUsbVideoList().addAll(tempList);
				videousb.notifyDataSetChanged();
				listSort();
				break;

			default:
				break;
			}
		};
	};
	
	private void listSort() {
//		Collections.sort(VideoList.getInstance().getUsbVideoList(), new Comparator<Map<String, Object>>() {
//
//			@Override
//			public int compare(Map<String, Object> map1,
//					Map<String, Object> map2) {
//				Collator collator = Collator.getInstance(java.util.Locale.ENGLISH);
//				return collator.getCollationKey(map1.get("name")+"").compareTo(collator.getCollationKey(map2.get("name")+""));
//			}
//		});
		Collections.sort(VideoList.getInstance().getUsbVideoList(), new Comparator<Map<String, Object>>() {

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
	}
	
	private void loadUSBFilesYd(){
		linearListView.setAdapter(videousb);
		VideoList.getInstance().getUsbVideoList().clear();
		tempList.clear();
		myHandler.post(getPathYdRunnable);
	}
	
	private Runnable getPathYdRunnable = new Runnable() {
		
		@Override
		public void run() {
			isScaning = true;
			tempListSize = 0;
			getPathYd(storageManager.getVolumePaths()[2]);
			mYdHandler.sendEmptyMessage(UI_REFRESH);
			mYdHandler.sendEmptyMessage(SORT);
			isScaning = false;
		}
	};

	public void getPathYd(String path)
	{
		File f=new File(path);
		File[] fs=f.listFiles();
		for (int i = 0; i < fs.length; i++) {				
			if(fs[i].getName().trim().toLowerCase().endsWith(".mp4") 
					|| fs[i].getName().trim().toLowerCase().endsWith(".3gp"))
			{										
				Map<String, Object> map=new HashMap<String, Object>();				
				map.put("name", fs[i].getName());
				map.put("moviepath", fs[i].getAbsolutePath());
//				tempList.add(map);	
				
				if (isMediaPluged) {
					tempList.add(map);
				}
			}
			if (fs[i].isDirectory()) {
				String newpath = fs[i].getAbsolutePath();
				getPathYd(newpath);
			}
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
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
	
	
	public static class VideoList {
		private List<Map<String, Object>> videolist = new ArrayList<Map<String,Object>>();
		private List<Map<String, Object>> usbvideoList = new ArrayList<Map<String,Object>>();
		
		static private VideoList _list; 
		static private boolean isDirty=true;
		static public VideoList getInstance() {
			if(_list == null) {
				_list = new VideoList();
			}
			return _list;
		}
		
		private VideoList() {
		}
		public List<Map<String, Object>> getLocalVideoList() {
			return videolist;
		}
		public List<Map<String, Object>> getUsbVideoList() {
			return usbvideoList;
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
	
	
	private void loadFiles(){
		VideoList.getInstance().getLocalVideoList().clear();
		List<String> fileList = FileUtil.GetFiles2("sdcard", "mp4,3gp");
		for(int i =0;i<fileList.size();i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("moviepath", fileList.get(i));
			VideoList.getInstance().getLocalVideoList().add(map);
		}
//		Collections.sort(VideoList.getInstance().getLocalVideoList(), new Comparator<Map<String, Object>>() {
//
//			@Override
//			public int compare(Map<String, Object> map1,
//					Map<String, Object> map2) {
//				Collator collator = Collator.getInstance();
//				String movie1 = map1.get("moviepath")+"";
//				String movie2 = map2.get("moviepath")+"";
//				String v1 = movie1.substring(movie1.lastIndexOf("/"));
//				String v2 = movie2.substring(movie2.lastIndexOf("/"));
//				return collator.getCollationKey(v1).compareTo(collator.getCollationKey(v2));
//			}
//		});
		
		//add by dw in yd
		Collections.sort(VideoList.getInstance().getLocalVideoList(), new Comparator<Map<String, Object>>() {
			
			@Override
			public int compare(Map<String, Object> map1,
					Map<String, Object> map2) {
				Collator collator = Collator.getInstance(java.util.Locale.CHINA);
				String movie1 = map1.get("moviepath")+"";
				String movie2 = map2.get("moviepath")+"";
				String v1 = movie1.substring(movie1.lastIndexOf("/") + 1);
				String v2 = movie2.substring(movie2.lastIndexOf("/") + 1);
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
//			Collections.sort(VideoList.getInstance().getLocalVideoList(), new Comparator<Map<String, Object>>() {
//
//				@Override
//				public int compare(Map<String, Object> map1,
//						Map<String, Object> map2) {
//					Collator collator = Collator.getInstance();
//					String movie1 = map1.get("moviepath")+"";
//					String movie2 = map2.get("moviepath")+"";
//					String v1 = movie1.substring(movie1.lastIndexOf("/"));
//					String v2 = movie2.substring(movie2.lastIndexOf("/"));
//					return collator.getCollationKey(v1).compareTo(collator.getCollationKey(v2));
//				}
//			});
//		}
		//add end
		
		
		mla.notifyDataSetChanged();
		linearListView.setVisibility(View.VISIBLE);
		pb_video_usbload.setVisibility(View.GONE);
	}
	
	public void getpath(String path)
	{
//		VideoList.getInstance().getUsbVideoList().clear();
		File f=new File(path);
		File[] fs=f.listFiles();
		for (int i = 0; i < fs.length; i++) {				
			if(fs[i].getName().trim().toLowerCase().endsWith(".mp4") 
					|| fs[i].getName().trim().toLowerCase().endsWith(".3gp"))
			{										
				Map<String, Object> map=new HashMap<String, Object>();				
				map.put("name", fs[i].getName());
				map.put("moviepath", fs[i].getAbsolutePath());
				VideoList.getInstance().getUsbVideoList().add(map);													
			}
			
			if(fs[i].isDirectory())
			{
			String	newpath=fs[i].getAbsolutePath();
				getpath(newpath);
			}
			
		}
		linearListView.setVisibility(View.VISIBLE);
		pb_video_usbload.setVisibility(View.GONE);
		videousb.notifyDataSetChanged();
		VideoList.getInstance().resetDirtyFlag();
		
	}
	private void loadUSBFiles(){
		linearListView.setAdapter(videousb);
		getpath(storageManager.getVolumePaths()[2]);
		Collections.sort(VideoList.getInstance().getUsbVideoList(), new Comparator<Map<String, Object>>() {

			@Override
			public int compare(Map<String, Object> map1,
					Map<String, Object> map2) {
				Collator collator = Collator.getInstance(java.util.Locale.CHINA);
				String movie1 = map1.get("moviepath")+"";
				String movie2 = map2.get("moviepath")+"";
				String v1 = movie1.substring(movie1.lastIndexOf("/"));
				String v2 = movie2.substring(movie2.lastIndexOf("/"));
				return collator.getCollationKey(v1).compareTo(collator.getCollationKey(v2));
			}
		});
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
	private void initView(){
//		shoots_bg_01 = (ImageView) findViewById(R.id.shoots_bg_01);
//		shoots_bg_01.setOnClickListener(null);
		txt_progress = (TextView) findViewById(R.id.txt_progress);
		txt_progress.setText(CopyFileService.CopyFileLocalToUSB());
		
		bt_select_all = (Button) findViewById(R.id.bt_select_all);
		bt_select_all.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(isusb){
					for(int i=0;i<VideoList.getInstance().getUsbVideoList().size();i++){
						recodeStatu.put(i, true);
						videousb.notifyDataSetChanged();
					}
				}else{
					for(int i=0;i<VideoList.getInstance().getLocalVideoList().size();i++){
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
                        service.putExtra("choose", false);//true：音乐；false：视频
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
                        service.putExtra("choose", false);//true：音乐；false：视频
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
					List<Map<String, Object>> list = null;
					try {
						list = deepCopy(VideoList.getInstance().getLocalVideoList());
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					boolean location = false;
					for (Entry<Integer, Boolean> entry : recodeStatu.entrySet()) {
						int key = entry.getKey();
						if((Boolean)recodeStatu.get(key)){
							if(VideoList.getInstance().getLocalVideoList().get(key).get("moviepath").equals(videopath[0])){
								location = true;
							}
							FileUtil.deleteFile(new File(list.get(key).get("moviepath")+""));
							refreshfilebtn.performClick();
						}
					}
					if(VideoFragment.playSts == 1){
						DeletePlayFresh(location);
					}else if(VideoFragment.playSts == 2){
						DeleteDisPlayFresh();
					}else if(VideoFragment.playSts == 0){
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
				if(!CommonUtils.isFastDoubleClick()){
					if(isusb)
					{
						//add by dw in yd
						if (!isScaning) {
							VideoList.getInstance().getUsbVideoList().clear();
							tempList.clear();
							linearListView.setVisibility(View.GONE);
							pb_video_usbload.setVisibility(View.VISIBLE);
							handler.sendEmptyMessage(0);
						}
//						if (Settings.System.getInt(getContentResolver(), "system_kandi_key", 0) == 1) {
//						}else {
//							VideoList.getInstance().getUsbVideoList().clear();
//							linearListView.setVisibility(View.GONE);
//							pb_video_usbload.setVisibility(View.VISIBLE);
//							handler.sendEmptyMessage(0);
//						}
						
						//add end
					}
					else
					{
						linearListView.setVisibility(View.GONE);
						pb_video_usbload.setVisibility(View.VISIBLE);
						handler.sendEmptyMessage(1);
					}
				}else{
					return;
				}
				
			}
		});
		bgview = (RelativeLayout) findViewById(R.id.bgview);
		bgview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		storageManager=new getmStorageManager(VideoListActivity.this);
		videousb = new MovieUsbadapter(VideoList.getInstance().getUsbVideoList(), VideoListActivity.this, videopath);
		pb_video_usbload = (ProgressBar)findViewById(R.id.pb_video_usbload);
		iv_video_nextline = (ImageView)findViewById(R.id.iv_video_nextline);
		ib_video_usb = (ImageButton)findViewById(R.id.ib_video_usb);
		ib_video_usb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				iv_video_nextline.setPadding(150, 0, 0, 0);
				isusb=true;
				if(VideoList.getInstance().isDirty())
				{
					VideoList.getInstance().getUsbVideoList().clear();
					tempList.clear();
				pb_video_usbload.setVisibility(View.VISIBLE);
				linearListView.setVisibility(View.GONE);	
				handler.sendEmptyMessage(0);
				
				}
				else
				{
					linearListView.setAdapter(videousb);
				}
				cancleAll();
				
			}
		});
		ib_video_local = (ImageButton)findViewById(R.id.ib_video_local);
		ib_video_local.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_video_nextline.setPadding(0, 0, 0, 0);
				isusb=false;
			
				//MovieListAdapter mla = new MovieListAdapter(VideoListActivity.this, VideoList.getInstance().getLocalVideoList(), linearListView,videopath);
				linearListView.setAdapter(mla);
				VideoListActivity.this.loadFiles();
				cancleAll();
			}
		});
		
		handler = new Handler()
		{			
				
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
			
			switch (msg.what) {
				case 0:
					
					//add by dw in yd
//					VideoListActivity.this.loadUSBFiles();
					
//					Log.i("info", "--- MusicListActivity_handleMessage_Settings --- " + Settings.System.getInt(getContentResolver(), "system_kandi_key", 0));
					myHandler.removeCallbacks(getPathYdRunnable);
					loadUSBFilesYd();
//					if (Settings.System.getInt(getContentResolver(), "system_kandi_key", 0) == 1) {
//					}else {
//						VideoListActivity.this.loadUSBFiles();
//					}
					//add end
					
					
				//	pb_music_usbload.setVisibility(View.GONE);
					break;
				case 1:
					VideoListActivity.this.loadFiles();
					break;
				case REFRESH_PROGRESS_ACTION:
					txt_progress.setText(CopyFileService.CopyFileLocalToUSB());
					break;
					
				
					
			}	
			}
		};
		mainview = (LinearLayout) findViewById(R.id.mainview);
		mainview.setOnClickListener(null);
		linearListView = (ListView) findViewById(R.id.linearListView);
		linearListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(isusb){
					if(videousb.isShow){
						CheckBox cb = (CheckBox) arg1.findViewById(R.id.check);
						boolean isCheck = !cb.isChecked();
						if(isCheck){
							recodeStatu.put(arg2, isCheck);
						}else{
							recodeStatu.remove(arg2);
						}
						cb.setChecked(isCheck);
					}else{
						VideoListSelectEvent event = new VideoListSelectEvent();
						event.setVideoIndex(arg2);
						event.setResultCode(0x02);
						event.setVideoSrc(2);
						event.setUsbVideoList(VideoList.getInstance().getUsbVideoList());
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
						VideoListSelectEvent event = new VideoListSelectEvent();
						event.setVideoIndex(arg2);
						event.setVideoSrc(1);
						event.setResultCode(0x01);
						EventBus.getDefault().post(event);
						finish();
					}
				}
			}
		});
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
		if("1".equals(videopath[1])){
			int keyvalue = 0;
			VideoListActivity.this.loadFiles();
			FreshVideoListSelectEvent event = new FreshVideoListSelectEvent();
			for(int i=0;i<VideoList.getInstance().getLocalVideoList().size();i++){
				if(VideoList.getInstance().getLocalVideoList().get(i).get("moviepath").equals(videopath[0])){
					keyvalue = i;
					break;
				}
			}
			event.setVideoIndex(keyvalue);
			event.setResultCode(0x01);
			event.setVideoSrc(1);
			event.setLocation(location);
			EventBus.getDefault().post(event);
		}
	}
	
	private void DeleteDisPlayFresh(){
		if("1".equals(videopath[1])){
			int keyvalue = 0;
			VideoListActivity.this.loadFiles();
			FreshVideoListSelectEvent event = new FreshVideoListSelectEvent();
			for(int i=0;i<VideoList.getInstance().getLocalVideoList().size();i++){
				if(VideoList.getInstance().getLocalVideoList().get(i).get("moviepath").equals(videopath[0])){
					keyvalue = i;
					break;
				}
			}
			event.setVideoIndex(keyvalue);
			event.setResultCode(0x03);
			event.setVideoSrc(1);
			EventBus.getDefault().post(event);
		}
	}
	
	private void initEvent(){
		EventBus.getDefault().register(this,"FinishMovieListEvent",FinishMovieListEvent.class);
		EventBus.getDefault().register(this,"UsbMediaStatesEvent",UsbMediaStatesEvent.class);
	}
	public void FinishMovieListEvent(
			FinishMovieListEvent event) {
		finish();
		overridePendingTransition(R.anim.stay, R.anim.slide_out_up);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//add by dw in yd
		
		HandlerThread thread = new HandlerThread("VideoThread");
		thread.start();
		myHandler = new MyHandler(thread.getLooper());
		Message msg = myHandler.obtainMessage();
		msg.sendToTarget();
		
		//add end
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.initEvent();
		setContentView(R.layout.video_file_layout);
		videopath = getIntent().getStringArrayExtra("videopath");
		mla = new MovieListAdapter(this, VideoList.getInstance().getLocalVideoList(), linearListView,videopath);
		
		this.initView();		
		this.loadFiles();
		VideoList.getInstance().setDirtyFlag();
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
						videousb.isShow = true;
						videousb.notifyDataSetChanged();
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
				while(isActivityAtTop("VideoListActivity")){
					try {
						if(count==5){
							handler.sendEmptyMessage(REFRESH_PROGRESS_ACTION);
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
	
	private void cancleAll() {
		mla.isShow = false;
		videousb.isShow = false;
		recodeStatu.clear();
		bt_import_select.setVisibility(View.GONE);
		bt_delete_select.setVisibility(View.GONE);
		bt_cancel.setVisibility(View.GONE);
		bt_select_all.setVisibility(View.GONE);
		if(isusb){
			videousb.notifyDataSetChanged();
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
			videousb.isShow = false;
			recodeStatu.clear();//这个参数定义可能要替换
			videousb.notifyDataSetChanged();
		}else{
			mla.isShow = false;
			recodeStatu.clear();
			mla.notifyDataSetChanged();
		}
	}
	
	@Override
	public boolean handleMessage(Message arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	 
public void UsbMediaStatesEvent(UsbMediaStatesEvent event) {
		
		switch(event.getUsbState()) {
		case MEDIA_PLUGED:
			{	 
//				Toast.makeText(VideoListActivity.this, "U盘已插入", 0).show();
				VideoList.getInstance().getUsbVideoList().clear();
//				videousb.notifyDataSetChanged();
//				isusb=true;
				iv_video_nextline.setPadding(150, 0, 0, 0);
				pb_video_usbload.setVisibility(View.VISIBLE);
				linearListView.setVisibility(View.GONE);	

				
				
				// add by dw in yd
//				VideoListActivity.this.loadUSBFiles();

//				Log.i("info", "--- MusicListActivity_UsbMediaStatesEvent_Settings --- " + Settings.System.getInt(getContentResolver(), "system_kandi_key", 0));
				myHandler.removeCallbacks(getPathYdRunnable);
				isMediaPluged = true;
				loadUSBFilesYd();
//				if (Settings.System.getInt(getContentResolver(), "system_kandi_key", 0) == 1) {
//				} else {
//					VideoListActivity.this.loadUSBFiles();
//				}
				// add end
			}
			break;
		case MEDIA_UNPLUGED:
			{
//				Toast.makeText(VideoListActivity.this, "U盘已拔出", 0).show();
					
				VideoList.getInstance().setDirtyFlag();
				VideoList.getInstance().getUsbVideoList().clear();
//					videousb.notifyDataSetChanged();
					
					
				//add by dw in yd
//				videousb.notifyDataSetChanged();
					
					myHandler.removeCallbacks(getPathYdRunnable);
					isMediaPluged = false;
					tempList.clear();
					MusicList.getInstance().getUsbMusicList().clear();
					videousb.notifyDataSetChanged();
				//add end
			}
			break;
		case UNKNOWN:
		default:
		}
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
		EventBus.getDefault().unregister(this,FinishMovieListEvent.class);
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


