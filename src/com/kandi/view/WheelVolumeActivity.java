package com.kandi.view;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kandi.home.R;

public class WheelVolumeActivity extends Activity {

	private Timer timer = null;
	private TimerTask task;
	private int STEP = 15;
	private AudioManager audiomanage;
	private int currentVolume, progress;
	private boolean direction;
	private TextView converse_time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wheelvolume);
		direction = getIntent().getBooleanExtra("direction", false);
		initView();
		RelativeLayout lay = (RelativeLayout) findViewById(R.id.buttom_shadow3);
		lay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		showProgress();
	}

	private void initView() {
		converse_time = (TextView) findViewById(R.id.converse_time);
		audiomanage = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		currentVolume = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);
		progress = 100 * currentVolume / STEP;
	}

	@Override
	protected void onResume() {
		if (timer == null) {
			initTimer();
		}
		currentVolume = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);
		progress = 100 * currentVolume / STEP;
		converse_time.setText(progress + "%");
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
		direction = intent.getBooleanExtra("direction", false);
		showProgress();
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

	private void initTimer() {
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				finish();
			}
		};
		timer.schedule(task, 3000); // 定时器5000 ms
	}

	private void showProgress() {
		if (direction) {
			if (progress >= 100) {
				audiomanage.setStreamVolume(AudioManager.STREAM_MUSIC, progress * STEP / 100, 0);
			} else if (progress >= 0) {
				progress = progress + 10;
				if (progress <= 100) {
					audiomanage.setStreamVolume(AudioManager.STREAM_MUSIC, progress * STEP / 100, 0);
				} else {
					audiomanage.setStreamVolume(AudioManager.STREAM_MUSIC, STEP, 0);
				}
			}
		} else {
			if (progress >= 0) {
				progress = progress - 5;
				if (progress <= 0) {
					audiomanage.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
				} else {
					audiomanage.setStreamVolume(AudioManager.STREAM_MUSIC, progress * STEP / 100, 0);
				}
			}
		}
	}

}
