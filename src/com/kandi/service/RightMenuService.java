package com.kandi.service;

import com.kandi.home.R;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class RightMenuService extends Service 
{

	//定义浮动窗口布局
    LinearLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
	WindowManager mWindowManager;
	
	public Button mFloatView;
	//手指是否移动
	private boolean isMove = false;
	
	private static final String TAG = "FxService";
	
	@Override
	public void onCreate() 
	{
		// TODO Auto-generated method stub
		super.onCreate();
		createFloatView();
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		return new MsgBinder();
	}
	
	public class MsgBinder extends Binder{  
        /** 
         * 获取当前Service的实例 
         * @return 
         */  
        public RightMenuService getService(){  
            return RightMenuService.this;  
        }  
    }  

	private void createFloatView()
	{
		wmParams = new WindowManager.LayoutParams();
		//获取WindowManagerImpl.CompatModeWrapper
		mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
		//设置window type
		wmParams.type = LayoutParams.TYPE_PHONE; 
		//设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888; 
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = 
//          LayoutParams.FLAG_NOT_TOUCH_MODAL |
          LayoutParams.FLAG_NOT_FOCUSABLE
//          LayoutParams.FLAG_NOT_TOUCHABLE
          ;
        
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT; 
        
        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = 0;
        wmParams.y = 0;

        /*// 设置悬浮窗口长宽数据
        wmParams.width = 200;
        wmParams.height = 80;*/
        
        //设置悬浮窗口长宽数据  
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.home_statednr_float, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        
//        Log.i(TAG, "mFloatLayout-->left" + mFloatLayout.getLeft());
//        Log.i(TAG, "mFloatLayout-->right" + mFloatLayout.getRight());
//        Log.i(TAG, "mFloatLayout-->top" + mFloatLayout.getTop());
//        Log.i(TAG, "mFloatLayout-->bottom" + mFloatLayout.getBottom());      
        
        //浮动窗口按钮
        mFloatView = (Button)mFloatLayout.findViewById(R.id.float_id);
        
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth()/2);
        Log.i(TAG, "Height/2--->" + mFloatView.getMeasuredHeight()/2);
        //设置监听浮动窗口的触摸移动
        mFloatView.setOnTouchListener(new OnTouchListener() 
        {
        	int lastX, lastY;
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					isMove = false;
					lastX = (int) event.getRawX();
					lastY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int dx =(int) event.getRawX() - lastX;
					int dy =(int) event.getRawY() - lastY;
					if(Math.abs(dx)>5 || Math.abs(dy)>5){
						isMove = true;
						// TODO Auto-generated method stub
						//getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
						wmParams.x = (int) - mFloatView.getMeasuredWidth()/2;
						//Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth()/2);
						//Log.i(TAG, "RawX" + event.getRawX());
						//Log.i(TAG, "X" + event.getX());
						//25为状态栏的高度
						wmParams.y = (int) event.getRawY() - mFloatView.getMeasuredHeight()/2 - 25 - 420;
						//Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredHeight()/2);
						//Log.i(TAG, "RawY" + event.getRawY());
						//Log.i(TAG, "Y" + event.getY());
						//刷新
						mWindowManager.updateViewLayout(mFloatLayout, wmParams);
					}else{
						isMove = false;
					}
					break;
				}
				return isMove;
			}
		});	
        
//        new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(5000);
//					m_handler.sendEmptyMessage(100);
////					Intent intent = new Intent(getApplicationContext(),SystemSettingActivity1_1.class);
////					startActivity(intent);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}).start();
	}
	
	@Override
	public void onDestroy() 
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mFloatLayout != null)
		{
			mWindowManager.removeView(mFloatLayout);
		}
	}
	
	Handler m_handler = new Handler(){
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			switch(msg.what){
			case 100:
				mFloatView.performClick();
				break;
			}
		}
	};
	
}
