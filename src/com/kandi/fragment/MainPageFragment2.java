package com.kandi.fragment;

import java.util.Set;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.kandi.application.BaseApplication;
import com.kandi.driver.CarSettingDriver;
import com.kandi.driver.CarSettingDriver.eWinDoorActionState;
import com.kandi.driver.CarSettingDriver.eWindow;
import com.kandi.driver.DriverServiceManger;
import com.kandi.event.DRI_CAR_BCM_Event;
import com.kandi.event.base.BaseEvent;
import com.kandi.home.R;
import com.kandi.widget.ImageStateButton;
import com.kandi.widget.PassThroughButton;
import com.util.ToastUtil;

import de.greenrobot.event.EventBus;

public class MainPageFragment2 extends Fragment  implements OnClickListener, OnTouchListener {  
	private View loadingView;

	@Override
	public void onResume() {
		refreshCarSettingViewButtonsStatus();
		super.onResume();
	}

	View rootView;

    public static MainPageFragment2 newInstance(String s) {  
    	MainPageFragment2 newFragment = new MainPageFragment2();  
        Bundle bundle = new Bundle();  
        bundle.putString("param", s);  
        newFragment.setArguments(bundle);  
 
        return newFragment;  
    }  
    
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {  
    	rootView = inflater.inflate(R.layout.main_car_setting_layout, container, false);  
		this.initEvent();
        this.initView();
        return rootView;  
    }
    
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this,DRI_CAR_BCM_Event.class);
	}

	private void initEvent(){
		//初始化EventBus接收方法
		EventBus.getDefault().register(this,"DRI_CAR_BCM_Event",DRI_CAR_BCM_Event.class);
	}

	public void DRI_CAR_BCM_Event(DRI_CAR_BCM_Event event) {
		//接收车辆控制Event，并转发至UI主线程
		Bundle bundle = event.bundle;
		Message message = Message.obtain();
		message.setData(bundle);
		mHandler.sendMessage(message);
	}

	int eventCount=0;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			MainPageFragment2.this.handleEventMessage(msg);
		}
	};
	
	//UI主进程消息处理
	private void handleEventMessage(Message msg) {
		final String m_key="KD_CAST_EVENT";
		Bundle bundle = msg.getData();
		
		//Toast.makeText(context, "BCM-MSG(#"+(eventCount++)+"):"+bundle, Toast.LENGTH_LONG).show();
		if(this.getActivity() != null) {
			ToastUtil.showDbgToast(this.getActivity().getApplicationContext(), "BCM-MSG(#"+(eventCount++)+"):"+bundle);
		}
		
    	Set<String> keySet = bundle.keySet();
	   	for(String key : keySet) {
	   		
	   		if(!key.startsWith(m_key)) continue;
	   		
	   		int eventId;
	   		try {
				eventId = Integer.parseInt(key.substring(m_key.length()));
			} catch (NumberFormatException e) {
				e.printStackTrace();
				continue;
			}
	   		
	   		if((eventId < 0) || (eventId >= BaseEvent.DRIEVENT.values().length)) continue;
        	BaseEvent.DRIEVENT ev = BaseEvent.DRIEVENT.values()[eventId];

        	switch(ev) {

        	case	DRI_CARDOOR	:		/**车门锁状态变化事件*/
	        	{
	        	    //接口定义OLD： *  车门        **  key+6       **  0:锁闭；1:解锁               **  boolean                       *
	        		//boolean isLockOn = ! bundle.getBoolean(key);   
	    	  	    //接口定义NEW： *  车门        **  key+6       **  0:解锁；1:锁闭               **  boolean                       *
	        		boolean isLockOn = bundle.getBoolean(key);   
	        		//if(btnMainCarSettingDoorLock.isSwitchOn() != isLockOn) {
	        			btnMainCarSettingDoorLock.setSwitchState(
	        					isLockOn?ImageStateButton.SWITCH_ON:ImageStateButton.SWITCH_OFF);
	    				effectDoorLock();
	        		//}
	        	}
	    		break;
    		
        	case	DRI_CARWINDOWS	:		/**车窗状态变化事件*/
	        	{
	        	     //*  车窗        **  key+7       **  1:上升；2:下降；3:暂停       **  int[],数组下标表示车窗编号    *
	        		int winState[] = bundle.getIntArray(key);
	        		
	        		//if(winState.length < 2) continue;
	        		
					//Left Window Effect
	        		switch(winState[0]) {
					case 1:
						btnMainCarSettingLeftWinUp.setPressed(true);
						main_car_setting_left_window_icon_up.startAnimation(animWinUp);
						break;
					case 2:
						btnMainCarSettingLeftWinDown.setPressed(true);
						main_car_setting_left_window_icon_down.startAnimation(animWinDown);
						break;
					case 3:
					default:
						btnMainCarSettingLeftWinUp.setPressed(false);
						btnMainCarSettingLeftWinDown.setPressed(false);
						main_car_setting_left_window_icon_up.clearAnimation();
						main_car_setting_left_window_icon_down.clearAnimation();
					}
					
					//Right Window Effect
	        		switch(winState[1]) {
					case 1:
						btnMainCarSettingRightWinUp.setPressed(true);
						main_car_setting_right_window_icon_up.startAnimation(animWinUp);
						break;
					case 2:
						btnMainCarSettingRightWinDown.setPressed(true);
						main_car_setting_right_window_icon_down.startAnimation(animWinDown);
						break;
					case 3:
					default:
						btnMainCarSettingRightWinUp.setPressed(false);
						btnMainCarSettingRightWinDown.setPressed(false);
						main_car_setting_right_window_icon_down.clearAnimation();
						main_car_setting_right_window_icon_up.clearAnimation();
					}
	        		
	        	}
        		break;
        		
        	case	DRI_TRUNKBOOT	:		/**后备箱状态变化事件*/
	        	{
	        	    // *  后备箱      **  key+8       **  0:锁闭；1:解锁               **  boolean                       *
	        		//BUGFIX: 后备箱      **  key+8       **  0:解锁；1:锁闭
	        		boolean isLocked = bundle.getBoolean(key);	//switchon == unlock ?

	        		btnMainCarSettingBackDoorLock.setSwitchState(
	        				isLocked?ImageStateButton.SWITCH_OFF:ImageStateButton.SWITCH_ON);
	        		
	        		effectBackdoorLock(isLocked);
	        	}
	    		break;
	
        	case	DRI_HEADLIGHT	:		/**大灯状态变化事件*/
	        	{
	        	    // *  大灯        **  key+10      **  1:远光灯 ；2:近光灯；3：关闭 **  int                           *
	        		//int switchState0 = mainLightSwitchBtnSet.getOnButton();
	        		switch(bundle.getInt(key)) {
	        		case 3:	//3：关闭
						btnMainCarSettingNearLamp.setSwitchState(ImageStateButton.SWITCH_OFF);
						btnMainCarSettingFarLamp.setSwitchState(ImageStateButton.SWITCH_OFF);
						effectMainLamp();
//	        			if(switchState0 != 0) {
//	    	        		mainLightSwitchBtnSet.switchOnButton(0, false);	//仅设置开关状态，不触发OnClick()事件
//	    	        		effectMainLamp();
//	        			}
	        			break;
	        		case 2:	//2:近光灯
						btnMainCarSettingNearLamp.setSwitchState(ImageStateButton.SWITCH_ON);
						btnMainCarSettingFarLamp.setSwitchState(ImageStateButton.SWITCH_OFF);
						effectMainLamp();
//	        			if(switchState0 != 1) {
//	    	        		mainLightSwitchBtnSet.switchOnButton(1, false);
//	    	        		effectMainLamp();
//	        			}
	        			break;
	        		case 1: //1:远光灯
						btnMainCarSettingNearLamp.setSwitchState(ImageStateButton.SWITCH_OFF);
						btnMainCarSettingFarLamp.setSwitchState(ImageStateButton.SWITCH_ON);
						effectMainLamp();
//	        			if(switchState0 != 2) {
//	    	        		mainLightSwitchBtnSet.switchOnButton(2, false);
//	    	        		effectMainLamp();
//	        			}
	        			break;
	        		}
	        	}
	    		break;
	    		
//        	case	DRI_DOUBLELAMP	:		/**双跳状态变化事件*/
//	        	{
//	        	    // *  双跳        **  key+11      **  0:关闭；1:打开               **  boolean                       *
//	        		boolean isSwitchOn = bundle.getBoolean(key);
////	        		if(btnMainCarSettingFogFrontLamp.isSwitchOn() != isSwitchOn) 
//	        		{
//	        			btnMainCarSettingDoubleLamp.setSwitchState(
//	    					isSwitchOn?ImageStateButton.SWITCH_ON:ImageStateButton.SWITCH_OFF);
//	    				effectDoubleFlashLamp(bundle.getBoolean(key));
//	        		}
//	        	}
//        		break;
        		
        	case	DRI_FORGLAMP	:		/**雾灯状态变化事件*/
	        	/*{
	        	    // *  前雾灯      **  key+12      **  0:关闭；1:打开               **  boolean                       *
	        		boolean isSwitchOn = bundle.getBoolean(key);
//	        		if(btnMainCarSettingFogFrontLamp.isSwitchOn() != isSwitchOn) 
	        		{
	        			btnMainCarSettingFogFrontLamp.setSwitchState(
	    					isSwitchOn?ImageStateButton.SWITCH_ON:ImageStateButton.SWITCH_OFF);
	    				effectFogFrontLamp();
	        		}
	        	}*/
        		break;
        		
        	case	DRI_LITTLELAMP	:		/**小灯状态变化事件*/
	        	{
	        	    // *  小灯        **  key+13      **  0:关闭；1:打开               **  boolean                       *
	        		boolean isSwitchOn = bundle.getBoolean(key);		
//	        		if(btnMainCarSettingPositionLamp.isSwitchOn() != isSwitchOn) 
	        		{
	        			btnMainCarSettingPositionLamp.setSwitchState(
	    					isSwitchOn?ImageStateButton.SWITCH_ON:ImageStateButton.SWITCH_OFF);
	    				effectPositionLamp();
	        		}
	        	}
	    		break;
	    		
        	case	DRI_BACKFOG	:		/**后雾灯状态变化事件*/
	        	{
	        		// *  后雾灯      **  key+16      **  0:关闭；1:打开               **  boolean                       *
	        		boolean isSwitchOn = bundle.getBoolean(key);
//	        		if(btnMainCarSettingFogRearLamp.isSwitchOn() != isSwitchOn) 
	        		{
	    				btnMainCarSettingFogRearLamp.setSwitchState(
	    					isSwitchOn?ImageStateButton.SWITCH_ON:ImageStateButton.SWITCH_OFF);
	    				effectFogRearLamp();
	        		}
	        	}
        		break;
        	case DRI_BCM_ONLINE:		/**BCM状态变化事件*/
	        	{
	        		// *  BCM状态     **  key+18      **  0:表示在线；1:表示离线       **  boolean                       *
	        		boolean flag = bundle.getBoolean(key);
        			this.setLoadingView(flag);
	        	}
	        	break;
	        }

	   	}
	}
	

	//panel 1
	//ImageInterlockSwitchButtonSet mainLightSwitchBtnSet;
	ImageStateButton btnMainCarSettingCloseLamp;
	ImageStateButton btnMainCarSettingNearLamp;
	ImageStateButton btnMainCarSettingFarLamp;

	//panel 2
	ImageStateButton btnMainCarSettingDoubleLamp;
	ImageStateButton btnMainCarSettingPositionLamp;
	ImageStateButton btnMainCarSettingFogRearLamp;
	
	//panel 3
	ImageStateButton btnMainCarSettingBackDoorLock;
	ImageStateButton btnMainCarSettingDoorLock;
	ImageStateButton btnMainCarSettingAllWinOpen;

	//window switches
	PassThroughButton btnMainCarSettingLeftWinDown;
	PassThroughButton btnMainCarSettingLeftWinUp;
	PassThroughButton btnMainCarSettingRightWinDown;
	PassThroughButton btnMainCarSettingRightWinUp;
	
	//car model
	ImageView main_car_setting_left_window_icon_up;
	ImageView main_car_setting_left_window_icon_down;
	ImageView main_car_setting_right_window_icon_up;
	ImageView main_car_setting_right_window_icon_down;
	Animation animWinUp;
	Animation animWinDown;
	
	ImageView main_car_setting_effect_car;
	

	private void setLoadingView(boolean isLoading) {
		if(loadingView.getVisibility() == View.GONE) {
			if (isLoading) {
				loadingView.setVisibility(View.VISIBLE);					
			}
		}
		else {
			if (!isLoading) {
				loadingView.setVisibility(View.GONE);					
			}
		}
	}
	
	public void initView(){
		main_car_setting_effect_car = (ImageView) rootView.findViewById(R.id.main_car_setting_effect_car);
		//美版屏蔽
//		if((SystemProperties.get("ro.product.model")).endsWith("K17")){
//			main_car_setting_effect_car.setImageResource(R.drawable.main_car_setting_effect_car);
//		}else{
//			main_car_setting_effect_car.setImageResource(R.drawable.main_car_setting_effect_car_global);
//		}
		loadingView = (View)rootView.findViewById(R.id.loadingView);
		setLoadingView(false);

		boolean __isSwitchStateByClick = false;
		btnMainCarSettingCloseLamp = new ImageStateButton(this.getResources(), 
				(ImageButton)rootView.findViewById(R.id.btnMainCarSettingCloseLamp),
				R.drawable.main_car_setting_btn_close_lamp_off,
				R.drawable.main_car_setting_btn_close_lamp_off,		
				R.drawable.main_car_setting_btn_close_lamp_off_pressed,
				R.drawable.main_car_setting_btn_close_lamp_off_pressed,	
				R.drawable.main_car_setting_btn_close_lamp_off_disabled,	//disable
				R.drawable.main_car_setting_btn_close_lamp_off_disabled,	//disable
				false
				);
		btnMainCarSettingCloseLamp.setOnClickListener(this);

		btnMainCarSettingNearLamp = new ImageStateButton(this.getResources(), 
				(ImageButton)rootView.findViewById(R.id.btnMainCarSettingNearLamp),
				R.drawable.main_car_setting_btn_near_lamp_off,
				R.drawable.main_car_setting_btn_near_lamp_on,
				R.drawable.main_car_setting_btn_near_lamp_off_pressed,
				R.drawable.main_car_setting_btn_near_lamp_on_pressed,
				R.drawable.main_car_setting_btn_near_lamp_off_disabled,   //disable
				R.drawable.main_car_setting_btn_near_lamp_on_disabled,   //disable
				__isSwitchStateByClick
				);
		btnMainCarSettingNearLamp.setOnClickListener(this);

		btnMainCarSettingFarLamp = new ImageStateButton(this.getResources(), 
				(ImageButton)rootView.findViewById(R.id.btnMainCarSettingFarLamp),
				R.drawable.main_car_setting_btn_far_lamp_off,
				R.drawable.main_car_setting_btn_far_lamp_on,
				R.drawable.main_car_setting_btn_far_lamp_off_pressed,
				R.drawable.main_car_setting_btn_far_lamp_on_pressed,
				R.drawable.main_car_setting_btn_far_lamp_off_disabled,   //disable
				R.drawable.main_car_setting_btn_far_lamp_on_disabled,   //disable
				__isSwitchStateByClick
				);
		btnMainCarSettingFarLamp.setOnClickListener(this);
		//*/
		
		//panel 2
		btnMainCarSettingDoubleLamp = new ImageStateButton(this.getResources(), 
				(ImageButton)rootView.findViewById(R.id.btnMainCarSettingDoubleLamp),
				R.drawable.main_car_setting_btn_fog_front_lamp_off,
				R.drawable.main_car_setting_btn_fog_front_lamp_on,
				R.drawable.main_car_setting_btn_fog_front_lamp_off_pressed,
				R.drawable.main_car_setting_btn_fog_front_lamp_on_pressed,
				R.drawable.main_car_setting_btn_fog_front_lamp_off_disabled_02,   //disable
				R.drawable.main_car_setting_btn_fog_front_lamp_on_disabled,   //disable
				__isSwitchStateByClick
				);
		btnMainCarSettingDoubleLamp.setEnabled(false);
		btnMainCarSettingDoubleLamp.setOnClickListener(this);
		//btnMainCarSettingFogFrontLamp.setOnClickListener(this);
		
		btnMainCarSettingPositionLamp = new ImageStateButton(this.getResources(), 
				(ImageButton)rootView.findViewById(R.id.btnMainCarSettingPositionLamp),
				R.drawable.main_car_setting_btn_position_lamp_off,
				R.drawable.main_car_setting_btn_position_lamp_on,
				R.drawable.main_car_setting_btn_position_lamp_off_pressed,
				R.drawable.main_car_setting_btn_position_lamp_on_pressed,
				R.drawable.main_car_setting_btn_position_lamp_off_disabled,   //disable
				R.drawable.main_car_setting_btn_position_lamp_on_disabled,   //disable
				__isSwitchStateByClick
				);
		btnMainCarSettingPositionLamp.setOnClickListener(this);

		btnMainCarSettingFogRearLamp = new ImageStateButton(this.getResources(), 
				(ImageButton)rootView.findViewById(R.id.btnMainCarSettingFogRearLamp),
				R.drawable.main_car_setting_btn_fog_lamp_off,
				R.drawable.main_car_setting_btn_fog_lamp_on,
				R.drawable.main_car_setting_btn_fog_lamp_off_pressed,
				R.drawable.main_car_setting_btn_fog_lamp_on_pressed,
				R.drawable.main_car_setting_btn_fog_lamp_off_disabled,   //disable
				R.drawable.main_car_setting_btn_fog_lamp_on_disabled,   //disable
				__isSwitchStateByClick
				);
		btnMainCarSettingFogRearLamp.setOnClickListener(this);

		//panel 3
		btnMainCarSettingBackDoorLock =  new ImageStateButton(this.getResources(), 
				(ImageButton)rootView.findViewById(R.id.btnMainCarSettingBackDoorLock),
				R.drawable.main_car_setting_btn_backdoor_lock_off,
				R.drawable.main_car_setting_btn_backdoor_lock_on,
				R.drawable.main_car_setting_btn_backdoor_lock_off_pressed,
				R.drawable.main_car_setting_btn_backdoor_lock_on_pressed,
				R.drawable.main_car_setting_btn_backdoor_lock_off_disabled,
				R.drawable.main_car_setting_btn_backdoor_lock_on_disabled,
				__isSwitchStateByClick
				);
		btnMainCarSettingBackDoorLock.setOnClickListener(this);
		
		btnMainCarSettingDoorLock = new ImageStateButton(this.getResources(), 
				(ImageButton)rootView.findViewById(R.id.btnMainCarSettingDoorLock),
				R.drawable.main_car_setting_btn_door_lock_off,
				R.drawable.main_car_setting_btn_door_lock_on,
				R.drawable.main_car_setting_btn_door_lock_off_pressed,
				R.drawable.main_car_setting_btn_door_lock_on_pressed,
				R.drawable.main_car_setting_btn_door_lock_off_disabled,	//disable
				R.drawable.main_car_setting_btn_door_lock_on_disabled,	//disable
				__isSwitchStateByClick
				);
		btnMainCarSettingDoorLock.setOnClickListener(this);

		btnMainCarSettingAllWinOpen = new ImageStateButton(this.getResources(), 
				(ImageButton)rootView.findViewById(R.id.btnMainCarSettingAllWinOpen),
				R.drawable.main_car_setting_btn_win_opening_off,
				R.drawable.main_car_setting_btn_win_opening_off,				//R.drawable.main_car_setting_btn_win_opening_on,
				R.drawable.main_car_setting_btn_win_opening_off_pressed,
				R.drawable.main_car_setting_btn_win_opening_off_pressed,		//R.drawable.main_car_setting_btn_win_opening_on_pressed,
				R.drawable.main_car_setting_btn_win_opening_off_disabled,
				R.drawable.main_car_setting_btn_win_opening_off_disabled,
				__isSwitchStateByClick
				);
		btnMainCarSettingAllWinOpen.setOnClickListener(this);
	
		//windows switches
		btnMainCarSettingLeftWinUp = (PassThroughButton) rootView.findViewById(R.id.btnMainCarSettingLeftWinUp);
		btnMainCarSettingLeftWinUp.setOnTouchListener(this);
		//btnMainCarSettingLeftWinUp.setOnClickListener(this);
		btnMainCarSettingLeftWinDown = (PassThroughButton) rootView.findViewById(R.id.btnMainCarSettingLeftWinDown);
		btnMainCarSettingLeftWinDown.setOnTouchListener(this);
		//btnMainCarSettingLeftWinDown.setOnClickListener(this);

		btnMainCarSettingRightWinUp = (PassThroughButton) rootView.findViewById(R.id.btnMainCarSettingRightWinUp);
		btnMainCarSettingRightWinUp.setOnTouchListener(this);
		btnMainCarSettingRightWinUp.setOnClickListener(this);
		btnMainCarSettingRightWinDown = (PassThroughButton) rootView.findViewById(R.id.btnMainCarSettingRightWinDown);
		btnMainCarSettingRightWinDown.setOnTouchListener(this);
		btnMainCarSettingRightWinDown.setOnClickListener(this);

		main_car_setting_left_window_icon_up = (ImageView) rootView.findViewById(R.id.main_car_setting_left_window_icon_up);
		main_car_setting_left_window_icon_down = (ImageView) rootView.findViewById(R.id.main_car_setting_left_window_icon_down);
		main_car_setting_right_window_icon_up = (ImageView) rootView.findViewById(R.id.main_car_setting_right_window_icon_up);
		main_car_setting_right_window_icon_down = (ImageView) rootView.findViewById(R.id.main_car_setting_right_window_icon_down);

		animWinUp = AnimationUtils.loadAnimation(this.getActivity(), R.anim.main_car_setting_effect_window_icon_up);
		animWinDown = AnimationUtils.loadAnimation(this.getActivity(), R.anim.main_car_setting_effect_window_icon_down);
		
		main_car_setting_left_window_icon_up.setVisibility(View.INVISIBLE);
		main_car_setting_left_window_icon_down.setVisibility(View.INVISIBLE);
		main_car_setting_right_window_icon_up.setVisibility(View.INVISIBLE);
		main_car_setting_right_window_icon_down.setVisibility(View.INVISIBLE);
		
	}

	public void refreshCarSettingViewButtonsStatus() {
		CarSettingDriver carSettingDrv = DriverServiceManger.getInstance().getCarSettingDriver();

		if(carSettingDrv != null) {
			try {

				setLoadingView(carSettingDrv.retreveCarInfo() != 0);	//BCM掉线
				
				//车门	
				btnMainCarSettingDoorLock.setSwitchState(
						carSettingDrv.isDoorsLocked()?ImageStateButton.SWITCH_ON:ImageStateButton.SWITCH_OFF);
				
				//后备箱
				btnMainCarSettingBackDoorLock.setSwitchState(
						carSettingDrv.isBackDoorOpen()?ImageStateButton.SWITCH_ON:ImageStateButton.SWITCH_OFF);

				//车窗
				switch(carSettingDrv.getWindowAction(eWindow.LEFT)) {
				case OPENING:
					main_car_setting_left_window_icon_down.startAnimation(animWinDown);
					break;
				case CLOSING:
					main_car_setting_left_window_icon_up.startAnimation(animWinUp);
					break;
				case STOPPED:
				default:
					main_car_setting_left_window_icon_down.clearAnimation();
					main_car_setting_left_window_icon_up.clearAnimation();
				}
				
				switch(carSettingDrv.getWindowAction(eWindow.RIGHT)) {
				case OPENING:
					main_car_setting_right_window_icon_down.startAnimation(animWinDown);
					break;
				case CLOSING:
					main_car_setting_right_window_icon_up.startAnimation(animWinUp);
					break;
				case STOPPED:
				default:
					main_car_setting_right_window_icon_down.clearAnimation();
					main_car_setting_right_window_icon_up.clearAnimation();
				}

				//位置灯		0x08	1			0x01:开启/0x02:关闭
				btnMainCarSettingPositionLamp.setSwitchState(
						carSettingDrv.isPositionLightOn()?ImageStateButton.SWITCH_ON:ImageStateButton.SWITCH_OFF);

				//前雾灯						0x01:开启/0x02:关闭
				/*btnMainCarSettingFogFrontLamp.setSwitchState(
						carSettingDrv.isFlashLightOn()?ImageStateButton.SWITCH_ON:ImageStateButton.SWITCH_OFF);*/
				
				//后雾灯		0x07	1			0x01:开启/0x02:关闭
				btnMainCarSettingFogRearLamp.setSwitchState(
						carSettingDrv.isFogLightRearOn()?ImageStateButton.SWITCH_ON:ImageStateButton.SWITCH_OFF);

				//大灯		0x05	1			0x01:远光灯/0x02:近光灯/0x03:关闭
				switch(carSettingDrv.getMainLightState()) {
				case OFF:
					//mainLightSwitchBtnSet.switchOnButton(0, false);
					btnMainCarSettingNearLamp.setSwitchState(ImageStateButton.SWITCH_OFF);
					btnMainCarSettingFarLamp.setSwitchState(ImageStateButton.SWITCH_OFF);
					break;
				case NEAR_LIGHT:
					//mainLightSwitchBtnSet.switchOnButton(1, false);
					btnMainCarSettingNearLamp.setSwitchState(ImageStateButton.SWITCH_ON);
					btnMainCarSettingFarLamp.setSwitchState(ImageStateButton.SWITCH_OFF);
					break;
				case FAR_LIGHT:
					//mainLightSwitchBtnSet.switchOnButton(2, false);
					btnMainCarSettingNearLamp.setSwitchState(ImageStateButton.SWITCH_OFF);
					btnMainCarSettingFarLamp.setSwitchState(ImageStateButton.SWITCH_ON);
					break;
				}
			}catch (RemoteException e) {
				e.printStackTrace();
			}		
		}
		else {
			//Toast.makeText(this.getActivity(), getString(R.string.back_service_not_start), Toast.LENGTH_LONG).show();
			Log.e("KDSERVICE", "CarSettingFg.onClick() service is null");
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent me) {

		Log.d("CarSettingActivity","onTouch()");

		if((me.getAction()==MotionEvent.ACTION_DOWN) || (me.getAction()==MotionEvent.ACTION_UP) || (me.getAction()==MotionEvent.ACTION_CANCEL)) {

			CarSettingDriver carSettingDrv = DriverServiceManger.getInstance().getCarSettingDriver();

			if(carSettingDrv != null) {
				try {
					switch(v.getId()) {

					case R.id.btnMainCarSettingLeftWinUp:
						//  车窗		0x02	1~7			0x01:升/0x02:降/0x03:停止
						if((me.getAction()==MotionEvent.ACTION_DOWN) && (!btnMainCarSettingLeftWinUp.isOnPassThroughArea(me))) {
							main_car_setting_left_window_icon_up.startAnimation(animWinUp);
							//R_service.setCar_Action(0x02,1,0x01);
							carSettingDrv.setWindowAction(eWindow.LEFT, eWinDoorActionState.CLOSING);
						}
						else if((me.getAction()==MotionEvent.ACTION_UP) || (me.getAction()==MotionEvent.ACTION_CANCEL)) {
							main_car_setting_left_window_icon_up.clearAnimation();
							//R_service.setCar_Action(0x02,1,0x03);
							carSettingDrv.setWindowAction(eWindow.LEFT, eWinDoorActionState.STOPPED);
						}
						break;
					case R.id.btnMainCarSettingLeftWinDown:
				        //  车窗		0x02	1~7			0x01:升/0x02:降/0x03:停止
						if((me.getAction()==MotionEvent.ACTION_DOWN) && (!btnMainCarSettingLeftWinDown.isOnPassThroughArea(me))) {
							main_car_setting_left_window_icon_down.startAnimation(animWinDown);
							//R_service.setCar_Action(0x02,1,0x02);
							carSettingDrv.setWindowAction(eWindow.LEFT, eWinDoorActionState.OPENING);
						}
						else if((me.getAction()==MotionEvent.ACTION_UP) || (me.getAction()==MotionEvent.ACTION_CANCEL)) {
							main_car_setting_left_window_icon_down.clearAnimation();
							//R_service.setCar_Action(0x02,1,0x03);
							carSettingDrv.setWindowAction(eWindow.LEFT, eWinDoorActionState.STOPPED);
						}
						break;
					case R.id.btnMainCarSettingRightWinUp:
						//  车窗		0x02	1~7			0x01:升/0x02:降/0x03:停止
						if((me.getAction()==MotionEvent.ACTION_DOWN) && (!btnMainCarSettingRightWinUp.isOnPassThroughArea(me))) {
							main_car_setting_right_window_icon_up.startAnimation(animWinUp);
							//R_service.setCar_Action(0x02,2,0x01);
							carSettingDrv.setWindowAction(eWindow.RIGHT, eWinDoorActionState.CLOSING);
						}
						else if((me.getAction()==MotionEvent.ACTION_UP) || (me.getAction()==MotionEvent.ACTION_CANCEL)) {
							main_car_setting_right_window_icon_up.clearAnimation();
							//R_service.setCar_Action(0x02,2,0x03);
							carSettingDrv.setWindowAction(eWindow.RIGHT, eWinDoorActionState.STOPPED);
						}
						break;
					case R.id.btnMainCarSettingRightWinDown:
						//  车窗		0x02	1~7			0x01:升/0x02:降/0x03:停止
						if((me.getAction()==MotionEvent.ACTION_DOWN) && (!btnMainCarSettingRightWinDown.isOnPassThroughArea(me))) {
							main_car_setting_right_window_icon_down.startAnimation(animWinDown);
							//R_service.setCar_Action(0x02,2,0x02);
							carSettingDrv.setWindowAction(eWindow.RIGHT, eWinDoorActionState.OPENING);
						}
						else if((me.getAction()==MotionEvent.ACTION_UP) || (me.getAction()==MotionEvent.ACTION_CANCEL)) {
							main_car_setting_right_window_icon_down.clearAnimation();
							//R_service.setCar_Action(0x02,2,0x03);
							carSettingDrv.setWindowAction(eWindow.RIGHT, eWinDoorActionState.STOPPED);
						}						
						break;
						
					default:
						//return false;
					}
	
				}catch (RemoteException e) {
					e.printStackTrace();
				}		
			}
			else {
				//Toast.makeText(this.getActivity(), getString(R.string.back_service_not_start), Toast.LENGTH_LONG).show();
				Log.e("KDSERVICE", "CarSettingFg.onTouch() service is null");
			}
		}
		return false;
	}



	@Override
	public void onClick(View v) {
		CarSettingDriver carSettingDrv = DriverServiceManger.getInstance().getCarSettingDriver();
		
		int tag = v.getId();
		Log.d("CarSettingActivity","onClick("+tag+")");
		
		if(carSettingDrv != null) {
			try {
				switch(v.getId()) {
				case R.id.btnMainCarSettingCloseLamp:
					///大灯 关闭
					carSettingDrv.setMainLightState(CarSettingDriver.eMainLightState.OFF);
					//effectMainLamp();
					break;
				case R.id.btnMainCarSettingNearLamp:
					//大灯 近光
					carSettingDrv.setMainLightState(CarSettingDriver.eMainLightState.NEAR_LIGHT);
					///effectMainLamp();
					break;
				case R.id.btnMainCarSettingFarLamp:
					//大灯 远光
					carSettingDrv.setMainLightState(CarSettingDriver.eMainLightState.FAR_LIGHT);
					///effectMainLamp();
					break;

				/*case R.id.btnMainCarSettingFogFrontLamp:
					//前雾灯
					boolean nextFlashLightOn = !carSettingDrv.isFlashLightOn();
					carSettingDrv.setFlashLightOn(nextFlashLightOn);
					
					///effectFogFrontLamp();
					break;*/
				case R.id.btnMainCarSettingDoubleLamp:
					//双跳灯
					//boolean nextFlashLightOn = !carSettingDrv.isFlashLightOn();
					carSettingDrv.setFlashLightOn(!btnMainCarSettingDoubleLamp.isSwitchOn());
					
					///effectFogFrontLamp();
					break;
				case R.id.btnMainCarSettingPositionLamp:
					//小灯(位置灯)
					carSettingDrv.setPositionLightOn(!btnMainCarSettingPositionLamp.isSwitchOn());
					///effectPositionLamp();
					break;
				case R.id.btnMainCarSettingFogRearLamp:
					//后雾灯
					carSettingDrv.setFogLightRearOn(!btnMainCarSettingFogRearLamp.isSwitchOn());
					///effectFogRearLamp();
					break;
					
				case R.id.btnMainCarSettingBackDoorLock:
					//后备箱 (只开锁，不关锁)
					if(BaseApplication.getInstance().requestCarSpeedMax()){
						break;
					}
					carSettingDrv.setBackDoorOpen(false);
					break;
				case R.id.btnMainCarSettingDoorLock:
					//中控门锁
					if(!btnMainCarSettingDoorLock.isSwitchOn() == false){
						if(BaseApplication.getInstance().requestCarSpeedMax()){
							break;
						}
					}
					carSettingDrv.setDoorsLocked(!btnMainCarSettingDoorLock.isSwitchOn());
					///effectDoorLock();
					break;
				case R.id.btnMainCarSettingAllWinOpen:
					
					//一键开窗
					//btnMainCarSettingAllWinOpen.isSwitchOn();
					carSettingDrv.triggerOneKeyWinOpen();
					//btnMainCarSettingAllWinOpen.setSwitchState(ImageStateButton.SWITCH_OFF);
					///effectOneKeyWinOpen();
					break;				
				}

			}catch (RemoteException e) {
				e.printStackTrace();
			}		
		}
		else {
			//Toast.makeText(this.getActivity(), getString(R.string.back_service_not_start), Toast.LENGTH_LONG).show();
			Log.e("KDSERVICE", "CarSettingFg.onClick() service is null");
		}
		
	}

	private void effectMainLamp() {
		
		ImageView nearLightImgView = (ImageView) rootView.findViewById(R.id.main_car_setting_effect_near_light);
		ImageView farLightImgView = (ImageView) rootView.findViewById(R.id.main_car_setting_effect_far_light);

		nearLightImgView.setVisibility(btnMainCarSettingNearLamp.isSwitchOn()?View.VISIBLE:View.INVISIBLE);
		farLightImgView.setVisibility(btnMainCarSettingFarLamp.isSwitchOn()?View.VISIBLE:View.INVISIBLE);
	}

	private void effectFogFrontLamp() {
		/*ImageView lightImgView = (ImageView) rootView.findViewById(R.id.main_car_setting_effect_fog01_light);
		lightImgView.setVisibility(btnMainCarSettingFogFrontLamp.isSwitchOn()?View.VISIBLE:View.INVISIBLE);*/
	}

	private void effectPositionLamp() {
		ImageView lightImgView = (ImageView) rootView.findViewById(R.id.main_car_setting_effect_position_light);
		if(btnMainCarSettingPositionLamp.isSwitchOn()) {
			lightImgView.setVisibility(View.VISIBLE);
		}
		else if(!isFlashingEffect) {
			lightImgView.setVisibility(View.INVISIBLE);
		}
	}

	private void effectFogRearLamp() {
		ImageView lightImgView = (ImageView) rootView.findViewById(R.id.main_car_setting_effect_fog_light);
		lightImgView.setVisibility(btnMainCarSettingFogRearLamp.isSwitchOn()?View.VISIBLE:View.INVISIBLE);
	}

	private boolean isFlashingEffect = false;
	private void effectDoubleFlashLamp(boolean isFlashing) {
		if (isFlashing) {
			ImageView imgView = (ImageView) rootView.findViewById(R.id.main_car_setting_effect_alert_light);
			Animation anim = AnimationUtils.loadAnimation(this.getActivity(), R.anim.main_car_setting_effect_alarm_lamp);
			imgView.startAnimation(anim);
		} else {
			ImageView imgView = ((ImageView) rootView.findViewById(R.id.main_car_setting_effect_alert_light));
			imgView.clearAnimation();
			imgView = (ImageView) rootView.findViewById(R.id.main_car_setting_effect_position_light);
			imgView.setVisibility(btnMainCarSettingPositionLamp.isSwitchOn()?View.VISIBLE:View.INVISIBLE);
		}
		isFlashingEffect = isFlashing;
	}

	private void effectBackdoorLock(boolean isLocked) {
		if(isLocked) {
			ImageView imgView = (ImageView) rootView.findViewById(R.id.main_car_setting_effect_car_houbei_lock);
			Animation anim = AnimationUtils.loadAnimation(this.getActivity(), R.anim.main_car_setting_effect_car_door);
			imgView.startAnimation(anim);
		}
		else {
			ImageView imgView = (ImageView) rootView.findViewById(R.id.main_car_setting_effect_car_houbei_unlock);
			Animation anim = AnimationUtils.loadAnimation(this.getActivity(), R.anim.main_car_setting_effect_car_door);
			imgView.startAnimation(anim);
		}
	}

	private void effectDoorLock() {
		if(btnMainCarSettingDoorLock.isSwitchOn()) {
			ImageView imgView = (ImageView) rootView.findViewById(R.id.main_car_setting_effect_car_door_lock);
			Animation anim = AnimationUtils.loadAnimation(this.getActivity(), R.anim.main_car_setting_effect_car_door);
			imgView.startAnimation(anim);
		}
		else {
			ImageView imgView = (ImageView) rootView.findViewById(R.id.main_car_setting_effect_car_door_unlock);
			Animation anim = AnimationUtils.loadAnimation(this.getActivity(), R.anim.main_car_setting_effect_car_door);
			imgView.startAnimation(anim);
		}
	}
	
}
