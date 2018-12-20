package com.kandi.view;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IKdAudioControlService;
import android.os.IKdBtService;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.driverlayer.kdos_driverServer.BlueDriver;
import com.kandi.application.BaseApplication;
import com.kandi.base.BaseActivity;
import com.kandi.event.ConnectBlueEvent;
import com.kandi.event.DRI_INSERT_CHARGEREvent;
import com.kandi.event.PlayBlueCallEndEvent;
import com.kandi.event.PlayCallOutGoingEvent;
import com.kandi.event.PlayCallShowNumEvent;
import com.kandi.event.base.BaseEvent;
import com.kandi.home.R;
import com.kandi.util.ACacheUtil;

import de.greenrobot.event.EventBus;

public class DialActivity extends BaseActivity{
	public static DialActivity instance;
	private IKdBtService btservice;
	IKdAudioControlService audioservice;
	private BlueDriver bluedriver = BaseApplication.bluedriver;
	String numbers = "";
	String calltimes = "";
	String phonename = "";
	private String phoneNum = "";
	private int[] state = new int[1];
	
	boolean flag = true;
	BlueDriver bl;
	
	private FrameLayout inputlayout;
	private TableLayout inputbtns;
	private ImageButton btn_1;
	private ImageButton btn_2;
	private ImageButton btn_3;
	private ImageButton btn_4;
	private ImageButton btn_5;
	private ImageButton btn_6;
	private ImageButton btn_7;
	private ImageButton btn_8;
	private ImageButton btn_9;
	private ImageButton btn_0;
	private ImageButton btn_star;
	private ImageButton btn_jin;
	private ImageButton btn_back;
	private ImageButton btn_call;
	private ImageButton btn_hangup;
	private TableRow call_space;
	private TableRow hangup_space;
	private EditText input;
	private TextView state_ringcall;
	private TextView contact_name;
	public static String simphonenumber = "";
	public static String bluephonename = "";
	ACacheUtil acache;
	
	public static boolean CALLGOING = false;
	private final int CALLGOING_ACTION_MSG = 1000;
	private final int CALLSHOWNUM_ACTION_MSG = 1002;
	private final int CALLNUMBER = 1001;
	String[] btorsim = new String[]{"1"};
	
	private View.OnClickListener btnCL = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			String txt = arg0.getTag()+"";
			String inputTxt = input.getText()+"";
			input.setText(inputTxt+txt);
			if(!flag){
				try {
					if(btservice != null){
						btservice.btSendDtmf(txt);
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (NoSuchMethodError e) {
					e.printStackTrace();
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}
	};
	private void initEvent(){
		EventBus.getDefault().register(this,"onConnectBlue",ConnectBlueEvent.class);
		EventBus.getDefault().register(this, "onPlayBlueCallEnd",PlayBlueCallEndEvent.class);
		EventBus.getDefault().register(this,"PlayCallOutGoing",PlayCallOutGoingEvent.class);
		EventBus.getDefault().register(this,"PlayCallShowNum",PlayCallShowNumEvent.class);
	}
	private void initView(){
		input = (EditText) findViewById(R.id.input);
		input.setFocusable(false);
		btn_1 = (ImageButton) findViewById(R.id.btn_1);
		btn_1.setTag("1");
		btn_1.setOnClickListener(btnCL);
		
		btn_2 = (ImageButton) findViewById(R.id.btn_2);
		btn_2.setTag("2");
		btn_2.setOnClickListener(btnCL);
		
		btn_3 = (ImageButton) findViewById(R.id.btn_3);
		btn_3.setTag("3");
		btn_3.setOnClickListener(btnCL);
		
		btn_4 = (ImageButton) findViewById(R.id.btn_4);
		btn_4.setTag("4");
		btn_4.setOnClickListener(btnCL);
		
		btn_5 = (ImageButton) findViewById(R.id.btn_5);
		btn_5.setTag("5");
		btn_5.setOnClickListener(btnCL);
		
		btn_6 = (ImageButton) findViewById(R.id.btn_6);
		btn_6.setTag("6");
		btn_6.setOnClickListener(btnCL);
		
		btn_7 = (ImageButton) findViewById(R.id.btn_7);
		btn_7.setTag("7");
		btn_7.setOnClickListener(btnCL);
		
		btn_8 = (ImageButton) findViewById(R.id.btn_8);
		btn_8.setTag("8");
		btn_8.setOnClickListener(btnCL);
		
		btn_9 = (ImageButton) findViewById(R.id.btn_9);
		btn_9.setTag("9");
		btn_9.setOnClickListener(btnCL);
		
		btn_0 = (ImageButton) findViewById(R.id.btn_0);
		btn_0.setTag("0");
		btn_0.setOnClickListener(btnCL);
		
		btn_jin = (ImageButton) findViewById(R.id.btn_jin);
		btn_jin.setTag("#");
		btn_jin.setOnClickListener(btnCL);
		
		btn_star = (ImageButton) findViewById(R.id.btn_star);
		btn_star.setTag("*");
		btn_star.setOnClickListener(btnCL);
		
		btn_call = (ImageButton) findViewById(R.id.btn_call);
		btn_call.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				call(input.getText().toString());
			}
		});
		
		btn_hangup = (ImageButton) findViewById(R.id.btn_hangup);
		btn_hangup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				handler.sendEmptyMessage(110);
//				try {
//					if(bluedriver!=null){
//						state = new int[1];
//						bluedriver.getCountState(btorsim,state);
//						if(state[0]==0){
//							handler.sendEmptyMessage(104);
//						}
//					}
//				} catch (RemoteException e) {
//					e.printStackTrace();
//				}
			}
		});
		
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String inputTxt = input.getText()+"";
				if(inputTxt.length()>0){
					input.setText(inputTxt.substring(0, inputTxt.length()-1));
				}
			}
		});
		btn_back.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View arg0) {
				input.setText("");
				return true;
			}
		});
		
		inputlayout = (FrameLayout) findViewById(R.id.inputlayout);
		inputlayout.setOnClickListener(null);
		
		inputbtns = (TableLayout) findViewById(R.id.inputbtns);
		inputbtns.setOnClickListener(null);
		
		call_space = (TableRow) findViewById(R.id.call_space);
		hangup_space = (TableRow) findViewById(R.id.hangup_space);
		
		state_ringcall = (TextView) findViewById(R.id.state_ringcall);
		contact_name = (TextView) findViewById(R.id.contact_name);
		setNumberButtonsEnabled(false);
		btn_call.setEnabled(false);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_call_main_layout);
		btservice = IKdBtService.Stub.asInterface(ServiceManager.getService("bt"));
		audioservice = IKdAudioControlService.Stub.asInterface(ServiceManager.getService("audioCtrl"));
		acache = ACacheUtil.get();
		instance = this;
		this.initView();
		this.initEvent();
		
	    phoneNum = acache.getAsString("phonenum");
		if(null != phoneNum){
			input.setText(phoneNum);
		}
	    setNumberButtonsEnabled(false);
	    //one_Key_intent = true;//到时候要去掉，这是测sim卡的
	    this.refreshView();
	    new Thread(new Runnable() {
	    	
	    	@Override
	    	public void run() {
	    		try {
	    			state = new int[1];
	    			if(bluedriver != null){
	    				while (true) {
	    					try {
	    						Thread.sleep(100);
	    					} catch (InterruptedException e) {
	    						e.printStackTrace();
	    					}
	    					bluedriver.getCountState(btorsim,state);
	    					while (state[0] == 1) {
	    						try {
	    							Thread.sleep(400);
	    						} catch (InterruptedException e) {
	    							e.printStackTrace();
	    						}
	    						String[] data = new String[2];
	    						bluedriver.getCountData(data);
	    						calltimes = data[0];
	    						if("".equals(calltimes)){
	    							handler.sendEmptyMessage(108);
	    						}else{
	    							flag = false;
	    							handler.sendEmptyMessage(106);
	    						}
	    						bluedriver.getCountState(btorsim,state);
	    						if(state[0]==0){
	    							handler.sendEmptyMessage(104);
	    							break;
	    						}
	    					}
	    					flag = true;
	    				}
	    			}
	    		} catch (RemoteException e) {
	    			e.printStackTrace();
	    		}
	    	}
	    }).start();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		registBroadCastReceiver();
		super.onResume();
		if(CALLGOING){
			handler.sendEmptyMessage(CALLGOING_ACTION_MSG);
		}
	}
	
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		//setNumberButtonsEnabled(!one_Key_intent);
		setNumberButtonsEnabled(false);
		phoneNum = acache.getAsString("phonenum");
		if(null != phoneNum){
			input.setText(phoneNum);
		}
		this.refreshView();
	}
	
	@Override
	protected void onDestroy() {
		unregistBroadCastReceiver();
		EventBus.getDefault().unregister(this,ConnectBlueEvent.class);
		EventBus.getDefault().unregister(this,PlayBlueCallEndEvent.class);
		EventBus.getDefault().unregister(this,PlayCallOutGoingEvent.class);
		EventBus.getDefault().unregister(this,PlayCallShowNumEvent.class);
		super.onDestroy();
	}
	
	private void refreshView() {
		handler.sendEmptyMessage(101);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(bluestate){
					handler.sendEmptyMessage(102);
				}else{
					handler.sendEmptyMessage(103);
				}
			}
		}).start();
	}
	
	private void call(final String phone) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(btservice!=null){
					try {
						if(!phone.matches(".*[a-z]+.*") && !"".equals(phone) && phone.length() > 1){
							if(btservice.btDial(phone)==0){
								//Log.i("BluDialAcitivity", "拨通成功");
								bluephonename = phone;
							}else{
								//Log.i("BluDialAcitivity", "拨通失败");
							}
						}
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		if(!phone.matches(".*[a-z]+.*") && !"".equals(phone) && phone.length() > 1){
			handler.sendEmptyMessage(100);
		}
	}
	public static boolean shutdown = false;
	private void hangup() {
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
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 100:
				call_space.setVisibility(View.GONE);
				hangup_space.setVisibility(View.VISIBLE);
				phoneNum = input.getText().toString();
				if(!"".equals(phoneNum)){
					try {
						phonename = getPhoneName(btservice.getContactsJsonString(), phoneNum);
						contact_name.setText(phonename);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					state_ringcall.setText(getString(R.string.calling));//正在呼叫
					call_space.setVisibility(View.GONE);
					hangup_space.setVisibility(View.VISIBLE);
				}
				break;
			case 101:
				setNumberButtonsEnabled(false);
				btn_call.setEnabled(false);
				break;
			case 102:
				setNumberButtonsEnabled(true);
				btn_call.setEnabled(true);
				state_ringcall.setText("");
				call_space.setVisibility(View.VISIBLE);
				hangup_space.setVisibility(View.GONE);
				break;
			case 103://请连接蓝牙
				input.setText("");
				state_ringcall.setText(getString(R.string.please_conn_blue));
				setNumberButtonsEnabled(false);
				btn_call.setEnabled(false);
				btn_hangup.setEnabled(false);
				call_space.setVisibility(View.VISIBLE);
				hangup_space.setVisibility(View.GONE);
				CALLGOING = false;
				break;
			case 104://通话结束
				setNumberButtonsEnabled(false);
				btn_call.setEnabled(false);
				btn_hangup.setEnabled(false);
				state_ringcall.setText(getString(R.string.call_end));
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						call_space.setVisibility(View.VISIBLE);
						hangup_space.setVisibility(View.GONE);
						if(bluestate){
							setNumberButtonsEnabled(true);
							btn_hangup.setEnabled(true);
							btn_call.setEnabled(true);
						}else{
							setNumberButtonsEnabled(false);
							btn_hangup.setEnabled(false);
							btn_call.setEnabled(false);
						}
						state_ringcall.setText("");
						contact_name.setText("");
						phonename = "";
						phoneNum = "";
						acache.remove("phonenum");
					}
				}, 3000);
				break;
			case 106:
				if("vwcsy001_M_v1_4".equals(SystemProperties.get("sys.kd.hardwareversion","vwcsy001_M_v1_4"))){
					setNumberButtonsEnabled(false);
				}
				btn_call.setEnabled(true);
				btn_hangup.setEnabled(true);
				state_ringcall.setText(calltimes);
				try {
					phoneNum = acache.getAsString("phonenum");
					if(phoneNum != null){
						phonename = getPhoneName(btservice.getContactsJsonString(), phoneNum);
						contact_name.setText(phonename);
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				call_space.setVisibility(View.GONE);
				hangup_space.setVisibility(View.VISIBLE);
				break;
			case 107://未插入SIM
				state_ringcall.setText(getString(R.string.sim_unin));
				setNumberButtonsEnabled(false);
				btn_call.setEnabled(false);
				break;
			case 108:
				state_ringcall.setText("");
				call_space.setVisibility(View.VISIBLE);
				hangup_space.setVisibility(View.GONE);
				setNumberButtonsEnabled(false);
				btn_call.setEnabled(false);
				break;
			case 109:
				setNumberButtonsEnabled(false);
				btn_call.setEnabled(false);
				state_ringcall.setText(getString(R.string.yet_open_function));
				input.setText("");
				break;
			case 110:
				hangup();
				break;
			case 111:
				state_ringcall.setText(getString(R.string.yet_open_function));
				input.setText("");
				call_space.setVisibility(View.VISIBLE);
				hangup_space.setVisibility(View.GONE);
				setNumberButtonsEnabled(false);
				break;
			case CALLGOING_ACTION_MSG:
				contact_name.setText(phonename);
				phoneNum = acache.getAsString("phonenum");
				if(null != phoneNum){
					input.setText(phoneNum);
				}
				state_ringcall.setText(getString(R.string.calling));//正在呼叫
				call_space.setVisibility(View.GONE);
				hangup_space.setVisibility(View.VISIBLE);
				btn_hangup.setEnabled(true);
				break;
			case CALLSHOWNUM_ACTION_MSG:
				phoneNum = acache.getAsString("phonenum");
				if(null != phoneNum){
					input.setText(phoneNum);
				}
				break;
			}
		}
		
	};
	
	private void setNumberButtonsEnabled(boolean enabled){
		btn_0.setEnabled(enabled);
		btn_1.setEnabled(enabled);
		btn_2.setEnabled(enabled);
		btn_3.setEnabled(enabled);
		btn_4.setEnabled(enabled);
		btn_5.setEnabled(enabled);
		btn_6.setEnabled(enabled);
		btn_7.setEnabled(enabled);
		btn_8.setEnabled(enabled);
		btn_9.setEnabled(enabled);
		btn_jin.setEnabled(enabled);
		btn_star.setEnabled(enabled);
		btn_back.setEnabled(enabled);
	}
	
	public void onConnectBlue(ConnectBlueEvent event) {
		refreshView();
	}
	
	public void onPlayBlueCallEnd(PlayBlueCallEndEvent event) {
		handler.sendEmptyMessage(104);
	}
	
	public void PlayCallOutGoing(PlayCallOutGoingEvent event){
		handler.sendEmptyMessage(CALLGOING_ACTION_MSG);
	}
	
	public void PlayCallShowNum(PlayCallShowNumEvent event){
		handler.sendEmptyMessage(CALLSHOWNUM_ACTION_MSG);
	}
	
	public static boolean bluestate = false;
	String callingtime;
	public static void setBlueState(boolean state){
		bluestate = state;
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
	
	BlueDialChanageReceiver blueDialReceiver;
	public final static String ACTION_WHEEL_HANGUP = "com.kangdi.BroadCast.WheelHangup";//多功能方向盘挂断
	
	public class BlueDialChanageReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ACTION_WHEEL_HANGUP)) {
				try {
					if(hangup_space.getVisibility() != View.GONE){
//						handler.sendEmptyMessage(110);
						if(bluedriver!=null){
							state = new int[1];
							bluedriver.getCountState(btorsim,state);
							if(state[0]==0){
								handler.sendEmptyMessage(104);
							}
						}
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void registBroadCastReceiver() {
        if (null == blueDialReceiver) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_WHEEL_HANGUP);
            blueDialReceiver = new BlueDialChanageReceiver();
            getApplicationContext().registerReceiver(blueDialReceiver, filter);
        }
    }

    private void unregistBroadCastReceiver() {
        if (null != blueDialReceiver) {
        	getApplicationContext().unregisterReceiver(blueDialReceiver);
        	blueDialReceiver = null;
        }
    }
    
    /**
     * 获取联系人姓名
     * @param str
     * @param phonenum
     * @return
     */
    
    public String getPhoneName(String str,String phonenum){
    	try {
    		if(!"".equals(str)){
    			JSONArray jsonArr = new JSONArray(str);
    			for(int i=0;i<jsonArr.length();i++){
    				JSONObject obj = (JSONObject)jsonArr.get(i);
    				JSONArray typeAndNumber = (JSONArray)obj.get("TypeAndNumber");
    				for(int j=0;j<typeAndNumber.length() && j<=1;j++){
    					JSONObject obj2 = (JSONObject)typeAndNumber.get(j);
    					String phone = ((String) obj2.get("phone")).replaceAll(" ", "");
    					phone = phone.replace("-", "");
    					if(phonenum.length()<=6){
    						if(phonenum.equals(phone)){
    							return (String) obj.getString("Name");
    						}
    					}else{
    						if(phonenum.equals(phone) || phone.contains(phonenum)){
    							return (String) obj.getString("Name");
    						}
    					}
    				}
    			}
    		}
    	} catch (JSONException e) {
    		e.printStackTrace();
    	}
    	return "";
    }
	
}
