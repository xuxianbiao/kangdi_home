package com.kandi.view;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.tsz.afinal.utils.SharedPreferencesUtils;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.kandi.application.BaseApplication;
import com.kandi.base.BaseActivity;
import com.kandi.dao.FMFavDao;
import com.kandi.driver.radio.Configs;
import com.kandi.driver.radio.RadioDriverAdapter;
import com.kandi.event.DRI_INSERT_CHARGEREvent;
import com.kandi.event.FinishRadioEvent;
import com.kandi.event.PlayFmBlueEvent;
import com.kandi.event.PlayFmEvent;
import com.kandi.event.PlayMusicEvent;
import com.kandi.event.PlayVideoEvent;
import com.kandi.event.SmallRadioEvent;
import com.kandi.event.base.BaseEvent;
import com.kandi.home.R;
import com.kandi.model.FMFavModel;
import com.kandi.model.PresistentData;
import com.kandi.util.CommonUtils;
import com.kandi.widget.ImageStateButton;

import de.greenrobot.event.EventBus;

public class RadioActivity extends BaseActivity  implements RadioDriverAdapter.IRadioListener {
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
//		radioDrv.setRadioListener(null);
		super.onPause();
		System.gc();
	}
	@Override
	protected void onResume() {
		registBroadCastReceiver();
		BaseApplication.getInstance().setWheelchoose(true);
		int status = radioDrv.isPowerOn()?1:0;
		Log.d("RadioActivity","RadioDriver.GetLocalRadioStatus()="+status);

		if(radioDrv.getMode()!=0) {
			radioDrv.setMode(0);	//调用.setMode() 原先播放的FM会停止播放
		}
		fRadioFeqMax = radioDrv.getRadioMaxFreq();
		fRadioFeqMin = radioDrv.getRadioMinFreq();
		
		switch(status) {
		case -1:
//			Toast.makeText(RadioActivity.this, "未找到收音机模块", Toast.LENGTH_SHORT).show();
//			setRadioPannelEnabled(false, false);
//			break;
		case 0:
			setRadioPannelEnabled(true, false);
			break;
		case 1:
			setRadioPannelEnabled(true, true);
			break;
			
		}
		
		///int status = radioDriver.GetLocalRadioStatus();

		switch(radioDrv.getSeekingState()) {
		case 1:
			btnSeekUp.switchOn(true, false);
			btnSeekDown.switchOn(false, false);
			textViewSeeking.setVisibility(View.VISIBLE);
			break;
		case 2:
			btnSeekUp.switchOn(false, false);
			btnSeekDown.switchOn(true, false);
			textViewSeeking.setVisibility(View.VISIBLE);
			break;
		case 0:
		default:
			//btnSeekUp.switchOn(false, false);
			//btnSeekDown.switchOn(false, false);
			textViewSeeking.setVisibility(View.INVISIBLE);
		}
			
		refreshFeqDisplay();
		radioDrv.setRadioListener(this);
		
		
		super.onResume();
	}
	
	@Override
	   public void onDestroy() {
		unregistBroadCastReceiver();
		EventBus.getDefault().unregister(this,FinishRadioEvent.class);
		EventBus.getDefault().unregister(this,PlayVideoEvent.class);
		EventBus.getDefault().unregister(this,PlayMusicEvent.class);
		EventBus.getDefault().unregister(this,PlayFmBlueEvent.class);
//		EventBus.getDefault().unregister(this,PlayFmEvent.class);
        super.onDestroy();
	}
	
	final static int MAX_SAVE_FREQ = 8;
	DecimalFormat freqStrFormat=new DecimalFormat(".0");

	private RadioDriverAdapter radioDrv;

	private ToggleButton btnRadioPower;
	private ImageStateButton btnSeekUp;
	private ImageStateButton btnSeekDown;
	private TextView textViewFreq;
	private SeekBar seekBarRadioFreq;
	private TextView textViewSeeking;
	
	private boolean isRadioEnabled;
	private double fRadioFeqMax;
	private double fRadioFeqMin;
	private ImageButton favbtn;
	
	private HorizontalScrollView favscrollview;
	private LinearLayout scrolllinear;
	
	
//	private IRadioDriver radioDriver;

	private ImageButton ll_fm_banzi;
	private ImageButton ib_fm_close;
	private void initScrollView(){
		scrolllinear.removeAllViews();
		List<FMFavModel> list = FMFavDao.getAllFav(this);
		for(int i =0;i<list.size();i++){
			FMFavModel ffm = list.get(i);
			LayoutInflater inflater = this.getLayoutInflater();
	        View rowView = inflater.inflate(R.layout.radio_fav_item, null);
	        TextView favfreqTv = (TextView) rowView.findViewById(R.id.favfreq);
	        favfreqTv.setText(ffm.getUrl()+"");
	        rowView.setTag(ffm.getUrl()+"");
	        
	        rowView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					///radioDriver.SetRadioFreq(Float.parseFloat(arg0.getTag()+"") );
					float freq = Float.parseFloat(arg0.getTag()+"") ;
					setFreq(freq);
					saveFreq(freq);
					refreshFeqDisplay();
				}
			});
	        rowView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View arg0) {
					final String freq = arg0.getTag()+"";
					List<FMFavModel> ffmlist = FMFavDao.findFavByUrl(RadioActivity.this, freq+"");
					if(ffmlist.size()==0){
						return false;
					}
					
					final AlertDialog cameralockDialog = new AlertDialog.Builder(RadioActivity.this).create();
					
					cameralockDialog.show();  
					cameralockDialog.getWindow().setContentView(R.layout.radioalert_layout);  
					cameralockDialog.getWindow()  
		            .findViewById(R.id.deletebtn).setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							FMFavModel fmfm = FMFavDao.findFavByUrl(RadioActivity.this, freq+"").get(0);
							FMFavDao.deleteFavFmById(RadioActivity.this, fmfm.getId());
							if(radioDrv.getFreq() == Float.parseFloat(fmfm.getUrl())){
								favbtn.setImageResource(R.drawable.fm_like_off);
							}
							initScrollView();
							cameralockDialog.dismiss();
						}
					});
					cameralockDialog.getWindow()  
		            .findViewById(R.id.cancelbtn).setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							cameralockDialog.dismiss();
						}
					});
					return true;
				}
			});
	        scrolllinear.addView(rowView);
		}
		
	}
	private void initEvent(){
		EventBus.getDefault().register(this,"FinishRadioEvent",FinishRadioEvent.class);
		
		EventBus.getDefault().register(this, "onPlayVideo",
				PlayVideoEvent.class);
		EventBus.getDefault().register(this, "onPlayMusic",
				PlayMusicEvent.class);
		EventBus.getDefault().register(this, "onPlayBlueMusic",
				PlayFmBlueEvent.class);
//		EventBus.getDefault().register(this, "onPlayFm",
//				PlayFmEvent.class);
	}
	
	public void onPlayBlueMusic(PlayFmBlueEvent event) {
		if(event.type==PlayFmBlueEvent.BLUE_MUSIC_STATE){
			if (btnRadioPower.isChecked()) {
				handler.sendEmptyMessage(112);
			}
		}
	}
	
	public void onPlayMusic(PlayMusicEvent event) {
		if(event.type==PlayMusicEvent.MUSIC_PLAY){
			if (btnRadioPower.isChecked()) {
				handler.sendEmptyMessage(112);
			}
		}
	}
	
	public void onPlayVideo(PlayVideoEvent event) {
		if (btnRadioPower.isChecked()) {
			handler.sendEmptyMessage(101);
		}
	}
	
//	public void onPlayFm(PlayFmEvent event) {
//		if (event.type == PlayFmEvent.FM_TYPE_LEFT) {
//			onClickBack();
//		}
//		else if (event.type == PlayFmEvent.FM_TYPE_LONG_LEFT) {
//			onLongClickBack();
//		}
//		else if (event.type == PlayFmEvent.FM_TYPE_RIGHT) {
//			onClickForward();
//		}
//		else if (event.type == PlayFmEvent.FM_TYPE_LONG_RIGHT) {
//			onLongClickForward();
//		}
//	}
	
	private void postFmInfoToMainPage(String info) {
		EventBus.getDefault().post(new SmallRadioEvent(info));
	}
	
	private void onLongClickForward() {
		if(radioDrv.isAllowSeekStep()) {
			doFreqSeekUp();
		}
	}
	private void onClickForward() {
		if(radioDrv.isAllowSeekStep()) {
			doFreqStepUp();
		}
		else {
			doFreqSeekUp();
		}
	}
	private void onLongClickBack() {
		if(radioDrv.isAllowSeekStep()) {
			doFreqSeekDown();
		}
	}
	private void onClickBack() {
		if(radioDrv.isAllowSeekStep()) {
			doFreqStepDown();
		}
		else {
			doFreqSeekDown();
		}
	}

	private OnCheckedChangeListener onCheckListener = new OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
     	
    		Log.d("RadioActivity","btnRadioPower isChecked="+isChecked);
			if(!CommonUtils.isFastDoubleClick(350)){
				if(isChecked) {
					//if(!radioDrv.isPowerOn()){
					//	openRadio();
					//}
					//openRadio();
					getDefaultFreq();
					handler.sendEmptyMessage(102);
					
					buttomEnable(true);
				}
				else {
					//closeRadio();
					if(radioDrv.isPowerOn()){
						handler.sendEmptyMessage(101);
					}
					buttomEnable(false);
				}
			}else{
				return;
			}
        }
    };

    private void openRadio(){
		///int status = radioDriver.OpenLocalRadio();
    	int status;
    	if(radioDrv.isPowerOn()) {
    		status = 1;
    		
    		//TODO: radio 播放中App重启， radio driver无法正确获频率 
    		if(radioDrv.getFreq() <0) {
    			radioDrv.powerOff();
    			radioDrv.powerOn();
    		}
    		
    	}
    	else {
    		float freq=PresistentData.getInstance().getLastPlayedRadioFmFreq();
    		if(freq<0) {
    			freq = (float)(fRadioFeqMin+fRadioFeqMax)/2f;
    		}
    		status = radioDrv.powerOn();
    		int flag = radioDrv.setFreq(freq);
    	}
    	
    	if(status == -1) {
    		//Toast.makeText(RadioActivity.this, "未找到收音机模块", Toast.LENGTH_SHORT).show();
    		setRadioPannelEnabled(false, false);
    	}
    	else if(status==0){
    		//Toast.makeText(RadioActivity.this, "收音机模块启动失败", Toast.LENGTH_SHORT).show();
    		setRadioPannelEnabled(true, false);
    	}
    	else {
    		setRadioPannelEnabled(true, true);
    		radioDrv.setSignalLevel(LEVEL);
    		//打开收音机时停止播放音乐
    		EventBus.getDefault().post(new PlayFmEvent());
    		if(mAudioManager.requestAudioFocus(afFMChangeListener, 14,
    				AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
        	}
    	}
    	Log.d("RadioActivity","RadioDriver.OpenLocalRadio()="+status);
    	
    	try{
    		postFmInfoToMainPage(textViewFreq.getText()+"");
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    private void closeRadio(){
		radioDrv.powerOff();
		if(!isNowChecked){
			abandonAudioFocus(afFMChangeListener);
		}
		setRadioPannelEnabled(true, false);
		Log.d("RadioActivity","RadioDriver.CloseLocalRadio()");
		textViewSeeking.setVisibility(View.INVISIBLE);
		try{
			postFmInfoToMainPage("hide");
		}catch(Exception e){
			e.printStackTrace();
		}
    }
    
	private void doFreqStepUp() {
		if(btnSeekUp.isSwitchOn()) {
			//如果搜台中则停止搜台
			textViewSeeking.setVisibility(View.INVISIBLE);
			btnSeekUp.switchOn(false, false);
			radioDrv.stopSeeking();
			return;
		}

		float freq=radioDrv.getFreq();
		setFreq(freq+0.1f);
		refreshFeqDisplay();
	}
	
	private void doFreqStepDown() {
		if(btnSeekDown.isSwitchOn()) {
			//如果搜台中则停止搜台
			textViewSeeking.setVisibility(View.INVISIBLE);
			btnSeekDown.switchOn(false, false);
			radioDrv.stopSeeking();
			return;
		}
		
		float freq=radioDrv.getFreq();
		setFreq(freq-0.1f);
		refreshFeqDisplay();
	}
	
	private void doFreqSeekUp() {
		if(btnSeekDown.isSwitchOn()) {
			//如果搜台中则停止搜台
			btnSeekDown.switchOn(false, false);
			radioDrv.stopSeeking();
		}
		
		if(btnSeekUp.isSwitchOn()) {
			//如果搜台中则停止搜台
			textViewSeeking.setVisibility(View.INVISIBLE);
			btnSeekUp.switchOn(false, false);
			radioDrv.stopSeeking();
		}
		else {
			textViewSeeking.setVisibility(View.VISIBLE);
			btnSeekUp.switchOn(true, false);
			radioDrv.startSeekingUp();
		}
	}
	
	private void doFreqSeekDown() {
		if(btnSeekUp.isSwitchOn()) {
			//如果搜台中则停止搜台
			btnSeekUp.switchOn(false, false);
			radioDrv.stopSeeking();
		}

		if(btnSeekDown.isSwitchOn()) {
			//如果搜台中则停止搜台
			textViewSeeking.setVisibility(View.INVISIBLE);
			btnSeekDown.switchOn(false, false);
			radioDrv.stopSeeking();
		}
		else {
			textViewSeeking.setVisibility(View.VISIBLE);
			btnSeekDown.switchOn(true, false);
			radioDrv.startSeekingDown();
		}
	}
	
	private SharedPreferences sp;
	private Editor editor;
	private ImageButton ib_fm_close_icon;
	private AudioManager mAudioManager;
	public static int LEVEL = 8;//收音机收台音效等级
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.m_radio_layout);		
		this.initEvent();
		BaseApplication ba = (BaseApplication) this.getApplication();
		sp=getSharedPreferences("aaa", MODE_WORLD_WRITEABLE);
		editor=sp.edit();
		///radioDriver = new RadioDriverAdapter();
		
		///radioDrv = new RadioDriverAdapter();
		radioDrv = RadioDriverAdapter.getInstance();
		
		scrolllinear = (LinearLayout) findViewById(R.id.scrolllinear);
		favscrollview = (HorizontalScrollView) findViewById(R.id.favscrollview);
		ll_fm_banzi = (ImageButton)findViewById(R.id.ll_fm_banzi);
		ib_fm_close_icon = (ImageButton)findViewById(R.id.ib_fm_close_icon);
		ib_fm_close = (ImageButton)findViewById(R.id.ib_fm_close);
		ll_fm_banzi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				editor.putInt("co", 1);
				editor.commit();
				ll_fm_banzi.setVisibility(View.INVISIBLE);				
				ib_fm_close_icon.setVisibility(View.VISIBLE);
			}
		});
		ib_fm_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				editor.putInt("co", 0);
				editor.commit();
				ib_fm_close_icon.setVisibility(View.INVISIBLE);				
				ll_fm_banzi.setVisibility(View.VISIBLE);
			}
		});
		if(sp!=null)
		{
		if(sp.getInt("co", 1)==1)
		{
			
			ll_fm_banzi.setVisibility(View.INVISIBLE);
			ib_fm_close_icon.setVisibility(View.VISIBLE);
		}
		if(sp.getInt("co", 1)==0)
		{
			ib_fm_close_icon.setVisibility(View.INVISIBLE);		
			ll_fm_banzi.setVisibility(View.VISIBLE);
		}
		}
		this.initScrollView();
		
		View vTextDemoMode = findViewById(R.id.textDemoMode);
		//* demo mode, the driver has bug, not able to get correct -1 status if the hw is not installed.
		if(radioDrv.isDummy()) {
			//Toast.makeText(RadioActivity.this, "未找到收音机模块,切换为演示模式!!!", Toast.LENGTH_SHORT).show();
			vTextDemoMode.setVisibility(android.view.View.VISIBLE);
		}
		else {
			vTextDemoMode.setVisibility(android.view.View.GONE);
		}

		
		
		favbtn = (ImageButton) findViewById(R.id.favbtn);		
		favbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				List<FMFavModel> list = FMFavDao.getAllFav(RadioActivity.this);
				float freq = radioDrv.getFreq();
				for(FMFavModel f:list){
					if(freq == Float.parseFloat(f.getUrl())){
						return;
					}
				}
				if(list.size()>= MAX_SAVE_FREQ ){
					final AlertDialog fmDialog = new AlertDialog.Builder(RadioActivity.this).create();
					
					fmDialog.show();  
					fmDialog.getWindow().setContentView(R.layout.fmalertdialog);  
					fmDialog.getWindow()  
		            .findViewById(R.id.confirmbtn).setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							fmDialog.dismiss();
						}
					});
					
					
//					new AlertDialog.Builder(RadioActivity.this)   
//					.setTitle("提示")
//					.setMessage("收藏电台数量已达到上限，请删除部分收藏项！")
//					.setNegativeButton("确定", null)
//					.show();
					return;
				}
				///float freq = radioDriver.GetRadioFreq();
				String freqStr=freqStrFormat.format(freq);
				List<FMFavModel> ffmlist = FMFavDao.findFavByUrl(RadioActivity.this, freqStr);
				if(ffmlist.size()>0){
					return;
				}
				FMFavModel ffm = new FMFavModel();
				ffm.setAddtime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				
				ffm.setTitle(freqStr);
				ffm.setUrl(freqStr);
				FMFavDao.addFav(RadioActivity.this, ffm);
				favbtn.setImageResource(R.drawable.fm_like_on);
				initScrollView();
			}
		});
//		favbtn.setOnLongClickListener(new View.OnLongClickListener() {
//			@Override
//			public boolean onLongClick(View arg0) {
//				float freq = radioDriver.GetRadioFreq();
//				List<FMFavModel> ffmlist = FMFavDao.findFavByUrl(RadioActivity.this, freq+"");
//				if(ffmlist.size()==0){
//					return false;
//				}
//				FMFavModel fmfm = FMFavDao.findFavByUrl(RadioActivity.this, freq+"").get(0);
//				FMFavDao.deleteFavFmById(RadioActivity.this, fmfm.getId());
//				favbtn.setImageResource(R.drawable.fm_like_off);
//				return true;
//			}
//		});

		seekBarRadioFreq = (SeekBar) findViewById(R.id.seekBar_radio_freq);
		seekBarRadioFreq.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				Log.d("RadioActivity", "seekBarRadioFreq -> onStartTrackingTouch()="+seekBar.getProgress());
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if(fromUser) { //is tracking by user.
					double freq = (progress*(fRadioFeqMax-fRadioFeqMin)/(double)(seekBar.getMax())) + fRadioFeqMin;
					textViewFreq.setText(freqStrFormat.format(freq));
					setFreq((float)freq);
					postFmInfoToMainPage(textViewFreq.getText() + "");
					Message msg = new Message();
					msg.what = 1001;
					msg.arg1 = (int)(freq*10);
					handler.sendMessageDelayed(msg, 200);
					Log.d("RadioActivity", "seekBarRadioFreq -> onProgressChanged()="+seekBar.getProgress()+",freq="+freq);
				}
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				double freq = (seekBar.getProgress()*(fRadioFeqMax-fRadioFeqMin)/(double)(seekBar.getMax())) + fRadioFeqMin;
				///radioDriver.SetRadioFreq((float)freq);
				setFreq((float)freq);
				Log.d("RadioActivity", "seekBarRadioFreq -> onStopTrackingTouch()="+seekBar.getProgress()+",freq="+freq);
				refreshFeqDisplay();
				saveFreq((float)freq);
			}
		});
		mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		btnRadioPower = (ToggleButton) findViewById(R.id.btnRadioPower);
		btnRadioPower.setOnCheckedChangeListener(onCheckListener);

		btnSeekUp =new ImageStateButton(getResources(), (ImageButton)findViewById(R.id.btnRadioSeekFoward), 
				R.drawable.fm_ctrl_next_r_off_normal, 
				R.drawable.fm_ctrl_next_r_on_normal,
				R.drawable.fm_ctrl_next_r_off_pressed,
				R.drawable.fm_ctrl_next_r_on_pressed, 
				R.drawable.fm_ctrl_next_r_off_disabled,
				R.drawable.fm_ctrl_next_r_on_disabled, 
				false);
				
				
		btnSeekUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				handler.sendEmptyMessageDelayed(1001,200);
				onClickForward();
				buttomEnable(false);
			}
		});
		
		btnSeekUp.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View arg0) {
				onLongClickForward();
				return true;
			}
		});
		btnSeekDown =new ImageStateButton(getResources(), (ImageButton)findViewById(R.id.btnRadioSeekBackward), 
				R.drawable.fm_ctrl_next_l_off_normal, 
				R.drawable.fm_ctrl_next_l_on_normal,
				R.drawable.fm_ctrl_next_l_off_pressed,
				R.drawable.fm_ctrl_next_l_on_pressed, 
				R.drawable.fm_ctrl_next_l_off_disabled,
				R.drawable.fm_ctrl_next_l_on_disabled, 
				false);
		
		btnSeekDown.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				handler.sendEmptyMessageDelayed(1002,200);
				onClickBack();
				buttomEnable(false);
			}
		});
		
		btnSeekDown.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View arg0) {
				onLongClickBack();
				return true;
			}
		});

		textViewFreq = (TextView) findViewById(R.id.freq);

		textViewSeeking = (TextView) findViewById(R.id.textViewSeeking);

		
//		int status = RadioDriver.GetLocalRadioStatus();
//		Log.d("RadioActivity","RadioDriver.GetLocalRadioStatus()="+status);
//		
//		if(status == -1) {
//			Toast.makeText(RadioActivity.this, "未找到收音机模块", Toast.LENGTH_SHORT).show();
//			setRadioPannelEnabled(false);
//		}
//		else if(status==0){
//			Toast.makeText(RadioActivity.this, "收音机模块启动失败", Toast.LENGTH_SHORT).show();
//			setRadioPannelEnabled(false);
//		}
//		else {
//			setRadioPannelEnabled(true);
//		}		
	}
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 100:
				openRadio();
				setFreq(msg.getData().getFloat("freq"));
				refreshFeqDisplay();
				break;
			case 101:
				closeRadio();
				break;
			case 102:
				openRadio();
				break;
			case 103:
				openRadio();
				//doFreqStepDown();
				onClickBack();
				break;
			case 104:
				openRadio();
				//doFreqStepUp();
				onClickForward();
				break;
			case 1001:	//指针频道设置
				setFreq((float)msg.arg1/10);
				break;
			case 112:
				closeRadio();
				break;
			default:
				break;
			}
		}
		
	};
    
	public void FinishRadioEvent(
			FinishRadioEvent event) {
		finish();
		//overridePendingTransition(R.anim.stay, R.anim.slide_out_up);
	}

	void setRadioPannelEnabled(boolean isRadioEnabled, boolean isRadioOn) {
		
		this.isRadioEnabled = isRadioEnabled;
		btnRadioPower.setEnabled(isRadioEnabled);
		btnRadioPower.setChecked(isRadioOn);
		btnSeekUp.setEnabled(isRadioOn);
		btnSeekDown.setEnabled(isRadioOn);
		favbtn.setEnabled(isRadioOn);
		this.seekBarRadioFreq.setEnabled(isRadioOn);
		this.refreshFeqDisplay();
	}
	
	void refreshFeqDisplay() {
		///float freq = radioDriver.GetRadioFreq();
		float freq = radioDrv.getFreq();
		 
		int prg=0;
		if((freq >= fRadioFeqMin)&&(freq <= fRadioFeqMax)) {
			String strFreq = freqStrFormat.format(freq);
			textViewFreq.setText(strFreq);
			postFmInfoToMainPage(textViewFreq.getText() + "");
			List<FMFavModel> ffmlist = FMFavDao.findFavByUrl(RadioActivity.this, strFreq);
			if(ffmlist.size()>0){
				favbtn.setImageResource(R.drawable.fm_like_on);
			}else{
				favbtn.setImageResource(R.drawable.fm_like_off);
			}
			prg = (int)((freq-this.fRadioFeqMin)/(this.fRadioFeqMax-this.fRadioFeqMin)*seekBarRadioFreq.getMax()+0.1);
			seekBarRadioFreq.setProgress(prg);
		}
		else {
			textViewFreq.setText("");
			if(freq < 0) {
				prg = seekBarRadioFreq.getMax()/2;
			}
			if(freq < fRadioFeqMin) {
				prg = 0;
			}
			else if(freq > fRadioFeqMax) {
				prg = seekBarRadioFreq.getMax();
			}
			seekBarRadioFreq.setProgress(prg);
		}
		Log.d("RadioActivity","refreshFeqDisplay() -- RadioDriver.GetRadioFreq()="+freq+",prg="+prg);
	}
	@Override
	public void OnFreqSeeking(float freq) {
		textViewSeeking.setVisibility(View.VISIBLE);
		postFmInfoToMainPage(getString(R.string.searching));
		textViewFreq.setText(freqStrFormat.format(freq));
		int prg = (int)((freq-this.fRadioFeqMin)/(this.fRadioFeqMax-this.fRadioFeqMin)*seekBarRadioFreq.getMax()+0.5);
		seekBarRadioFreq.setProgress(prg);
		
		postFmInfoToMainPage(freq+"");
	}
	@Override
	public void OnFreqSeeked(float freq) {

		textViewSeeking.setVisibility(View.INVISIBLE);
		if(radioDrv.isPowerOn()){
			buttomEnable(true);
		}else{
			btnSeekDown.switchOn(false, false);
			btnSeekUp.switchOn(false, false);
			buttomEnable(false);
		}
		postFmInfoToMainPage("enable");
		int prg=50;
		if(freq >0) {
			textViewFreq.setText(freqStrFormat.format(freq));
			prg = (int)((freq-this.fRadioFeqMin)/(this.fRadioFeqMax-this.fRadioFeqMin)*seekBarRadioFreq.getMax()+0.5);
			seekBarRadioFreq.setProgress(prg);
			PresistentData.getInstance().setLastPlayedRadioFmFreq(freq);
			saveFreq(freq);
		}
		else {
			textViewFreq.setText("");
			prg = seekBarRadioFreq.getMax()/2;
			seekBarRadioFreq.setProgress(prg);
		}
		
		if(btnSeekUp.isSwitchOn())
		{
			btnSeekUp.switchOn(false, false);
		}

		if(btnSeekDown.isSwitchOn())
		{
			btnSeekDown.switchOn(false, false);
		}

		//无论搜台成功与否更新频率显示
		refreshFeqDisplay();
		
		Log.d("RadioActivity","OnFreqSeeked() -- RadioDriver.GetRadioFreq()="+freq+",prg="+prg);

	}
	@Override
	public void OnFreqSeekError(int err) {
		textViewSeeking.setVisibility(View.INVISIBLE);

		//Toast.makeText(RadioActivity.this, "FM Seek Error", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void OnFreqSeekGetError(int err) {
		textViewSeeking.setVisibility(View.INVISIBLE);
		buttomEnable(true);
		postFmInfoToMainPage("enable");
		if(btnSeekUp.isSwitchOn())
		{
			btnSeekUp.switchOn(false, false);
		}

		if(btnSeekDown.isSwitchOn())
		{
			btnSeekDown.switchOn(false, false);
		}
	}
	
	public int setFreq(float freq) {
		int flag = radioDrv.setFreq(freq);
		if(flag>0) {
			PresistentData.getInstance().setLastPlayedRadioFmFreq(freq);
		}
		return flag;
	}
	
	private void saveFreq(float freq){
		Editor edt = SharedPreferencesUtils.getEditor(this);
		edt.putString(Configs.FM_NAME, freq+"");
		edt.commit();
	}
	private float getDefaultFreq(){
		float freq=0;
		freq = Float.parseFloat(SharedPreferencesUtils.getSharedPreferences(this).getString(Configs.FM_NAME,Configs.DEFAULT_FM_FREQ));
		int prg=50;

		textViewFreq.setText(freqStrFormat.format(freq));
		prg = (int)((freq-this.fRadioFeqMin)/(this.fRadioFeqMax-this.fRadioFeqMin)*seekBarRadioFreq.getMax()+0.5);
		seekBarRadioFreq.setProgress(prg);
		PresistentData.getInstance().setLastPlayedRadioFmFreq(freq);
		if(SharedPreferencesUtils.getSharedPreferences(this).getBoolean(Configs.FM_INIT,Configs.DEFAULT_FM_INIT)){
			setFreq(freq);
			Editor edt = SharedPreferencesUtils.getEditor(this);
			edt.putBoolean(Configs.FM_INIT, false);
			edt.commit();
		}
		return freq;
	}
	
	private void buttomEnable(boolean isenable){
		if(isenable){
			btnSeekDown.setEnabled(true);
			btnSeekUp.setEnabled(true);
			ib_fm_close.setEnabled(true);
			seekBarRadioFreq.setEnabled(true);
		}else{
			btnSeekDown.setEnabled(false);
			btnSeekUp.setEnabled(false);
			ib_fm_close.setEnabled(false);
			seekBarRadioFreq.setEnabled(false);
		}
	}
	
	public final static String ACTION_WHEEL_MUSIC_PREV = "com.kangdi.BroadCast.WheelMusicPrev";//多功能方向盘音乐上一首
	public final static String ACTION_WHEEL_MUSIC_NEXT = "com.kangdi.BroadCast.WheelMusicNext";//多功能方向盘音乐下一首
	RadioReceiver radioReceiver;
	public class RadioReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ACTION_WHEEL_MUSIC_NEXT)) {
				if(BaseApplication.getInstance().isWheelchoose()){
					if(radioDrv.isPowerOn()){
						onClickForward();
					}else{
						btnRadioPower.setChecked(true);
					}
				}
			}else if (intent.getAction().equals(ACTION_WHEEL_MUSIC_PREV)) {
				if(BaseApplication.getInstance().isWheelchoose()){
					if(radioDrv.isPowerOn()){
						onClickBack();
					}else{
						btnRadioPower.setChecked(true);
					}
				}
			}
		}
	}
	
	private void registBroadCastReceiver() {
        if (null == radioReceiver) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_WHEEL_MUSIC_NEXT);
            filter.addAction(ACTION_WHEEL_MUSIC_PREV);
            radioReceiver = new RadioReceiver();
            registerReceiver(radioReceiver, filter);
        }
    }

    private void unregistBroadCastReceiver() {
        if (null != radioReceiver) {
        	unregisterReceiver(radioReceiver);
        	radioReceiver = null;
        }
    }
	
	@Override
	public void CarChargingEvent(com.kandi.event.CarChargingEvent event) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void VolumeClickEvent(com.kandi.event.VolumeClickEvent event) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void DRI_INSERT_CHARGEREvent(
			com.kandi.event.DRI_INSERT_CHARGEREvent event) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void DRI_CHARGER_ONOFFEvent(
			com.kandi.event.DRI_CHARGER_ONOFFEvent event) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void doReceive(Intent intent) {

		final String m_key = "KD_CAST_EVENT";
		Bundle bundle = intent.getExtras();

		// 第一步分拣广播事件并用EventBus转发bundle对象（bundle有事件名称key和值）
		Set<String> keySet = bundle.keySet();
		for (String key : keySet) {
			if (!key.startsWith(m_key))
				continue;

			int eventId;
			try {
				eventId = Integer.parseInt(key.substring(m_key.length()));
			} catch (NumberFormatException e) {
				e.printStackTrace();
				continue;
			}

			if ((eventId >= BaseEvent.DRIEVENT.values().length)
					|| (eventId < 0)) {
				continue;
			}
			BaseEvent.DRIEVENT ev = BaseEvent.DRIEVENT.values()[eventId];

			switch (ev) {
			case DRI_INSERT_CHARGER:
				/** 充电抢插入状态变化事件 */
				try {
					EventBus.getDefault().postSticky(
							new DRI_INSERT_CHARGEREvent(bundle));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	
	}
	
	public void abandonAudioFocus(OnAudioFocusChangeListener listener) {
    	mAudioManager.abandonAudioFocus(listener);
	}
	
	boolean isNowChecked = false;
	/**
	 * FM监听器
	 */
	public OnAudioFocusChangeListener afFMChangeListener = new OnAudioFocusChangeListener() {
		public void onAudioFocusChange(int focusChange) {
			if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
				Log.i("fmFocusChange", "AUDIOFOCUS_LOSS_TRANSIENT");
			} else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
				Log.i("fmFocusChange", "AUDIOFOCUS_GAIN   ="+isNowChecked);
				if(isNowChecked){
					if(!btnRadioPower.isChecked()){
						btnRadioPower.setChecked(true);
					}
				}
			} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
				Log.i("fmFocusChange", "AUDIOFOCUS_LOSS   ="+isNowChecked);
				isNowChecked = false;
				if(btnRadioPower.isChecked()){
					isNowChecked = true;
					btnRadioPower.setChecked(false);
				}
			} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
				Log.i("fmFocusChange", "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK   ="+isNowChecked);
				isNowChecked = false;
				if(btnRadioPower.isChecked()){
					isNowChecked = true;
					btnRadioPower.setChecked(false);
				}
			}

		}
	};
}
