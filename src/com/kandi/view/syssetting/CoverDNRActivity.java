package com.kandi.view.syssetting;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.kandi.home.R;

public class CoverDNRActivity extends Activity {
	
	private ImageView cover_bg;
	public static CoverDNRActivity instance = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_coverdnr_layout);
		View rootView = findViewById(android.R.id.content);
		rootView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
		instance = this;
		cover_bg = (ImageView) findViewById(R.id.cover_bg);
		cover_bg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(StateDNRActivity.instance != null){
					StateDNRActivity.instance.finish();
					StateDNRActivity.instance = null;
				}
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						finish();
						instance = null;
					}
				}, 200);
			}
		});
	}
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 100:
				
				break;
			}
		}
		
	};
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
			finish();      
			return true;    
		}
		return super.onTouchEvent(event); 
	}
	
}
