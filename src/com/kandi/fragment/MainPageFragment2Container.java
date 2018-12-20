package com.kandi.fragment;

import java.util.Timer;
import java.util.TimerTask;

import com.kandi.home.R;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainPageFragment2Container extends Fragment {
	boolean inited = false;
	TimerTask task;
	Timer timer;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mainpager_fragment2_container,
				null);
		delayInit();
		return view;
	}

	private void delayInit() {
		task = new TimerTask() {
			@Override
			public void run() {
				uiHandler.obtainMessage().sendToTarget();
			}
		};

		timer = new Timer();
		timer.schedule(task, 200); // 延时1000ms后执行，1000ms执行一次

	}

	private Handler uiHandler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {
			init();
		};
	};

	public void init() {
		if (!inited) {
			if(getActivity()!=null){
				FragmentTransaction transaction = getActivity()
						.getSupportFragmentManager().beginTransaction();
				MainPageFragment2 secondFragment = new MainPageFragment2();
				transaction.add(R.id.fragment2_container, secondFragment);
				transaction.commit();
				inited = true;
			}
		}
	}

	public boolean isInited() {
		return inited;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(task != null) {
			task.cancel();
			task=null;
			if(timer!=null){
				timer.cancel();
				timer=null;
			}
		}
	}
}
