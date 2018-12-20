package com.kandi.event.base;

import android.os.Bundle;

public abstract class BaseEvent {
	public Bundle bundle;
	public String text;
	
    /*
     *@brif: 驱动服务程序的事件通过广播消息的方式发送：
     *@para: key： "KD_CAST_EVENT"；
     *       value：定义见下表 ,
     * **************************************************************************
     * ********************************表1  驱动服务程序广播消息定义***********************
     * **************************************************************************
     * 【类别】     ** 【广播Key】  ** 【广播Value】                 ** 【备注】                       *
     *  档位        **  key+0       **  0:隐藏；1:弹出               **  boolean                       *
     *  空调面板    **  key+1       **  0:使能；1:禁用               **  boolean                       *
     *  充电插入    **  key+2       **  0:拔出；1:插入               **  boolean                       *
     *  充电起停    **  key+3       **  0:停止；1:启动 2:故障停止    **  int                           *
     *  故障        **  key+4       **  0:解除；1:发生               **  boolean，详细故障通过接口读取 *
     *  告警        **  key+5       **  0:解除；1:发生               **  boolean，详细告警通过接口读取 *
     *  车门        **  key+6       **  0:锁闭；1:解锁               **  boolean                       *
     *  车窗        **  key+7       **  1:上升；2:下降；3:暂停       **  int[],数组下标表示车窗编号    *
     *  后备箱      **  key+8       **  0:解锁；1:锁闭               **  boolean                       *
     *  充电盖      **  key+9       **  0:锁闭；1:解锁               **  boolean                       *
     *  大灯        **  key+10      **  1:远光灯 ；2:近光灯；3：关闭 **  int                           *
     *  双跳        **  key+11      **  0:关闭；1:打开               **  boolean                       *
     *  前雾灯      **  key+12      **  0:关闭；1:打开               **  boolean                       *
     *  小灯        **  key+13      **  0:关闭；1:打开               **  boolean                       *
     *  助力转向    **  key+14      **  1:低助力；2:中助力；3:高助力 **  int                           *
     *  电池舱门    **  key+15      **  1:上升；2:下降；3：暂停      **  int                           *
     *  后雾灯      **  key+16      **  0:关闭；1:打开               **  boolean                       *
     *  参数设置    **  key+17      **  0:设置异常；1:设置完成       **  boolean                       *
     *  BCM状态     **  key+18      **  0:表示在线；1:表示离线       **  boolean                       *
     * **************************************************************************
     */
	
//	public static enum DRIEVENT{
//		DRI_DNR_WINSHOW,	/**档位隐藏状态变化事件*/
//		DRI_AIR_ENABLE,		/**空调面板使能或者禁止状态变化事件*/
//		DRI_INSERT_CHARGER, /**充电抢插入状态变化事件*/
//		DRI_CHARGER_ONOFF,	/**充电机启动或者停止状态变化事件*/
//		DRI_ERR,			/**故障状态变化事件*/
//		DRI_WARING,			/**告警状态变化事件*/
//		DRI_CARDOOR,		/**车门锁状态变化事件*/
//		DRI_CARWINDOWS,		/**车窗状态变化事件*/
//		DRI_TRUNKBOOT,		/**后备箱状态变化事件*/
//		DRI_CHARGERTOP,		/**充电盖状态变化事件*/
//		DRI_HEADLIGHT,		/**大灯状态变化事件*/
//		DRI_DOUBLELAMP,		/**双跳状态变化事件*/
//		DRI_FORGLAMP,		/**雾灯状态变化事件*/
//		DRI_LITTLELAMP,		/**小灯状态变化事件*/
//		DRI_ASSISTEDS,		/**助力转向状态变化事件*/
//		DRI_BATDOOR,		/**电池舱门状态变化事件*/
//		DRI_BACKFOG,		/**后雾灯状态变化事件*/
//	    DRI_SYS_CFG,		/**系统设置状态变化事件*/
//	    DRI_BCM_ONLINE,	    /**BCM状态变化事件*/
//		DRI_EVENT_TOTAL	
//	}
	
	public enum DRIEVENT {
		DRI_DNR_WINSHOW		(0),		/**档位隐藏状态变化事件*/
		DRI_AIR_ENABLE		(1),		/**空调面板使能或者禁止状态变化事件*/
		DRI_INSERT_CHARGER	(2), 		/**充电抢插入状态变化事件*/
		DRI_CHARGER_ONOFF	(3),		/**充电机启动或者停止状态变化事件*/
		DRI_ERR				(4),		/**故障状态变化事件*/
		DRI_WARING			(5),		/**告警状态变化事件*/
		DRI_CARDOOR			(6),		/**车门锁状态变化事件*/
		DRI_CARWINDOWS		(7),		/**车窗状态变化事件*/
		DRI_TRUNKBOOT		(8),		/**后备箱状态变化事件*/
		DRI_CHARGERTOP		(9),		/**充电盖状态变化事件*/
		DRI_HEADLIGHT		(10),		/**大灯状态变化事件*/
		DRI_DOUBLELAMP		(11),		/**双跳状态变化事件*/
		DRI_FORGLAMP		(12),		/**雾灯状态变化事件*/
		DRI_LITTLELAMP		(13),		/**小灯状态变化事件*/
		DRI_ASSISTEDS		(14),		/**助力转向状态变化事件*/
		DRI_BATDOOR			(15),		/**电池舱门状态变化事件*/
		DRI_BACKFOG			(16),		/**后雾灯状态变化事件*/
	    DRI_SYS_CFG	     	(17),		/**系统设置状态变化事件*/
	    DRI_BCM_ONLINE		(18),	    /**BCM状态变化事件*/
		DRI_EVENT_TOTAL		(19),
		DRI_MFLSTATUS		(20);

		private int nState;
		private DRIEVENT(int _nState) {
			this.nState = _nState;
		}
		
		int getIndex() {
			return nState;
		}
	}

    public BaseEvent() {
        super();
    }

    public BaseEvent(String text) {
        super();
        this.text = text;
    }
    public BaseEvent(Bundle bundle) {
        super();
        this.bundle = bundle;
    }
}
