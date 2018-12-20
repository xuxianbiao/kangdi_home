package com.driverlayer.kdos_driverServer;
interface BlueDriver{
	/*
	*接口功能：开始计时服务；
	*入口参数：param[2]
	*		param[0]:number
	*		param[1]:which 0为sim，1为blue
	*返回值：开始计时服务
	*/
	void startCountService(in String[] param);
	/*
	*接口功能：销毁计时服务程序
	*入口参数：无
	*返回值：销毁计时服务程序
	*入口参数：param[0]:which 0为sim，1为blue
	*/
	void stopCountService(in String[] param);
	/*
	*接口功能：获取计时状态
	*入口参数：state[1],btorsim[1]
	*输出参数：state[0]:1计时中，0停止计时
	*       btorsim[0]:which 0为sim，1为blue
	*返回值：获取计时状态
	*/
	void getCountState(in String[] btorsim,out int[] state);
	/*
	*接口功能：获取计时数据
	*入口参数：data[2]
	*输出参数： data[0]:蓝牙计时时长
	*		data[1]:sim计时时长
	*返回值：获取计时数据
	*/
	void getCountData(out String[] data);
}
