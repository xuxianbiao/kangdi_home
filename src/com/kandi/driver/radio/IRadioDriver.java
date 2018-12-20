package com.kandi.driver.radio;

public interface IRadioDriver {

	/**
	 * 获取电台最小频率
	 * @return
	 */
	public abstract float getMinFreq();

	/**
	 * 获取电台最大频率
	 * @return
	 */
	public abstract float getMaxFreq();

	/**
	 * 获取电台开关状态
	 * @return
	 */
	public abstract boolean isPowerOn();

	/*
	 * 介绍:收音机启动或者关闭接口
	 * 参数：无
	 * 返回值：1：开启，0：关闭；
	 */
	public abstract int Tea5767_GetStatus();

	/*
	 * 介绍:收音机启动或者关闭接口
	 * 参数：para:true表示启动收音机，false表示关闭收音机
	 * 返回值：1：启动或者关闭成功，0：表示启动或者关闭失败，-1表示收音机不存在;
	 */
	public abstract int Tea5767_Open(boolean para);

	/*
	 * 获取当前收音机频段调谐范围，单位MHZ
	 * 参数： mode =0:FM模式，=1:AM模式
	 * 	   返回double[0] 频段上限，double[1]频段下限
	 */
	public abstract void Tea5767_RangeFreq(int mode, float[] range);

	/*
	 * 介绍：设置收音机收台音效等级
	 * 参数：level范围1-16，推荐设置为8以上
	 * 返回值：0成功，其他值失败
	 */
	public abstract int Tea5767_Level(int level);

	/*
	 * 介绍：用于选择系统的输出音源类型
	 * 参数：true设置静音，false关闭静音
	 * 返回值：true设置成功，flase设置失败
	 */
	public abstract boolean Tea5767_Mute(boolean mute);

	/*
	 * 介绍：设置收音机工作模式，AM或者FM模式
	 * 参数：mode=0表示FM模式，1表示AM模式
	 * 返回值：true表示设置成功；
	 */
	public abstract boolean Tea5767_SetMode(int mode);

	/*
	 * 介绍：获取收音机工作模式，AM或者FM模式
	 * 参数：无
	 * 返回值：0表示FM模式，1表示AM模式
	 */
	public abstract int Tea5767_GetMode();

	/*
	 * 介绍：设置收音机工作频率设置
	 * 参数：电台频率
	 * 返回值：>0表示设置成功，<0表示设置失败
	 */
	public abstract int Tea5767_SetFreq(float freq);

	/*
	 * 介绍：获取电台频率
	 * 参数：无
	 * 返回值：当前播放的电台频率
	 */
	public abstract float Tea5767_GetFreq();

	/*
	 * 介绍：向上电台搜索
	 * 参数：无
	 * 返回值：有效的电台频率,1表示搜索完成，-1表示收音机为启动
	 */
	public abstract float Tea5767_SeekUp();

	/*
	 * 介绍：向下电台搜索
	 * 参数：无
	 * 返回值：有效的电台频率
	 */
	public abstract float Tea5767_SeekDown();

	/*
	 * 介绍：判断自动收台是否成功标志位判断；
	 * 参数：无；
	 * 返回值：true表示收台成功；false表示收台失败；
	 */
	public abstract boolean Tea5767_SeekSuccess();

}