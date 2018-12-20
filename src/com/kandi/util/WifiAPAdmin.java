package com.kandi.util;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiAPAdmin {
	public static final String TAG = "WifiAPAdmin";
	
	public static void closeWifiAp(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE); 
		closeWifiAp(wifiManager);
	}
	
	private WifiManager mWifiManager = null;
	
	private Context mContext = null;
	public WifiAPAdmin(Context context) {
		mContext = context;
		
		mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);  
		
		closeWifiAp(mWifiManager);
	}
	
	private String mSSID = "";
	private String mPasswd = "";
	public void startWifiAp(String ssid, String passwd) {
		mSSID = ssid;
		mPasswd = passwd;
		
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		} 
		
		stratWifiAp();
		
		MyTimerCheck timerCheck = new MyTimerCheck() {
			
			@Override
			public void doTimerCheckWork() {
				
				if (isWifiApEnabled(mWifiManager)) {
					Log.v(TAG, "Wifi enabled success!");
					this.exit();
				} else {
					Log.v(TAG, "Wifi enabled failed!");
				}
			}

			@Override
			public void doTimeOutWork() {
				this.exit();
			}
		};
		timerCheck.start(15, 1000);
		
	}

	public void stratWifiAp() {
		Method method1 = null;
		try {
			method1 = mWifiManager.getClass().getMethod("setWifiApEnabled",
					WifiConfiguration.class, boolean.class);
			WifiConfiguration netConfig = new WifiConfiguration();

			netConfig.SSID = mSSID;
			netConfig.preSharedKey = mPasswd;

			netConfig.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.OPEN);
			netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			netConfig.allowedKeyManagement
					.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			netConfig.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			netConfig.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			netConfig.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.CCMP);
			netConfig.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.TKIP);

			method1.invoke(mWifiManager, netConfig, true);

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	private static void closeWifiAp(WifiManager wifiManager) {
		if (isWifiApEnabled(wifiManager)) {
			try {
				Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
				method.setAccessible(true);

				WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);

				Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
				method2.invoke(wifiManager, config, false);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	private static boolean isWifiApEnabled(WifiManager wifiManager) {
		try {
			Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
			method.setAccessible(true);
			return (Boolean) method.invoke(wifiManager);

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	


	public abstract class MyTimerCheck {
		private int mCount = 0;
		private int mTimeOutCount = 1;
		private int mSleepTime = 1000; // 1s
		private boolean mExitFlag = false;
		private Thread mThread = null;
		
		/**
		 * Do not process UI work in this.
		 */
		public abstract void doTimerCheckWork();
		
		public abstract void doTimeOutWork();
		
		public MyTimerCheck() {
			mThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while (!mExitFlag) {
						mCount++;
						if (mCount < mTimeOutCount) {
							doTimerCheckWork();
							try {
								Thread.sleep(mSleepTime);
							} catch (InterruptedException e) {
								e.printStackTrace();
								exit();
							}
						} else {
							doTimeOutWork();
						}
					}
				}
			});
		}
		
		/**
		 * start
		 * @param times  How many times will check?
		 * @param sleepTime ms, Every check sleep time.
		 */
		public void start(int timeOutCount, int sleepTime) {
			mTimeOutCount = timeOutCount;
			mSleepTime = sleepTime;
			
			mThread.start();
		}
		
		public void exit() {
			mExitFlag = true;
		}
		
	}

}
