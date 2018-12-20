package com.kandi.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kandi.home.R;
import com.kandi.event.ChangeMainBgEvent;
import com.kandi.event.ThemePopFinishEvent;

import de.greenrobot.event.EventBus;

public class ThemePopService extends Service 
{

	RelativeLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
	WindowManager mWindowManager;
	
	private Button topInternetBtn;
	
	private RelativeLayout bigbg;
	
	private ImageButton pic1;
	private ImageButton pic2;
	private ImageButton pic3;
	private ImageButton pic4;
	private ImageButton pic5;
	private ImageButton pic6;
	
	public void ThemePopFinishEvent(ThemePopFinishEvent event){
		this.stopSelf();
	}
	
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
		EventBus.getDefault().register(this,"ThemePopFinishEvent",ThemePopFinishEvent.class);
		createFloatView();
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	private void createFloatView()
	{
		wmParams = new WindowManager.LayoutParams();
		mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
		wmParams.type = LayoutParams.TYPE_PHONE; 
        wmParams.format = PixelFormat.RGBA_8888; 
        wmParams.flags = 
//          LayoutParams.FLAG_NOT_TOUCH_MODAL |
          LayoutParams.FLAG_NOT_FOCUSABLE
          ;
        
        wmParams.gravity = Gravity.CENTER; 
        
        wmParams.x = 0;
        wmParams.y = 0;

        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        mFloatLayout = (RelativeLayout) inflater.inflate(R.layout.selecttheme_win, null);
        mWindowManager.addView(mFloatLayout, wmParams);
        
        topInternetBtn = (Button)mFloatLayout.findViewById(R.id.testbtn);
        bigbg = (RelativeLayout) mFloatLayout.findViewById(R.id.bigbg);
        pic1 = (ImageButton) mFloatLayout.findViewById(R.id.pic1);
        pic1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try{
					EventBus.getDefault().postSticky(new ChangeMainBgEvent("1"));
				}catch(Exception e){
					e.printStackTrace();
				}
				ThemePopService.this.stopSelf();
			}
		});
        pic2 = (ImageButton) mFloatLayout.findViewById(R.id.pic2);
        pic2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try{
					EventBus.getDefault().postSticky(new ChangeMainBgEvent("2"));
				}catch(Exception e){
					e.printStackTrace();
				}
				ThemePopService.this.stopSelf();
			}
		});
        pic3 = (ImageButton) mFloatLayout.findViewById(R.id.pic3);
        pic3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try{
					EventBus.getDefault().postSticky(new ChangeMainBgEvent("3"));
				}catch(Exception e){
					e.printStackTrace();
				}
				ThemePopService.this.stopSelf();
			}
		});
        pic4 = (ImageButton) mFloatLayout.findViewById(R.id.pic4);
        pic4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try{
					EventBus.getDefault().postSticky(new ChangeMainBgEvent("4"));
				}catch(Exception e){
					e.printStackTrace();
				}
				ThemePopService.this.stopSelf();
			}
		});
        pic5 = (ImageButton) mFloatLayout.findViewById(R.id.pic5);
        pic5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try{
					EventBus.getDefault().postSticky(new ChangeMainBgEvent("5"));
				}catch(Exception e){
					e.printStackTrace();
				}
				ThemePopService.this.stopSelf();
			}
		});
        pic6 = (ImageButton) mFloatLayout.findViewById(R.id.pic6);
        pic6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try{
					EventBus.getDefault().postSticky(new ChangeMainBgEvent("6"));
				}catch(Exception e){
					e.printStackTrace();
				}
				ThemePopService.this.stopSelf();
			}
		});
        
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        topInternetBtn.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				//Toast.makeText(ThemePopService.this, "web", Toast.LENGTH_SHORT).show();
				//EventBus.getDefault().postSticky(new ThemePopFinishEvent());
				ThemePopService.this.stopSelf();
			}
		});
        bigbg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ThemePopService.this.stopSelf();
			}
		});
	}
	
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		if(mFloatLayout != null)
		{
			mWindowManager.removeView(mFloatLayout);
		}
	}
	
}
