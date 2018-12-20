package com.kandi.view;

import java.io.File;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.kandi.driver.ConfigDriver38MgnUpgrade;
import com.kandi.driver.DriverServiceManger;
import com.kandi.home.R;
import com.kandi.util.ACacheUtil;
import com.kandi.util.FileUtil;
import com.util.ToastUtil;

public class UpgradingActivity extends Activity {

	public boolean btStatus = false;
	public TextView uptext;
	public TextView uppercent;
	public TextView textView1;
	UpgradingReceiver upgradingReceiver;
	String[] path;
	ACacheUtil acache;
	public String nowpath = "";
	public String result_info = "";
	public String show_result_info = "";
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
		setContentView(R.layout.upgrading);
		uptext = (TextView) findViewById(R.id.uptext);
		uppercent = (TextView) findViewById(R.id.uppercent);
		textView1 = (TextView) findViewById(R.id.textView1);
		path = getIntent().getStringArrayExtra("path");
		if(path != null && path.length > 0){
			acache = ACacheUtil.get();
			ConfigDriver38MgnUpgrade model = DriverServiceManger.getInstance().getConfigDriver38MgnUpgrade();
			if(model != null){
				try {
					position = 0;
					nowpath = "";
					interrupt = acache.getAsString("interrupt");
					if("1".equals(interrupt)){
						acache.remove("interrupt");
						for(int i=position;i<path.length;i++){
							nowpath = path[position];
							Intent intent = new Intent();
							intent.setAction("com.kangdi.forcecancle");
							intent.putExtra("failreason", STATUS_SUCCESS);
							intent.putExtra("nowpath", nowpath);
							sendBroadcast(intent);
							failcount = 0;//升级成功计数清除
							up_timeout = 0;
							String resultfile = nowpath.substring(nowpath.lastIndexOf("/")+1);
							result_info += (resultfile.length()>10?resultfile.substring(0, 10)+"...":resultfile)+getString(R.string.upfailed)+"\n";
							uptext.setText(result_info);
							show_result_info += getDevName(resultfile) + ":  " + getString(R.string.upfailed) +"\n";
						}
						position = path.length;
						m_handler.sendEmptyMessageDelayed(1005, 1000);
						return;
					}
					if(path.length > 0){
						nowpath = path[position];
						model.getSystemStartUpdataHex(nowpath, 0, true);
						starttime = System.currentTimeMillis();
						position++;
						up_timeout = 0;
						failcount = 0;
						textView1.setText(getDevName(nowpath.substring(nowpath.lastIndexOf("/")+1))+getString(R.string.upgrading));
						m_handler.sendEmptyMessageDelayed(1001, 500);
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}else{
				//提示用户不满足条件升级信息放入缓存，预计在设置里查找
				clearCache();
				finish();
			}
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		registBroadCastReceiver();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(!btStatus){
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), UpgradingActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getApplicationContext().startActivity(intent);
				}
			}, 1000);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregistBroadCastReceiver();
		btStatus = false;
	}

	public final static String UPING = "com.kangdi.forciblyuping";//强制升级中
	public class UpgradingReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(UPING)) {
				boolean action = intent.getBooleanExtra("action", true);
				if(!action){
					finish();
				}
			}
		}
	}
	
	private void registBroadCastReceiver() {
        if (null == upgradingReceiver) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(UPING);
            upgradingReceiver = new UpgradingReceiver();
            registerReceiver(upgradingReceiver, filter);
        }
    }

    private void unregistBroadCastReceiver() {
        if (null != upgradingReceiver) {
        	unregisterReceiver(upgradingReceiver);
        	upgradingReceiver = null;
        }
    }
    
    ConfigDriver38MgnUpgrade model;
	int position = 0;
	int up_timeout = 0;
	int failcount = 0;
	long starttime = 0;
	short usetime = 0;
	final int Timeout_Num = 16;
	Handler m_handler = new Handler(){
		public void handleMessage(Message msg){
			switch (msg.what) {
			case 1001:
				model = DriverServiceManger.getInstance().getConfigDriver38MgnUpgrade();
				if(model != null){
					try {
						model.getSystemUpdataStatus();
						int state = model.getUpgradeState();
						int progress = model.getSystemUpdataStatus();
						if(state == 0xff || progress == 0){//超时
							up_timeout++;
							if(up_timeout > Timeout_Num){
								//失败发送升级失败报文
								Log.i("sendsucces", "fail"+position);
								Intent intent = new Intent();
								intent.setAction("com.kangdi.requpstatus");
								intent.putExtra("nowpath", nowpath);
								usetime = (short) ((System.currentTimeMillis() - starttime)/1000);
								intent.putExtra("usetime", usetime);
								intent.putExtra("failreason", STATUS_UPTIMEOUT);
								sendBroadcast(intent);
//								String resultfile = nowpath.substring(nowpath.lastIndexOf("/")+1);
//								result_info += (resultfile.length()>10?resultfile.substring(0, 10)+"...":resultfile)+getString(R.string.upfailed)+"\n";
//								uptext.setText(result_info);
								show_result_info += getDevName(nowpath.substring(nowpath.lastIndexOf("/")+1)) + ":  " + getString(R.string.upfailed) +"\n";
								if(path.length == position){
									m_handler.sendEmptyMessageDelayed(1005, 1000);
									return;
								}
								m_handler.sendEmptyMessageDelayed(1004, 500);
								up_timeout = 0;
								return;
							}
						}
						if(state != 0xaa){
							if(!(state == 0x01 || state == 0x02)){
								m_handler.sendEmptyMessageDelayed(1001, 500);
							}else{
								Message message = new Message();
								message.what = 1003;
								Bundle bundle = new Bundle();
								bundle.putInt("failreason", state);
								message.setData(bundle);
								m_handler.sendMessageDelayed(message, 500);
//							m_handler.sendEmptyMessageDelayed(1003, 500);
							}
						}else{
							m_handler.sendEmptyMessageDelayed(1002, 500);
						}
						Log.i("percent", ""+progress+"%" + state);
						uppercent.setText(getString(R.string.up_process)+":"+progress+"%");
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				break;
			case 1002:
				//成功发送升级成功报文
				Log.i("sendsucces", "sure"+position);
				Intent intent = new Intent();
				intent.setAction("com.kangdi.requpstatus");
				intent.putExtra("failreason", STATUS_SUCCESS);
				intent.putExtra("nowpath", nowpath);
				usetime = (short) ((System.currentTimeMillis() - starttime)/1000);
				intent.putExtra("usetime", usetime);
				sendBroadcast(intent);
				failcount = 0;//升级成功计数清除
				up_timeout = 0;
				String resultfile = nowpath.substring(nowpath.lastIndexOf("/")+1);
				result_info += (resultfile.length()>10?resultfile.substring(0, 10)+"...":resultfile)+getString(R.string.upsuccessed)+"\n";
				uptext.setText(result_info);
				show_result_info += getDevName(resultfile) + ":  " + getString(R.string.upsuccessed) +"\n";
				m_handler.sendEmptyMessageDelayed(1004, 500);
				if(path.length == position){
					m_handler.sendEmptyMessageDelayed(1005, 1000);
					return;
				}
				break;
			case 1003:
				//失败发送升级失败报文
				Log.i("sendsucces", "fail"+position);
				if(failcount < 2){
					position--;
					failcount++;
				}else{
					Intent intent2 = new Intent();
					intent2.setAction("com.kangdi.requpstatus");
					intent2.putExtra("nowpath", nowpath);
					int status = msg.getData().getInt("failreason");
					usetime = (short) ((System.currentTimeMillis() - starttime)/1000);
					intent2.putExtra("usetime", usetime);
					if(status == 0x01){
						intent2.putExtra("failreason", STATUS_STARTINFOFAIL);
					}else if(status == 0x02){
						intent2.putExtra("failreason", STATUS_FILECHECKFAIL);
					}
					sendBroadcast(intent2);
					resultfile = nowpath.substring(nowpath.lastIndexOf("/")+1);
					result_info += (resultfile.length()>10?resultfile.substring(0, 10)+"...":resultfile)+getString(R.string.upfailed)+"\n";
					uptext.setText(result_info);
					show_result_info += getDevName(resultfile) + ":  " + getString(R.string.upfailed) +"\n";
					failcount = 0;
					up_timeout = 0;
				}
				if(path.length == position){
					m_handler.sendEmptyMessageDelayed(1005, 1000);
					return;
				}
				m_handler.sendEmptyMessageDelayed(1004, 500);
				break;
			case 1004:
				updataHex();
				break;
			case 1005:
				if(path.length == position){
					try {
						model.getSystemStartUpdataHex(nowpath, 0, false);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					btStatus = true;
					//并提示升级结束
					ToastUtil.showToast(getApplicationContext(), getString(R.string.upover), Toast.LENGTH_SHORT);
					clearCache();
					//删除对应的升级文件及其日志,并结束界面
					clearFile();
					finish();
				}
				break;
			default:
				break;
			}
		}
	};
	
	String interrupt = "";
	
	public void updataHex(){
		if(path.length > position){
			model = DriverServiceManger.getInstance().getConfigDriver38MgnUpgrade();
			if(model != null){
				try {
					acache = ACacheUtil.get();
					interrupt = acache.getAsString("interrupt");
					if("1".equals(interrupt)){
						acache.remove("interrupt");
						for(int i=position;i<path.length;i++){
							nowpath = path[position];
							Intent intent = new Intent();
							intent.setAction("com.kangdi.forcecancle");
							intent.putExtra("failreason", STATUS_SUCCESS);
							intent.putExtra("nowpath", nowpath);
							sendBroadcast(intent);
							failcount = 0;//升级成功计数清除
							up_timeout = 0;
							String resultfile = nowpath.substring(nowpath.lastIndexOf("/")+1);
							result_info += (resultfile.length()>10?resultfile.substring(0, 10)+"...":resultfile)+getString(R.string.upfailed)+"\n";
							uptext.setText(result_info);
							show_result_info += getDevName(resultfile) + ":  " + getString(R.string.upfailed) +"\n";
						}
						position = path.length;
						m_handler.sendEmptyMessageDelayed(1005, 1000);
						return;
					}
					nowpath = path[position];
					model.getSystemStartUpdataHex(nowpath, 0, true);
					starttime = System.currentTimeMillis();
					textView1.setText(getDevName(nowpath.substring(nowpath.lastIndexOf("/")+1))+getString(R.string.upgrading));
					position++;
					m_handler.sendEmptyMessageDelayed(1001, 500);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void clearCache(){
		acache.remove("downloadurl");
		acache.remove("upstatus");
		acache.remove("interrupt");
	}
	
	public void clearFile(){
		if(path != null && path.length > 0){
			for(int i=0;i<path.length;i++){
				FileUtil.deleteFile(new File(path[i]));
				FileUtil.deleteFile(new File(path[i].replace(".bin", "_log_e.txt")));
			}
		}
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), PushUpActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("path", path);
		intent.putExtra("ShowOrHide", true);
		intent.putExtra("firmstatus", show_result_info);
		startActivity(intent);
	}
	
	public String getDevName(String filename){
		String devName = filename.substring(0, 2).toUpperCase();
		if(devName.equals("A2")){
			devName = "EAS";
		}else if(devName.equals("A3")){
			devName = "EPS";
		}else if(devName.equals("A4")){
			devName = "BCM";
		}else if(devName.equals("A6")){
			devName = "CHG";
		}else if(devName.equals("A7")){
			devName = "MCU";
		}else if(devName.equals("A8")){
			devName = "BMU("+filename.substring(filename.indexOf(".")-2, filename.indexOf("."))+")";
		}else if(devName.equals("A9")){
			devName = "DCDC";
		}else if(devName.equals("AA")){
			devName = "VCU";
		}else if(devName.equals("AB")){
			devName = "PEPS";
		}else if(devName.equals("AC")){
			devName = "ICU";
		}else if(devName.equals("0A")){
			devName = "LCG";
		}else if(devName.equals("0C")){
			devName = "EPB";
		}else if(devName.equals("0D")){
			devName = "MFL";
		}
		return devName;
	}

}
