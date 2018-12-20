package com.kandi.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.kandi.adapter.ModeWheelViewAdapter;
import com.kandi.home.R;
import com.lee.wheel.widget.TosAdapterView;
import com.lee.wheel.widget.WheelView;

public class WheelModeActivity extends Activity {

	private Timer timer = null;
	private TimerTask task;
	int position;
	private Intent modeintent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wheelmode);
		position = getIntent().getIntExtra("position", 1);
		if(position == 0){
			flag = true;
		}else if(position == 2){
			flag = false;
		}
		initWheelView();
		RelativeLayout lay = (RelativeLayout) findViewById(R.id.buttom_shadow3);
		lay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String[] infos = getActivityAtSecond();
				if (infos != null) {
					// 启动上一个activity
					gotoActivity(infos[0], infos[1]);
				} else {
					finish();
				}
			}
		});
	}

	/**方式二*/
	private List<String> windWheelPosList = new ArrayList<String>();
	private ModeWheelViewAdapter windWvAdapter;
	WheelView tempwv;
	private void initWheelView() {
		windWheelPosList.add("1");
		windWheelPosList.add("2");
		windWheelPosList.add("3");
		windWvAdapter = new ModeWheelViewAdapter(windWheelPosList, getApplicationContext());
		tempwv = (WheelView) findViewById(R.id.tempwv);
		tempwv.setScrollCycle(false);
		tempwv.setAdapter(windWvAdapter);
		tempwv.setOnItemSelectedListener(modeListener);
		tempwv.setSelection(position);
	}
	int waitWheelBack = 0;
	boolean isWaitWheelBack = false;
	private TosAdapterView.OnItemSelectedListener modeListener = new TosAdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(TosAdapterView<?> parent, View view, int pos, long id) {
			position = pos;
		}
		@Override
		public void onNothingSelected(TosAdapterView<?> parent) {
		}
	};

	@Override
	protected void onResume() {
		if (timer == null) {
			initTimer();
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if(task != null){
			task.cancel();
			task=null;
			if (timer != null) {
				timer.cancel();
				timer = null;
			}
		}
		super.onDestroy();
	}

	boolean flag = true;
	@Override
	protected void onNewIntent(Intent intent) {
		if(task != null){
			task.cancel();
			task=null;
			if (timer != null) {
				timer.cancel();
				timer = null;
			}
		}
		position = intent.getIntExtra("position", 1);
		if(flag){
			tempwv.scrollToChild(position);
		}else{
			tempwv.scrollToChild(0);
		}
		if(position == 0){
			flag = true;
		}else if(position == 2){
			flag = false;
		}
		super.onNewIntent(intent);
	}

	void gotoActivity(String pkg, String activityName) {
		Intent homeIntent = new Intent();
		homeIntent = new Intent();
		homeIntent.setComponent(new ComponentName(pkg, activityName));
		homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			startActivity(homeIntent);
		} catch (Exception e) {
		}
	}

	private String[] getActivityAtSecond() {
		try {
			ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
			ComponentName cn = am.getRunningTasks(2).get(1).topActivity;
			String[] infos = new String[2];
			infos[0] = cn.getPackageName();
			infos[1] = cn.getClassName();
			return infos;
		} catch (Exception e) {
			return null;
		}

	}

	private void initTimer() {
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				if(position == 0){
					modeintent = new Intent(getApplicationContext(), RadioActivity.class);
				}else if(position == 1){
					modeintent = new Intent(getApplicationContext(), EntertainmentFragmentActivity.class);
				}else if(position == 2){
					modeintent = new Intent(getApplicationContext(), EntertainmentFragmentVideoActivity.class);
				}
				modeintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(modeintent);
				finish();
			}
		};
		timer.schedule(task, 1000); // 定时器5000 ms
	}

}
