package com.kandi.view;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IKdAudioControlService;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.driverlayer.kdos_driverServer.BlueDriver;
import com.kandi.application.BaseApplication;
import com.kandi.event.PlaySimCallEndEvent;
import com.kandi.home.R;

import de.greenrobot.event.EventBus;

public class OneKeyActivity extends Activity{

	private TextView converse_time;
	private Button btn_call_answer;
	private Button btn_call_hangup;
	private View buttom_shadow;
	private int[] state = new int[1];
	private BlueDriver bluedriver = BaseApplication.bluedriver;
	String numbers = "";
	String calltimes = "";
	String phonename = "";
	public static boolean CALLSTATE;
	
	IKdAudioControlService audioservice;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onekeycall);
		audioservice = IKdAudioControlService.Stub.asInterface(ServiceManager.getService("audioCtrl"));
		initView();
		initEvent();
		View rootView = findViewById(android.R.id.content);
		rootView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
	}
	
	private void initEvent() {
		EventBus.getDefault().register(this, "onPlaySimCallEnd",PlaySimCallEndEvent.class);
	}
	
	public void onPlaySimCallEnd(PlaySimCallEndEvent event){
		mhandler.sendEmptyMessage(104);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		this.setIntent(intent);
		super.onNewIntent(intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(CALLSTATE){
			mhandler.sendEmptyMessage(103);
		}
	}

	private void initView() {
		buttom_shadow = (View) findViewById(R.id.buttom_shadow);
		buttom_shadow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent homeIntent = new Intent();
		        homeIntent = new Intent(getApplicationContext(),MainActivity.class);
		        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        startActivity(homeIntent);
			}
		});
		converse_time = (TextView) findViewById(R.id.converse_time);
		btn_call_answer = (Button) findViewById(R.id.btn_call_answer);
		btn_call_answer.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				TelephonyManager telMgr = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
				if(telMgr.getSimState()==TelephonyManager.SIM_STATE_READY){
					simCardCall(getString(R.string.onekeyrealnum));
					mhandler.sendEmptyMessage(103);
					CALLSTATE = true;
				}
			}
		});
		btn_call_hangup = (Button) findViewById(R.id.btn_call_hangup);
		btn_call_hangup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				endCall(getApplication());
				try {
					if(bluedriver!=null){
						state = new int[1];
						bluedriver.getCountState(new String[]{"0"},state);
						if(state[0]==0){
							mhandler.sendEmptyMessage(104);
						}
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				CALLSTATE = false;
			}
		});
		TelephonyManager telMgr = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
		if(!(telMgr.getSimState()==TelephonyManager.SIM_STATE_READY)){
			mhandler.sendEmptyMessage(102);
			converse_time.setText(getString(R.string.sim_unin));
		}
		CountTimeView();
	}
	
	private void CountTimeView(){
		new Thread(new Runnable() {
	    	
	    	@Override
	    	public void run() {
	    		try {
	    			state = new int[1];
	    			String[] btorsim = new String[]{"0"};
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
	    						String[] data = new String[5];
	    						bluedriver.getCountData(data);
	    						calltimes = data[4];
	    						if("".equals(calltimes)){
    								mhandler.sendEmptyMessage(100);
    							}else{
    								mhandler.sendEmptyMessage(101);
    							}
	    						bluedriver.getCountState(btorsim,state);
	    						if(state[0]==0){
	    							mhandler.sendEmptyMessage(104);
	    							break;
	    						}
	    					}
	    				}
	    			}
	    		} catch (RemoteException e) {
	    			e.printStackTrace();
	    		}
	    	}
	    }).start();
	}
	
	Handler mhandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 100:
				converse_time.setText(calltimes);
				btn_call_answer.setVisibility(View.VISIBLE);
				btn_call_hangup.setVisibility(View.GONE);
				btn_call_answer.setEnabled(false);
				break;
			case 101:
				converse_time.setText(getString(R.string.callingtime)+calltimes);
				btn_call_answer.setVisibility(View.GONE);
				btn_call_hangup.setVisibility(View.VISIBLE);
				break;
			case 102:
				Toast.makeText(getApplication(), getString(R.string.sim_unin), Toast.LENGTH_SHORT);
				btn_call_answer.setEnabled(false);
				break;
			case 103:
				converse_time.setText(getString(R.string.calling));
				btn_call_answer.setVisibility(View.GONE);
				btn_call_hangup.setVisibility(View.VISIBLE);
				break;
			case 104:
				converse_time.setText(getString(R.string.call_end));
				btn_call_answer.setVisibility(View.VISIBLE);
				btn_call_hangup.setVisibility(View.GONE);
				btn_call_answer.setEnabled(true);
				break;
			}
		}
		
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent homeIntent = new Intent();
	        homeIntent = new Intent(this,MainActivity.class);
	        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        startActivity(homeIntent);
            return true;
        }
		return super.onKeyDown(keyCode, event);
	}
	
	private void simCardCall(String phone){
		if(phone != null && !phone.equals("")){
			Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));
	        startActivity(intent);
		}
	}
	
	public void endCall(Context context) {
		try {
			Object telephonyObject = getTelephonyObject(context);
			if (null != telephonyObject) {
				Class telephonyClass = telephonyObject.getClass();
				Method endCallMethod = telephonyClass.getMethod("endCall");
				endCallMethod.setAccessible(true);
				endCallMethod.invoke(telephonyObject);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	private Object getTelephonyObject(Context context) {
		Object telephonyObject = null;
		try {
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			Class telManager = telephonyManager.getClass();
			Method getITelephony = telManager.getDeclaredMethod("getITelephony");
			getITelephony.setAccessible(true);
			telephonyObject = getITelephony.invoke(telephonyManager);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return telephonyObject;
	}

}
