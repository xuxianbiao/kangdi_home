package com.kandi.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kandi.application.BaseApplication;
import com.kandi.home.R;
import com.kandi.util.ACacheUtil;
import com.kandi.util.FileUtil;
import com.util.ToastUtil;

public class PushUpActivity extends Activity implements OnClickListener{

	public boolean btStatus = false;
	public TextView up_info;
	public Button up_delay;
	public Button up_submit;
	public Button up_cancel;
	public Button up_over;
	public RelativeLayout layout;
	public TextView title;
	public String[] path;
	public boolean ShowOrHide = false;
	ACacheUtil acache;
	public String nowpath = "";
	/**
	 0x00：成功 	0x01：设备升级中 	0x02：不满足升级条件[车速大于0或档位不在N或P档] 
	 0x03：用户取消升级	0x04：用户暂缓升级 	0x05：升级超时 		0x06：下载文件失败 
	 0x07：启动信息失败	 0x08：固件校验失败
	 */
	byte STATUS_SUCCESS = 0x00;
	byte STATUS_UPING = 0x01;
	byte STATUS_NOTALLOW = 0x02;
	byte STATUS_CANCEL = 0x03;
	byte STATUS_DELAY = 0x04;
	byte STATUS_UPTIMEOUT = 0x05;
	byte STATUS_DOWNFAIL = 0x06;
	byte STATUS_STARTINFOFAIL = 0x07;
	byte STATUS_FILECHECKFAIL = 0x08;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pushup);
		initView();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	private void initView() {
		layout = (RelativeLayout) findViewById(R.id.layout);
		layout.setOnClickListener(this);
		up_info = (TextView) findViewById(R.id.up_info);
		up_info.setMovementMethod(ScrollingMovementMethod.getInstance());
		path = getIntent().getStringArrayExtra("path");
		ShowOrHide = getIntent().getBooleanExtra("ShowOrHide", false);
		up_delay = (Button) findViewById(R.id.up_delay);
		up_delay.setOnClickListener(this);
		up_submit = (Button) findViewById(R.id.up_submit);
		up_submit.setOnClickListener(this);
		up_cancel = (Button) findViewById(R.id.up_cancel);
		up_cancel.setOnClickListener(this);
		up_over = (Button) findViewById(R.id.up_over);
		up_over.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		acache = ACacheUtil.get();
		String logconent = "";
		if(ShowOrHide){
			up_delay.setVisibility(View.INVISIBLE);
			up_submit.setVisibility(View.INVISIBLE);
			up_cancel.setVisibility(View.INVISIBLE);
			up_over.setVisibility(View.VISIBLE);
			logconent = getIntent().getStringExtra("firmstatus");
			up_info.setText(logconent);
			title.setText(getString(R.string.firmup_status));
		}else{
			up_delay.setVisibility(View.VISIBLE);
			up_submit.setVisibility(View.VISIBLE);
			up_cancel.setVisibility(View.VISIBLE);
			up_over.setVisibility(View.GONE);
			title.setText(getString(R.string.firmup));
			InputStream inputStream = null;
			try {
				if(path != null && path.length > 0){
					for(int i=0;i<path.length;i++){
						inputStream = new FileInputStream(new File(path[i].replace(".bin", "_log_e.txt")));
						logconent += FileUtil.getString(inputStream)+"\n";
					}
					if(inputStream != null){
						inputStream.close();
					}
					up_info.setText(logconent);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.up_delay:
			Intent updelay = new Intent();
			updelay.setAction("com.kangdi.upcancle");
			updelay.putExtra("failreason", STATUS_DELAY);
			updelay.putExtra("path", path);
			sendBroadcast(updelay);
			clearCache();
			btStatus = true;
			finish();
			break;
		case R.id.up_submit:
			if(BaseApplication.getInstance().getVal_speed() == 0 && BaseApplication.getInstance().getDnr_postion() == 2){
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), UpgradingActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("path", path);
				startActivity(intent);
			}else{
				//提示用户不满足条件升级信息放入缓存，预计在设置里查找
				ToastUtil.showToast(getApplicationContext(), getString(R.string.upcondition), Toast.LENGTH_SHORT);
				Intent upsubmit = new Intent();
				upsubmit.setAction("com.kangdi.upcancle");
				upsubmit.putExtra("failreason", STATUS_NOTALLOW);
				upsubmit.putExtra("path", path);
				sendBroadcast(upsubmit);
				clearCache();
			}
			btStatus = true;
			finish();
			break;
		case R.id.up_cancel:
			Intent upcancel = new Intent();
			upcancel.setAction("com.kangdi.upcancle");
			upcancel.putExtra("failreason", STATUS_CANCEL);
			upcancel.putExtra("path", path);
			sendBroadcast(upcancel);
			clearCache();
			//清除下载的文件
			clearFile();
			btStatus = true;
			finish();
			break;
		case R.id.up_over:
			clearCache();
			//清除下载的文件
			clearFile();
			btStatus = true;
			finish();
			break;

		default:
			break;
		}
	}
	
	public void clearCache(){
		acache.remove("upstatus");
	}
	
	public void clearFile(){
		if(path != null && path.length > 0){
			for(int i=0;i<path.length;i++){
				FileUtil.deleteFile(new File(path[i]));
				FileUtil.deleteFile(new File(path[i].replace(".bin", "_log_e.txt")));
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(!btStatus){
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), PushUpActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getApplicationContext().startActivity(intent);
				}
			}, 1000);
		}
	}
	
}
