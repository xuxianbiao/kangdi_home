package com.kandi.application;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Log;
import android.widget.Toast;

import com.driverlayer.kdos_driverServer.BlueDriver;
import com.driverlayer.kdos_driverServer.IECarDriver;
import com.kandi.driver.DriverServiceManger;
import com.kandi.driver.EcocEnergyInfoDriver;
import com.kandi.driver.StateDNRInfoDriver;
import com.kandi.home.R;
import com.kandi.model.PresistentData;
import com.kandi.socket.SocketClient;
import com.kandi.util.ACacheUtil;
import com.kandi.view.DialActivity;
import com.util.ToastUtil;
import com.util.system.CrashHandler;

public class BaseApplication extends Application {
	private static final String TAG = "kangdi_voice";
    private static BaseApplication mApplication = null;
    public static BlueDriver bluedriver;
    final int Restart_time=5;//如果检查到服务程序崩溃，5秒后将重启服务
    int TimeOut_Count=0;
    private EcocEnergyInfoDriver spmodel;
    private StateDNRInfoDriver dnrmodel;
    public int val_speed;
    public int dnr_postion;
    public boolean wheelchoose;
    public boolean blue_status = false;
    public boolean blue_music_state = false;
    SocketClient socket;
    public AudioManager mAudioManager;
	@Override
	public void onCreate() {
		super.onCreate();
		DriverServiceManger.getInstance().startService(this);
		PresistentData.initInstance(this);
		mApplication = this;
		mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		setmAudioManager(mAudioManager);
		ACacheUtil acache = ACacheUtil.get();
		acache.remove("upstatus");
		acache.remove("phonenum");
		
		 CrashHandler crashHandler = CrashHandler.getInstance();
		 crashHandler.init(this);
		 socket = new SocketClient();

		// Intent BottomFloatOptionService = new Intent(this,
		// BottomFloatOptionService.class);
		// startService(BottomFloatOptionService);
		Intent service = new Intent("com.kangdi.InitService");
		bindService(service, conn, Context.BIND_AUTO_CREATE);
		mHandler.sendEmptyMessageDelayed(1000, 3000);
		
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				IECarDriver model = DriverServiceManger.getInstance().R_service;
				if (model == null) {
					TimeOut_Count++;
		        	if(TimeOut_Count>=Restart_time){
			            restartKdService();
			            TimeOut_Count = 0;
		        	}
				}
			}
		}, 15000, 1000);
		
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				spmodel = DriverServiceManger.getInstance().getEcocEnergyInfoDriver();
				if(spmodel!=null){
					try {
						spmodel.retreveGeneralInfo();
						setVal_speed(spmodel.getCarSpeed());
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				dnrmodel = DriverServiceManger.getInstance().getStateDNRInfoDriver();
				if(dnrmodel!=null){
					try {
						setDnr_postion(dnrmodel.getCarDNRInfo());
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
		}, 3000, 1000);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					try {
						String result = socket.sendMsg("");
						if (result.contains("com.kangdi.BroadCast.CallStart")) {
							Log.i("resultCallStart", "resultCallStart");
							if(mAudioManager.requestAudioFocus(afBTChangeListener, 11,
									AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
								Log.i("resultCallStart", "requestAudioFocus");
							}
						}else if(result.contains("com.kangdi.BroadCast.CallEnd")){
							Log.i("resultCallStart", "resultCallEnd");
							if (mAudioManager.abandonAudioFocus(afBTChangeListener) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
								Log.i("resultCallStart", "abandonAudioFocus");
							}
						}
						Thread.sleep(100);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
	}
	
	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			bluedriver = BlueDriver.Stub.asInterface(arg1);
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			bluedriver = null;
		}

	};
	
    public static BaseApplication getInstance() {
        return mApplication;
    }
	/**
	 * 当后台服务未启动时，尝试重新启动KD后台服务
	 * 
	 * @return false:后台服务启动中; true:后台服务已重启成功，无需重启
	 */
	public boolean restartKdService() {
		if (!DriverServiceManger.getInstance().isServiceRunning()) {
			DriverServiceManger.getInstance().startService(this);
			return false;
		} else {
			return true;
		}
	}
	public AudioManager getmAudioManager() {
		return mAudioManager;
	}
	public void setmAudioManager(AudioManager mAudioManager) {
		this.mAudioManager = mAudioManager;
	}
	public int getVal_speed() {
		return val_speed;
	}
	public void setVal_speed(int val_speed) {
		this.val_speed = val_speed;
	}
	
	public int getDnr_postion() {
		return dnr_postion;
	}
	public void setDnr_postion(int dnr_postion) {
		this.dnr_postion = dnr_postion;
	}
	public boolean isWheelchoose() {
		return wheelchoose;
	}
	public void setWheelchoose(boolean wheelchoose) {
		this.wheelchoose = wheelchoose;
	}
	public boolean isBlue_status() {
		return blue_status;
	}
	public void setBlue_status(boolean blue_status) {
		this.blue_status = blue_status;
	}
	public boolean isBlue_music_state() {
		return blue_music_state;
	}
	public void setBlue_music_state(boolean blue_music_state) {
		this.blue_music_state = blue_music_state;
	}
	/**
     * 请求车辆速度上限，达到上限返回true，反之返回false
     * @author XuXianbiao
     * @return
     */
    public boolean requestCarSpeedMax(){
    	if(getVal_speed()>5){
    		ToastUtil.showToast(getApplicationContext(), getString(R.string.speed_max), Toast.LENGTH_SHORT);
    		return true;//速度大于5时返回true，即在方法处理中return；或提示Toast
    	}else{
    		return false;
    	}
    }
    
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	String btconnected = SystemProperties.get("sys.kd.btconnected","");
    		if(!"".equals(btconnected)){
    			if("yes".equals(btconnected)){
    				DialActivity.setBlueState(true);
    				setBlue_status(true);
    			}else if("no".equals(btconnected)){
    				DialActivity.setBlueState(false);
    				setBlue_status(false);
    				mHandler.sendEmptyMessageDelayed(1000, 3000);
    			}
    		}
        }
    };
    
    /**
	 * BT监听器
	 */
	public OnAudioFocusChangeListener afBTChangeListener = new OnAudioFocusChangeListener() {
		public void onAudioFocusChange(int focusChange) {
			if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
			} else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
			} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
			}

		}
	};
}
