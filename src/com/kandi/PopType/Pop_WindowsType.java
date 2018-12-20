package com.kandi.PopType;
/*
 * 定义系统所有界面的弹出框类型
 */
public enum Pop_WindowsType {
	/*
	 * WIFI开头的信息用于确定弹出框的内容
	 */
	WIFI_PASSWORD(""),	//wifi密码设置
	WIFI_SCAN(""),		//wifi网络信息查看
	WIFI_INVADEDE(""),
	
	/*
	 * MESSAGE开头的用于确定界面返回时执行的动作
	 */
	MESSAGE_DELETE_WIFI("delate_wifi"),
	MESSAGE_SET_WIFI("wifi_seting"),
	
	CMD_COMMOM("POP_WINDOWS");//用于界面间数据传输KEY
	String Message;
	Pop_WindowsType(String str){
		Message = str;
	}
	public String GetMessage(){
		return this.Message;
	}
	
}
