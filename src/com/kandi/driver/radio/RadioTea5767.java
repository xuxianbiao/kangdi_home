package com.kandi.driver.radio;
import android.content.Context;
import android.os.IFmService;
import android.os.RemoteException;
import android.os.ServiceManager;

/*
 * auth:plz
 * bref:实现收音机测试封装类，实现收音机启动、停止、音效等级、模式、频率设置和双向收台功能
 * ver：radio0.1
 * data:2015/6/3
 */
public class RadioTea5767 implements IRadioDriver{
	private float Default_MinFreq=87; //收音机最小频率
	private float Default_MaxFreq=108;//收音机最大频率
	private final int Default_Level=6;//收音机默认音效等级1~16
	private boolean Status;
	IFmService radio;

	/* (non-Javadoc)
	 * @see com.kandi.driver.radio.IRadioDrv#getMinFreq()
	 */
	@Override
	public float getMinFreq() {
		return Default_MinFreq;
	}

	/* (non-Javadoc)
	 * @see com.kandi.driver.radio.IRadioDrv#getMaxFreq()
	 */
	@Override
	public float getMaxFreq() {
		return Default_MaxFreq;
	}

	/* (non-Javadoc)
	 * @see com.kandi.driver.radio.IRadioDrv#isPowerOn()
	 */
	@Override
	public boolean isPowerOn() {
		return Status;
	}

	public RadioTea5767(){
		Status = false;
		radio = IFmService.Stub.asInterface(ServiceManager.getService("fm"));
	}
	
	/*
	 * 介绍:收音机启动或者关闭接口
	 * 参数：无
	 * 返回值：1：开启，0：关闭；
	 */
	/* (non-Javadoc)
	 * @see com.kandi.driver.radio.IRadioDrv#Tea5767_GetStatus()
	 */
	@Override
	public int Tea5767_GetStatus(){
		int status = 0;
		try {
			status = radio.GetLocalRadioStatus();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}
	/*
	 * 介绍:收音机启动或者关闭接口
	 * 参数：para:true表示启动收音机，false表示关闭收音机
	 * 返回值：1：启动或者关闭成功，0：表示启动或者关闭失败，-1表示收音机不存在;
	 */
	/* (non-Javadoc)
	 * @see com.kandi.driver.radio.IRadioDrv#Tea5767_Open(boolean)
	 */
	@Override
	public int Tea5767_Open(boolean para){
		int res=0;
			if(para){
				try {
					res = radio.OpenLocalRadio();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(res==1){
					Status = true;
					Tea5767_Level(Default_Level);
				}else{
					Status = false;
					return 0;
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return 1; 
			}else{	//关闭收音机
				try {
					res = radio.CloseLocalRadio();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(res==1){
					Status = false;
					return 1;
				}else{
					return 0;
				}
			} 
	}
	
	/*
     * 获取当前收音机频段调谐范围，单位MHZ
     * 参数： mode =0:FM模式，=1:AM模式
     * 	   返回double[0] 频段上限，double[1]频段下限
	 */
	/* (non-Javadoc)
	 * @see com.kandi.driver.radio.IRadioDrv#Tea5767_RangeFreq(int, float[])
	 */
	@Override
	public void Tea5767_RangeFreq(int mode,float[] range){
		 if(mode==0||mode==1){ 
			 float[] temp;
			try {
				temp = radio.GetRadioFreqRange(mode);
				 range[0] = temp[0];
				 range[1] = temp[1];
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
	}
	
	/*
	 * 介绍：设置收音机收台音效等级
	 * 参数：level范围1-16，推荐设置为8以上
	 * 返回值：0成功，其他值失败
	 */
	/* (non-Javadoc)
	 * @see com.kandi.driver.radio.IRadioDrv#Tea5767_Level(int)
	 */
	@Override
	public
	int Tea5767_Level(int level){
		 if(level>16 || level<=0){
			 return -1;
		 }
		try {
			return radio.SetRadioActiveLevel(level);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	 
	/*
	 * 介绍：用于选择系统的输出音源类型
	 * 参数：true设置静音，false关闭静音
	 * 返回值：true设置成功，flase设置失败
	 */
	/* (non-Javadoc)
	 * @see com.kandi.driver.radio.IRadioDrv#Tea5767_Mute(boolean)
	 */
	@Override
	public
	boolean Tea5767_Mute(boolean mute){
		if(mute){
			try {
				return radio.RadioMute(1);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//静音
		}else{
			try {
				return radio.RadioMute(0);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/*
	 * 介绍：设置收音机工作模式，AM或者FM模式
	 * 参数：mode=0表示FM模式，1表示AM模式
	 * 返回值：true表示设置成功；
	 */
	/* (non-Javadoc)
	 * @see com.kandi.driver.radio.IRadioDrv#Tea5767_SetMode(int)
	 */
	@Override
	public
	boolean Tea5767_SetMode(int mode){
		if(mode==0||mode==1){
			try {
				return radio.SetRadioMode(mode);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			return false;
		}
		return false;
	}
	 
	/*
	 * 介绍：获取收音机工作模式，AM或者FM模式
	 * 参数：无
	 * 返回值：0表示FM模式，1表示AM模式
	 */
	/* (non-Javadoc)
	 * @see com.kandi.driver.radio.IRadioDrv#Tea5767_GetMode()
	 */
	@Override
	public int Tea5767_GetMode(){
		try {
			return radio.GetRadioMode();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	 
	/*
	 * 介绍：设置收音机工作频率设置
	 * 参数：电台频率
	 * 返回值：>0表示设置成功，<0表示设置失败
	 */
	/* (non-Javadoc)
	 * @see com.kandi.driver.radio.IRadioDrv#Tea5767_SetFreq(float)
	 */
	@Override
	public
	int Tea5767_SetFreq(float freq){
		 if(freq<Default_MinFreq || freq>Default_MaxFreq){
			 return -1;
		 }
		if(Status){
			int res;
			try {
				res = radio.SetRadioFreq(freq);
				return res;
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return -1;
		}else{
			return -1;
		}
	}
	 
	/*
	 * 介绍：获取电台频率
	 * 参数：无
	 * 返回值：当前播放的电台频率
	 */
	/* (non-Javadoc)
	 * @see com.kandi.driver.radio.IRadioDrv#Tea5767_GetFreq()
	 */
	@Override
	public float Tea5767_GetFreq(){
		if(Status){
			try {
				return radio.GetRadioFreq();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return -1;
		}else{
			return -1;
		}
	}
	 
	/*
	 * 介绍：向上电台搜索
	 * 参数：无
	 * 返回值：有效的电台频率,1表示搜索完成，-1表示收音机为启动
	 */
	/* (non-Javadoc)
	 * @see com.kandi.driver.radio.IRadioDrv#Tea5767_SeekUp()
	 */
	@Override
	public
	float Tea5767_SeekUp(){
		if(Status){
			try {
				return radio.RadioFreqSeekUp();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return -1;
		}else{
			return -1;
		}
	}
	 
	/* (non-Javadoc)
	 * @see com.kandi.driver.radio.IRadioDrv#Tea5767_SeekDown()
	 */
	@Override
	public
	/*
	 * 介绍：向下电台搜索
	 * 参数：无
	 * 返回值：有效的电台频率
	 */
	 float Tea5767_SeekDown(){
		if(Status){
			try {
				return radio.RadioFreqSeekDown();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return -1;
		}else{
			return -1;
		}
	}  
	/*
	 * 介绍：判断自动收台是否成功标志位判断；
	 * 参数：无；
	 * 返回值：true表示收台成功；false表示收台失败；
	 */
	/* (non-Javadoc)
	 * @see com.kandi.driver.radio.IRadioDrv#Tea5767_SeekSuccess()
	 */
	@Override
	public boolean Tea5767_SeekSuccess(){
		boolean res=false;
		try {
			res = radio.isSeekSuccess();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
}