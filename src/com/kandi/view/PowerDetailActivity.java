package com.kandi.view;

import java.util.ArrayList;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.kandi.adapter.MainFragmentPageAdapter;
import com.kandi.base.BaseActivity;
import com.kandi.driver.DriverServiceManger;
import com.kandi.driver.EnergyInfoDriver;
import com.kandi.event.DRI_INSERT_CHARGEREvent;
import com.kandi.event.base.BaseEvent;
import com.kandi.home.R;

import de.greenrobot.event.EventBus;

public class PowerDetailActivity extends BaseActivity implements Callback{
	
	ArrayList<Fragment> fragmentList;
	MainFragmentPageAdapter mainPageAdapter;
	
	private String currentIntent = "";
	private ViewPager viewPager;
	private BatViewPagerIndicator indicator; // viewpager的指示器
	private Handler handler;
	private BatDetailFragment firstFragment;
	private EnergyInfoDriver model;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.power_pannel);
		handler = new Handler(this);
		handler.sendEmptyMessageDelayed(100, 1000);

		Log.i("Kandi", "BATActivity initView");
		// 1. 初始化Viewpager，绑定adapter
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		fragmentList = new ArrayList<Fragment>();
		firstFragment = BatDetailFragment.newInstance(0);
		fragmentList.add(firstFragment);
		
		mainPageAdapter = new MainFragmentPageAdapter(getSupportFragmentManager(), fragmentList);
		viewPager.setAdapter(mainPageAdapter);
		// 2. 给Viewpager绑定指示器
		indicator = (BatViewPagerIndicator) findViewById(R.id.batviewpager_indicator);
		indicator.setViewPager(viewPager);
		viewPager.setCurrentItem(0);
	}

	@Override
	public boolean handleMessage(Message arg0) {
		// TODO Auto-generated method stub
		if(arg0.what==100){
			model = DriverServiceManger.getInstance().getEnergyInfoDriver();
			if(model!=null){
				int num = model.getBattaryCabinNum();
				if(num<model.MAX_BATTARY_SIZE){
					if(num<fragmentList.size()&&num>=0){
						if(num == 0 || num<viewPager.getCurrentItem()){
							viewPager.setCurrentItem(0);
						}
						num = fragmentList.size() - num - 1;//至少保证存在一个电池
						int list_num  = fragmentList.size();
						for(int i=0;i<num;i++){
							fragmentList.remove(list_num-i-1);
						}
					}else if(num>fragmentList.size()){
						num = num-fragmentList.size();
						int oldsize = fragmentList.size();
						for(int i=0;i<num;i++){
							fragmentList.add(BatDetailFragment.newInstance(oldsize+i));
						}
					}
					viewPager.getAdapter().notifyDataSetChanged();
					indicator.setViewPager(viewPager);
				}
			}
			handler.sendEmptyMessageDelayed(100, 8000);
		}
		return false;
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
