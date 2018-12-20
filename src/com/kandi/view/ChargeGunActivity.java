package com.kandi.view;

import java.util.Set;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.kandi.base.BaseActivity;
import com.kandi.event.DRI_INSERT_CHARGEREvent;
import com.kandi.event.FinishChargeGunEvent;
import com.kandi.event.base.BaseEvent;
import com.kandi.home.R;

import de.greenrobot.event.EventBus;

public class ChargeGunActivity extends BaseActivity{
	private ImageView chargeGunView;
	private AnimationDrawable chargeAnim;
	private TextView chargeGunSts;
	private Handler handler;
	private String chargetype;
	private void initEvent(){
		EventBus.getDefault().register(this,"FinishChargeGunEvent",FinishChargeGunEvent.class);
	}
	private void initView(){
		chargeGunSts = (TextView) findViewById(R.id.chargegunsts);
		chargeGunView = (ImageView) findViewById(R.id.chargegun);
		if(this.chargetype.equals("start")){
			chargeGunView.setImageResource(R.drawable.chargegunanim);
			chargeGunSts.setText(getString(R.string.charggun_in));
			
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent(ChargeGunActivity.this, ChargingActivity.class);
					ChargeGunActivity.this.startActivity(intent);
					finish();
				}
			}, 3000);
		}else if(this.chargetype.equals("end")){
			chargeGunView.setImageResource(R.drawable.chargegunoutanim);
			chargeGunSts.setText(getString(R.string.charggun_out));
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					finish();
				}
			}, 3000);
		}
		chargeAnim = (AnimationDrawable) chargeGunView.getDrawable();  
		chargeAnim.start();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chargegun_pannel);
		handler = new Handler();
		this.chargetype = this.getIntent().getStringExtra("chargetype");
		Log.i("charge", "ChargeGunActivity, charegetype:"+chargetype);
		if(chargetype==null){
			chargetype = "start";
		}
		this.initEvent();
		this.initView();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this,FinishChargeGunEvent.class);
	}
	public void FinishChargeGunEvent(
			FinishChargeGunEvent event) {
		chargeGunView.setImageResource(R.drawable.chargegunoutanim);  
		chargeAnim = (AnimationDrawable) chargeGunView.getDrawable();  
		chargeAnim.start();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				finish();
				
				//overridePendingTransition(R.anim.stay, R.anim.slide_out_down);
			}
		}, 2000);
		
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
}
