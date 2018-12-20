package com.kandi.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.kandi.event.CarChargingEvent;
import com.kandi.event.DRI_INSERT_CHARGEREvent;
import com.kandi.event.UsbMediaStatesEvent;
import com.kandi.home.R;
import com.util.ToastUtil;

import de.greenrobot.event.EventBus;

public abstract class BaseActivity extends FragmentActivity {
	private BaseReceiver baseReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this, "DRI_INSERT_CHARGEREvent",
				DRI_INSERT_CHARGEREvent.class);
		EventBus.getDefault().register(this, "DRI_CHARGER_ONOFFEvent",
				com.kandi.event.DRI_CHARGER_ONOFFEvent.class);
		
	}

	public abstract void CarChargingEvent(CarChargingEvent event);

	public abstract void VolumeClickEvent(com.kandi.event.VolumeClickEvent event);

	public abstract void DRI_INSERT_CHARGEREvent(DRI_INSERT_CHARGEREvent event);

	public abstract void DRI_CHARGER_ONOFFEvent(
			com.kandi.event.DRI_CHARGER_ONOFFEvent event);

	
	@Override
	protected void onStart() {
		baseReceiver = new BaseReceiver();

		// KD Driver Events
		IntentFilter kdIntentFilter = new IntentFilter();
		kdIntentFilter.addAction("com.driverlayer.kdos_driverserver");
		registerReceiver(baseReceiver, kdIntentFilter);

		// USB State Events
		IntentFilter storageIntentFilter = new IntentFilter();
		storageIntentFilter.addAction(Intent.ACTION_MEDIA_CHECKING);
		storageIntentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		storageIntentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
		storageIntentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
		storageIntentFilter.addDataScheme("file");

		registerReceiver(baseReceiver, storageIntentFilter);
		super.onStart();
	}

	/**
	 * 基类广播接收
	 * */
	private class BaseReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			BaseActivity.this.doReceiveBase(intent);
		}
	}

	@Override
	protected void onStop() {
		unregisterReceiver(baseReceiver);
		super.onStop();
	}

	/**
	 * 处理接收参数
	 * */
	public void doReceiveBase(Intent intent) {

		// U盘插入事件
		if (intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)
				|| intent.getAction().equals(Intent.ACTION_MEDIA_CHECKING)) {
			// USB_STATE_ON;
			EventBus.getDefault().postSticky(
					new UsbMediaStatesEvent(
							UsbMediaStatesEvent.MEDIASTATE.MEDIA_PLUGED));
			ToastUtil.showToast(this.getApplicationContext(), getString(R.string.usb_in),
					Toast.LENGTH_SHORT);
		}
		// U盘拔出事件
		else if (intent.getAction().equals(Intent.ACTION_MEDIA_EJECT)
				|| intent.getAction().equals(Intent.ACTION_MEDIA_REMOVED)) {
			// USB_STATE_OFF;
			EventBus.getDefault().postSticky(
					new UsbMediaStatesEvent(
							UsbMediaStatesEvent.MEDIASTATE.MEDIA_UNPLUGED));
			ToastUtil.showToast(this.getApplicationContext(), getString(R.string.usb_out),
					Toast.LENGTH_SHORT);
		}
		// 其他广播事件
		else {
			this.doReceive(intent);
		}
	}

	public abstract void doReceive(Intent intent);

	public BaseReceiver getBaseReceiver() {
		return baseReceiver;
	}

	public void setBaseReceiver(BaseReceiver baseReceiver) {
		this.baseReceiver = baseReceiver;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this,DRI_INSERT_CHARGEREvent.class);
		EventBus.getDefault().unregister(this,com.kandi.event.DRI_CHARGER_ONOFFEvent.class);
	}
}
