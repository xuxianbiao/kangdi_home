package com.kandi.driver.radio;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class RadioDriverAdapter {
	
	static private RadioDriverAdapter radioDriverAdapter=null;
	static public RadioDriverAdapter getInstance() {
		if(radioDriverAdapter == null) {
			radioDriverAdapter = new RadioDriverAdapter();
		}
		return radioDriverAdapter;
	}

	//seek step up config
	private boolean _isAllowSeekStep=false;
	public boolean isAllowSeekStep() {
		return _isAllowSeekStep;
	}
	private boolean _isDummy;
	private IRadioDriver tea5767;
	SeekThread seekThread; 
	
	/**
	 * 判断系统是否支持收音机
	 * @return	true：当前系统无收音机驱动，使用模拟驱动演示收音机; false：当前系统已安装真实收音机驱动
	 */
	public boolean isDummy() {
		return _isDummy;
	}
	
	public RadioDriverAdapter() {
		
		/*
		//演示模式
		tea5767 = new RadioTea5767Dummy();
		_isDummy = true;
		/*/
		//工作模式
		try {
			tea5767 = new RadioTea5767();
			_isDummy = false;
		} catch(java.lang.NoClassDefFoundError e) {
			tea5767 = new RadioTea5767Dummy();
			_isDummy = true;
		}
		//*/
		
		//setRadioListener(listener);
		
		seekThread = new SeekThread();
		seekThread.start();
	}
	
	/**
	 * 向上电台搜索
	 * @return 有效的电台频率,1表示搜索完成，-1表示收音机为启动
	 */
	public float SeekUp(){
		return this.tea5767.Tea5767_SeekUp();
	}
	
	/**
	 * 向下电台搜索
	 * @return 有效的电台频率,1表示搜索完成，-1表示收音机为启动
	 */
	public float SeekDown(){
		return this.tea5767.Tea5767_SeekDown();
	}
	
	/**
	 * 开始向上搜台
	 */
	public void startSeekingUp() {
		seekThread.SetSeekPara(true,true);
	}

	/**
	 * 开始向下搜台
	 */
	public void startSeekingDown() {
		seekThread.SetSeekPara(true,false);
	}
	
	/**
	 * 停止搜台
	 */
	public void stopSeeking() {
		seekThread.SetSeekPara(false,false);
	}
	

	/**
	 * 设置收音机工作频率设置
	 * @param freq	电台频率
	 * @return	>0表示设置成功，<0表示设置失败
	 */
	public int setFreq(float freq) {
		return tea5767.Tea5767_SetFreq(freq);
	}
	
	/**
	 * 获取当前频率
	 * @return	
	 */
	public float getFreq() {
		return tea5767.Tea5767_GetFreq();
	}

	/**
	 * 开启收音机
	 * @return 1：启动或者关闭成功，0：表示启动或者关闭失败，-1表示收音机不存在;
	 */
	public int powerOn() {
		return tea5767.Tea5767_Open(true);
	}
	
	/**
	 * 关闭收音机
	 * @return 1：启动或者关闭成功，0：表示启动或者关闭失败，-1表示收音机不存在;
	 */
	public int powerOff() {
		return tea5767.Tea5767_Open(false);
	}

	public boolean isPowerOn() {
		//return tea5767.isPowerOn();
		return tea5767.Tea5767_GetStatus()==1;
	}
	

	/**
	 * 设置收音机 FM/AM模式
	 * @param mode	0表示FM模式，1表示AM模式
	 * @return true表示设置成功
	 */
	public boolean setMode(int mode) {
		return tea5767.Tea5767_SetMode(mode);
	}
	
	/**
	 * 获取收音机 FM/AM模式
	 * @return 0表示FM模式，1表示AM模式
	 */
	public int getMode() {
		return tea5767.Tea5767_GetMode();
	}
	
	/**
	 * 获取收音机频率下限
	 * @return
	 */
	public float getRadioMinFreq() {
		return tea5767.getMinFreq();
	}

	/**
	 * 获取收音机频率上限
	 * @return
	 */
	public float getRadioMaxFreq() {
		return tea5767.getMaxFreq();
	}
	

	/**
	 * 用于选择系统的输出音源类型
	 * @param isMute true设置静音，false关闭静音
	 * @return true设置成功，flase设置失败
	 */
	public boolean setMute(boolean isMute) {
		return tea5767.Tea5767_Mute(isMute);
	}
	
	/**
	 * 设置收音机收台音效等级
	 * @param level	level范围1-16，推荐设置为8以上
	 * @return 0成功，其他值失败
	 */
	public int setSignalLevel(int level) {
		return tea5767.Tea5767_Level(level);
	}
	
	
	
	public interface IRadioListener {
		/**
		 * 电台搜索中频率更新
		 * @param freq	当前搜索中的频率
		 */
		void OnFreqSeeking(float freq);

		/**
		 * 电台搜索成功频率更新
		 * @param freq 搜索到的频率
		 */
		void OnFreqSeeked(float freq);
		
		/**
		 * 电台搜索失败
		 * @param err 错误代码
		 */
		void OnFreqSeekError(int err);
		
		/**
		 * 电台频道获取失败
		 * @param err 错误代码
		 */
		void OnFreqSeekGetError(int err);
	}

	private IRadioListener _radioListener;

	public void setRadioListener(IRadioListener listener) {
		this._radioListener = listener;
	}
	
	public int getSeekingState() {
		return this.seekThread.getSeekingState();
	}

	
	/*
	 *收台线程 
	 */
	class SeekThread extends Thread {
		private final int MSG_SEEKOK=401;
		private final int MSG_SEEKING=410;
		private final int MSG_SEEKFAIL=400;
		private final int MSG_SEEKERROR=411;
		private final int MSG_SEEKUP=501;
		private final int MSG_SEEKDOWN=502;
		private int meesage=MSG_SEEKOK;
		private	int Start_Seek=0;	//seeking state 1:seeking forward, 2:seeking backward
		private boolean Seek_Dir=true;
		private float Current_Freq=0;
		private float Max_Freq,Min_Freq;
		private boolean res=false;
		
		SeekThread(){
			Max_Freq = RadioDriverAdapter.this.tea5767.getMaxFreq();
			Min_Freq = RadioDriverAdapter.this.tea5767.getMinFreq();
		}
		/**
		 * 返回搜台状态
		 * @return	0：搜台停止，1：向上搜台中，2：向下搜台中
		 */
		public int getSeekingState() {
			return Start_Seek;
		}
		
		public void run(){
//			while(true){
//				if(Start_Seek>0){
//					res = RadioDriverAdapter.this.tea5767.Tea5767_SeekSuccess();
//					
//					if(RadioDriverAdapter.this._radioListener != null) {
//						if(res) {
//							Start_Seek = 0;	//搜台完毕
//							m_hadler.sendEmptyMessage(MSG_SEEKOK);
//						}else{
//							m_hadler.sendEmptyMessage(MSG_SEEKING);
//						}
//					}
//					try {
//						Thread.sleep(120);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}else{
//					try {
//						Thread.sleep(200);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
		}
		
		/*
		 * 自动收台参数设置
		 * start:true表示启动收台，false表示停止收台
		 * dir：搜索方向，true表示向上收台，false表示向下收台；
		 * cur_freq:表示当前开始频率；
		 * 
		 */
		public void SetSeekPara(final boolean start,final boolean dir){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					Current_Freq = RadioDriverAdapter.this.tea5767.Tea5767_GetFreq();
					if(Current_Freq > 0){
						if(dir){
							Seek_Dir = true;
							if(RadioDriverAdapter.this.tea5767.Tea5767_SeekUp() >= 0){
								m_hadler.sendEmptyMessage(MSG_SEEKOK);
								return;
							}else{
								m_hadler.sendEmptyMessage(MSG_SEEKFAIL);
								return;
							}
						}else{
							Seek_Dir = false;
							if(RadioDriverAdapter.this.tea5767.Tea5767_SeekDown() >= 0){
								Start_Seek = 0;	//搜台完毕
								m_hadler.sendEmptyMessage(MSG_SEEKOK);
								return;
							}else{
								m_hadler.sendEmptyMessage(MSG_SEEKFAIL);
								return;
							}
						}
//						Start_Seek = start?(dir?1:2):0;
					}else{
						m_hadler.sendEmptyMessage(MSG_SEEKERROR);
					}
				}
			}).start();
		}
		
		Handler m_hadler = new Handler(){
			final String path = "mnt/sdcard/FmSeekResulte.xls";

			public void handleMessage(Message msg){
				super.handleMessage(msg);
				
//				try {
				
				if(RadioDriverAdapter.this._radioListener != null) {
					
					switch(msg.what){
					case MSG_SEEKOK:
						float freq = getFreq();
						if(freq<=Min_Freq){
							freq = Current_Freq;
							setFreq(freq);
						}else if(freq>=Max_Freq){
							freq = Current_Freq;
							setFreq(freq);
						}
						RadioDriverAdapter.this._radioListener.OnFreqSeeked(freq);
						break;
					case MSG_SEEKING:
						if(Seek_Dir){
							Current_Freq = (float) (Current_Freq+0.1);
							if(Current_Freq>=Max_Freq){
								Current_Freq = Max_Freq;
							}
						}else{
							Current_Freq = (float) (Current_Freq-0.1);
							if(Current_Freq<=Min_Freq){
								Current_Freq = Min_Freq;
							}
						}
						RadioDriverAdapter.this._radioListener.OnFreqSeeking(Current_Freq);
						Log.i("FM", "Senking:"+" "+Current_Freq);
						break;
					case MSG_SEEKERROR:
						RadioDriverAdapter.this._radioListener.OnFreqSeekGetError(-1);
						break;
					case MSG_SEEKFAIL:
						setFreq(Current_Freq);
						RadioDriverAdapter.this._radioListener.OnFreqSeeked(Current_Freq);
						break;
					default:
						RadioDriverAdapter.this._radioListener.OnFreqSeekError(-1);
						break;
					}
				}
				
//				} catch(java.lang.NullPointerException e) {
//					RadioDriverAdapter.this._radioListener = null;
//				}
			}
		};
	}
	
	
	
}
