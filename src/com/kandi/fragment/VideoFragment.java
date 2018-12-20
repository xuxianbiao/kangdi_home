package com.kandi.fragment;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.kandi.application.BaseApplication;
import com.kandi.event.FreshVideoListSelectEvent;
import com.kandi.event.PlayMusicEvent;
import com.kandi.event.PlayVideoBlueEvent;
import com.kandi.event.PlayVideoEvent;
import com.kandi.event.UsbMediaStatesEvent;
import com.kandi.event.VideoControlEvent;
import com.kandi.event.VideoListSelectEvent;
import com.kandi.home.R;
import com.kandi.util.FileUtil;
import com.kandi.view.VideoListActivity;
import com.kandi.view.VideoListActivity.VideoList;
import com.yd.manager.YdUtils;

import de.greenrobot.event.EventBus;

public class VideoFragment extends Fragment implements
		android.os.Handler.Callback {

	public static VideoFragment instance;
	private SurfaceView canmare_SurfaceView;
	private ImageButton canmare_play;
	private ImageView screen_play;
	private ImageView pauseCaptureView;
	private MediaPlayer mediaPlayer;
	private int currentPosition = 0;
	public static int playSts = 0;// 0 idle 1:playing 2:pause
	private ImageButton media_play;
	private String currentPlayFilePath = "";
	private String Video_Src="1";	//1表示本地资源，2表示USB资源
	private List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
	private SeekBar progressBar1;
	private Handler handler;
	private ImageButton media_next;
	private int currentIndex = 0;
	private TextView media_current;
	private TextView media_total;
	private ImageButton listBtn;
	private ImageButton media_pre;
	private Timer timer = null;
	private TimerTask task;
	private boolean seekbar = false;
	private int progress;
	private String sec;
	private boolean isusb = false;
	private final int INIT_LOCALVIDEO=2000;
	private Handler movieDealyHandler;
	private Runnable movieDealyRunnable;
	
	private AudioManager mAudioManager;

	private View rootView;
	private boolean hasInited = false;
	private FullScreenInterface fullScreenIntf;
	public View black_bg;

	public void setFullScreenIntf(FullScreenInterface fullScreenIntf) {
		this.fullScreenIntf = fullScreenIntf;
	}

	private OnSeekBarChangeListener osbcl = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			seekbar = false;
			progress = seekBar.getProgress();
			if (mediaPlayer != null) {
				int duration = mediaPlayer.getDuration();
				float seekto = (float) duration
						* ((float) progress / (float) 100);
				System.out.println("~~~~~~~~~~" + seekto);
				mediaPlayer.seekTo(progress);
			}

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			seekbar = true;
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

		}
	};
	private View rl_video;

	private void initEvent() {
		EventBus.getDefault().register(this, "UsbMediaStatesEvent",
				UsbMediaStatesEvent.class);
		EventBus.getDefault().register(this, "onVideoSelected",
				VideoListSelectEvent.class);
				
		EventBus.getDefault().register(this, "onFreshVideoSelected",
				FreshVideoListSelectEvent.class);		

		EventBus.getDefault().register(this, "onPlayMusic",
				PlayMusicEvent.class);
				
		EventBus.getDefault().register(this, "onPlayVideoBlue",
				PlayVideoBlueEvent.class);

		EventBus.getDefault().register(this, "onVideoControlEvent",
				VideoControlEvent.class);
	}

	public void initView(View view) {
		black_bg = (View) view.findViewById(R.id.black_bg);
		canmare_SurfaceView = (SurfaceView) view.findViewById(R.id.surfaceView);
		holder = canmare_SurfaceView.getHolder();
		holder.addCallback(new Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				/*
				 * if (mediaPlayer != null && mediaPlayer.isPlaying()) {
				 * currentPosition = mediaPlayer.getCurrentPosition();
				 * mediaPlayer.stop(); }
				 */
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// if (currentPosition > 0) {
				// play(currentPosition);
				// currentPosition = 0;
				// }
				if (mediaPlayer == null) {
					mediaPlayer = new MediaPlayer();

				}

				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mediaPlayer.setDisplay(holder);
				// hh.sendEmptyMessage(0);
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
			}

		});
		handler = new Handler(this);
		listBtn = (ImageButton) view.findViewById(R.id.list);
		listBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (BaseApplication.getInstance().requestCarSpeedMax()){
					return;
				}
				Intent intent = new Intent(getActivity(),
						VideoListActivity.class);
				if (dataList.size() != 0) {
					String videoPath = dataList.get(currentIndex).get(
							"moviepath")
							+ "";
//					String path = videoPath.substring(videoPath.lastIndexOf("/") + 1);
					String[] data={videoPath,Video_Src};
					intent.putExtra("videopath", data);
				}

//				VideoFragment.this.startActivityForResult(intent, 0);
				startActivity(intent);
			}
		});

		media_current = (TextView) view.findViewById(R.id.time1);
		media_total = (TextView) view.findViewById(R.id.time3);
		media_pre = (ImageButton) view.findViewById(R.id.media_pre);
		media_pre.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (BaseApplication.getInstance().requestCarSpeedMax()){
					return;
				}
				
				if (currentIndex == 0 || dataList.size() == 0) {
					return;
				}

				if (mediaPlayer != null) {
					playSts = 0;

					currentIndex = currentIndex == 0 ? 0 : currentIndex - 1;
					currentPlayFilePath = dataList.get(currentIndex).get(
							"moviepath")
							+ "";
					mediaPlayer.reset();
					canmare_play
							.setImageResource(R.drawable.video_pause_selector);
					play(0);
					screen_play.setVisibility(View.INVISIBLE);
					black_bg.setVisibility(View.GONE);
				}

			}
		});
		media_next = (ImageButton) view.findViewById(R.id.media_next);
		media_next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (BaseApplication.getInstance().requestCarSpeedMax()){
					return;
				}
				
				if (currentIndex >= dataList.size() - 1) {
					return;
				}

				if (mediaPlayer != null) {
					playSts = 0;
					currentIndex = currentIndex + 1;
					currentPlayFilePath = dataList.get(currentIndex).get(
							"moviepath")
							+ "";
					mediaPlayer.reset();
					canmare_play
							.setImageResource(R.drawable.video_pause_selector);
					play(0);
					screen_play.setVisibility(View.INVISIBLE);
					black_bg.setVisibility(View.GONE);
				}
			}
		});
		progressBar1 = (SeekBar) view.findViewById(R.id.imageView_progress1);
		progressBar1.setOnSeekBarChangeListener(osbcl);

		screen_play = (ImageView) view.findViewById(R.id.screen_play);
		screen_play.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (BaseApplication.getInstance().requestCarSpeedMax()){
					return;
				}
				onPlayBtnClick();
			}
		});
		
		canmare_play = (ImageButton) view.findViewById(R.id.media_play);
		canmare_play.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (BaseApplication.getInstance().requestCarSpeedMax()){
					return;
				}
				onPlayBtnClick();
			}
		});

		if (dataList.size() > 0) {
			currentPlayFilePath = dataList.get(0).get("moviepath") + "";
		} else {
		}

		rl_video = view.findViewById(R.id.rl_video);
		expand = (ImageButton) view.findViewById(R.id.expand);
		expand.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dealFullScreen();
			}
		});

		View vedioView = view.findViewById(R.id.vedio);
		vedioView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dealFullScreen();
			}
		});
		
		// 延时播放视频的handler和runnable
		movieDealyHandler = new Handler();
		movieDealyRunnable = new Runnable() {
			
			@Override
			public void run() {
				if(mAudioManager.requestAudioFocus(afSystemChangeListener, AudioManager.STREAM_MUSIC,
						AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
					mediaPlayer.start();
					canmare_play
					.setImageResource(R.drawable.video_pause_selector);
					playSts = 1;
					screen_play.setVisibility(View.INVISIBLE);
					black_bg.setVisibility(View.GONE);
				}
			}
		};
	}

	private void dealFullScreen() {
//		if (playSts != 1) {
//			return;
//		}

		if (rl_video.getVisibility() == View.VISIBLE) {
			// 全屏
			fullScreenIntf.requestFullScreen(true);
			rl_video.setVisibility(View.INVISIBLE);
			progressBar1.setVisibility(View.INVISIBLE);
		} else {
			// 不全屏
			fullScreenIntf.requestFullScreen(false);
			rl_video.setVisibility(View.VISIBLE);
			progressBar1.setVisibility(View.VISIBLE);
			progressBar1.invalidate();
		}
	}

	private void onPlayBtnClick() {
		if ("".equals(currentPlayFilePath) && dataList.size() > 0) {
			currentPlayFilePath = dataList.get(0).get("moviepath") + "";
		}

		File file = new File(currentPlayFilePath);
		if (!file.exists()) {
			currentPlayFilePath = "";
			return;
		}
		if (mediaPlayer != null) {
			if (playSts == 2) {
				EventBus.getDefault().post(new PlayVideoEvent());
				movieDealyHandler.postDelayed(movieDealyRunnable, 5L);
				
			} else if (playSts == 0) {
				if(mAudioManager.requestAudioFocus(afSystemChangeListener, AudioManager.STREAM_MUSIC,
						AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
					currentIndex = 0;
					currentPlayFilePath = dataList.get(currentIndex).get(
							"moviepath")
							+ "";
					mediaPlayer.reset();
					canmare_play
					.setImageResource(R.drawable.video_pause_selector);
					play(0);
					screen_play.setVisibility(View.INVISIBLE);
					black_bg.setVisibility(View.GONE);
				}
			} else if (playSts == 1) {
				if(mAudioManager.abandonAudioFocus(afSystemChangeListener) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
					playSts = 2;
					canmare_play
					.setImageResource(R.drawable.play_btn_selector);
					mediaPlayer.pause();
					screen_play.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	private SharedPreferences sp;
	private Editor editor;
	private Handler hh = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				try {
					mediaPlayer.setDataSource(currentPlayFilePath);
					mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					mediaPlayer.prepareAsync();
					mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

						@Override
						public void onPrepared(MediaPlayer mp) {
							mediaPlayer.pause();
							mediaPlayer.seekTo(progress);
							progressBar1.setMax(mediaPlayer.getDuration());
							progressBar1.setProgress(progress);
							sec = sec2Time((mediaPlayer.getDuration() / 1000));
							media_total.setText("" + sec);
							// startProcess();
							// canmare_play.setEnabled(false);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 1:
				onPlayBtnClick();
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.video_fragment_layout, null);
		 initFragment();
		instance = this;
		mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
		return rootView;

	};

	public void initFragment() {
		if (hasInited) {
			return;
		}

		sp = getActivity().getSharedPreferences("exitvideo",
				Activity.MODE_WORLD_WRITEABLE);
		editor = sp.edit();
		this.initView(rootView);
		initEvent();
		handler.sendEmptyMessage(INIT_LOCALVIDEO);
		hasInited = true;
	}

	private Bitmap getVideoThumbnail(String videoPath, int width, int height,
			int kind) {
		Bitmap bitmap = null;
		try {
			bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
			System.out.println("w" + bitmap.getWidth());
			System.out.println("h" + bitmap.getHeight());
			bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
					ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	private void loadFiles() {
		List<String> fileList = FileUtil.GetFiles2("sdcard", "mp4,3gp");
		for (int i = 0; i < fileList.size(); i++) {

			String videoPath = fileList.get(i);
			if (!videoPath.contains("._")) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("moviepath", fileList.get(i));
				// Bitmap icon = this.getVideoThumbnail(map.get("moviepath")+"",
				// 160, 90, MediaStore.Images.Thumbnails.MICRO_KIND);
				// map.put("itemImage", icon);
				dataList.add(map);
			}

		}
//		Collections.sort(dataList, new Comparator<Map<String, Object>>() {
//
//			@Override
//			public int compare(Map<String, Object> map1,
//					Map<String, Object> map2) {
//				Collator collator = Collator.getInstance();
//				String movie1 = map1.get("moviepath") + "";
//				String movie2 = map2.get("moviepath") + "";
//				String v1 = movie1.substring(movie1.lastIndexOf("/"));
//				String v2 = movie2.substring(movie2.lastIndexOf("/"));
//				return collator.getCollationKey(v1).compareTo(
//						collator.getCollationKey(v2));
//			}
//		});
		
		//add by dw in yd
		Collections.sort(dataList, new Comparator<Map<String, Object>>() {
			
			@Override
			public int compare(Map<String, Object> map1,
					Map<String, Object> map2) {
				Collator collator = Collator.getInstance(java.util.Locale.CHINA);
				String movie1 = map1.get("moviepath") + "";
				String movie2 = map2.get("moviepath") + "";
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
//		if (Settings.System.getInt(getActivity().getContentResolver(), "system_kandi_key", 0) == 1) {
//		}else {
//			Collections.sort(dataList, new Comparator<Map<String, Object>>() {
//
//				@Override
//				public int compare(Map<String, Object> map1,
//						Map<String, Object> map2) {
//					Collator collator = Collator.getInstance();
//					String movie1 = map1.get("moviepath") + "";
//					String movie2 = map2.get("moviepath") + "";
//					String v1 = movie1.substring(movie1.lastIndexOf("/"));
//					String v2 = movie2.substring(movie2.lastIndexOf("/"));
//					return collator.getCollationKey(v1).compareTo(
//							collator.getCollationKey(v2));
//				}
//			});
//		}
		//add end
		
	}

	private ImageButton expand;
	private SurfaceHolder holder;

	protected void play(final int msec) {
		try {
			if (mediaPlayer == null) {
				mediaPlayer = new MediaPlayer();

			}
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					if (playSts == 1) {
						currentIndex++;
						if(dataList == null || dataList.size() == 0){
							mediaPlayer.reset();
							playSts = 2;
							return;
						}
						if (currentIndex > dataList.size() - 1) {
							currentIndex = 0;
						}
						try{
						currentPlayFilePath = dataList.get(currentIndex).get(
								"moviepath")
								+ "";

						mediaPlayer.reset();
						} catch(Exception e){
							e.printStackTrace();
						}
						play(0);
					} else {
						playSts = 1;
					}
				}

			});
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setDataSource(currentPlayFilePath);
			mediaPlayer.setDisplay(holder);
			mediaPlayer.prepareAsync();
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					mediaPlayer.start();
					mediaPlayer.seekTo(msec);
					progressBar1.setMax(mediaPlayer.getDuration());
					sec = sec2Time((mediaPlayer.getDuration() / 1000));
					media_total.setText("" + sec);
				}
			});

			playSts = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}

		EventBus.getDefault().post(new PlayVideoEvent());
	}

	protected void replay() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.seekTo(0);
			return;
		}
		play(0);
	}

	private String sec2Time(int secv) {
		int hour = secv / 3600;
		int min = (secv / 60) % 60;
		int sec = (secv % 3600) % 60;
		System.out.println(hour + ":" + min + ":" + sec);
		String hourStr = hour < 10 ? "0" + hour : hour + "";
		String minStr = min < 10 ? "0" + min : min + "";
		String secStr = sec < 10 ? "0" + sec : sec + "";
		if (hour == 0) {

			return minStr + ":" + secStr;
		} else {
			return hourStr + ":" + minStr + ":" + secStr;
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (msg.what == 1 || msg.what == 0) {
			if (mediaPlayer != null) {
				if (mediaPlayer.isPlaying()) {
					if (BaseApplication.getInstance().requestCarSpeedMax()){
						playSts = 2;
						canmare_play
								.setImageResource(R.drawable.play_btn_selector);
						mediaPlayer.pause();
						screen_play.setVisibility(View.VISIBLE);
						black_bg.setVisibility(View.VISIBLE);
					}
					if(black_bg.getVisibility() == View.VISIBLE){
						black_bg.setVisibility(View.GONE);
					}
					int current = mediaPlayer.getCurrentPosition();
					String sec = sec2Time((current / 1000));
					media_current.setText(sec);
					if (!seekbar) {
						progressBar1.setProgress(current);
					}
//					ToastUtil.showDbgToast(getActivity(), "" + seekbar);
				}
			}

		}else if (msg.what == INIT_LOCALVIDEO) {
			loadFiles();
		}
		return false;
	}

	private void initTimer() {
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				try {
					if (null == handler) {
						return;
					}
					handler.sendEmptyMessage(playSts);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		};
		timer.schedule(task, 0, 500);
	}

	public void saveInfo(Context context, String key,
			List<Map<String, Object>> datalist) {
		JSONArray mJsonArray = new JSONArray();
		for (int i = 0; i < datalist.size(); i++) {
			Map<String, Object> itemMap = datalist.get(i);
			Iterator<Entry<String, Object>> iterator = itemMap.entrySet()
					.iterator();

			JSONObject object = new JSONObject();

			while (iterator.hasNext()) {
				Entry<String, Object> entry = iterator.next();

				try {
					object.put(entry.getKey(), entry.getValue());
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
			mJsonArray.put(object);
		}
		editor = sp.edit();
		editor.putString(key, mJsonArray.toString());
		editor.commit();
	}

	public List<Map<String, Object>> loadInfo(Context context, String key) {
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		String result = sp.getString(key, "");
		try {
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject itemObject = array.getJSONObject(i);
				Map<String, Object> itemMap = new HashMap<String, Object>();
				JSONArray names = itemObject.names();
				if (names != null) {
					for (int j = 0; j < names.length(); j++) {
						String name = names.getString(j);
						String value = itemObject.getString(name);
						itemMap.put(name, value);
					}
				}
				datas.add(itemMap);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return datas;

	}

	public void UsbMediaStatesEvent(UsbMediaStatesEvent event) {

		switch (event.getUsbState()) {
		case MEDIA_PLUGED: {
			// Toast.makeText(MusicActivity.this, "U盘已插入", 0).show();
			VideoList.getInstance().setDirtyFlag();
		}
			break;
		case MEDIA_UNPLUGED: {
			// Toast.makeText(MusicActivity.this, "U盘已拔出", 0).show();
			VideoList.getInstance().setDirtyFlag();
			if (isusb) {
				VideoList.getInstance().getUsbVideoList().clear();
				dataList.clear();
				mediaPlayer.reset();
				// this.loadFiles();
				screen_play.setVisibility(View.VISIBLE);
				black_bg.setVisibility(View.VISIBLE);
				media_current.setText("00:00");
				media_total.setText("00:00");
				progressBar1.setProgress(0);
				canmare_play
						.setImageResource(R.drawable.play_btn_selector);

			}
		}
			break;
		case UNKNOWN:
		default:
		}
	}

	public void onVideoSelected(VideoListSelectEvent event) {
		int resultCode = event.getResultCode();
		if (resultCode == 1) {
			isusb = false;
			dataList.clear();
			loadFiles();
			currentIndex = event.getVideoIndex();
			Video_Src = String.valueOf(event.getVideoSrc());
			if (currentIndex >= 0 && currentIndex <= dataList.size() - 1) {
				screen_play.setVisibility(View.GONE);
				black_bg.setVisibility(View.GONE);
				// canmare_pic.setBackgroundColor(getResources().getColor(R.color.none_color));
				canmare_play
						.setImageResource(R.drawable.video_pause_selector);
				// currentIndex = currentIndex+1;
				currentPlayFilePath = dataList.get(currentIndex).get(
						"moviepath")
						+ "";
				System.out
						.println("currentPlayFilePath:" + currentPlayFilePath);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (mediaPlayer != null) {
					mediaPlayer.reset();
				}
				play(0);

			}
		} else if (resultCode == 2) {
			isusb = true;
			System.out.println("~~~~~onActivityResult!!!");
			currentIndex = event.getVideoIndex();
//			dataList.clear();
			dataList = event.getUsbVideoList();
			Video_Src = String.valueOf(event.getVideoSrc());
			if (currentIndex >= 0 && currentIndex <= dataList.size() - 1) {
				screen_play.setVisibility(View.GONE);
				black_bg.setVisibility(View.GONE);
				screen_play.setBackgroundColor(getResources().getColor(
						R.color.none_color));
				canmare_play
						.setImageResource(R.drawable.video_pause_selector);
				// currentIndex = currentIndex+1;
				currentPlayFilePath = dataList.get(currentIndex).get(
						"moviepath")
						+ "";
				System.out
						.println("currentPlayFilePath:" + currentPlayFilePath);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (mediaPlayer != null) {
					mediaPlayer.reset();
				}
				playSts = 1;
				play(0);

			}
		}
	}
	
	public void onFreshVideoSelected(FreshVideoListSelectEvent event) {
		int resultCode = event.getResultCode();
		if (resultCode == 1) {
			isusb = false;
			dataList.clear();
			loadFiles();
			currentIndex = event.getVideoIndex();
			Video_Src = String.valueOf(event.getVideoSrc());
			if (currentIndex >= 0 && currentIndex <= dataList.size() - 1) {
				screen_play.setVisibility(View.GONE);
				black_bg.setVisibility(View.GONE);
				canmare_play
						.setImageResource(R.drawable.video_pause_selector);
				currentPlayFilePath = dataList.get(currentIndex).get(
						"moviepath")
						+ "";
				System.out.println("currentPlayFilePath:" + currentPlayFilePath);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(event.isLocation()){
					if (mediaPlayer != null) {
						mediaPlayer.reset();
					}
					play(0);
				}

			}else if(dataList.size()==0){
				screen_play.setVisibility(View.VISIBLE);
				black_bg.setVisibility(View.VISIBLE);
				canmare_play
						.setImageResource(R.drawable.play_btn_selector);
				if (mediaPlayer != null) {
					mediaPlayer.reset();
				}
				play(0);
			}
		} else if (resultCode == 3) {
			isusb = false;
			dataList.clear();
			loadFiles();
			currentIndex = event.getVideoIndex();
			Video_Src = String.valueOf(event.getVideoSrc());
		}
	}

	@Override
	public void onResume() {
		if (timer == null) {
			initTimer();
		}
		super.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();
		if (playSts == 1) {
			onPlayBtnClick();
		}
		
		if (null != rl_video && rl_video.getVisibility() != View.VISIBLE) {
			// 不全屏
			fullScreenIntf.requestFullScreen(false);
			rl_video.setVisibility(View.VISIBLE);
			progressBar1.setVisibility(View.VISIBLE);
		}
		
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onDestroy() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		EventBus.getDefault().unregister(this,UsbMediaStatesEvent.class);
		EventBus.getDefault().unregister(this,VideoListSelectEvent.class);
		EventBus.getDefault().unregister(this,FreshVideoListSelectEvent.class);
		EventBus.getDefault().unregister(this,PlayMusicEvent.class);
		EventBus.getDefault().unregister(this,PlayVideoBlueEvent.class);
		EventBus.getDefault().unregister(this,VideoControlEvent.class);
		saveInfo(getActivity(), "videopath", dataList);
		editor.putInt("currentIndex", currentIndex);
		editor.commit();
		editor.putInt("progress", progress);
		editor.commit();
		editor.putInt("playSts", playSts);
		editor.commit();
		editor.putString("sec", sec);
		instance = null;
		super.onDestroy();
	}

	@Override
	public void onPause() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		super.onPause();
	}

	public void onPlayVideoBlue(PlayVideoBlueEvent event) {
		if(playSts == 1 && event.type == PlayVideoBlueEvent.BLUE_MUSIC_STATE){
			hh.sendEmptyMessage(1);
		}
	}
	
	public void onPlayMusic(PlayMusicEvent event) {
		if (playSts == 1 && event.type == PlayMusicEvent.MUSIC_PLAY) {
			onPlayBtnClick();
		}
	}

	public void onVideoControlEvent(VideoControlEvent event) {
		onPlayBtnClick();
	}

	public interface FullScreenInterface {
		public void requestFullScreen(boolean fullScreen);
	};
	
	boolean nowVideoStatus = false;
	/**
	 * 本地系统音乐监听器
	 */
	public OnAudioFocusChangeListener afSystemChangeListener = new OnAudioFocusChangeListener() {
		public void onAudioFocusChange(int focusChange) {
			if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
			} else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
				if(nowVideoStatus){
					onPlayBtnClick();
				}
			} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
				nowVideoStatus = false;
				if (playSts == 1) {
					nowVideoStatus = true;
					onPlayBtnClick();
				}
			} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
				nowVideoStatus = false;
				if (playSts == 1) {
					nowVideoStatus = true;
					onPlayBtnClick();
				}
			}

		}
	};
}
