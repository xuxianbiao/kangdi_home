package com.kandi.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.utils.SharedPreferencesUtils;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IFmService;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.kandi.customview.HomeMileView;
import com.kandi.driver.DriverServiceManger;
import com.kandi.driver.EcocEnergyInfoDriver;
import com.kandi.driver.radio.Configs;
import com.kandi.event.ChangeMainBgEvent;
import com.kandi.event.DeskMusicProgressEvent;
import com.kandi.event.PlayFmEvent;
import com.kandi.event.PlayMusicEvent;
import com.kandi.event.SmallBlueMusicEvent;
import com.kandi.event.SmallMusicEvent;
import com.kandi.event.SmallRadioEvent;
import com.kandi.home.R;
import com.kandi.model.MusicPlayerModel;
import com.kandi.service.ThemePopService;
import com.kandi.view.MainActivity;
import com.kandi.view.WeatherDialog;
import com.kandi.widget.MediaPlayertextview;

import de.greenrobot.event.EventBus;

public class MainPageFragment1 extends Fragment {
	private float carSoc = 0;
	private int carKwh = 0;
	private int remainMile = 0;
	private String weatherResStr;
//	private HomeWeatherView hwv;
	private HomeMileView charingtxtarea;
	private TextView charging_state;
	private ImageButton themebtn;
	private View mainview;
	private View rl_video;
	private TextView timeTv, dateTv,dataTv2;
	private Timer timer;
	private TimerTask timerTask;
	private boolean isActive = false;
	private  Handler mHandler = null;
	private static final String WALLPAPER_SHARED_PREF = "wallpaperSharedPref";
	private static final String WALLPAPER_SELECT_INDEX = "wallpaperIndex";
	private WeatherDialog weatherdialog = null; 

	int wallPaperId[] = { R.drawable.bg_01_01, R.drawable.bg_02_01,
			R.drawable.bg_03_01, R.drawable.bg_04_01, R.drawable.bg_05_01,
			R.drawable.bg_06_01 };

	public static MainPageFragment1 newInstance(String s) {
		MainPageFragment1 newFragment = new MainPageFragment1();
		Bundle bundle = new Bundle();
		bundle.putString("param", s);
		newFragment.setArguments(bundle);
		return newFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_layout, container, false);
		initView(view);
		initPlayArea(view);
		initWeather();
		mHandler = new Handler();
		EventBus.getDefault().register(this, "SmallMusicEvent",
				SmallMusicEvent.class);
		EventBus.getDefault().register(this, "SmallBlueMusicEvent",
				SmallBlueMusicEvent.class);
		EventBus.getDefault().register(this, "SmallRadioEvent",
				SmallRadioEvent.class);
		EventBus.getDefault().register(this, "ChangeMainBgEvent",
				ChangeMainBgEvent.class);
		
		EventBus.getDefault().register(this, "onPlayMusic",
				PlayMusicEvent.class);

		getActivity().registerReceiver(timerTickReciever,
				new IntentFilter(Intent.ACTION_TIME_TICK));

		return view;
	}

	
	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(timerTickReciever);
		unregistBroadCastReceiver();
		if(timerTask != null) {
			timerTask.cancel();
			timerTask=null;
			if(timer!=null){
				timer.cancel();
				timer=null;
			}
		}
		EventBus.getDefault().unregister(this,SmallMusicEvent.class);
		EventBus.getDefault().unregister(this,SmallBlueMusicEvent.class);
		EventBus.getDefault().unregister(this,SmallRadioEvent.class);
		EventBus.getDefault().unregister(this,ChangeMainBgEvent.class);
		EventBus.getDefault().unregister(this,PlayMusicEvent.class);
		super.onDestroy();
	}

	public void ChangeMainBgEvent(ChangeMainBgEvent event) {
		String picStr = event.getText();
		int wallPaperIndex = Integer.parseInt(picStr) - 1;
		mainview.setBackgroundResource(wallPaperId[wallPaperIndex]);
		
		if(getActivity() != null){
			SharedPreferences spf = getActivity().getSharedPreferences(
					WALLPAPER_SHARED_PREF, Activity.MODE_PRIVATE);
			Editor editor = spf.edit();
			editor.putInt(WALLPAPER_SELECT_INDEX, wallPaperIndex);
			editor.commit();
		}
	}

	private void initView(View view) {
		mainview = view.findViewById(R.id.bgview);
		//天气功能暂时关闭 美标版
//		hwv = (HomeWeatherView) view.findViewById(R.id.hwv);
//		hwv.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {//选择城市
//				weatherdialog = new WeatherDialog(getActivity());
//				Window dialogWindow = weatherdialog.getWindow();
//				WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//				dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
//				lp.y = 300;
//				dialogWindow.setAttributes(lp);
//				weatherdialog.show();
//			}
//		});
		charingtxtarea = (HomeMileView) view.findViewById(R.id.charingtxtarea);
		// 设置公里和电池百分比
		int soc = (int) carSoc;
		// charingtxtarea.setMile(132, 23);
		if (remainMile == 0) {
			charingtxtarea.setMile(999, carKwh, soc);
		} else {
			charingtxtarea.setMile(remainMile, carKwh, soc);
		}
//		charging_state = (TextView) view.findViewById(R.id.charging_state);
//		charging_state.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				Intent intent = new Intent(MainPageFragment1.this.getActivity(), ChargingActivity.class);
//				MainPageFragment1.this.getActivity().startActivity(intent);			
//				}
//		});

		themebtn = (ImageButton) view.findViewById(R.id.themebtn);
		themebtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), ThemePopService.class);
				getActivity().startService(intent);
			}
		});

		// 初始化时间
		timeTv = (TextView) view.findViewById(R.id.timelab);
		dateTv = (TextView) view.findViewById(R.id.datelab);
		dataTv2 = (TextView) view.findViewById(R.id.dateday);

		// 初始化壁纸
		SharedPreferences spf = getActivity().getSharedPreferences(
				WALLPAPER_SHARED_PREF, Activity.MODE_PRIVATE);
		int wallPaperIndex = spf.getInt(WALLPAPER_SELECT_INDEX, 0);
		mainview.setBackgroundResource(wallPaperId[wallPaperIndex]);

		timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(2);
			}
		};
		timer.schedule(timerTask, 0, 1000);
	}

	BroadcastReceiver timerTickReciever = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			setTimeAndDate();
		}
	};
	Handler weatherHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 101:
//				hwv.setWeather(lMap,getActivity());
				break;
			case 200:
				try {
					float now_freq = radio.GetRadioFreq();
					if(now_freq > 0){
						if(radio.RadioFreqSeekUp() >= 0){
							float freq = radio.GetRadioFreq();
							float Min_Freq = 87;
							float Max_Freq = 108;
							if(freq<=Min_Freq){
								freq = now_freq;
								radio.SetRadioFreq(freq);
							}else if(freq>=Max_Freq){
								freq = now_freq;
								radio.SetRadioFreq(freq);
							}
							radiotext.setText("FM "+freq);
							btnRadioSeekFoward.setEnabled(true);
							btnRadioSeekBackward.setEnabled(true);
							return;
						}else{
							radio.SetRadioFreq(now_freq);
							radiotext.setText("FM "+now_freq);
							btnRadioSeekFoward.setEnabled(true);
							btnRadioSeekBackward.setEnabled(true);
							return;
						}
					}else{
						btnRadioSeekFoward.setEnabled(true);
						btnRadioSeekBackward.setEnabled(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 201:
				try {
					float now_freq = radio.GetRadioFreq();
					if(now_freq > 0){
						if(radio.RadioFreqSeekDown() >= 0){
							float freq = radio.GetRadioFreq();
							float Min_Freq = 87;
							float Max_Freq = 108;
							if(freq<=Min_Freq){
								freq = now_freq;
								radio.SetRadioFreq(freq);
							}else if(freq>=Max_Freq){
								freq = now_freq;
								radio.SetRadioFreq(freq);
							}
							radiotext.setText("FM "+freq);
							btnRadioSeekFoward.setEnabled(true);
							btnRadioSeekBackward.setEnabled(true);
							return;
						}else{
							radio.SetRadioFreq(now_freq);
							radiotext.setText("FM "+now_freq);
							btnRadioSeekFoward.setEnabled(true);
							btnRadioSeekBackward.setEnabled(true);
							return;
						}
					}else{
						btnRadioSeekFoward.setEnabled(true);
						btnRadioSeekBackward.setEnabled(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		}
	};
	private void setTimeAndDate() {
		SimpleDateFormat dateFormat, timeFormat;
		java.text.DateFormat data=DateFormat.getDateFormat(this.getActivity());
		String datapattern = ((SimpleDateFormat) data).toPattern();
		if(datapattern.equals("M/d/y")){
			datapattern = "MM/dd/yyyy";
		}else if(datapattern.equals("y-M-d")){
			datapattern = "yyyy-MM-dd";
		}
		dateFormat = new SimpleDateFormat(datapattern+"EEEE");
//		timeFormat = new SimpleDateFormat("HH:mm");
		Date date = new Date(System.currentTimeMillis());
		String dateStr = dateFormat.format(date);
		dateStr = dateStr.replace(
				MainActivity.getContext().getString(R.string.week),
				MainActivity.getContext().getString(R.string.week2));
		ContentResolver cv = getActivity().getContentResolver();
		String strTimeFormat = android.provider.Settings.System.getString(cv,
				android.provider.Settings.System.TIME_12_24);
		if(strTimeFormat != null || !"".equals(strTimeFormat)) {
			SimpleDateFormat sdf;
			if("24".equals(strTimeFormat)){
				sdf = new SimpleDateFormat("HH:mm");
			}else{
				sdf = new SimpleDateFormat("a hh:mm");
			}
			String timeStr = sdf.format(date);
			timeTv.setText(timeStr);
		}else{
			android.provider.Settings.System.putString(cv,android.provider.Settings.System.TIME_12_24,"12");
		}
		String date1 = dateStr.substring(0, 10);
		String date2 = dateStr.substring(10, dateStr.length());
		dateTv.setText(date1);
		dataTv2.setText(date2);
		
	}

	@Override
	public void onResume() {
		registBroadCastReceiver();
		super.onResume();
		setTimeAndDate();
		isActive = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		isActive = false;
		Intent stopIntent = new Intent(getActivity(), ThemePopService.class);
        getActivity().stopService(stopIntent);
	}

	private void initWeather() {
		/**
		 * 发送获取天气广播
		 */
		String cityname = SharedPreferencesUtils.getSharedPreferences(
				getActivity()).getString(Configs.WEATHER_CITY,
				Configs.DEFAULT_WEATHER_CITY);
		Intent intent = new Intent();
		intent.putExtra("cityname", cityname);
		intent.setAction(QUERY_WEATHER);  
        getActivity().sendBroadcast(intent,null);
        
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			refreshPannel();
			int soc = (int) carSoc;
			charingtxtarea.setMile(remainMile, carKwh, soc);
		};
	};

	private void refreshPannel() {
		EcocEnergyInfoDriver model = DriverServiceManger.getInstance()
				.getEcocEnergyInfoDriver();

		if (model != null) {
			try {
				model.retreveGeneralInfo();
			} catch (RemoteException e) {
				e.printStackTrace();
				return;
			}
			// 将模型数据更新到屏幕...
			carSoc = model.getSOC();
			carKwh = model.getRemainKWH();
			remainMile = model.getRemainMileage();
		}
	}

	private RelativeLayout smallmusic;
	private RelativeLayout smallbluemusic;
	private RelativeLayout smallradio;
	private MediaPlayertextview musicpath;
	private TextView musictime1;
	private TextView musictime2;
	private SeekBar musictimeline;
	private MediaPlayer player;
	private IFmService radio;
	private TextView radiotext;
	private ImageView hou, qian, middle;
	
	private ImageButton btnRadioSeekBackward;
	private ImageButton btnRadioSeekFoward;
	private boolean isSeekTracking = false;
	private MediaPlayertextview bluemusicname;
	private TextView bluetime;
	private ImageButton blueprev;
	private ImageButton bluenext;

	private void initPlayArea(View view) {
		smallmusic = (RelativeLayout) view.findViewById(R.id.smallmusic);
		smallbluemusic = (RelativeLayout) view.findViewById(R.id.smallbluemusic);
		smallradio = (RelativeLayout) view.findViewById(R.id.smallradio);

		musicpath = (MediaPlayertextview) view.findViewById(R.id.musicpath);
		musictime1 = (TextView) view.findViewById(R.id.musictime1);
		musictime2 = (TextView) view.findViewById(R.id.musictime2);
		musictimeline = (SeekBar) view.findViewById(R.id.musictimeline);
		musictimeline.setMax(100);
		musictimeline.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				isSeekTracking = false;
				Log.i("LeiTest", "onStopTrackingTouch");
				DeskMusicProgressEvent event = new DeskMusicProgressEvent();
				event.progress = arg0.getProgress();
				EventBus.getDefault().post(event);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				isSeekTracking = true;
				Log.i("LeiTest", "onStartTrackingTouch");
			}
			
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			}
		});
		
		hou = (ImageButton) smallmusic.findViewById(R.id.prev);
		hou.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MusicPlayerModel.getInstance().previous();
				MusicPlayerModel.getInstance().play();
			}
		});
		middle = (ImageButton) smallmusic.findViewById(R.id.play);
		middle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (MusicPlayerModel.getInstance().isPlaying()) {
					EventBus.getDefault().post(new PlayFmEvent());
					middle.setImageResource(R.drawable.home_small_music_play_selector);
				} else {
					boolean success = MusicPlayerModel.getInstance().play();
					if (success) {
						middle.setImageResource(R.drawable.home_small_music_pause_selector);
					}
					
				}
			}
		});
		qian = (ImageButton) smallmusic.findViewById(R.id.next);
		qian.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MusicPlayerModel.getInstance().next();
				MusicPlayerModel.getInstance().play();
			}
		});
		initSmallBluePannel(smallbluemusic);
		initSmallRadioPannel(smallradio);
	}

	public void initSmallBluePannel(View view){
		bluemusicname = (MediaPlayertextview) view.findViewById(R.id.bluemusicname);
		bluetime = (TextView) view.findViewById(R.id.bluetime);
		blueprev = (ImageButton) view.findViewById(R.id.blueprev);
		blueprev.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction("com.kangdi.BroadCast.MusicBlueNext");
				getActivity().sendBroadcast(intent);
			}
		});
		bluenext = (ImageButton) view.findViewById(R.id.bluenext);
		bluenext.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction("com.kangdi.BroadCast.MusicBlueNext");
				getActivity().sendBroadcast(intent);
			}
		});
	}
	
	private void initSmallRadioPannel(View view) {
		radio = IFmService.Stub.asInterface(ServiceManager.getService("fm"));
		radiotext = (TextView) view.findViewById(R.id.radiotext);
		btnRadioSeekBackward = (ImageButton) view.findViewById(R.id.btnRadioSeekBackward);
		btnRadioSeekFoward = (ImageButton) view.findViewById(R.id.btnRadioSeekFoward);
		btnRadioSeekBackward.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				notifyFmFragment(PlayFmEvent.FM_TYPE_LEFT);
				btnRadioSeekFoward.setEnabled(false);
				btnRadioSeekBackward.setEnabled(false);
				weatherHandler.sendEmptyMessage(201);
			}
		});
		
		btnRadioSeekBackward.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				//notifyFmFragment(PlayFmEvent.FM_TYPE_LONG_LEFT);
				return true;
			}
		});
		
		btnRadioSeekFoward.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				notifyFmFragment(PlayFmEvent.FM_TYPE_RIGHT);
				btnRadioSeekFoward.setEnabled(false);
				btnRadioSeekBackward.setEnabled(false);
				weatherHandler.sendEmptyMessage(200);
			}
		});
		
		btnRadioSeekFoward.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				//notifyFmFragment(PlayFmEvent.FM_TYPE_LONG_RIGHT);
				return true;
			}
		});
	}
	
	private void notifyFmFragment(int type) {
		PlayFmEvent event = new PlayFmEvent();
		event.type = type;
		EventBus.getDefault().post(event);
	}
	
	public void SmallMusicEvent(SmallMusicEvent event) {
		try {
			if(radio != null){
				if(radio.GetLocalRadioStatus() == 1){
					return;
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		if (event.text.equals("hide")) {
			smallmusic.setVisibility(View.GONE);
		} else {
			if (smallbluemusic.getVisibility() != View.GONE) {
				smallbluemusic.setVisibility(View.GONE);
			}
			if (smallmusic.getVisibility() != View.VISIBLE) {
				smallmusic.setVisibility(View.VISIBLE);
				smallradio.setVisibility(View.GONE);
			}
			String smallpath = musicpath.getText().toString();
			if(smallpath != null){
				if(!smallpath.equals(MusicPlayerModel.getInstance().getCurrentMusicFileName())){
					musicpath.setText(MusicPlayerModel.getInstance()
							.getCurrentMusicFileName() + "");
				}
			}
			musictime1.setText(event.time1);
			musictime2.setText(event.time2);
			
			if (!isSeekTracking) {
				musictimeline.setProgress(event.duration);
			}
			
		}
	}
	public void SmallBlueMusicEvent(SmallBlueMusicEvent event) {
		if (event.text.equals("hide")) {
			smallbluemusic.setVisibility(View.GONE);
		} else {
			if (smallmusic.getVisibility() != View.GONE) {
				smallmusic.setVisibility(View.GONE);
				//smallradio.setVisibility(View.INVISIBLE);
			}
			if (smallbluemusic.getVisibility() != View.VISIBLE) {
				smallbluemusic.setVisibility(View.VISIBLE);
				smallradio.setVisibility(View.GONE);
			}
			String smallpath = bluemusicname.getText().toString();
			if(smallpath != null){
				if(!smallpath.equals(event.musicName)){
					bluemusicname.setText(event.musicName);
				}
			}
			bluetime.setText(event.time1);
		}
	}
	//收音机面板暂时隐藏
	public void SmallRadioEvent(SmallRadioEvent event) {
		if (event.text.equals("hide")) {
			smallradio.setVisibility(View.GONE);
		}else if(event.text.equals("enable")){
			btnRadioSeekFoward.setEnabled(true);
			btnRadioSeekBackward.setEnabled(true);
		}else {
			if (smallradio.getVisibility() != View.VISIBLE) {
				smallradio.setVisibility(View.VISIBLE);
				smallmusic.setVisibility(View.GONE);
				smallbluemusic.setVisibility(View.GONE);
			}
			if (event.text.contains(MainActivity.getContext().getString(R.string.ing))) {
				radiotext.setText(event.text);
			}
			else {
				radiotext.setText("FM " + event.text);
			}
			//smallradio.setVisibility(View.VISIBLE);
		}
	}

	public void onPlayMusic(PlayMusicEvent event) {
		if (event.type == PlayMusicEvent.MUSIC_PAUSE) {
			if (isActive) {
				middle.setImageResource(R.drawable.home_small_music_play_selector);
			}
			else {
				smallmusic.setVisibility(View.INVISIBLE);
			}
			
		}
		else if (event.type == PlayMusicEvent.MUSIC_PLAY) {
			smallmusic.setVisibility(View.VISIBLE);
			smallradio.setVisibility(View.GONE);
			middle.setImageResource(R.drawable.home_small_music_pause_selector);
		}
	}
	
	public enum CHARGE_STATUS {UNKNOWN_MSG,CHARGEGUN_PLUGED,CHARGEGUN_UNPLUGED, CHARGE_START, CHARGE_STOP, CHARGE_ERROR};
	private CHARGE_STATUS mChargeStatus = CHARGE_STATUS.CHARGE_STOP;
	
//	public void ChargeInsterEvent(
//			com.kandi.event.DRI_INSERT_CHARGEREvent event){
//		
//		
//		Intent intent = new Intent(MainActivity.getContext(), ChargeGunActivity.class);
//
//	    // *  充电插入    **  key+2       **  0:拔出；1:插入               **  boolean                       *
//		final String skeyChargeGun="KD_CAST_EVENT2";
//		
//		Bundle bundle = event.bundle;
//		boolean isGunPlugedIn = bundle.getBoolean(skeyChargeGun);   
//		Log.d("Charge", "ChargeInsterEvent isGunPlugedIn:"+isGunPlugedIn);
//		EcocEnergyInfoDriver model = DriverServiceManger.getInstance().getEcocEnergyInfoDriver();
//		if(model != null) {
//			model.setChargeGunState(isGunPlugedIn?1:0);
//		}
//
//		if(isGunPlugedIn) {
//			charingtxtarea.fireChargeInsert();
//			setChargeStatus(CHARGE_STATUS.CHARGEGUN_PLUGED);
//			refreshChargeStatus();
////			ToastUtil.showToast(getActivity(), "充电枪插入",Toast.LENGTH_LONG);
//			intent.putExtra("chargetype", "start");		//显示插入充电枪动画
//		}
//		else {
//			charingtxtarea.fireChargePull();
//			setChargeStatus(CHARGE_STATUS.CHARGEGUN_UNPLUGED);
//			refreshChargeStatus();
//			//ToastUtil.showToast(getApplicationContext(), "充电枪拔出",Toast.LENGTH_LONG);
//			intent.putExtra("chargetype", "end");		//显示拔出充电枪动画
//
//			//当充电枪拔出时充电界面关闭
//			try {
//				EventBus.getDefault().postSticky(new FinishChargingEvent());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//		}
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(intent);
//	}
//	
//	public void ChargeOnOffEvent(
//			com.kandi.event.DRI_CHARGER_ONOFFEvent event){
//
//	    // *  充电起停    **  key+3       **  0:停止；1:启动 2:故障停止    **  int                           *
//		final String skeyChargeState="KD_CAST_EVENT3";
//
//		Bundle bundle = event.bundle;
//		int state = bundle.getInt(skeyChargeState);   
//		
//		EcocEnergyInfoDriver model = DriverServiceManger.getInstance().getEcocEnergyInfoDriver();
//		if(model != null) {
//			model.setChargingState(state);
//		}
//
//		switch(state) {
//		case 0:	//停止充电
//			charingtxtarea.fireChargeStop();
//			setChargeStatus(CHARGE_STATUS.CHARGE_STOP);
//			refreshChargeStatus();
////			ToastUtil.showToast(getActivity(), "停止充电",Toast.LENGTH_LONG);
//			break;
//		case 1:	//启动充电
////			Intent intent = new Intent(this, ChargingActivity.class);
////			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
////			intent.putExtra("chargetype", "start");		
////			startActivity(intent);
//			
//			charingtxtarea.fireChargeStart();
//			setChargeStatus(CHARGE_STATUS.CHARGE_START);
//			refreshChargeStatus();
////			ToastUtil.showToast(getActivity(), "启动充电",Toast.LENGTH_LONG);
//			break;
//		case 2:	//充电故障
//			charingtxtarea.fireChargeStop();
//			setChargeStatus(CHARGE_STATUS.CHARGE_ERROR);
//			refreshChargeStatus();
////			ToastUtil.showToast(getActivity(), "充电异常",Toast.LENGTH_LONG);
//			break;
//		default: //unknown charge message
//			setChargeStatus(CHARGE_STATUS.UNKNOWN_MSG);
////			ToastUtil.showToast(getActivity(), "未知充电事件",Toast.LENGTH_LONG);
//		}
//		
//		//TODO: 通知充电UI（CharingActivity）更新状态
//
////		try {
////			EventBus.getDefault().postSticky(
////					new com.wizinno.kandi.event.FinishChargeGunEvent());
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
//		
////		Intent intent = new Intent(this, ChargeGunActivity.class);
////		intent.putExtra("chargetype", "end");
////		startActivity(intent);
//	}
	
//	private void setChargeStatus(CHARGE_STATUS state) {
//		mChargeStatus = state;
//		refreshChargeStatus();
//
//	}
	
//	private void refreshChargeStatus() {
//		switch(mChargeStatus) {
//		case CHARGEGUN_PLUGED:
//			this.charging_state.setTextColor(Color.WHITE);
//			this.charging_state.setText(MainActivity.getContext().getString(R.string.charggun_in));
//			this.charging_state.setVisibility(View.VISIBLE);
//			break;
//			
//		case CHARGEGUN_UNPLUGED:
//			this.charging_state.setTextColor(Color.WHITE);
//			this.charging_state.setText(MainActivity.getContext().getString(R.string.charggun_out));
//			this.charging_state.setVisibility(View.VISIBLE);
//			break;
//
//		case CHARGE_START:
//			this.charging_state.setTextColor(Color.WHITE);
//			this.charging_state.setText(MainActivity.getContext().getString(R.string.charging));
//			this.charging_state.setVisibility(View.VISIBLE);
//			break;
//		
//		case CHARGE_STOP:
//			this.charging_state.setTextColor(Color.WHITE);
//			this.charging_state.setText(MainActivity.getContext().getString(R.string.charg_stoped));
//			this.charging_state.setVisibility(View.VISIBLE);
//			break;
//		
//		case CHARGE_ERROR:
//			this.charging_state.setTextColor(Color.RED);
//			this.charging_state.setText(MainActivity.getContext().getString(R.string.charg_error));
//			this.charging_state.setVisibility(View.VISIBLE);
//			break;
//		
//		case UNKNOWN_MSG:
//			this.charging_state.setTextColor(Color.RED);
//			this.charging_state.setText(MainActivity.getContext().getString(R.string.charg_unknow));
//			this.charging_state.setVisibility(View.VISIBLE);
//			break;
//		}
//		
//		if(mChargeStatus != CHARGE_STATUS.CHARGE_START ) {
//			handler.postDelayed(new Runnable() {
//				@Override
//				public void run() {
//						if(mChargeStatus != CHARGE_STATUS.CHARGE_START ) {
//							charging_state.setVisibility(View.GONE);
//						}
//					}
//			}, 5000);
//		}
//
//	}
	
	private static final String UPDATE_WEATHER="yunos.weather.actioin.update";
	private static final String QUERY_WEATHER = "yunos.weather.action.query";
	private BroadcastReceiverHelper mBroadcastReceiverHelper;
	
	Map<String, String> lMap = new HashMap<String, String>();
    
    public class BroadcastReceiverHelper extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction()
                    .equals(UPDATE_WEATHER)) {
                    Bundle   bundle =   intent.getExtras();
                    if(bundle!=null){
                    	lMap= (Map<String, String>) bundle.getSerializable("allDayWeatherEntity");
               updateWeather(lMap);

            }
        }
    }

    }
	
    private void updateWeather(Map<String, String> lMap) {
    	weatherHandler.sendEmptyMessage(101);
    }
    
	private void registBroadCastReceiver() {
        if (null == mBroadcastReceiverHelper) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(UPDATE_WEATHER);
            mBroadcastReceiverHelper = new BroadcastReceiverHelper();
            getActivity().registerReceiver(mBroadcastReceiverHelper, filter);
        }
    }

    private void unregistBroadCastReceiver() {
        if (null != mBroadcastReceiverHelper) {
        	getActivity().unregisterReceiver(mBroadcastReceiverHelper);
            mBroadcastReceiverHelper = null;
        }
    }
	
}
