package com.kandi.view;

import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;

import com.kandi.base.BaseActivity;
import com.kandi.event.DRI_INSERT_CHARGEREvent;
import com.kandi.event.base.BaseEvent;
import com.kandi.fragment.MusicFragment;
import com.kandi.home.R;

import de.greenrobot.event.EventBus;

/**
 * 娱乐弹出时的fragment
 */
public class EntertainmentFragmentActivity extends BaseActivity {
	private Fragment musicFragment;
	private ImageButton leftBtn;
	private int currentFragmentPos = -1;
	
	private View container1, container2;
	FragmentTransaction transaction;

	@Override
	protected void onCreate(Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState );
		setContentView(R.layout.entainment_layout);

		leftBtn = (ImageButton) findViewById(R.id.entainment_video);
		leftBtn.setImageResource(R.drawable.music_tap_musicplay_on);
		container1 = findViewById(R.id.entainment_container);
		container2 = findViewById(R.id.entainment_container2);
		
		// 默认装载的fragment是“音乐播放”
		if (savedInstanceState == null) {
			transaction = getSupportFragmentManager()
					.beginTransaction();
			if(MusicFragment.instance == null){
				musicFragment = new MusicFragment();
				transaction.add(R.id.entainment_container, musicFragment);
			}
			transaction.commit();
		}
		loadFragmentIntoLayout(1);
		currentFragmentPos = 1;
	}

	/**
	 * 加载fragment到layout中
	 * 
	 * @param pos
	 *            代表fragment的位置，0视频播放 1音乐播放
	 */
	private void loadFragmentIntoLayout(int pos) {
		if (currentFragmentPos == pos) {
			// 如果要加载的fragment已经呈现，则无需重复加载
			return;
		}

		if (pos == 1) {
			// 点击的是"音乐播放"
			container1.setVisibility(View.VISIBLE);
			container2.setVisibility(View.GONE);
			this.getWindow().setBackgroundDrawableResource(R.drawable.bg_cd);
		} else {
			// 点击的是"shipin播放"
			container1.setVisibility(View.GONE);
			container2.setVisibility(View.VISIBLE);
			this.getWindow().setBackgroundDrawableResource(R.drawable.bg_cd);
//			videoFragment.initFragment(); // will not re-init
		}

		currentFragmentPos = pos;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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
