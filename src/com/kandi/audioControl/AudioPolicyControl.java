package com.kandi.audioControl;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;


public class AudioPolicyControl {
	private final String getRadioStatusCommond = "tinymix 1";
	private final String get3GStatusCommond = "tinymix 2";
	private final String get3GLastVolumeCommond = "tinymix 21";
	private final String getRadioLastVolumeCommond = "tinymix 22";
	private final String getI2sOutLastVolumeCommond = "tinymix 23";
	private GPIO_Operation GPIO_55_Operation = null;
	private GPIO_Operation GPIO_137_Operation = null;
	private AudioManager audioManager = null;
	public AudioPolicyControl()
	{
		//实例化两个GPIO对象
		GPIO_55_Operation = new GPIO_Operation(GPIO_Operation.GPIO_55);
		GPIO_137_Operation = new GPIO_Operation(GPIO_Operation.GPIO_137);
	}
	
	public int getRadioLastVolume()
	{
		int result = this.tinymix(getRadioLastVolumeCommond);
		System.out.println("zhuhainan getRadioLastVolume:"+result);
		if (result < 0)
			return 120;
		else
			return result;
	}
	
	public boolean setRadioLastVolume(int volume)
	{
		int result = this.tinymix("tinymix 22 "+volume);
		System.out.println("zhuhainan setRadioLastVolume:"+volume);
		if (result < 0)
			return false;
		else
			return true;
	}

	
	public int get3GLastVolume()
	{
		int result = this.tinymix(get3GLastVolumeCommond);
		System.out.println("zhuhainan get3GLastVolume:"+result);
		if (result < 0)
			return 120;
		else
			return result;
	}

	
	public int getI2sOutLastVolume()
	{
		int result = this.tinymix(getI2sOutLastVolumeCommond);
		System.out.println("zhuhainan getI2sOutLastVolume:"+result);
		if (result < 0)
			return 120;
		else
			return result;
	}
	
	//判断收音机是否给打开
	public boolean isOpenRadio() {
		int result = tinymix(getRadioStatusCommond);
		//tinymix失败
		if (result < 0) {
			System.out.println("getRadioStatus fail");
			return false;
		}
		if (result == 1)
			return true;
		else
			return false;
	}
	//判断3G是否打开
	public boolean isOpen3G() {
		int result = tinymix(get3GStatusCommond);
		//tinymix失败
		if (result < 0) {
			System.out.println("get3GStatusCommond fail");
		}
		if (result == 1)
			return true;
		else
			return false;
	}
	//关闭3G声音通道
	public boolean close3G() {
		int result = this.tinymix("tinymix 2 0");
		if (result < 0)
			return false;
		else
		{
			//判断收音机是否打开
			System.out.println("zhuhainan close3G radio statu: " + isOpenRadio());
			if(isOpenRadio())
			{
				//切换到上一次设置收音机的音量大小
				int volume = getRadioLastVolume();
				System.out.println("zhuhainan setHardwareVolume ==============================");
				setHardwareVolume(volume,volume);
				System.out.println("zhuhainan setHardwareVolume==============================");
				//如果是打开状态，将声音通道切换到收音机
				return switchRadio();
			}
			//调整i2s out 音量
			else
			{
				//切换到上一次设置i2s out的音量大小
				int volume = getI2sOutLastVolume();
				setHardwareVolume(volume,volume);
			}
			return true;
		}
	}

	//切换模拟信号通道成空闲状态
	public boolean switchToidel()
	{
		//模拟通道 切换：
		if(!GPIO_55_Operation.gpioSetValues(GPIO_Operation.GPIO_VALUE_HIGH))
			return false;
		if(!GPIO_137_Operation.gpioSetValues(GPIO_Operation.GPIO_VALUE_LOW))
			return false;
		return true;
	}
	//打开3G声音通道
	public boolean open3G() {
		int result = this.tinymix("tinymix 2 1");
		if (result < 0)
			return false;
		else
		{
			//切换到上一次设置的音量大小
			int volume = get3GLastVolume();
			setHardwareVolume(volume,volume);
			//模拟通道 切换：
    		if(!GPIO_55_Operation.gpioSetValues(GPIO_Operation.GPIO_VALUE_LOW))
    			return false;
    		if(!GPIO_137_Operation.gpioSetValues(GPIO_Operation.GPIO_VALUE_LOW))
    			return false;
			return true;
		}
	}
	//切换到收音机声音通道函数
	public boolean switchRadio()
	{
		//模拟通道 切换：
		if(!GPIO_55_Operation.gpioSetValues(GPIO_Operation.GPIO_VALUE_HIGH))
			return false;
		if(!GPIO_137_Operation.gpioSetValues(GPIO_Operation.GPIO_VALUE_HIGH))
			return false;
		return true;
	}
	//关闭收音机声音通道函数
	public boolean closeRadio() {
		int result = this.tinymix("tinymix 1 0");
		if (result < 0)
			return false;
		else
		{
			//判断3G是否给打开
			System.out.println("zhuhainan closeRadio 3g statu: " + isOpen3G());
			if(!isOpen3G())
			{
				//如果3G没有给打开，音频返回到i2s out
				if(!switchToidel())
					return false;
				result = this.tinymix("tinymix 33 0");
				if (result < 0)
					return false;
				result = this.tinymix("tinymix 29 0");
				if (result < 0)
					return false;

				//切换到上一次设置i2s out的音量大小
				int volume = getI2sOutLastVolume();
				setHardwareVolume(volume,volume);
			}
			return true;
		}
	}
	
	//打开收音机声音通道
	public boolean openRadio(Context context) {
		int result = this.tinymix("tinymix 1 1");
		if (result < 0)
			return false;
		else
		{
			System.out.println("zhuhainan openRadio 3g statu: " + isOpen3G());
			if(!isOpen3G())
			{
				if(!switchRadio())
					return false;
				result = this.tinymix("tinymix 33 1");
				if (result < 0)
					return false;
				result = this.tinymix("tinymix 29 1");
				if (result < 0)
					return false;
				
				
	    		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE) ;
	    		int currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);// 当前的媒体音量
	
	    		System.out.println("zhuhainan openRadio currVolume: "+currVolume);
				//范围是0-15
	    		int value = 0 ;
	    		if(currVolume == 0)
				{
	    			value = 0;
				}
				else
				{
					//50是一个基准值 127是最大值 0是最小值
					double double_value = currVolume;
					value = (int)(double_value/15*(127.0-50.0)+50.0);
				}
				setRadioLastVolume(value);
				setHardwareVolume(value,value);
			}
			return true;
		}
	}
	
	//设置硬件扬声器的音量
	public boolean setHardwareVolume(int left_value, int right_value) {
		int result = tinymix("tinymix 7 " + left_value + " " + right_value);
		System.out.println("zhuhainan setHardwareVolume:"+left_value);
		if (result < 0) {
			System.out.println("setHardwareVolume fail");
			return false;
		} else
			return true;
	}
	
	//底层切换声音通道的接口
	private native int tinymix(String cmd);

	static {
		System.loadLibrary("qy_tinymix");
	}
}

