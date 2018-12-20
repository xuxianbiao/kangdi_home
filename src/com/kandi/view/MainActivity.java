package com.kandi.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.IKdBtService;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.kandi.adapter.MainFragmentPageAdapter;
import com.kandi.base.BaseActivity;
import com.kandi.driver.DriverServiceManger;
import com.kandi.driver.EcocEnergyInfoDriver;
import com.kandi.event.CarChargingEvent;
import com.kandi.event.DRI_AIR_ENABLEEvent;
import com.kandi.event.DRI_CHARGER_ONOFFEvent;
import com.kandi.event.DRI_INSERT_CHARGEREvent;
import com.kandi.event.base.BaseEvent;
import com.kandi.fragment.MainPageFragment1;
import com.kandi.fragment.MainPageFragment2Container;
import com.kandi.home.R;
import com.kandi.service.RightMenuService;
import com.kandi.util.CommonUtils;
import com.kandi.view.syssetting.StateDNRActivity;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity implements Callback {
	private final String DRIBROADCAST = "com.driverlayer.kdos_driverserver";
	int[] m_recvwindow;
	private Intent chargingIntent;
	
	private String currentIntent = "";
	private ViewPager viewPager;
	private ViewPagerIndicator indicator; // viewpager的指示器
	private Handler handler;
	private MainPageFragment1 firstFragment;
	
	private static Activity context;
	
	ArrayList<Fragment> fragmentList;
	MainFragmentPageAdapter mainPageAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context = this;
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Intent intent2 = new Intent();
        intent2.setComponent(new ComponentName("com.kandi.settings",
                    "com.kandi.settings.driver.DriverServiceManger"));
        this.startService(intent2);
        
		setContentView(R.layout.home_pannel);
		btservice = IKdBtService.Stub.asInterface(ServiceManager.getService("bt"));
		View rootView = findViewById(android.R.id.content);
		rootView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
		 
		handler = new Handler(this);

		initView();
		if(!isServiceWork(this,"com.kandi.systemui.service.KandiSystemUiService")){
			Intent intent1 = new Intent();
	        intent1.setComponent(new ComponentName("com.kandi.systemui",
	                    "com.kandi.systemui.service.KandiSystemUiService"));
	        this.startService(intent1);
		}else{
			
		}
		
		WelcomeActivity.finishActivity();

//		Intent intent = new Intent(this, RightMenuService.class);
//		bindService(intent, conn, Context.BIND_AUTO_CREATE); 
		
		Log.i("Kandi", "MainActivity onCreate out");
		registBroadCastReceiver();
		
	}
	
	 /**
		 * 判断某个服务是否正在运行的方法
		 * 
		 * @param mContext
		 * @param serviceName
		 *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
		 * @return true代表正在运行，false代表服务没有正在运行
		 */
		public boolean isServiceWork(Context mContext, String serviceName) {
			boolean isWork = false;
			ActivityManager myAM = (ActivityManager) mContext
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningServiceInfo> myList = myAM.getRunningServices(40);
			if (myList.size() <= 0) {
				return false;
			}
			for (int i = 0; i < myList.size(); i++) {
				String mName = myList.get(i).service.getClassName().toString();
				if (mName.equals(serviceName)) {
					isWork = true;
					break;
				}
			}
			return isWork;
		}
	
	private RightMenuService rightservice;
	private EcocEnergyInfoDriver model;
	private ServiceConnection conn = new ServiceConnection(){
		Intent intent;
		boolean flag = false;
		boolean isUp = false;
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			// TODO Auto-generated method stub
			rightservice = ((RightMenuService.MsgBinder)arg1).getService();
			rightservice.mFloatView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(StateDNRActivity.instance!=null){
						StateDNRActivity.instance.finish();
						StateDNRActivity.instance = null;
					}else{
						flag = false;
					}
					if(flag){
						if(intent!=null){
							if(StateDNRActivity.instance != null){
								StateDNRActivity.instance.finish();
								StateDNRActivity.instance = null;
							}
						}
						flag = false;
					}else{
						model = DriverServiceManger.getInstance().getEcocEnergyInfoDriver();
						if(model!=null){
							intent = new Intent(getApplicationContext(),StateDNRActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							getApplicationContext().startActivity(intent);
							flag = true;
						}
					}
				}
			});
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					while(true){
						/**速度变化带来的变化*/
						model = DriverServiceManger.getInstance().getEcocEnergyInfoDriver();
						if(model!=null){
							try {
								model.retreveGeneralInfo();
							} catch (RemoteException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(model.getCarSpeed()<=1){
								m_handler.sendEmptyMessage(100);
							}else if(model.getCarSpeed()>=5){
								m_handler.sendEmptyMessage(101);
								isUp = false;
							}else{
								isUp = false;
							}
						}else {
							Log.e("KDSERVICE", "Speed service is null");
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}).start();
			System.out.println("###RightMenuService Connect Success");
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub
		}
		
		Handler m_handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 100:
					if(!isUp){
						flag = true;
						intent = new Intent(getApplicationContext(),StateDNRActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						getApplicationContext().startActivity(intent);
						rightservice.mFloatView.setVisibility(View.VISIBLE);
						isUp = true;
					}
					break;
				case 101:
					if(StateDNRActivity.instance != null){
						StateDNRActivity.instance.finish();
					}
					flag = true;
					rightservice.mFloatView.setVisibility(View.INVISIBLE);
					break;

				}
			}
			
		};
		
	};
	
//	@Override
//	public void onBackPressed(){
//	}

	private void initView() {
		Log.i("Kandi", "MainActivity initView");
		// 1. 初始化Viewpager，绑定adapter
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		fragmentList = new ArrayList<Fragment>();
		firstFragment = MainPageFragment1.newInstance("");
		final MainPageFragment2Container secondFragment = new MainPageFragment2Container();
//		MainPageFragment2 secondFragment = new MainPageFragment2();
		
		fragmentList.add(firstFragment);
		fragmentList.add(secondFragment);

		mainPageAdapter = new MainFragmentPageAdapter(getSupportFragmentManager(), fragmentList);
		viewPager.setAdapter(mainPageAdapter);

		// 2. 给Viewpager绑定指示器
		indicator = (ViewPagerIndicator) findViewById(R.id.viewpager_indicator);
		indicator.setViewPager(viewPager);
	}
	
	public static Activity getContext(){
		return context;
	}
	

	@Override
	public void CarChargingEvent(CarChargingEvent event) {
		if (!currentIntent.equals("carcharge")) {
			currentIntent = "carcharge";
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					chargingIntent = new Intent(MainActivity.this,
							ChargingActivity.class);
					chargingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(chargingIntent);
				}
			}, 0);
		}

	}


	@Override
	public void VolumeClickEvent(com.kandi.event.VolumeClickEvent event) {
		currentIntent = "";
	}

	int dbgcount = 0;

	// 广播接收处理回调方法
	@Override
	public void doReceive(Intent intent) {}
	

	// 插入充电枪
	@Override
	public void DRI_INSERT_CHARGEREvent(
			com.kandi.event.DRI_INSERT_CHARGEREvent event) {
//		if(firstFragment != null){
//			firstFragment.ChargeInsterEvent(event);
//		}
	}

	@Override
	public void DRI_CHARGER_ONOFFEvent(
			com.kandi.event.DRI_CHARGER_ONOFFEvent event) {
//		if(firstFragment != null){
//			firstFragment.ChargeOnOffEvent(event);
//		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		return false;
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		viewPager.setCurrentItem(0);
		Bundle bundle = intent.getExtras();
        if (bundle != null) {
        	int car_set = bundle.getInt("car_set");
        	indicator.setPage(car_set);
        }
	}
	
	@Override
	protected void onDestroy() {
		unregistBroadCastReceiver();
		super.onDestroy();
	}

	public interface ViewPagerListener {
		void onPageSelected(int index);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	}
	
	private void registBroadCastReceiver() {
        if (null == carStateChanageReceiver) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(DRIBROADCAST);
            filter.addAction(ACTION_WHEEL_VOLUMEADD);
            filter.addAction(ACTION_WHEEL_VOLUMEREDUCE);
            filter.addAction(ACTION_WHEEL_MODE);
            filter.addAction(ACTION_WHEEL_HANGUP);
            filter.addAction(ACTION_WHEEL_VOICE);
            filter.addAction(ACTION_WHEEL_MUTE);
            carStateChanageReceiver = new CarStateChanageReceiver();
            getApplicationContext().registerReceiver(carStateChanageReceiver, filter);
        }
    }

    private void unregistBroadCastReceiver() {
        if (null != carStateChanageReceiver) {
        	getApplicationContext().unregisterReceiver(carStateChanageReceiver);
        	carStateChanageReceiver = null;
        }
    }
    
    public CarStateChanageReceiver carStateChanageReceiver;
    IKdBtService btservice;
    public final static String ACTION_WHEEL_VOLUMEADD = "com.kangdi.BroadCast.WheelVolumeAdd";//多功能方向盘音量+
	public final static String ACTION_WHEEL_VOLUMEREDUCE = "com.kangdi.BroadCast.WheelVolumeReduce";//多功能方向盘音量-
	public final static String ACTION_WHEEL_MODE = "com.kangdi.BroadCast.WheelMode";//多功能方向盘模式
	public final static String ACTION_WHEEL_VOICE = "com.kangdi.BroadCast.WheelVoice";//多功能方向盘语音输入
	public final static String ACTION_WHEEL_MUTE = "com.kangdi.BroadCast.Mute";//多功能方向盘静音
	public final static String ACTION_WHEEL_MUSIC_PREV = "com.kangdi.BroadCast.WheelMusicPrev";//多功能方向盘音乐上一首
	public final static String ACTION_WHEEL_MUSIC_NEXT = "com.kangdi.BroadCast.WheelMusicNext";//多功能方向盘音乐下一首
	public final static String ACTION_WHEEL_CALL = "com.kangdi.BroadCast.WheelCall";//多功能方向盘接听
	public final static String ACTION_WHEEL_HANGUP = "com.kangdi.BroadCast.WheelHangup";//多功能方向盘挂断
    String[] mfl_status = new String[]{ACTION_WHEEL_VOLUMEADD,ACTION_WHEEL_MODE,ACTION_WHEEL_VOLUMEREDUCE,ACTION_WHEEL_VOICE,ACTION_WHEEL_MUSIC_NEXT,ACTION_WHEEL_CALL,ACTION_WHEEL_MUSIC_PREV,ACTION_WHEEL_MUTE,ACTION_WHEEL_HANGUP};
    int count = 0;
    public class CarStateChanageReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(DRIBROADCAST)){
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
//						Toast.makeText(this, "错误：未知的后台服务事件! ID=" + eventId,
//								Toast.LENGTH_LONG).show();
						continue;
					}
					BaseEvent.DRIEVENT ev = BaseEvent.DRIEVENT.values()[eventId];
					switch (ev) {

					case DRI_DNR_WINSHOW:
						/** 档位隐藏状态变化事件 */
						// TODO:
						break;

					case DRI_AIR_ENABLE:
						/** 空调面板使能或者禁止状态变化事件 */
						try {
							EventBus.getDefault().postSticky(
									new DRI_AIR_ENABLEEvent(bundle));
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;

//					case DRI_INSERT_CHARGER:
//						/** 充电抢插入状态变化事件 */
//						try {
//							EventBus.getDefault().postSticky(
//									new DRI_INSERT_CHARGEREvent(bundle));
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
////						ToastUtil.showDbgToast(getApplicationContext(), "MSG");
//						break;
//					case DRI_CHARGER_ONOFF:
//						/** 充电机启动或者停止状态变化事件 */
//						try {
//							EventBus.getDefault().postSticky(
//									new DRI_CHARGER_ONOFFEvent(bundle));
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						break;

					case DRI_ERR:
						/** 故障状态变化事件 */
						try {
							EventBus.getDefault().postSticky(
									new com.kandi.event.DRI_ERREvent(bundle));
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case DRI_WARING:
						/** 告警状态变化事件 */
						try {
							EventBus.getDefault().postSticky(
									new com.kandi.event.DRI_WARINGEvent(bundle));
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;

					case DRI_CARDOOR:/** 车门锁状态变化事件 */
					case DRI_CARWINDOWS:/** 车窗状态变化事件 */
					case DRI_TRUNKBOOT:/** 后备箱状态变化事件 */
					case DRI_CHARGERTOP:/** 充电盖状态变化事件 */
					case DRI_HEADLIGHT:/** 大灯状态变化事件 */
					case DRI_DOUBLELAMP:/** 双跳状态变化事件 */
					case DRI_FORGLAMP:/** 雾灯状态变化事件 */
					case DRI_LITTLELAMP:/** 小灯状态变化事件 */
					case DRI_ASSISTEDS:/** 助力转向状态变化事件 */
					case DRI_BATDOOR:/** 电池舱门状态变化事件 */
					case DRI_BACKFOG:/** 后雾灯状态变化事件 */
					case DRI_BCM_ONLINE:
						/** BCM状态变化事件 */
						try {
							EventBus.getDefault().postSticky(
									new com.kandi.event.DRI_CAR_BCM_Event(bundle));
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case DRI_SYS_CFG:
						sendBroadcast(new Intent("com.kangdi.paramreturn"));
						break;
					case DRI_MFLSTATUS:
						int[] status_array = bundle.getIntArray("KD_CAST_EVENT"+20);
						Intent intent_status = new Intent();
						for(int i=0;i<status_array.length;i++){
							if(status_array[i] != 0){
								if(i == 5){
									if(status_array[i] == 2){
										intent_status.setAction(mfl_status[8]);
									}else{
										intent_status.setAction(mfl_status[i]);
									}
								}else{
									intent_status.setAction(mfl_status[i]);
								}
								context.sendBroadcast(intent_status);
							}
						}
						break;
					}
				}
			}else if(intent.getAction().equals(ACTION_WHEEL_MODE)){
				if(!CommonUtils.isFastDoubleClick(400)){
					if(count >= 3){
						count = 0;
					}
					Intent modeintent = null;
					if(count == 0){
						modeintent = new Intent(context, RadioActivity.class);
					}else if(count == 1){
						modeintent = new Intent(context, EntertainmentFragmentActivity.class);
					}else if(count == 2){
						modeintent = new Intent(context, EntertainmentFragmentVideoActivity.class);
					}
					modeintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(modeintent);
					count++;
				}
			}else if(intent.getAction().equals(ACTION_WHEEL_VOLUMEADD)){
				if(!CommonUtils.isFastDoubleClick(200)){
					Intent i = new Intent(context, WheelVolumeActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.putExtra("direction", true);
					context.startActivity(i);
				}
			}else if(intent.getAction().equals(ACTION_WHEEL_VOLUMEREDUCE)){
				if(!CommonUtils.isFastDoubleClick(200)){
					Intent i = new Intent(context, WheelVolumeActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.putExtra("direction", false);
					context.startActivity(i);
				}
			}else if(intent.getAction().equals(ACTION_WHEEL_HANGUP)){
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							btservice = IKdBtService.Stub.asInterface(ServiceManager.getService("bt"));
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
	}
}
