package com.kandi.view;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kandi.customview.HomeMileView;
import com.kandi.driver.DriverServiceManger;
import com.kandi.driver.EcocEnergyInfoDriver;
import com.kandi.event.FinishChargingEvent;
import com.kandi.home.R;

import de.greenrobot.event.EventBus;

public class ChargingActivity extends Activity implements Callback{
	private ImageView chargingpercent;
	private RelativeLayout bgview;
	private Timer timer;
	private Handler handler;
	private int remainMile;
	private HomeMileView hmv;
	private int carSoc;
	private int carKwh;
	private ImageView carimg;
	private ImageView chargingcable;
	private ImageView incharge_state;
	private AnimationDrawable chargeCableAnim;
	private AnimationDrawable chargeBatteryAnim;
	private int theLastState=-999;
	private void refreshPannel() {
		EcocEnergyInfoDriver model = DriverServiceManger.getInstance().getEcocEnergyInfoDriver();

		if(model != null) {
			try {
				model.retreveGeneralInfo();
			}catch (RemoteException e) {
				e.printStackTrace();
				return;
			}
			//将模型数据更新到屏幕...
			carKwh = model.getRemainKWH();
			remainMile = model.getRemainMileage();
			hmv.setMile(remainMile, carKwh, carSoc);

			carSoc = (int)(model.getSOC()+0.5);
			changePercent(carSoc);
			
			if(model.getChargeGunState() == 0) {
				finish();	//当充电枪插入/拔出事件间隔太短时，可能无法响应拔出事件，通过状态查询更新状态״̬
			}
			else {
				//根据充电事件更新UI效果
				setChargingEffert(model.getCargingState());
			}

		}
		else {
			//ToastUtil.showToast(this.getApplicationContext(), "警告：后台服务未启动!(002)", Toast.LENGTH_LONG);
			Log.e("KDSERVICE", "MainActivity.refreshPannel() service is null");
		}
	}
	private void initEvent(){
		EventBus.getDefault().register(this,"FinishChargingEvent",FinishChargingEvent.class);
	}
	private void initView(){
		chargingcable = (ImageView) findViewById(R.id.chargingcable);
		chargingcable.setImageResource(R.drawable.chargingcableanim);
		carimg = (ImageView) findViewById(R.id.carimg);
		chargingpercent = (ImageView) findViewById(R.id.chargingpercent);
		incharge_state  = (ImageView) findViewById(R.id.incharge_state);
		charge_text = (TextView)findViewById(R.id.charge_text);
		bgview = (RelativeLayout) findViewById(R.id.bgview);
		bgview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		hmv = (HomeMileView) findViewById(R.id.charingtxtarea);
		
		hmv.setMile(remainMile, carKwh, carSoc);

		setChargingEffert(-1); //default start (stop charge)
//		refreshPannel();
}
	private void initTimer(){
		if(timer == null){
			timer = new Timer();
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					handler.sendEmptyMessage(0);
				}
			}, 500, 1000);
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.charge_pannel);
		handler = new Handler(this);
		this.initEvent();
		this.initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initTimer();
	}
	@Override
	protected void onPause() {
		super.onPause();
		if(timer != null){
			timer.cancel();
			timer = null;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(timer != null){
			timer.cancel();
			timer = null;
		}
		EventBus.getDefault().unregister(this,FinishChargingEvent.class);
	}
	public void FinishChargingEvent( FinishChargingEvent event) {
//		Intent intent = new Intent(this, ChargeGunActivity.class);
//		intent.putExtra("chargetype", "end");
//		startActivity(intent);
		finish();
	}
	
	private final int rDrawableChargingProgress[] = {
			R.drawable.charging_car_effect_chassis_energy_block1,
			R.drawable.charging_car_effect_chassis_energy_block2,
			R.drawable.charging_car_effect_chassis_energy_block3,
			R.drawable.charging_car_effect_chassis_energy_block4,
			R.drawable.charging_car_effect_chassis_energy_block5,
			R.drawable.charging_car_effect_chassis_energy_block6,
			R.drawable.charging_car_effect_chassis_energy_block7,
			R.drawable.charging_car_effect_chassis_energy_block8,
			R.drawable.charging_car_effect_chassis_energy_block9,
			R.drawable.charging_car_effect_chassis_energy_block10,
			R.drawable.charging_car_effect_chassis_energy_block11,
			R.drawable.charging_car_effect_chassis_energy_block12,
			R.drawable.charging_car_effect_chassis_energy_block13,
			R.drawable.charging_car_effect_chassis_energy_block14,
			R.drawable.charging_car_effect_chassis_energy_block15,
			R.drawable.charging_car_effect_chassis_energy_block16,
			R.drawable.charging_car_effect_chassis_energy_block17,
			R.drawable.charging_car_effect_chassis_energy_block18,
			R.drawable.charging_car_effect_chassis_energy_block19,
			R.drawable.charging_car_effect_chassis_energy_block20,
			R.drawable.charging_car_effect_chassis_energy_block21,
			R.drawable.charging_car_effect_chassis_energy_block22,
			R.drawable.charging_car_effect_chassis_energy_block23,
			R.drawable.charging_car_effect_chassis_energy_block24,
			R.drawable.charging_car_effect_chassis_energy_block25,
	};
	private TextView charge_text;
	
	private void changePercent(int soc){
		
		int i = (int)((double)soc/100.0*(rDrawableChargingProgress.length-1)+0.5);
		
		if(i<0) {
			i=0;
		}
		else if (i >= rDrawableChargingProgress.length) { 
			i = rDrawableChargingProgress.length - 1;
		}
		
		chargingpercent.setImageResource(rDrawableChargingProgress[i]);
		charge_text.setText(soc+"%");
	}

	
	private void setChargingEffert(int state) {
		if(theLastState == state) return;
		//*  充电起停    **  key+3       **  0:停止；1:启动 2:故障停止    **  int     *
		Drawable drawable;
		switch(state) {
   		case -1:	//初始状态̬
   			carimg.setImageResource(R.drawable.charging_car_effect_car);
   			chargeCableAnim = (AnimationDrawable) chargingcable.getDrawable(); 
   			chargeCableAnim.stop();
   			incharge_state.setVisibility(View.GONE);
   			charge_text.setVisibility(View.GONE);
   			break;
   		case 0:	//0:停止ֹ
   			carimg.setImageResource(R.drawable.charging_car_effect_car);
   			chargeCableAnim = (AnimationDrawable) chargingcable.getDrawable(); 
   			chargeCableAnim.stop();
   			incharge_state.setImageResource(R.drawable.battery_over);
   			charge_text.setVisibility(View.GONE);
   			drawable = incharge_state.getDrawable();
   			if(drawable.getClass().getName() == "AnimationDrawable") {
	   			chargeBatteryAnim = (AnimationDrawable) drawable;  
	   			chargeBatteryAnim.stop();
   			}
   			break;
   		case 1:	//1:启动
   			carimg.setImageResource(R.drawable.charging_car_effect_car);
   			chargeCableAnim = (AnimationDrawable) chargingcable.getDrawable();  
   			chargeCableAnim.start();
   			incharge_state.setVisibility(View.VISIBLE);
   			charge_text.setVisibility(View.VISIBLE);
   			incharge_state.setImageResource(R.drawable.chargingbattery_anim);
   			chargeBatteryAnim = (AnimationDrawable) incharge_state.getDrawable();  
   			chargeBatteryAnim.start();
   			break;
   		case 2:	// 2:故障停止
   		default:
   			carimg.setImageResource(R.drawable.charging_car_effect_car_abnormal);
   			chargeCableAnim = (AnimationDrawable) chargingcable.getDrawable(); 
   			chargeCableAnim.stop();
   			incharge_state.setVisibility(View.VISIBLE);
   			charge_text.setVisibility(View.GONE);
   			incharge_state.setImageResource(R.drawable.battery_yichang);
   			drawable = incharge_state.getDrawable();
   			if(drawable.getClass().getName() == "AnimationDrawable") {
	   			chargeBatteryAnim = (AnimationDrawable) drawable;  
	   			chargeBatteryAnim.stop();
   			}
   		}
		
		theLastState=state;
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		refreshPannel();
		return false;
	}
	/*@Override
	public void VolumeClickEvent(com.kandi.event.VolumeClickEvent event) {
		
	}
	@Override
	public void DRI_INSERT_CHARGEREvent(
			com.kandi.event.DRI_INSERT_CHARGEREvent event) {
		
	}
	@Override
	public void CarChargingEvent(com.kandi.event.CarChargingEvent event) {
		
	}
	@Override
	public void DRI_CHARGER_ONOFFEvent(
			com.kandi.event.DRI_CHARGER_ONOFFEvent event) {
		
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
				*//** 充电抢插入状态变化事件 *//*
				try {
					EventBus.getDefault().postSticky(
							new DRI_INSERT_CHARGEREvent(bundle));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	
	}*/
}
