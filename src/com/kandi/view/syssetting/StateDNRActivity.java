package com.kandi.view.syssetting;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.kandi.base.BaseWinActivity;
import com.kandi.driver.DriverServiceManger;
import com.kandi.driver.StateDNRInfoDriver;
import com.kandi.home.R;
import com.kandi.util.CommonUtils;

public class StateDNRActivity extends BaseWinActivity{
	
	private RelativeLayout stateview;
	private ToggleButton state_d;
	private ToggleButton state_n;
	private ToggleButton state_r;
	private StateDNRInfoDriver model;
	private int dpoint[];
	private int npoint[];
	private int rpoint[];
	
	private TextView dSelectedItem;
	public static Timer time = new Timer();
	
	public static StateDNRActivity instance = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(LayoutParams.FLAG_NOT_TOUCH_MODAL, LayoutParams.FLAG_NOT_TOUCH_MODAL);
		this.getWindow().setFlags(LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.home_statednr_layout);
		instance = this;
		this.initView();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(instance != null){
					m_hadler.sendEmptyMessage(103);
					try {
						Thread.sleep(1300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private void initView() {
		stateview = (RelativeLayout) findViewById(R.id.stateview);
		
		state_d = (ToggleButton) findViewById(R.id.state_d);
		state_n = (ToggleButton) findViewById(R.id.state_n);
		state_r = (ToggleButton) findViewById(R.id.state_r);
		dpoint = new int[2];
		npoint = new int[2];
		rpoint = new int[2];
		
		
		state_d.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(state_n.isChecked()){
					state_n.setChecked(false);
					state_n.setVisibility(View.INVISIBLE);
					try {
						model.setCarDNRInfo(0x01);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				state_d.setChecked(true);
				state_d.setVisibility(View.VISIBLE);
				if(state_r.isChecked()){
					state_d.setChecked(false);
					state_d.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		state_n.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(state_d.isChecked()){
					state_d.setChecked(false);
					state_d.setVisibility(View.INVISIBLE);
				}
				if(state_r.isChecked()){
					state_r.setChecked(false);
					state_r.setVisibility(View.INVISIBLE);
				}
				try {
					model.setCarDNRInfo(0x02);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				state_n.setChecked(true);
				state_n.setVisibility(View.VISIBLE);
			}
		});
		
		state_r.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(state_n.isChecked()){
					state_n.setChecked(false);
					state_n.setVisibility(View.INVISIBLE);
					try {
						model.setCarDNRInfo(0x03);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				state_r.setChecked(true);
				state_r.setVisibility(View.VISIBLE);
				if(state_d.isChecked()){
					state_r.setChecked(false);
					state_r.setVisibility(View.INVISIBLE);
				}
			}
		});
		dSelectedItem = new TextView(StateDNRActivity.this);
		stateview.setOnTouchListener(new View.OnTouchListener() {
			int RawX,RawY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				state_d.getLocationOnScreen(dpoint);
				state_n.getLocationOnScreen(npoint);
				state_r.getLocationOnScreen(rpoint);
				int state = event.getAction();
				switch (state) {
				case MotionEvent.ACTION_DOWN:
					break;

				case MotionEvent.ACTION_MOVE:
					
					break;
					
				case MotionEvent.ACTION_UP:
					if(!CommonUtils.isFastDoubleClick()){
						RawX = (int) event.getRawX();
						RawY = (int) event.getRawY();
						if(RawX <= (dpoint[0]+state_d.getMeasuredWidth()) && RawX >= dpoint[0]
								&& RawY >= dpoint[1] && RawY <= (dpoint[1]+state_d.getMeasuredHeight())){
							if(state_n.isChecked()){
								state_n.setChecked(false);
								state_n.setVisibility(View.INVISIBLE);
								try {
									model.setCarDNRInfo(0x01);
								} catch (RemoteException e) {
									e.printStackTrace();
								}
								if(dSelectedItem!=null){
									stateview.removeView(dSelectedItem);
								}
								dSelectedItem.setBackgroundResource(R.drawable.gearshift_n_on);
								stateview.addView(dSelectedItem);
								SetImageSlide(dSelectedItem, 30, 30, 360, 18);
								time.schedule(new TimerTask() {
									
									@Override
									public void run() {
										m_hadler.sendEmptyMessage(101);
									}
								}, 140);
							}
							if(state_r.isChecked()){
								state_d.setChecked(false);
								state_d.setVisibility(View.INVISIBLE);
							}
						}
						if(RawX <= (npoint[0]+state_n.getMeasuredWidth()) && RawX >= npoint[0]
								&& RawY >= npoint[1] && RawY <= (npoint[1]+state_n.getMeasuredHeight())){
							if(state_d.isChecked()){
								state_d.setChecked(false);
								state_d.setVisibility(View.INVISIBLE);
								if(dSelectedItem!=null){
									stateview.removeView(dSelectedItem);
								}
								dSelectedItem.setBackgroundResource(R.drawable.gearshift_d_on);
								stateview.addView(dSelectedItem);
								SetImageSlide(dSelectedItem, 30, 30, 18, 371);
							}
							if(state_r.isChecked()){
								state_r.setChecked(false);
								state_r.setVisibility(View.INVISIBLE);
								if(dSelectedItem!=null){
									stateview.removeView(dSelectedItem);
								}
								dSelectedItem.setBackgroundResource(R.drawable.gearshift_r_on);
								stateview.addView(dSelectedItem);
								SetImageSlide(dSelectedItem, 330, 30, 371, 371);
							}
							try {
								model.setCarDNRInfo(0x02);
							} catch (RemoteException e) {
								e.printStackTrace();
							}
							time.schedule(new TimerTask() {
								
								@Override
								public void run() {
									m_hadler.sendEmptyMessage(100);
								}
							}, 150);
						}
						if(RawX <= (rpoint[0]+state_r.getMeasuredWidth()) && RawX >= rpoint[0]
								&& RawY >= rpoint[1] && RawY <= (rpoint[1]+state_r.getMeasuredHeight())){
							if(state_n.isChecked()){
								state_n.setChecked(false);
								state_n.setVisibility(View.INVISIBLE);
								try {
									model.setCarDNRInfo(0x03);
								} catch (RemoteException e) {
									e.printStackTrace();
								}
								if(dSelectedItem!=null){
									stateview.removeView(dSelectedItem);
								}
								dSelectedItem.setBackgroundResource(R.drawable.gearshift_n_on);
								stateview.addView(dSelectedItem);
								SetImageSlide(dSelectedItem, 30, 330, 371, 371);
								time.schedule(new TimerTask() {
									
									@Override
									public void run() {
										m_hadler.sendEmptyMessage(102);
									}
								}, 150);
							}
							if(state_d.isChecked()){
								state_r.setChecked(false);
								state_r.setVisibility(View.INVISIBLE);
							}
						}
						if(!((RawX <= (dpoint[0]+state_d.getMeasuredWidth()) && RawX >= dpoint[0]
								&& RawY >= dpoint[1] && RawY <= (dpoint[1]+state_d.getMeasuredHeight()))
								||(RawX <= (npoint[0]+state_n.getMeasuredWidth()) && RawX >= npoint[0]
										&& RawY >= npoint[1] && RawY <= (npoint[1]+state_n.getMeasuredHeight()))
										||(RawX <= (rpoint[0]+state_r.getMeasuredWidth()) && RawX >= rpoint[0]
												&& RawY >= rpoint[1] && RawY <= (rpoint[1]+state_r.getMeasuredHeight())))){
							StateDNRActivity.instance = null;
							finish();
						}
					}
					break;
				}
				return true;
			}
		});
		
	}



	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
			StateDNRActivity.instance = null;
			finish();      
			return true;    
		}
		return super.onTouchEvent(event); 
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//m_hadler.sendEmptyMessage(103);
	}

	private void refreshPannel() {
		model = DriverServiceManger.getInstance().getStateDNRInfoDriver();
		if(model!=null){
			try {
				int dnrstate = model.getCarDNRInfo();
				if(dnrstate == 0x01){
					state_d.setChecked(true);
					state_n.setChecked(false);
					state_r.setChecked(false);
					state_d.setVisibility(View.VISIBLE);
					state_n.setVisibility(View.INVISIBLE);
					state_r.setVisibility(View.INVISIBLE);
//					if(CoverDNRActivity.instance == null){
//						Intent intent = new Intent(this,CoverDNRActivity.class);
//						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						startActivity(intent);
//					}
				}else if(dnrstate == 0x02){
					state_d.setChecked(false);
					state_n.setChecked(true);
					state_r.setChecked(false);
					state_d.setVisibility(View.INVISIBLE);
					state_n.setVisibility(View.VISIBLE);
					state_r.setVisibility(View.INVISIBLE);
//					if(CoverDNRActivity.instance != null){
//						CoverDNRActivity.instance.finish();
//						CoverDNRActivity.instance = null;
//					}
				}else if(dnrstate == 0x03){
					state_d.setChecked(false);
					state_n.setChecked(false);
					state_r.setChecked(true);
					state_d.setVisibility(View.INVISIBLE);
					state_n.setVisibility(View.INVISIBLE);
					state_r.setVisibility(View.VISIBLE);
//					if(CoverDNRActivity.instance == null){
//						Intent intent = new Intent(this,CoverDNRActivity.class);
//						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						startActivity(intent);
//					}
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			//Toast.makeText(this, "警告：后台服务未启动!(002)", Toast.LENGTH_LONG).show();
			Log.e("KDSERVICE", "StateDNRActivity.initView service is null");
		}
	}
	
	public void SetImageSlide(View v, int startX, int toX, int startY, int toY) {
		TranslateAnimation anim = new TranslateAnimation(startX, toX, startY, toY);
		anim.setDuration(150);
		anim.setFillAfter(true);
		v.startAnimation(anim);
	}
	

	@SuppressLint("HandlerLeak")
	Handler m_hadler = new Handler(){
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			switch(msg.what){
			case 100:
				stateview.removeView(dSelectedItem);
				state_n.setChecked(true);
				state_n.setVisibility(View.VISIBLE);
//				if(CoverDNRActivity.instance != null){
//					CoverDNRActivity.instance.finish();
//					CoverDNRActivity.instance = null;
//				}
				break;
			case 101:
				stateview.removeView(dSelectedItem);
				state_d.setChecked(true);
				state_d.setVisibility(View.VISIBLE);
//				if(CoverDNRActivity.instance == null){
//					Intent intent = new Intent(getApplicationContext(),CoverDNRActivity.class);
//					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					startActivity(intent);
//				}
				break;
			case 102:
				stateview.removeView(dSelectedItem);
				state_r.setChecked(true);
				state_r.setVisibility(View.VISIBLE);
//				if(CoverDNRActivity.instance == null){
//					Intent intent = new Intent(getApplicationContext(),CoverDNRActivity.class);
//					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					startActivity(intent);
//				}
				break;
			case 103:
				refreshPannel();
				break;
			}
		}
	};

	
}
