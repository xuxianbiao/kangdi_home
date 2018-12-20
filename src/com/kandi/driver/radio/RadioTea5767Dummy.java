package com.kandi.driver.radio;

import android.os.BatteryStats.Timer;
import android.os.Looper;
import android.os.RemoteException;
import android.widget.Toast;

public class RadioTea5767Dummy implements IRadioDriver {
	private float _MinFreq=87; //收音机最小频率
	private float _MaxFreq=108;//收音机最大频率
	private boolean _isPowerOn=false;
	private boolean _seekSuccess = false;
	
	private float _currentFreq = (_MinFreq + _MaxFreq) /2;

	@Override
	public float getMinFreq() {
		return _MinFreq;
	}

	@Override
	public float getMaxFreq() {
		return _MaxFreq;
	}

	@Override
	public boolean isPowerOn() {
		return _isPowerOn;
	}

	@Override
	public int Tea5767_GetStatus() {
		return _isPowerOn?1:0;
	}

	@Override
	public int Tea5767_Open(boolean para) {
		_isPowerOn=para;
		return 1;	//success
	}

	@Override
	public void Tea5767_RangeFreq(int mode, float[] range) {
		range[0] = _MaxFreq;
		range[1] = _MinFreq;

	}

	@Override
	public int Tea5767_Level(int level) {
		return 0;
	}

	@Override
	public boolean Tea5767_Mute(boolean mute) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean Tea5767_SetMode(int mode) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int Tea5767_GetMode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int Tea5767_SetFreq(float freq) {
		 if(freq<_MinFreq || freq>_MaxFreq){
			 return -1;
		 }
		if(_isPowerOn){
			int res;
			res = 0; //radio.SetRadioFreq(freq);
			_currentFreq = freq;
			return res;
		}else{
			return -1;
		}
	}

	@Override
	public float Tea5767_GetFreq() {
		return _currentFreq;
	}

	@Override
	public float Tea5767_SeekUp() {
		_seekSuccess=false;
		new Thread() {
			public void run() {
				try {
					for(int i=0;i<17;i++) {
						_currentFreq +=0.1;
						if(_currentFreq > RadioTea5767Dummy.this.getMaxFreq()) {
							_currentFreq -= getMaxFreq() - getMinFreq();
						}
						Thread.sleep(100);
					}
					_seekSuccess = true;

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();

		return 0;
	}

	@Override
	public float Tea5767_SeekDown() {
		_seekSuccess=false;
		new Thread() {
			public void run() {
				try {
					for(int i=0;i<17;i++) {
						_currentFreq -=0.1;
						if(_currentFreq < RadioTea5767Dummy.this.getMinFreq()) {
							_currentFreq += getMaxFreq() - getMinFreq();
						}
						Thread.sleep(100);
					}
					_seekSuccess = true;

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();

		return 0;
	}

	@Override
	public boolean Tea5767_SeekSuccess() {
		return _seekSuccess;
	}

}
