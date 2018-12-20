package com.kandi.fragment;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IKdBtService;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.storage.StorageManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.kandi.application.BaseApplication;
import com.kandi.driver.radio.Configs;
import com.kandi.driver.radio.RadioDriverAdapter;
import com.kandi.event.ConnectBlueEvent;
import com.kandi.event.DeskMusicProgressEvent;
import com.kandi.event.FinishMusicEvent;
import com.kandi.event.FreshMusicListSelectEvent;
import com.kandi.event.MusicListSelectEvent;
import com.kandi.event.MusicPlayerModelEvent;
import com.kandi.event.PlayFmBlueEvent;
import com.kandi.event.PlayFmEvent;
import com.kandi.event.PlayVideoBlueEvent;
import com.kandi.event.PlayVideoEvent;
import com.kandi.event.SmallBlueMusicEvent;
import com.kandi.event.SmallMusicEvent;
import com.kandi.event.UsbMediaStatesEvent;
import com.kandi.home.R;
import com.kandi.model.MusicPlayerModel;
import com.kandi.util.FileUtil;
import com.kandi.view.MusicListActivity;
import com.kandi.view.MusicListActivity.MusicList;
import com.util.ToastUtil;
import com.yd.manager.ThreadPoolManager;
import com.yd.manager.YdUtils;

import de.greenrobot.event.EventBus;

/**
 * 改造后的音乐Fragment
 * 
 */
public class MusicFragment extends Fragment implements Callback {
	
	public static MusicFragment instance;
	private static String TAG = "music";
	private enum WHATMSG {
		UNKNOWN, UPDATE_PROGRESS, USB_PLUGIN, USB_UNPLUG
	};
	
	private final int MUSIC_PLAY_ACTION_MSG=1000;
	private final int INIT_LOCALMUSIC=2000;
	private final int INIT_USBMUSIC=3000;

	private Timer timer = null;
	private TimerTask task;

	MusicPlayerModel mPlayerModel;

	private TextView blue_time;
	private Button btn_blue_music;
	private ImageView music_box_now;
	private SeekBar imageView_progress;
	private Handler handler;
	private boolean isSeekbarTrackingTouch = false;
	private ImageButton prebtn;
	private ImageButton nextbtn;
	private ImageButton playbtn;
	private ImageButton musiclistbtn;
	private ImageButton equalizerbtn;
	private ImageButton favbtn;
	private ImageButton playmodebtn;
	private Animation operatingAnim;
	private TextView media_current;
	private TextView media_duration;
	private TextView music_time_textview;
	private TextView blue_music_time_textview;
	private ImageView music_box_flag_play;
	private ImageView music_box_cd_light;
	private ImageView blue_img;
	private SharedPreferences sp;
	private boolean isusb = false;
	private boolean blue_music_state = false;
	private int MusicPosition;
	private int RecordProgress;
	private int MusicPath;
	private boolean OnceFlag = false;
	IKdBtService btservice;
	BlueMusicChanageReceiver blueMusicReceiver;
	
	
	//add by dw in yd
	private static final int SORT = 1;
	private static final int INIT_LOCALMUSIC_YD = 2;
	private static final int INIT_USBMUSIC_YD = 3;
	private static int INIT_MESSAGE_YD;
	
	List<Map<String, Object>> tempUsbMusicList = new ArrayList<Map<String, Object>>();
	
	private Handler mYdHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SORT:
				usbmusicList.clear(); 
				usbmusicList.addAll(tempUsbMusicList);
				listSort();
				//sort finish
				mYdHandler.sendEmptyMessage(INIT_MESSAGE_YD);
				break;
			case INIT_LOCALMUSIC_YD:
				initLocalMusicYd();
				break;
			case INIT_USBMUSIC_YD:
				initUsbMusicYd();
				break;

			default:
				break;
			}
		};
	};
	
	private Runnable getUsbMusicListYdRunnable = new Runnable() {
		
		@Override
		public void run() {
			getUsbMusicListYd(storageManager.getVolumePaths()[2]);
			mYdHandler.sendEmptyMessage(SORT);
		}
	};

	public void getUsbMusicListYd(String path) {
		File f = new File(path);
		File[] fs = f.listFiles();
		if (fs != null) {
			for (int i = 0; i < fs.length; i++) {
				if (fs[i].getName().trim().toLowerCase().endsWith(".mp3")
						|| fs[i].getName().trim().toLowerCase()
								.endsWith(".amr")
						|| fs[i].getName().trim().toLowerCase()
								.endsWith(".flac")
						|| fs[i].getName().trim().toLowerCase()
								.endsWith(".m4a")
						|| fs[i].getName().trim().toLowerCase()
								.endsWith(".m4r")
						|| fs[i].getName().trim().toLowerCase()
								.endsWith(".wav")
						|| fs[i].getName().trim().toLowerCase()
								.endsWith(".aac")) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("name", fs[i].getName());
					map.put("musicpath", fs[i].getAbsolutePath());
					tempUsbMusicList.add(map);
				}
				if (fs[i].isDirectory()) {
					newpath = fs[i].getAbsolutePath();
					getUsbMusicListYd(newpath);
				}
				
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected void initUsbMusicYd() {
		mPlayerModel.loadPlayList(usbmusicList);
		if (getActivity() != null) {
			MusicPosition = SharedPreferencesUtils
					.getSharedPreferences(getActivity()).getInt(
							Configs.MUSIC_POSITION,
							Configs.DEFAULT_MUSIC_POSITION);
		}
		mPlayerModel.setCurrentMusic(MusicPosition);
		initMusic();
	}

	protected void initLocalMusicYd() {
		mPlayerModel.loadPlayList(usbmusicList);
		mPlayerModel.music_src = 2;
		if (getActivity() != null) {
			Editor edt = SharedPreferencesUtils
					.getEditor(getActivity());
			edt.putInt(Configs.MUSIC_PATH, mPlayerModel.music_src);
			edt.commit();
		}
	}

	private void listSort() {
//		Collections.sort(usbmusicList, new Comparator<Map<String, Object>>() {
//			@Override
//			public int compare(Map<String, Object> map1,
//					Map<String, Object> map2) {
//				Collator collator = Collator.getInstance(java.util.Locale.ENGLISH);
//				return collator.getCollationKey(map1.get("name")+"").compareTo(collator.getCollationKey(map2.get("name")+""));
//			}
//		});
		Collections.sort(usbmusicList, new Comparator<Map<String, Object>>() {
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
	
	//add end
	
	
	
	private OnSeekBarChangeListener osbcl = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			isSeekbarTrackingTouch = false;
			int progress = seekBar.getProgress();

			if (mPlayerModel != null) {
				int duration = mPlayerModel.getCurrentMusicDuration();
				float seekto = (float) duration
						* ((float) progress / (float) seekBar.getMax());
				mPlayerModel.seekTo((int) seekto);

				String sec = MusicFragment.this.sec2Time((int) (seekto / 1000));
				media_current.setText(sec);
			}

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			isSeekbarTrackingTouch = true;
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

			if (mPlayerModel != null) {
				int duration = mPlayerModel.getCurrentMusicDuration();
				float seekto = (float) duration
						* ((float) progress / (float) seekBar.getMax());

				String sec = MusicFragment.this.sec2Time((int) (seekto / 1000));
				media_current.setText(sec);
			}
		}
	};

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
//		Collections.sort(musicList, new Comparator<Map<String, Object>>() {
//
//			@Override
//			public int compare(Map<String, Object> map1,
//					Map<String, Object> map2) {
//				Collator collator = Collator.getInstance();
//				String music1 = map1.get("musicpath") + "";
//				String music2 = map2.get("musicpath") + "";
//				String v1 = music1.substring(music1.lastIndexOf("/"));
//				String v2 = music2.substring(music2.lastIndexOf("/"));
//				return collator.getCollationKey(v1).compareTo(
//						collator.getCollationKey(v2));
//			}
//		});
		
		//add by dw in yd
		Collections.sort(musicList, new Comparator<Map<String, Object>>() {
			
			@Override
			public int compare(Map<String, Object> map1,
					Map<String, Object> map2) {
				Collator collator = Collator.getInstance(java.util.Locale.CHINA);
				String music1 = map1.get("musicpath") + "";
				String music2 = map2.get("musicpath") + "";
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
//		if (Settings.System.getInt(getActivity().getContentResolver(), "system_kandi_key", 0) == 1) {
//		}else {
//			Collections.sort(musicList, new Comparator<Map<String, Object>>() {
//
//				@Override
//				public int compare(Map<String, Object> map1,
//						Map<String, Object> map2) {
//					Collator collator = Collator.getInstance();
//					String music1 = map1.get("musicpath") + "";
//					String music2 = map2.get("musicpath") + "";
//					String v1 = music1.substring(music1.lastIndexOf("/"));
//					String v2 = music2.substring(music2.lastIndexOf("/"));
//					return collator.getCollationKey(v1).compareTo(
//							collator.getCollationKey(v2));
//				}
//			});
//		}
		//add end

		return musicList;
	}
	
	private getmStorageManager storageManager;
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
	private String newpath;
	List<Map<String, Object>> usbmusicList = new ArrayList<Map<String,Object>>();
	public List<Map<String, Object>> GetUsbmusicList(String path)
	{
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
					usbmusicList.add(map);
				}
				if(fs[i].isDirectory())
				{
					newpath=fs[i].getAbsolutePath();
					GetUsbmusicList(newpath);
				}
			}
		}
		Collections.sort(usbmusicList, new Comparator<Map<String, Object>>() {

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
		return usbmusicList;
	}

	public Bitmap toRoundBitmap(Bitmap bitmap) throws Exception{
		// 圆形图片宽高
		int width = bitmap.getWidth();
		int height = bitmap.getWidth();
		// 正方形的边长
		int r = 0;
		// 取最短边做边长
		if (width > height) {
			r = height;
		} else {
			r = width;
		}
		// 构建一个bitmap
		Bitmap backgroundBmp = Bitmap.createBitmap(width, height,
				Config.ARGB_8888);
		// new一个Canvas，在backgroundBmp上画图
		Canvas canvas = new Canvas(backgroundBmp);
		Paint paint = new Paint();
		// 设置边缘光滑，去掉锯齿
		paint.setAntiAlias(true);
		// 宽高相等，即正方形
		RectF rect = new RectF(0, 0, r, r);
		// 通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
		// 且都等于r/2时，画出来的圆角矩形就是圆形
		canvas.drawRoundRect(rect, r / 2, r / 2, paint);
		// 设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// canvas将bitmap画在backgroundBmp上
		canvas.drawBitmap(bitmap, null, rect, paint);
		// 返回已经绘画好的backgroundBmp
		return backgroundBmp;
	}
	
	int[] Image={R.drawable.music_record_cd_default,R.drawable.music_record_cd_001,R.drawable.music_record_cd_002,R.drawable.music_record_cd_003,R.drawable.music_record_cd_005};

	private void setMp3Image(String path) {
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		try {
			mmr.setDataSource(path);
		} catch (IllegalArgumentException e) {
			music_box_now.setImageResource(R.drawable.music_record_cd_default);
			return;
		} catch (RuntimeException e){
			music_box_now.setImageResource(R.drawable.music_record_cd_default);
			return;
		}

		byte[] artBytes = mmr.getEmbeddedPicture();
		if (artBytes != null) {
			try {
				InputStream is = new ByteArrayInputStream(mmr.getEmbeddedPicture());
				Bitmap bm = BitmapFactory.decodeStream(is);
				Bitmap roundBm = toRoundBitmap(bm);
				music_box_now.setImageBitmap(roundBm);
			} catch (Exception e) {
				final int max=5;
				int random=Integer.valueOf((int) (Math.random()*max));//获得一个随机数
				if(random>Image.length){
					random = 0;
				}
				music_box_now.setImageResource(Image[random]);
			}
		} else {
			final int max=5;
			int random=Integer.valueOf((int) (Math.random()*max));//获得一个随机数
			if(random>Image.length){
				random = 0;
			}
			music_box_now.setImageResource(Image[random]);
			// imgArt.setImageDrawable(getResources().getDrawable(R.drawable.adele));
		}
	}

	RadioDriverAdapter radioDrv;
	
	private void initView(View view) {
		radioDrv = RadioDriverAdapter.getInstance();
		handler = new Handler(this);

		blue_img = (ImageView) view.findViewById(R.id.blue_img);
		music_box_cd_light = (ImageView) view
				.findViewById(R.id.music_box_cd_light);
		music_box_flag_play = (ImageView) view
				.findViewById(R.id.music_box_flag_play);
		music_time_textview = (TextView) view
				.findViewById(R.id.music_time_textview);
		blue_music_time_textview = (TextView) view
				.findViewById(R.id.blue_music_time_textview);
		music_time_textview.setText("");
		media_current = (TextView) view.findViewById(R.id.time1);
		media_duration = (TextView) view.findViewById(R.id.time2);

		musiclistbtn = (ImageButton) view.findViewById(R.id.musiclistbtn);
		musiclistbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),
						MusicListActivity.class);
				if (isusb == true) {
					// intent.putExtra("usbList", (Serializable)musicList);
				}
				if (mPlayerModel.getMusicList().size() > 0) {
					String path0 = mPlayerModel.getMusicList()
							.get(mPlayerModel.getCurrentMusicIndex())
							.get("musicpath")
							+ "";
//					String path = path0.substring(path0.lastIndexOf("/") + 1);
					
					String[] data={path0,String.valueOf(mPlayerModel.music_src)};
					intent.putExtra("musicpath", data);
				}else{
					String[] data={"null",String.valueOf(mPlayerModel.music_src)};
					intent.putExtra("musicpath", data);
				}
				MusicFragment.this.startActivityForResult(intent, 0);
			}
		});
		equalizerbtn = (ImageButton) view.findViewById(R.id.equalizerbtn);
		equalizerbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

			}
		});

		favbtn = (ImageButton) view.findViewById(R.id.favbtn);
		favbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

			}
		});
		playmodebtn = (ImageButton) view.findViewById(R.id.playmodebtn);

		playmodebtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				switch (MusicFragment.this.mPlayerModel.switchPlayMode()) {
				case PLAY_ALL:
					playmodebtn
							.setImageResource(R.drawable.music_xunhuan_btn_selector);
					break;
				case PLAY_SINGLE:
					playmodebtn
							.setImageResource(R.drawable.music_singlexunhuan_btn_selector);
					break;
				case PLAY_RANDOM:
					playmodebtn
							.setImageResource(R.drawable.music_suijixunhuan_btn_selector);
					break;
				default:
					break;
				}
				sp.edit()
						.putInt("playmode",
								mPlayerModel.getPlayMode().ordinal()).commit();

			}
		});

		prebtn = (ImageButton) view.findViewById(R.id.btn1);
		prebtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				WhellPrev();
			}

		});
		nextbtn = (ImageButton) view.findViewById(R.id.btn3);
		nextbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				WhellNext();
			}

		});
		playbtn = (ImageButton) view.findViewById(R.id.btn2);
		playbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mhandler.sendEmptyMessage(MUSIC_PLAY_ACTION_MSG);
//				onMusicPlay();
			}
		});
		imageView_progress = (SeekBar) view
				.findViewById(R.id.imageView_progress);
		imageView_progress.setOnSeekBarChangeListener(osbcl);
		imageView_progress.setMax(100);
		blue_time = (TextView) view.findViewById(R.id.blue_time);
		
		/*blue_music_state = SharedPreferencesUtils.getSharedPreferences(
				getActivity()).getBoolean(Configs.BLUE_MUSIC,
						Configs.DEFAULT_BLUE_MUSIC);*/
		btn_blue_music = (Button) view.findViewById(R.id.btn_blue_music);
//		btn_blue_music.setChecked(blue_music_state);
		if(!SystemProperties.get("sys.kd.hardwareversion","").equals("V1.2")){//高版本
			//蓝牙音乐开关是否打开
			if(blue_music_state){
				MusicFragment.this.initPlayAnim(1);
				musiclistbtn.setVisibility(View.INVISIBLE);
				playmodebtn.setVisibility(View.INVISIBLE);
				blue_time.setVisibility(View.VISIBLE);
				music_time_textview.setVisibility(View.GONE);
				blue_music_time_textview.setVisibility(View.VISIBLE);
				imageView_progress.setVisibility(View.GONE);
				media_current.setVisibility(View.GONE);
				media_duration.setVisibility(View.GONE);
				playbtn.setVisibility(View.INVISIBLE);
				if(mAudioManager.requestAudioFocus(afBTChangeListener, 13,
						AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
				}
//				try {
//					btservice.btAvrPlay();
//				} catch (RemoteException e) {
//					e.printStackTrace();
//				}
				if(radioDrv.isPowerOn()){
					PlayFmBlueEvent event = new PlayFmBlueEvent();
					event.type = PlayFmBlueEvent.BLUE_MUSIC_STATE;
					EventBus.getDefault().post(event);
				}
				if(BaseApplication.getInstance().isBlue_status()){
					if(!"".equals(blue_music_time_textview.getText().toString())){
						EventBus.getDefault().post(
								new SmallBlueMusicEvent("show", blue_music_time_textview
										.getText() + "", blue_time.getText() + ""));
					}else{
						EventBus.getDefault().post(new SmallMusicEvent("hide"));
					}
				}
			}
			btn_blue_music.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!blue_music_state){
						if(SystemProperties.get("sys.kd.hardwareversion").contains("TEST-KDV")){
							String btacconnected = SystemProperties.get("sys.kd.btacconnected","no");
							if(btacconnected.equals("no")){
								ToastUtil.showToast(getActivity(), getString(R.string.btacunconnect), Toast.LENGTH_SHORT);
								return;
							}
						}
						switch (mPlayerModel.getPlayState()) {
						case STOP:
							break;
						case PAUSE:
							break;
						case PLAYING:
							mPlayerModel.pause();
							break;
						}
						MusicFragment.this.initPlayAnim(1);
						playbtn.setImageResource(R.drawable.music_start_btn_selector);
						music_box_now.clearAnimation();
						musiclistbtn.setVisibility(View.INVISIBLE);
						playmodebtn.setVisibility(View.INVISIBLE);
						blue_time.setVisibility(View.VISIBLE);
						imageView_progress.setVisibility(View.GONE);
						media_current.setVisibility(View.GONE);
						media_duration.setVisibility(View.GONE);
						music_time_textview.setVisibility(View.GONE);
						blue_music_time_textview.setVisibility(View.VISIBLE);
						playbtn.setVisibility(View.INVISIBLE);
						try {
							if(mAudioManager.requestAudioFocus(afBTChangeListener, 13,
									AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
							}
//							btservice.btAvrPlay();
							if(btservice != null && BaseApplication.getInstance().isBlue_status()){
								btservice.btGetMediaInfo();
							}
						} catch (RemoteException e) {
							e.printStackTrace();
						} catch (NoSuchMethodError e){
							e.printStackTrace();
						} catch (Exception e){
							e.printStackTrace();
						}
						if(radioDrv.isPowerOn()){
							PlayFmBlueEvent event = new PlayFmBlueEvent();
							event.type = PlayFmBlueEvent.BLUE_MUSIC_STATE;
							EventBus.getDefault().post(event);
						}
						if(!"".equals(blue_music_time_textview.getText().toString())){
							EventBus.getDefault().post(
									new SmallBlueMusicEvent("show", blue_music_time_textview
											.getText() + "", blue_time.getText() + ""));
						}else{
							EventBus.getDefault().post(new SmallMusicEvent("hide"));
						}
						btn_blue_music.setBackgroundResource(R.drawable.toggle_bg_on_n);
						btnowstatus = false;
					}else{
						if(!btnowstatus){
							abandonAudioFocus(afBTChangeListener);
						}
//						if(mAudioManager.requestAudioFocus(afChangeListener, 12,
//								AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED &&
//								mAudioManager.requestAudioFocus(afSystemChangeListener, AudioManager.STREAM_MUSIC,
//										AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
//						}
//						try {
//							btservice.btAvrPause();
//						} catch (RemoteException e) {
//							e.printStackTrace();
//						}
						MusicFragment.this.initPlayAnim(1);
						playbtn.setImageResource(R.drawable.music_start_btn_selector);
						music_box_now.clearAnimation();
						musiclistbtn.setVisibility(View.VISIBLE);
						playmodebtn.setVisibility(View.VISIBLE);
						blue_time.setVisibility(View.GONE);
						imageView_progress.setVisibility(View.VISIBLE);
						media_current.setVisibility(View.VISIBLE);
						media_duration.setVisibility(View.VISIBLE);
						music_time_textview.setVisibility(View.VISIBLE);
						blue_music_time_textview.setVisibility(View.GONE);
						playbtn.setVisibility(View.VISIBLE);
						bluemusicstate = true;
						PlayFmBlueEvent event = new PlayFmBlueEvent();
						event.type = PlayFmBlueEvent.BLUE_MUSIC_NOSTATE;
						EventBus.getDefault().post(event);
						EventBus.getDefault().post(new SmallBlueMusicEvent("hide"));
						btn_blue_music.setBackgroundResource(R.drawable.toggle_bg_off_n);
					}
					blue_music_state = !blue_music_state;
					BaseApplication.getInstance().setBlue_music_state(blue_music_state);
				}
			});
		}else{
			btn_blue_music.setVisibility(View.INVISIBLE);
			blue_img.setVisibility(View.INVISIBLE);
		}
		
		music_box_now = (ImageView) view.findViewById(R.id.music_box_now);
		operatingAnim = AnimationUtils.loadAnimation(getActivity(),
				R.anim.diskrotation);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		storageManager=new getmStorageManager(getActivity());

	}
	
	private boolean isActivityAtTop(String activityName) {
        ActivityManager am = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        if (cn.getClassName().contains(activityName)) {
            return true;
        }
        return false;
    }
	
	private boolean stopmusicflag = false;
	private boolean stopmusicflag2 = false;
	Handler mhandler = new Handler(){

		@SuppressWarnings("incomplete-switch")
		@Override
		public void handleMessage(Message msg) {
			/*if(getActivity() != null){
				blue_music_state = SharedPreferencesUtils.getSharedPreferences(
						getActivity()).getBoolean(Configs.BLUE_MUSIC,
								Configs.DEFAULT_BLUE_MUSIC);
			}*/
			switch (msg.what) {
			case INIT_LOCALMUSIC:
				if(GetLocalFiles().size()>0){
					mPlayerModel.loadPlayList(GetLocalFiles());
					if(getActivity() != null){
						MusicPosition =  SharedPreferencesUtils.getSharedPreferences(
								getActivity()).getInt(Configs.MUSIC_POSITION,
										Configs.DEFAULT_MUSIC_POSITION);
					}
					mPlayerModel.setCurrentMusic(MusicPosition);
					initMusic();
				} else {
					//add by dw in yd
					
					INIT_MESSAGE_YD = INIT_LOCALMUSIC_YD;
					tempUsbMusicList.clear();
					ThreadPoolManager.getInstance().getFixedThreadPool().execute(getUsbMusicListYdRunnable);
//					if (Settings.System.getInt(getActivity().getContentResolver(), "system_kandi_key", 0) == 1) {
//					}else {
//						mPlayerModel.loadPlayList(GetUsbmusicList(storageManager
//								.getVolumePaths()[2]));
//						mPlayerModel.music_src = 2;
//						if (getActivity() != null) {
//							Editor edt = SharedPreferencesUtils
//									.getEditor(getActivity());
//							edt.putInt(Configs.MUSIC_PATH, mPlayerModel.music_src);
//							edt.commit();
//						}
//					}
				}
				break;
			case INIT_USBMUSIC:
				//add by dw in yd
				INIT_MESSAGE_YD = INIT_USBMUSIC_YD;
				tempUsbMusicList.clear();
				ThreadPoolManager.getInstance().getFixedThreadPool().execute(getUsbMusicListYdRunnable);
//				if (Settings.System.getInt(getActivity().getContentResolver(), "system_kandi_key", 0) == 1) {
//				}else {
//					mPlayerModel.loadPlayList(GetUsbmusicList(storageManager
//							.getVolumePaths()[2]));
//					if (getActivity() != null) {
//						MusicPosition = SharedPreferencesUtils
//								.getSharedPreferences(getActivity()).getInt(
//										Configs.MUSIC_POSITION,
//										Configs.DEFAULT_MUSIC_POSITION);
//					}
//					mPlayerModel.setCurrentMusic(MusicPosition);
//					initMusic();
//				}
				
				//add end
				break;
			case 104:
				try {
					if(msg.obj != null && msg.obj instanceof String){
						String music_info = (String) msg.obj;
						JSONArray jsonArr = new JSONArray(music_info);
						JSONObject obj = (JSONObject)jsonArr.get(0);
						String json_songname = obj.getString("SongName");
						String json_singername = obj.getString("SingerName");
						if("".equals(json_singername)){
							blue_music_time_textview.setText(json_songname);
						}else{
							blue_music_time_textview.setText(json_songname+"-"+json_singername);
						}
						if(obj.getString("SongTotalTime").matches("^[0-9]*$")){
							int SongTotalTime = obj.getInt("SongTotalTime");
							int min = SongTotalTime/1000/60;
							int sec = SongTotalTime/1000%60;
							blue_time.setText((min>=10?min:("0"+min))+":"+(sec>=10?sec:("0"+sec)));
						}else{
							blue_time.setText(getString(R.string.unknow));
						}
						if(blue_music_state){
							if(!"".equals(blue_music_time_textview.getText().toString())){
								EventBus.getDefault().post(
										new SmallBlueMusicEvent("show", blue_music_time_textview
												.getText() + "", blue_time.getText() + ""));
							}else{
								EventBus.getDefault().post(new SmallMusicEvent("hide"));
							}
						}else{
							EventBus.getDefault().post(new SmallBlueMusicEvent("hide"));
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case MUSIC_PLAY_ACTION_MSG:
				onMusicPlay();
				break;
			case 400:
				if(nowAudioStatus){
					if(localplaystatus){
						if(!blue_music_state){
							if (mPlayerModel.play()) {
								localplaystatus = false;
								playbtn.setImageResource(R.drawable.music_stop_btn_selector);
								MusicFragment.this.startPlayAnim(1000);
								music_box_now.setAnimation(operatingAnim);
							}
						}
					}
				}
				break;
			}
		}
		
	};
	
	
	private void initTimer(){
		timer = new Timer();
	    task = new TimerTask() {
			@Override
			public void run() {
				try{
					handler.sendEmptyMessage(WHATMSG.UPDATE_PROGRESS.ordinal());
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		};
		
		timer.schedule(task, 0, 500);	//定时器500 ms
	}
	

	private void initEvent() {
		EventBus.getDefault().register(this, "MusicPlayerModelEvent",
				MusicPlayerModelEvent.class);
		EventBus.getDefault().register(this, "FinishMusicEvent",
				FinishMusicEvent.class);
		EventBus.getDefault().register(this, "UsbMediaStatesEvent",
				UsbMediaStatesEvent.class);

		EventBus.getDefault().register(this, "onMusicSelected",
				MusicListSelectEvent.class);
		
		EventBus.getDefault().register(this, "onFreshMusicSelected",
				FreshMusicListSelectEvent.class);
		
		EventBus.getDefault().register(this, "onPlayVideo",
				PlayVideoEvent.class);
		
		EventBus.getDefault().register(this, "onConnectBlue",
				ConnectBlueEvent.class);
		
		EventBus.getDefault().register(this, "onPlayFm",
				PlayFmEvent.class);
		
		EventBus.getDefault().register(this, "onDeskProgress",
				DeskMusicProgressEvent.class);
	}
	
	
	public void onConnectBlue(ConnectBlueEvent event){
		if(event.type==ConnectBlueEvent.BLUE_DISCONN_STATE){
			if(blue_music_state){
				btn_blue_music.performClick();
			}
		}
	}
	
	public void onDeskProgress(DeskMusicProgressEvent event) {
		int progress = event.progress;
		imageView_progress.setProgress(progress);
		osbcl.onStopTrackingTouch(imageView_progress);
	}
	
	public void onPlayVideo(PlayVideoEvent event) {
		if(blue_music_state){
			btn_blue_music.performClick();
		}
		if (mPlayerModel.isPlaying()) {
			if (mAudioManager.abandonAudioFocus(afChangeListener) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED && 
    				mAudioManager.abandonAudioFocus(afSystemChangeListener) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
    			if (mPlayerModel.pause()) {
    				MusicFragment.this.stopPlayAnim(1000);
    				playbtn.setImageResource(R.drawable.music_start_btn_selector);
    				music_box_now.clearAnimation();
    			}
    		}
		}
	}
	
	public void onPlayFm(PlayFmEvent event) {
		localplaystatus = false;
		if(blue_music_state){
//			try {
//				btservice.btAvrPause();
//			} catch (RemoteException e) {
//				e.printStackTrace();
//			}
			MusicFragment.this.initPlayAnim(1);
			playbtn.setImageResource(R.drawable.music_start_btn_selector);
			music_box_now.clearAnimation();
			musiclistbtn.setVisibility(View.VISIBLE);
			playmodebtn.setVisibility(View.VISIBLE);
			blue_time.setVisibility(View.GONE);
			imageView_progress.setVisibility(View.VISIBLE);
			media_current.setVisibility(View.VISIBLE);
			media_duration.setVisibility(View.VISIBLE);
			music_time_textview.setVisibility(View.VISIBLE);
			blue_music_time_textview.setVisibility(View.GONE);
			playbtn.setVisibility(View.VISIBLE);
			bluemusicstate = true;
			EventBus.getDefault().post(new SmallBlueMusicEvent("hide"));
			btn_blue_music.setBackgroundResource(R.drawable.toggle_bg_off_n);
			blue_music_state = !blue_music_state;
			BaseApplication.getInstance().setBlue_music_state(blue_music_state);
		}
		if(mPausedByTransientLossOfFocus){
			mPausedByTransientLossOfFocus = false;
			stopPlayAnim(1000);
		}
		if (mPlayerModel.isPlaying()) {
			mhandler.sendEmptyMessage(MUSIC_PLAY_ACTION_MSG);
//			onMusicPlay();
		}
	}

	
	boolean flag_play = true;
	private void startPlayAnim(final int duration) {
		music_box_cd_light.setVisibility(android.view.View.INVISIBLE);
		music_box_cd_light.clearAnimation();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if(flag_play){
					RotateAnimation ra = new RotateAnimation(0, 30, 68, 64);
					ra.setFillAfter(true);
					ra.setDuration(duration);
					music_box_flag_play.startAnimation(ra);
					flag_play = false;
				}

				AlphaAnimation aa = new AlphaAnimation(0, 1);
				aa.setFillAfter(true);
				aa.setDuration(duration);
				music_box_cd_light.startAnimation(aa);
			}
		}, 0);

	}

	private void stopPlayAnim(final int duration) {
		flag_play = true;
		music_box_flag_play.setVisibility(android.view.View.VISIBLE);
		music_box_cd_light.setVisibility(android.view.View.VISIBLE);
//		music_box_flag_play.clearAnimation();
		music_box_cd_light.clearAnimation();

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				RotateAnimation ra = new RotateAnimation(30, 0, 68, 64);
				ra.setFillAfter(true);
				ra.setDuration(duration);
				music_box_flag_play.startAnimation(ra);

				AlphaAnimation aa = new AlphaAnimation(1, 0);
				aa.setFillAfter(true);
				aa.setDuration(duration);
				music_box_cd_light.startAnimation(aa);
			}
		}, 0);
	}
	
	private void initPlayAnim(final int duration) {
		flag_play = true;
		music_box_flag_play.setVisibility(android.view.View.VISIBLE);
		music_box_cd_light.setVisibility(android.view.View.INVISIBLE);
		music_box_flag_play.clearAnimation();
		music_box_cd_light.clearAnimation();
	}

	private void initPlayMode() {
		sp = getActivity().getSharedPreferences("musicinfo", 0);
		int index = sp.getInt("playmode", 0);
		MusicPlayerModel.PLAYMODE playMode = MusicPlayerModel.PLAYMODE.values()[index];

		this.mPlayerModel.setPlayMode(playMode);

		switch (playMode) {
		case PLAY_ALL:
			playmodebtn.setImageResource(R.drawable.music_xunhuan_btn_selector);
			break;
		case PLAY_SINGLE:
			playmodebtn
					.setImageResource(R.drawable.music_singlexunhuan_btn_selector);
			break;
		case PLAY_RANDOM:
			playmodebtn
					.setImageResource(R.drawable.music_suijixunhuan_btn_selector);
			break;
		default:
			break;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.music_main_layout, null);
		mAudioManager = (AudioManager) BaseApplication.getInstance().getSystemService(BaseApplication.getInstance().AUDIO_SERVICE);
		btservice = IKdBtService.Stub.asInterface(ServiceManager.getService("bt"));
		initFragment(view);
		instance = this;
		return view;
	}

	private void initFragment(View view) {
		mPlayerModel = MusicPlayerModel.getInstance();
		initView(view);

		// 设置循环方式
		initPlayMode();

		initEvent();
		
		/*blue_music_state =  SharedPreferencesUtils.getSharedPreferences(
				getActivity()).getBoolean(Configs.BLUE_MUSIC,
						Configs.DEFAULT_BLUE_MUSIC);*/
		if(!blue_music_state){
			switch (mPlayerModel.getPlayState()) {
			case PLAYING:
				refreshPannel();
				break;
				
			case PAUSE:
//				abandonAudioFocus();
				this.stopPlayAnim(1);
				refreshPannel();
				break;
				
			case IDEL:
			case STOP:
			case ERROR:
			default:
				DefaultMethod();
			}
		}else{
			switch (mPlayerModel.getPlayState()) {
			case PLAYING:
			case PAUSE:
			case IDEL:
			case STOP:
			case ERROR:
			default:
				DefaultMethod();
			}
		}
	}
	
	public void DefaultMethod(){
		this.stopPlayAnim(1);
		if(getActivity() != null){
			mPlayerModel.music_src = SharedPreferencesUtils.getSharedPreferences(
					getActivity()).getInt(Configs.MUSIC_PATH,
							Configs.DEFAULT_MUSIC_PATH);
		}
		if(mPlayerModel.music_src == 1){
			mhandler.sendEmptyMessage(INIT_LOCALMUSIC);
		}else if(mPlayerModel.music_src == 2){
			mhandler.sendEmptyMessage(INIT_USBMUSIC);
		}
	}

	void MusicPlayerModelEvent(MusicPlayerModelEvent event) {
		refreshPannel();
	}

	public void FinishMusicEvent(FinishMusicEvent event) {
		if(getActivity() != null){
			getActivity().finish();
		}
	}

	private String sec2Time(int secv) {
		int hour = secv / 3600;
		int min = (secv / 60) % 60;
		int sec = (secv % 3600) % 60;
		// /System.out.println(hour+":"+min+":"+sec);
		String hourStr = hour < 10 ? "0" + hour : hour + "";
		String minStr = min < 10 ? "0" + min : min + "";
		String secStr = sec < 10 ? "0" + sec : sec + "";
		if (hour == 0) {

			return minStr + ":" + secStr;
		} else {
			return hourStr + ":" + minStr + ":" + secStr;
		}
	}

	private int recordcount;
	
	@Override
	public boolean handleMessage(Message msg) {
		if (msg.what == WHATMSG.UPDATE_PROGRESS.ordinal()) {

			if (mPlayerModel.isPlaying()) {
				if (!isSeekbarTrackingTouch) {
					int current = mPlayerModel.getCurrentPosition();
					String sec = sec2Time((current / 1000));
					media_current.setText(sec);

					int duration = mPlayerModel.getCurrentMusicDuration();
					float progress;
					if (duration != 0) {
						progress = (float) current / (float) duration
								* imageView_progress.getMax();
					} else {
						progress = 0;
					}
					imageView_progress.setProgress((int) progress);
					
					if(recordcount == 3){
						if(getActivity() != null){
							Editor edt = SharedPreferencesUtils.getEditor(getActivity());
							edt.putInt(Configs.MUSIC_PROGRESS_POSITION, (int)progress);
							edt.commit();
						}
						recordcount = 0;
					}else{
						recordcount++;
					}

					try {
						EventBus.getDefault().post(
								new SmallMusicEvent("show", music_time_textview
										.getText() + "", (int) progress, sec
										+ "", media_duration.getText() + ""));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return false;
	}

	public void onMusicSelected(MusicListSelectEvent event) {
		int resultCode = event.getResultCode();
		if (resultCode == 1) {
			isusb = false;
			mPlayerModel.loadPlayList(GetLocalFiles());
			mPlayerModel.setCurrentMusic(event.getMusicIndex());
			mPlayerModel.music_src = event.getMusicSrc();
			if (mPlayerModel.getCurrentMusicIndex() < mPlayerModel
					.getMusicList().size()) {
				mhandler.sendEmptyMessage(MUSIC_PLAY_ACTION_MSG);
//				onMusicPlay();
			}
		} else if (resultCode == 2) {
			isusb = true;
			mPlayerModel.loadPlayList(event.getUsbmusicList());
			mPlayerModel.setCurrentMusic(event.getMusicIndex());
			mPlayerModel.music_src = event.getMusicSrc();
			mhandler.sendEmptyMessage(MUSIC_PLAY_ACTION_MSG);
//			onMusicPlay();
		}
	}
	
	public void onFreshMusicSelected(FreshMusicListSelectEvent event) {
		int resultCode = event.getResultCode();
		if (resultCode == 1) {
			isusb = false;
			boolean location = event.isLocation();
			if(location){
				mPlayerModel.loadPlayList(GetLocalFiles());
			}else{
				mPlayerModel.loadPlayListSec(GetLocalFiles());
			}
			mPlayerModel.setCurrentMusic(event.getMusicIndex());
			mPlayerModel.music_src = event.getMusicSrc();
			if (mPlayerModel.getCurrentMusicIndex() < mPlayerModel
					.getMusicList().size()) {
				if(location){
					mhandler.sendEmptyMessage(MUSIC_PLAY_ACTION_MSG);
				}
//				onMusicPlay();
			}else{
				if (mPlayerModel.pause()) {
        			MusicFragment.this.stopPlayAnim(1000);
        			playbtn.setImageResource(R.drawable.music_start_btn_selector);
        			music_box_now.clearAnimation();
        		}
			}
//			if (mPlayerModel.pause()) {
//    			MusicFragment.this.stopPlayAnim(1000);
//    			playbtn.setImageResource(R.drawable.music_start_btn_selector);
//    			music_box_now.clearAnimation();
//    		}
		} else if (resultCode == 3) {
			isusb = false;
			mPlayerModel.loadPlayListSec(GetLocalFiles());
			mPlayerModel.setCurrentMusic(event.getMusicIndex());
			mPlayerModel.music_src = event.getMusicSrc();
		}
	}

	private boolean bluemusicstate = true;
	void onMusicPlay() {
		// if(mPlayerModel.getMusicList().size()!=0){
        /*if(getActivity() != null){
        	blue_music_state =  SharedPreferencesUtils.getSharedPreferences(
        			getActivity()).getBoolean(Configs.BLUE_MUSIC,
        					Configs.DEFAULT_BLUE_MUSIC);
        }*/
        if(blue_music_state){
//        	if(bluemusicstate){
//        		new Thread(new Runnable() {
//        			
//        			@Override
//        			public void run() {
//        				try {
//        					if(btservice.btAvrPlay()==0){
////        						if(mAudioManager.requestAudioFocus(afBTChangeListener, 13,
////        								AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
////        						}
//        						PlayVideoBlueEvent event = new PlayVideoBlueEvent();
//        						event.type = PlayVideoBlueEvent.BLUE_MUSIC_STATE;
//        						EventBus.getDefault().post(event);
//        					}
//        				} catch (RemoteException e) {
//        					e.printStackTrace();
//        				}
//        			}
//        		}).start();
//        	}else{
//        		new Thread(new Runnable() {
//        			
//        			@Override
//        			public void run() {
//        				try {
//        					if(btservice.btAvrPause()==0){
////        						if (mAudioManager.abandonAudioFocus(afBTChangeListener) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
////        						}
//        					}
//        				} catch (RemoteException e) {
//        					e.printStackTrace();
//        				}
//        			}
//        		}).start();
//        	}
        }else{
        	switch (mPlayerModel.getPlayState()) {
        	case IDEL:
        	case STOP:
        		if(mPlayerModel.getMusicList().size() > 0){
        			if(mAudioManager.requestAudioFocus(afChangeListener, 12,
        					AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED &&
        					mAudioManager.requestAudioFocus(afSystemChangeListener, AudioManager.STREAM_MUSIC,
        							AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
        				if (mPlayerModel.play()) {
        					playbtn.setImageResource(R.drawable.music_stop_btn_selector);
        					MusicFragment.this.setMp3Image(mPlayerModel
        							.getCurrentMusicFilePath());
        					MusicFragment.this.startPlayAnim(1000);
        					music_box_now.startAnimation(operatingAnim);
        					music_time_textview.setText(mPlayerModel
        							.getCurrentMusicFileName().substring(
        									0,
        									mPlayerModel.getCurrentMusicFileName()
        									.lastIndexOf(".")));
        					media_duration.setText(sec2Time(mPlayerModel
        							.getCurrentMusicDuration() / 1000));
        					
        					if(!OnceFlag){
        						if(getActivity() != null){
        							MusicPosition =  SharedPreferencesUtils.getSharedPreferences(
        									getActivity()).getInt(Configs.MUSIC_POSITION,
        											Configs.DEFAULT_MUSIC_POSITION);
        							int musicindex = mPlayerModel.getCurrentMusicIndex();
        							if(MusicPosition == musicindex){
        								RecordProgress =  SharedPreferencesUtils.getSharedPreferences(
        										getActivity()).getInt(Configs.MUSIC_PROGRESS_POSITION,
        												Configs.DEFAULT_MUSIC_PROGRESS_POSITION);
        								
        								if (mPlayerModel != null) {
        									int duration = mPlayerModel.getCurrentMusicDuration();
        									float seekto = (float) duration
        											* ((float) RecordProgress / (float) 100);
        									mPlayerModel.seekTo((int) seekto);
        								}
        							}
        							OnceFlag = true;
        						}
        					}
        				}
        			}
        		}
        		
        		break;
        	case PLAYING:
        		if (mAudioManager.abandonAudioFocus(afChangeListener) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED && 
        				mAudioManager.abandonAudioFocus(afSystemChangeListener) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
        			if (mPlayerModel.pause()) {
        				MusicFragment.this.stopPlayAnim(1000);
        				playbtn.setImageResource(R.drawable.music_start_btn_selector);
        				music_box_now.clearAnimation();
        			}
        		}
        		break;
        		
        	case PAUSE:
        		if(mPlayerModel.getMusicList().size() > 0){
        			if(mAudioManager.requestAudioFocus(afChangeListener, 12,
							AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED &&
									mAudioManager.requestAudioFocus(afSystemChangeListener, AudioManager.STREAM_MUSIC,
									AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
        				if (mPlayerModel.play()) {
        					playbtn.setImageResource(R.drawable.music_stop_btn_selector);
        					MusicFragment.this.startPlayAnim(1000);
        					music_box_now.setAnimation(operatingAnim);
        					// media_duration.setText(sec2Time(mPlayerModel.getCurrentMusicDuration()/1000));
        				}
        			}
        		}
        		
        		break;
        		
        	case ERROR:
        		// }
        	}
        }
	}

	/**state true:不执行摇臂动画；flase：执行摇臂动画*/
	private void onPrev(boolean state) {
		if(mAudioManager.requestAudioFocus(afChangeListener, 12,
				AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED &&
				mAudioManager.requestAudioFocus(afSystemChangeListener, AudioManager.STREAM_MUSIC,
						AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
			if (mPlayerModel.previous()) {
				music_time_textview.setText(mPlayerModel.getCurrentMusicFileName()
						.substring(
								0,
								mPlayerModel.getCurrentMusicFileName().lastIndexOf(
										".")));
				media_duration.setText(sec2Time(mPlayerModel
						.getCurrentMusicDuration() / 1000));
				music_box_now.clearAnimation();
				MusicFragment.this.setMp3Image(mPlayerModel
						.getCurrentMusicFilePath());
				
				if (mPlayerModel.isPlaying()) {
					music_box_now.startAnimation(operatingAnim);
				}
			} else if (!mPlayerModel.getCurrentMusicFileName().isEmpty()) {
				music_time_textview.setText(mPlayerModel.getCurrentMusicFileName()
						.substring(
								0,
								mPlayerModel.getCurrentMusicFileName().lastIndexOf(
										".")));
				media_duration.setText(sec2Time(mPlayerModel
						.getCurrentMusicDuration() / 1000));
				music_box_now.clearAnimation();
				MusicFragment.this.setMp3Image("");
			}
			NextPrevSong(state);
		}
	}

	private void onNext(boolean state) {
		if(mAudioManager.requestAudioFocus(afChangeListener, 12,
				AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED &&
				mAudioManager.requestAudioFocus(afSystemChangeListener, AudioManager.STREAM_MUSIC,
						AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
			if (mPlayerModel.next()) {
				music_time_textview.setText(mPlayerModel.getCurrentMusicFileName()
						.substring(
								0,
								mPlayerModel.getCurrentMusicFileName().lastIndexOf(
										".")));
				media_duration.setText(sec2Time(mPlayerModel
						.getCurrentMusicDuration() / 1000));
				music_box_now.clearAnimation();
				MusicFragment.this.setMp3Image(mPlayerModel
						.getCurrentMusicFilePath());
				
				if (mPlayerModel.isPlaying()) {
					music_box_now.startAnimation(operatingAnim);
				}
			} else if (!mPlayerModel.getCurrentMusicFileName().isEmpty()) {
				music_time_textview.setText(mPlayerModel.getCurrentMusicFileName()
						.substring(
								0,
								mPlayerModel.getCurrentMusicFileName().lastIndexOf(
										".")));
				media_duration.setText(sec2Time(mPlayerModel
						.getCurrentMusicDuration() / 1000));
				music_box_now.clearAnimation();
				MusicFragment.this.setMp3Image("");
			}
			NextPrevSong(state);
		}
	}
	
	/**添加上、下一首切歌并播放*/
	public void NextPrevSong(boolean flag){
		switch (mPlayerModel.getPlayState()) {
    	case IDEL:
    	case STOP:
    		if (mPlayerModel.play()) {
    			if(!flag){
    				playbtn.setImageResource(R.drawable.music_stop_btn_selector);
    				MusicFragment.this.setMp3Image(mPlayerModel
    						.getCurrentMusicFilePath());
    				MusicFragment.this.startPlayAnim(1000);
    				music_box_now.startAnimation(operatingAnim);
    				music_time_textview.setText(mPlayerModel
    						.getCurrentMusicFileName().substring(
    								0,
    								mPlayerModel.getCurrentMusicFileName()
    								.lastIndexOf(".")));
    				media_duration.setText(sec2Time(mPlayerModel
    						.getCurrentMusicDuration() / 1000));
    			}
    		}
    		
    		break;
    		
    	case PAUSE:
    		if(mPlayerModel.getMusicList().size() > 0){
    			if (mPlayerModel.play()) {
    				if(!flag){
    					playbtn.setImageResource(R.drawable.music_stop_btn_selector);
    					MusicFragment.this.startPlayAnim(1000);
    					music_box_now.setAnimation(operatingAnim);
    				}
    			}
    		}
    		
    		break;
    		
    	case ERROR:
    		// }
    	}
	}

	private void refreshPannel() {
		if(mPlayerModel.getCurrentMusicFileName().length()>0){
			music_time_textview
			.setText(mPlayerModel.getCurrentMusicFileName()
					.substring(
							0,
							mPlayerModel.getCurrentMusicFileName()
							.lastIndexOf(".")));
		}
		if(getActivity() != null){
			MusicPosition =  SharedPreferencesUtils.getSharedPreferences(
					getActivity()).getInt(Configs.MUSIC_POSITION,
							Configs.DEFAULT_MUSIC_POSITION);
			int musicindex = mPlayerModel.getCurrentMusicIndex();
			if(MusicPosition != musicindex){
				Editor edt = SharedPreferencesUtils.getEditor(getActivity());
				edt.putInt(Configs.MUSIC_POSITION, musicindex);
				edt.commit();
			}
			MusicPath = SharedPreferencesUtils.getSharedPreferences(
					getActivity()).getInt(Configs.MUSIC_PATH,
							Configs.DEFAULT_MUSIC_PATH);
			if(MusicPath != mPlayerModel.music_src){
				Editor edt = SharedPreferencesUtils.getEditor(getActivity());
				edt.putInt(Configs.MUSIC_PATH, mPlayerModel.music_src);
				edt.commit();
			}
		}
		media_duration
				.setText(sec2Time(mPlayerModel.getCurrentMusicDuration() / 1000));
		MusicFragment.this.setMp3Image(mPlayerModel.getCurrentMusicFilePath());

		if (mPlayerModel.isPlaying()) {
			startPlayAnim(1000);
			music_box_now.startAnimation(operatingAnim);
			playbtn.setImageResource(R.drawable.music_stop_btn_selector);
		} else {
			playbtn.setImageResource(R.drawable.music_start_btn_selector);
			music_box_now.clearAnimation();
		}
	}
	
	@Override
	public void onResume() {
		registBroadCastReceiver();
		if(timer == null) {
			initTimer();
		}
		if(this.mPlayerModel.isPlaying()) {
			//播放状态离开Music界面返回时恢复唱臂到播放位置
//			music_box_flag_play.clearAnimation();
			music_box_cd_light.clearAnimation();
			music_box_cd_light.setVisibility(View.VISIBLE);
		}
		BaseApplication.getInstance().setWheelchoose(false);
		super.onResume();
	}
	
	public void UsbMediaStatesEvent(UsbMediaStatesEvent event) {

		switch (event.getUsbState()) {
		case MEDIA_PLUGED: {
			// Toast.makeText(MusicActivity.this, "U鐩樺凡鎻掑叆", 0).show();
 			MusicListActivity.MusicList.getInstance().setDirtyFlag();
		}
			break;
		case MEDIA_UNPLUGED: {
			MusicListActivity.MusicList.getInstance().setDirtyFlag();
			// Toast.makeText(MusicActivity.this, "U鐩樺凡鎷斿嚭", 0).show();
			if (isusb) {
				mPlayerModel.stop();
				mPlayerModel.loadPlayList(GetLocalFiles());
				mPlayerModel.music_src=1;
				mPlayerModel.prepare();
				playbtn.setImageResource(R.drawable.music_ctrl_play_normal);
				music_box_now.clearAnimation();
				if(!flag_play){
					stopPlayAnim(1000);
				}
				EventBus.getDefault().post(new SmallMusicEvent("hide"));
			}
			isusb = false;
		}
			break;
		case UNKNOWN:
		default:
		}
	}

	@Override
	public void onDestroy() {
		unregistBroadCastReceiver();
		if(timer != null) {
			timer.cancel();
			timer = null;
		}
		mPlayerModel.setListener(null);
		instance = null;
		EventBus.getDefault().unregister(this,MusicPlayerModelEvent.class);
		EventBus.getDefault().unregister(this,FinishMusicEvent.class);
		EventBus.getDefault().unregister(this,UsbMediaStatesEvent.class);
		EventBus.getDefault().unregister(this,MusicListSelectEvent.class);
		EventBus.getDefault().unregister(this,FreshMusicListSelectEvent.class);
		EventBus.getDefault().unregister(this,PlayVideoEvent.class);
		EventBus.getDefault().unregister(this,ConnectBlueEvent.class);
		EventBus.getDefault().unregister(this,PlayFmEvent.class);
		EventBus.getDefault().unregister(this,DeskMusicProgressEvent.class);
		super.onDestroy();
	}


	@Override
	public void onPause() {
		super.onPause();
	}
	
	private boolean mPausedByTransientLossOfFocus;
	private AudioManager mAudioManager;
	
	public final static String ACTION_MUSIC_BLUE_PREV = "com.kangdi.BroadCast.MusicBluePrev";//小窗蓝牙音乐上一首
	public final static String ACTION_MUSIC_BLUE_NEXT = "com.kangdi.BroadCast.MusicBlueNext";//小窗蓝牙音乐下一首
	public final static String ACTION_WHEEL_MUSIC_PREV = "com.kangdi.BroadCast.WheelMusicPrev";//多功能方向盘音乐上一首
	public final static String ACTION_WHEEL_MUSIC_NEXT = "com.kangdi.BroadCast.WheelMusicNext";//多功能方向盘音乐下一首
	public final static String ACTION_MUSIC_INFO_CHANGED = "com.kangdi.BroadCast.MusicInfoChanged";//歌曲信息变更
	public final static String KEY_MUSICINFO = "com.kangdi.key.musicinfo";//歌曲信息变更之后的发送广播的String数据的KEY
	public class BlueMusicChanageReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ACTION_MUSIC_INFO_CHANGED)) {
				String music_info = intent.getStringExtra(KEY_MUSICINFO);
				mhandler.sendMessage(mhandler.obtainMessage(104, music_info));
			}else if (intent.getAction().equals(ACTION_WHEEL_MUSIC_NEXT)) {
				if(!BaseApplication.getInstance().isWheelchoose()){
					WhellNext();
				}
			}else if (intent.getAction().equals(ACTION_WHEEL_MUSIC_PREV)) {
				if(!BaseApplication.getInstance().isWheelchoose()){
					WhellPrev();
				}
			}else if (intent.getAction().equals(ACTION_MUSIC_BLUE_PREV)) {
				WhellPrev();
			}else if (intent.getAction().equals(ACTION_MUSIC_BLUE_NEXT)) {
				WhellNext();
			}
		}
	}
	
	private void registBroadCastReceiver() {
        if (null == blueMusicReceiver) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_MUSIC_INFO_CHANGED);
            filter.addAction(ACTION_WHEEL_MUSIC_NEXT);
            filter.addAction(ACTION_WHEEL_MUSIC_PREV);
            filter.addAction(ACTION_MUSIC_BLUE_PREV);
            filter.addAction(ACTION_MUSIC_BLUE_NEXT);
            blueMusicReceiver = new BlueMusicChanageReceiver();
            getActivity().registerReceiver(blueMusicReceiver, filter);
        }
    }

    private void unregistBroadCastReceiver() {
        if (null != blueMusicReceiver) {
        	getActivity().unregisterReceiver(blueMusicReceiver);
        	blueMusicReceiver = null;
        }
    }
    
    
    private void initMusic() {
		if(mPlayerModel.getCurrentMusicFileName().length()>0){
			music_time_textview 
			.setText(mPlayerModel.getCurrentMusicFileName()
					.substring(
							0,
							mPlayerModel.getCurrentMusicFileName()
							.lastIndexOf(".")));
		}
		MusicFragment.this.setMp3Image(mPlayerModel.getCurrentMusicFilePath());
	}
    
    public void WhellPrev(){
    	if(getActivity() != null){
    		/*blue_music_state = SharedPreferencesUtils.getSharedPreferences(
    				getActivity()).getBoolean(Configs.BLUE_MUSIC,
    						Configs.DEFAULT_BLUE_MUSIC);*/
    		if(blue_music_state){
    			if(btservice != null){
    				new Thread(new Runnable() {
    					public void run() {
    						try {
    							if(btservice.btAvrLast()==0){
    								PlayVideoBlueEvent event = new PlayVideoBlueEvent();
    								event.type = PlayVideoBlueEvent.BLUE_MUSIC_STATE;
    								EventBus.getDefault().post(event);
    							}
    						} catch (RemoteException e) {
    							e.printStackTrace();
    						}
    					}
    				}).start();
    			}
    		}else{
    			onPrev(false);
    		}
    	}
    }
    
    public void WhellNext(){
    	if(getActivity() != null){
    		/*blue_music_state = SharedPreferencesUtils.getSharedPreferences(
    				getActivity()).getBoolean(Configs.BLUE_MUSIC,
    						Configs.DEFAULT_BLUE_MUSIC);*/
    		if(blue_music_state){
    			if(btservice != null){
    				new Thread(new Runnable() {
    					public void run() {
    						try {
    							if(btservice.btAvrNext()==0){
    								PlayVideoBlueEvent event = new PlayVideoBlueEvent();
    								event.type = PlayVideoBlueEvent.BLUE_MUSIC_STATE;
    								EventBus.getDefault().post(event);
    							}
    						} catch (RemoteException e) {
    							e.printStackTrace();
    						}
    					}
    				}).start();
    			}
    		}else{
    			onNext(false);
    		}
    	}
    }
    
    public void abandonAudioFocus(OnAudioFocusChangeListener listener) {
    	mAudioManager.abandonAudioFocus(listener);
	}
	
    boolean btnowstatus = false;
	/**
	 * 蓝牙音乐监听器
	 */
	public OnAudioFocusChangeListener afBTChangeListener = new OnAudioFocusChangeListener() {
		public void onAudioFocusChange(int focusChange) {
			if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
			} else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
				if(btnowstatus){
					if(!blue_music_state){
						btn_blue_music.performClick();
					}
				}
			} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
				btnowstatus = false;
				if(blue_music_state){
					btnowstatus = true;
					btn_blue_music.performClick();
				}
			} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
				btnowstatus = false;
				if(blue_music_state){
					btnowstatus = true;
					btn_blue_music.performClick();
				}
			}

		}
	};

	/**
	 * 本地音乐监听器
	 */
	public OnAudioFocusChangeListener afChangeListener = new OnAudioFocusChangeListener() {
		public void onAudioFocusChange(int focusChange) {
			if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
			} else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
			} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
			}

		}
	};
	
	boolean localplaystatus = false;
	boolean nowAudioStatus = false;
	/**
	 * 本地系统音乐监听器
	 */
	public OnAudioFocusChangeListener afSystemChangeListener = new OnAudioFocusChangeListener() {
		public void onAudioFocusChange(int focusChange) {
			if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
				Log.i("localMusic", "System AUDIOFOCUS_LOSS_TRANSIENT");
			} else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
				Log.i("localMusic", "System AUDIOFOCUS_LOSS_TRANSIENT");
				nowAudioStatus = true;
				mhandler.sendEmptyMessageDelayed(400, 2000);
			} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
				Log.i("localMusic", "System AUDIOFOCUS_LOSS_TRANSIENT");
				nowAudioStatus = false;
				if (mPlayerModel.isPlaying()) {
					localplaystatus = true;
					if (mPlayerModel.pause()) {
						MusicFragment.this.stopPlayAnim(1000);
						playbtn.setImageResource(R.drawable.music_start_btn_selector);
						music_box_now.clearAnimation();
					}
				}
			} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
				Log.i("localMusic", "System AUDIOFOCUS_LOSS_TRANSIENT");
				nowAudioStatus = false;
				if (mPlayerModel.isPlaying()) {
					localplaystatus = true;
					if (mPlayerModel.pause()) {
						MusicFragment.this.stopPlayAnim(1000);
						playbtn.setImageResource(R.drawable.music_start_btn_selector);
						music_box_now.clearAnimation();
					}
				}
			}

		}
	};

}
