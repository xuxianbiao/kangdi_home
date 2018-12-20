package com.kandi.broadcast;

import java.util.Set;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Bundle;
import android.os.IKdAudioControlService;
import android.os.IKdBtService;
import android.os.RemoteException;
import android.os.ServiceManager;

import com.driverlayer.kdos_driverServer.BlueDriver;
import com.kandi.application.BaseApplication;
import com.kandi.event.ConnectBlueEvent;
import com.kandi.event.PlayBlueCallEndEvent;
import com.kandi.event.PlayCallOutGoingEvent;
import com.kandi.event.PlayCallShowNumEvent;
import com.kandi.event.base.BaseEvent;
import com.kandi.home.R;
import com.kandi.util.ACacheUtil;
import com.kandi.util.CommonUtils;
import com.kandi.view.DialActivity;
import com.kandi.view.EntertainmentFragmentActivity;
import com.kandi.view.EntertainmentFragmentVideoActivity;
import com.kandi.view.PushUpActivity;
import com.kandi.view.RadioActivity;
import com.kandi.view.UpgradingActivity;
import com.kandi.view.WheelModeActivity;
import com.kandi.view.WheelVolumeActivity;

import de.greenrobot.event.EventBus;

public class GetBroadCast extends BroadcastReceiver {
	IKdBtService btservice = IKdBtService.Stub.asInterface(ServiceManager.getService("bt"));
	IKdAudioControlService audioservice = IKdAudioControlService.Stub.asInterface(ServiceManager.getService("audioCtrl"));
	BlueDriver bluedriver = BaseApplication.bluedriver;
	public final static String UP_DATA = "com.kangdi.userchoose";//用户升级确认
	public final static String UPING = "com.kangdi.forciblyuping";//强制升级中
	public final static String FORCE_UPING = "com.kangdi.forceuping";
	public final static String ACTION_HC = "com.kangdi.BroadCast.HandsFreeConnect";//手机蓝牙已连接
	public final static String ACTION_HD = "com.kangdi.BroadCast.HandsFreeDisconnect";//手机蓝牙已断开
	public final static String ACTION_SIMCALL = "com.kangdi.simcall";//sim来电接听后需要展开电话盘
	public final static String ACTION_CALLSTART = "com.kangdi.BroadCast.CallStart";//接通事件
    public final static String ACTION_CALLEND = "com.kangdi.BroadCast.CallEnd";//挂断事件
	public final static String ACTION_SIM_CALL_START = "com.kangdi.BroadCast.SimCallStart";//SIM卡接通广播
	public final static String ACTION_CALLOUTGOING_START = "com.kangdi.BroadCast.CallOutGoing";//蓝牙电话拨出号码
	public final static String ACTION_WHEEL_VOLUMEADD = "com.kangdi.BroadCast.WheelVolumeAdd";//多功能方向盘音量+
	public final static String ACTION_WHEEL_VOLUMEREDUCE = "com.kangdi.BroadCast.WheelVolumeReduce";//多功能方向盘音量-
	public final static String ACTION_WHEEL_MODE = "com.kangdi.BroadCast.WheelMode";//多功能方向盘模式
	public final static String ACTION_WHEEL = "com.driverlayer.kdos_driverserver";
	public final static String ACTION_WHEEL_MUSIC_PREV = "com.kangdi.BroadCast.WheelMusicPrev";//多功能方向盘音乐上一首
	public final static String ACTION_WHEEL_MUSIC_NEXT = "com.kangdi.BroadCast.WheelMusicNext";//多功能方向盘音乐下一首
	public final static String ACTION_WHEEL_CALL = "com.kangdi.BroadCast.WheelCall";//多功能方向盘接听
	public final static String ACTION_WHEEL_HANGUP = "com.kangdi.BroadCast.WheelHangup";//多功能方向盘挂断
	private int[] state;
	public static boolean openblvoicepath = false;
	public static int oncebl = 0;
	public static int oncebl2 = 0;
	public static int count = 0;
	Intent modeintent;
	public String[] mfl_status = new String[]{ACTION_WHEEL_VOLUMEADD,ACTION_WHEEL_MODE,ACTION_WHEEL_VOLUMEREDUCE,ACTION_WHEEL_MUSIC_NEXT,ACTION_WHEEL_CALL,ACTION_WHEEL_MUSIC_PREV,ACTION_WHEEL_HANGUP};
	int[] mode_status = new int[]{0,1,2,1};
	public boolean which_mode = false;//true:滚动模式；false:切换模式
	private AudioManager mAudioManager;
	@Override
	public void onReceive(Context context, final Intent intent) {
		if(intent.getAction().equals(FORCE_UPING)){
			Intent uping = new Intent();
			uping.setClass(context, UpgradingActivity.class);
			uping.putExtra("path", intent.getStringArrayExtra("path"));
			uping.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(uping);
		}
		if(intent.getAction().equals(UPING)){
			boolean action = intent.getBooleanExtra("action", true);
			if(action){
				Intent uping = new Intent();
				uping.setClass(context, UpgradingActivity.class);
				uping.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(uping);
			}
		}
		if(intent.getAction().equals(UP_DATA)){
			//添加弹窗用户确认是否升级
			Intent intent2 = new Intent();
			intent2.setClass(context, PushUpActivity.class);
			intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent2.putExtra("path", intent.getStringArrayExtra("path"));
			context.startActivity(intent2);
		}
//		if(intent.getAction().equals(ACTION_WHEEL)){
//			final String m_key = "KD_CAST_EVENT";
//			Bundle bundle = intent.getExtras();
//
//			// 第一步分拣广播事件并用EventBus转发bundle对象（bundle有事件名称key和值）
//			Set<String> keySet = 	bundle.keySet();
//			for (String key : keySet) {
//				if (!key.startsWith(m_key))
//					continue;
//
//				int eventId;
//				try {
//					eventId = Integer.parseInt(key.substring(m_key.length()));
//				} catch (NumberFormatException e) {
//					e.printStackTrace();
//					continue;
//				}
//
//				if ((eventId >= BaseEvent.DRIEVENT.values().length)
//						|| (eventId < 0)) {
//					continue;
//				}
//				BaseEvent.DRIEVENT ev = BaseEvent.DRIEVENT.values()[eventId];
//
//				switch (ev) {
//				case DRI_MFLSTATUS:
//					/** 多功能方向盘状态变化事件 */
////					int[] status_array = bundle.getIntArray("KD_CAST_EVENT"+20);
////					Intent intent_status = new Intent();
////					for(int i=0;i<status_array.length;i++){
////						if(status_array[i] != 0){
////							if(i == 4){
////								if(status_array[i] == 2){
////									intent_status.setAction(mfl_status[6]);
////								}else{
////									intent_status.setAction(mfl_status[i]);
////								}
////							}else{
////								intent_status.setAction(mfl_status[i]);
////							}
////							context.sendBroadcast(intent_status);
////							Log.i("getMflbroad", "=============");
////						}
////					}
//					break;
//				default:
//					break;
//				}
//			}
//		}
//		if(intent.getAction().equals(ACTION_WHEEL_MODE)){
//			if(!CommonUtils.isFastDoubleClick(400)){
//				if(which_mode){
//					if(count >= 4){
//						count = 0;
//					}
//					modeintent = new Intent(context, WheelModeActivity.class);
//					modeintent.putExtra("position", mode_status[count]);
//					modeintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					context.startActivity(modeintent);
//					count++;
//				}else{
//					if(count >= 3){
//						count = 0;
//					}
//					if(count == 0){
//						modeintent = new Intent(context, RadioActivity.class);
//					}else if(count == 1){
//						modeintent = new Intent(context, EntertainmentFragmentActivity.class);
//					}else if(count == 2){
//						modeintent = new Intent(context, EntertainmentFragmentVideoActivity.class);
//					}
//					modeintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					context.startActivity(modeintent);
//					count++;
//				}
//			}
//		}
//		if(intent.getAction().equals(ACTION_WHEEL_VOLUMEADD)){
//			if(!CommonUtils.isFastDoubleClick(200)){
//				Intent i = new Intent(context, WheelVolumeActivity.class);
//				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				i.putExtra("direction", true);
//				context.startActivity(i);
//			}
//		}
//		if(intent.getAction().equals(ACTION_WHEEL_VOLUMEREDUCE)){
//			if(!CommonUtils.isFastDoubleClick(200)){
//				Intent i = new Intent(context, WheelVolumeActivity.class);
//				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				i.putExtra("direction", false);
//				context.startActivity(i);
//			}
//		}
		if(intent.getAction().equals(ACTION_HC)){//手机蓝牙已链接
			DialActivity.setBlueState(true);
			BaseApplication.getInstance().setBlue_status(true);
			ConnectBlueEvent event = new ConnectBlueEvent();
			event.type = ConnectBlueEvent.BLUE_CONN_STATE;
			EventBus.getDefault().post(event);
			//Toast.makeText(context, "手机蓝牙已链接", Toast.LENGTH_LONG).show();
		}
		if(intent.getAction().equals(ACTION_HD)){//手机蓝牙已断开
			DialActivity.setBlueState(false);
			BaseApplication.getInstance().setBlue_status(false);
			ConnectBlueEvent event = new ConnectBlueEvent();
			event.type = ConnectBlueEvent.BLUE_DISCONN_STATE;
			EventBus.getDefault().post(event);
			//Toast.makeText(context, "手机蓝牙已断开", Toast.LENGTH_LONG).show();
		}
		if(intent.getAction().equals(ACTION_SIMCALL)){
			Intent callintent = new Intent(context, DialActivity.class);
			callintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			callintent.putExtra("number", intent.getStringExtra("number"));
			callintent.putExtra("one_key_intent", 1);
			context.startActivity(callintent);
		}
		

		if(intent.getAction().equals(ACTION_CALLOUTGOING_START)){
			if(oncebl<1){
				oncebl++;
				if(!openblvoicepath){
					mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
					if(mAudioManager.requestAudioFocus(afBtPhoneChangeListener, 11,
		    				AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
		        	}
					openblvoicepath = true;
				}
				DialActivity.CALLGOING = true;
				PlayCallOutGoingEvent playCallOutGoingEvent = new PlayCallOutGoingEvent();
				playCallOutGoingEvent.type = PlayCallOutGoingEvent.CALLOUTGOING_STATE;
				EventBus.getDefault().post(playCallOutGoingEvent);
			}
		}
		
		if(intent.getAction().equals(ACTION_CALLSTART)){
			//Log.i("RingCallReceiver", "接通事件");
			if(oncebl2<1){
				oncebl2++;
				DialActivity.CALLGOING = false;
				if(!openblvoicepath){
					mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
					if(mAudioManager.requestAudioFocus(afBtPhoneChangeListener, 11,
		    				AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
		        	}
					openblvoicepath = true;
				}
				String phonenumber = intent.getStringExtra("com.kangdi.key.phonenum")==null?context.getString(R.string.unknow):intent.getStringExtra("com.kangdi.key.phonenum");
				ACacheUtil acache = ACacheUtil.get();
				acache.put("phonenum", phonenumber);
				String[] param = new String[]{phonenumber,"1"};
				if(bluedriver!=null){
					try {
						bluedriver.startCountService(param);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				PlayCallShowNumEvent playCallShowNumEvent = new PlayCallShowNumEvent();
				playCallShowNumEvent.type = PlayCallShowNumEvent.CALLSHOWNUM_STATE;
				EventBus.getDefault().post(playCallShowNumEvent);
//				if(!isTopActivity("DialActivity")){
//					Intent callintent = new Intent(context, DialActivity.class);
//					callintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					callintent.putExtra("phonenum", phonenumber);
//					context.startActivity(callintent);
//				}
			}
		}
		if(intent.getAction().equals(ACTION_CALLEND)){
			//Log.i("RingCallReceiver", "挂断事件");
			openblvoicepath = false;
			DialActivity.CALLGOING = false;
			oncebl = 0;
			oncebl2 = 0;
			try {
				if(bluedriver!=null){
					state = new int[1];
					String[] state2 = new String[]{"1"};
					bluedriver.getCountState(state2,state);
					if(state[0]==0){
						PlayBlueCallEndEvent event = new PlayBlueCallEndEvent();
						event.type = PlayBlueCallEndEvent.BLUE_STATE;
						EventBus.getDefault().post(event);
					}
					mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
					if (mAudioManager.abandonAudioFocus(afBtPhoneChangeListener) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
					}
					bluedriver.stopCountService(state2);
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
//		if(intent.getAction().equals(ACTION_SIM_CALL_START)){
//			//Log.i("ACTION_SIM_CALL_START", "SIM接通事件");
//			OneKeyActivity.CALLSTATE = false;
//			String[] param = new String[]{context.getString(R.string.onekeynum),"0"};
//			if(bluedriver!=null){
//				try {
//					bluedriver.startCountService(param);
//				} catch (RemoteException e) {
//					e.printStackTrace();
//				}
//			}
//			if(!isTopActivity("OneKeyActivity")){
//				Intent callintent = new Intent(context, OneKeyActivity.class);
//				callintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				context.startActivity(callintent);
//			}
//		}
//		
//		if(intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)){//系统sim action不同
//    		TelephonyManager telMgr =  (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
//    		String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
//    		int state = telMgr.getCallState();
//    		switch (state) {
//    		case TelephonyManager.CALL_STATE_RINGING:
//    			Log.i("RingCallReceiver", "[Broadcast]等待接电话=" + phoneNumber);
//    			break;
//    		case TelephonyManager.CALL_STATE_IDLE:
//    			Log.i("RingCallReceiver", "[Broadcast]电话挂断=" + phoneNumber);
//    			OneKeyActivity.CALLSTATE = false;
//    			int[] data = new int[1];
//    			try {
//    				bluedriver.getCountState(data);
//    				if(data[0] == 0){
//    					PlaySimCallEndEvent event = new PlaySimCallEndEvent();
//    					event.type = PlaySimCallEndEvent.SIM_NOSTATE;
//    					EventBus.getDefault().post(event);
//    				}
//    				if(bluedriver!=null){
//    					String[] state2 = new String[]{"0"};
//    					bluedriver.stopCountService(state2);
//    				}
//				} catch (RemoteException e) {
//					e.printStackTrace();
//				}
//    			break;
//    		case TelephonyManager.CALL_STATE_OFFHOOK:
//    			Log.i("RingCallReceiver", "[Broadcast]通话中=" + phoneNumber);
//    			break;
//    		} 
//    	}
		
		if(intent.getAction().equals(ACTION_WHEEL_HANGUP)){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						if(btservice!=null){
							btservice.btHungupCall();
						}
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		
	}
	
	private boolean isTopActivity(String name)
    {
        boolean isTop = false;
        ActivityManager am = (ActivityManager) BaseApplication.getInstance().getSystemService("activity");
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        if (cn.getClassName().contains(name))
        {
            isTop = true;
        }
        return isTop;
    }
	
	/**
	 * BtPhone监听器
	 */
	public static OnAudioFocusChangeListener afBtPhoneChangeListener = new OnAudioFocusChangeListener() {
		public void onAudioFocusChange(int focusChange) {
			if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
			} else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
			} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
			}

		}
	};

}
