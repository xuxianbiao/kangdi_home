package com.kandi.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.view.Window;
import android.widget.FrameLayout;

import com.kandi.home.R;

public class WelcomeActivity extends Activity{
	public static Activity context = null;
	public static boolean First=false;
	private FrameLayout welcome_bg;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context = this;
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		welcome_bg = (FrameLayout) findViewById(R.id.welcome_bg);
		if((SystemProperties.get("ro.product.model")).endsWith("K17")){
			welcome_bg.setBackgroundResource(R.drawable.welcome_bg);
		}else{
			welcome_bg.setBackgroundResource(R.drawable.welcome_global_bg);
		}
		handler.sendEmptyMessageDelayed(1, 3000);
		
	}
	
	protected void onResume() {
		super.onResume();
//		if(!First){
//			First = true;
//			super.onResume();
//			Handler handler = new Handler();
//			handler.postDelayed(new Runnable() {
//
//				@Override
//				public void run() {
//					Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
//					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					startActivity(intent);
//				}
//			}, 1500);
//		}else{
//			onDestroy();
//		}
	}
	
	Handler	handler = new Handler()
	{			
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);	
			Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
	};

	public static void finishActivity(){
		if(WelcomeActivity.context != null){
			WelcomeActivity.context.finish();
		}
	}
	public void onDestroy(){
		super.onDestroy();
	}
}
