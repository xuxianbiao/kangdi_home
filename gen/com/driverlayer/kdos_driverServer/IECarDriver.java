/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\AndroidStudioProjects\\QY_K22\\kangdi_home\\src\\com\\driverlayer\\kdos_driverServer\\IECarDriver.aidl
 */
package com.driverlayer.kdos_driverServer;
public interface IECarDriver extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.driverlayer.kdos_driverServer.IECarDriver
{
private static final java.lang.String DESCRIPTOR = "com.driverlayer.kdos_driverServer.IECarDriver";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.driverlayer.kdos_driverServer.IECarDriver interface,
 * generating a proxy if needed.
 */
public static com.driverlayer.kdos_driverServer.IECarDriver asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.driverlayer.kdos_driverServer.IECarDriver))) {
return ((com.driverlayer.kdos_driverServer.IECarDriver)iin);
}
return new com.driverlayer.kdos_driverServer.IECarDriver.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getVersion:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getVersion();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getBaseInfo_Bcu:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getBaseInfo_Bcu();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_Ecoc_getGeneral_Battery:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int[] _arg1;
int _arg1_length = data.readInt();
if ((_arg1_length<0)) {
_arg1 = null;
}
else {
_arg1 = new int[_arg1_length];
}
java.lang.String _result = this.Ecoc_getGeneral_Battery(_arg0, _arg1);
reply.writeNoException();
reply.writeString(_result);
reply.writeIntArray(_arg1);
return true;
}
case TRANSACTION_Ecoc_getGeneral_Car:
{
data.enforceInterface(DESCRIPTOR);
int[] _arg0;
int _arg0_length = data.readInt();
if ((_arg0_length<0)) {
_arg0 = null;
}
else {
_arg0 = new int[_arg0_length];
}
int _result = this.Ecoc_getGeneral_Car(_arg0);
reply.writeNoException();
reply.writeInt(_result);
reply.writeIntArray(_arg0);
return true;
}
case TRANSACTION_Ecoc_getTrendData:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
int[] _arg3;
int _arg3_length = data.readInt();
if ((_arg3_length<0)) {
_arg3 = null;
}
else {
_arg3 = new int[_arg3_length];
}
int _result = this.Ecoc_getTrendData(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeInt(_result);
reply.writeIntArray(_arg3);
return true;
}
case TRANSACTION_getGeneral_Battery:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int[] _arg1;
int _arg1_length = data.readInt();
if ((_arg1_length<0)) {
_arg1 = null;
}
else {
_arg1 = new int[_arg1_length];
}
java.lang.String _result = this.getGeneral_Battery(_arg0, _arg1);
reply.writeNoException();
reply.writeString(_result);
reply.writeIntArray(_arg1);
return true;
}
case TRANSACTION_getGeneral_Car:
{
data.enforceInterface(DESCRIPTOR);
int[] _arg0;
int _arg0_length = data.readInt();
if ((_arg0_length<0)) {
_arg0 = null;
}
else {
_arg0 = new int[_arg0_length];
}
int _result = this.getGeneral_Car(_arg0);
reply.writeNoException();
reply.writeInt(_result);
reply.writeIntArray(_arg0);
return true;
}
case TRANSACTION_getDetial_BatCellVol:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int[] _arg1;
int _arg1_length = data.readInt();
if ((_arg1_length<0)) {
_arg1 = null;
}
else {
_arg1 = new int[_arg1_length];
}
int _result = this.getDetial_BatCellVol(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
reply.writeIntArray(_arg1);
return true;
}
case TRANSACTION_getDetial_BatTemp:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int[] _arg1;
int _arg1_length = data.readInt();
if ((_arg1_length<0)) {
_arg1 = null;
}
else {
_arg1 = new int[_arg1_length];
}
int _result = this.getDetial_BatTemp(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
reply.writeIntArray(_arg1);
return true;
}
case TRANSACTION_getAllBat_Soc:
{
data.enforceInterface(DESCRIPTOR);
int[] _arg0;
int _arg0_length = data.readInt();
if ((_arg0_length<0)) {
_arg0 = null;
}
else {
_arg0 = new int[_arg0_length];
}
int _result = this.getAllBat_Soc(_arg0);
reply.writeNoException();
reply.writeInt(_result);
reply.writeIntArray(_arg0);
return true;
}
case TRANSACTION_getGeneral_Charger:
{
data.enforceInterface(DESCRIPTOR);
int[] _arg0;
int _arg0_length = data.readInt();
if ((_arg0_length<0)) {
_arg0 = null;
}
else {
_arg0 = new int[_arg0_length];
}
java.lang.String _result = this.getGeneral_Charger(_arg0);
reply.writeNoException();
reply.writeString(_result);
reply.writeIntArray(_arg0);
return true;
}
case TRANSACTION_getError_Car:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String[] _result = this.getError_Car();
reply.writeNoException();
reply.writeStringArray(_result);
return true;
}
case TRANSACTION_cleanError_Car:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.cleanError_Car();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getWarning_Car:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String[] _result = this.getWarning_Car();
reply.writeNoException();
reply.writeStringArray(_result);
return true;
}
case TRANSACTION_cleanWarning_Car:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.cleanWarning_Car();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_set_OneKeyOpenWindow:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.set_OneKeyOpenWindow(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setCar_DNR:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.setCar_DNR(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getCar_DNR:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getCar_DNR();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setCar_WorkMode:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.setCar_WorkMode(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getCar_WorkMode:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getCar_WorkMode();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setCar_EPSassistance:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.setCar_EPSassistance(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getCar_EPSassistance:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getCar_EPSassistance();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setAirCon_Para:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
int _arg3;
_arg3 = data.readInt();
int _arg4;
_arg4 = data.readInt();
int _result = this.setAirCon_Para(_arg0, _arg1, _arg2, _arg3, _arg4);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_GetAirCon_Status:
{
data.enforceInterface(DESCRIPTOR);
int[] _arg0;
int _arg0_length = data.readInt();
if ((_arg0_length<0)) {
_arg0 = null;
}
else {
_arg0 = new int[_arg0_length];
}
int _result = this.GetAirCon_Status(_arg0);
reply.writeNoException();
reply.writeInt(_result);
reply.writeIntArray(_arg0);
return true;
}
case TRANSACTION_setCar_Action:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
this.setCar_Action(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_getCarState:
{
data.enforceInterface(DESCRIPTOR);
int[] _arg0;
int _arg0_length = data.readInt();
if ((_arg0_length<0)) {
_arg0 = null;
}
else {
_arg0 = new int[_arg0_length];
}
int _result = this.getCarState(_arg0);
reply.writeNoException();
reply.writeInt(_result);
reply.writeIntArray(_arg0);
return true;
}
case TRANSACTION_getBattaryParam:
{
data.enforceInterface(DESCRIPTOR);
int[] _arg0;
int _arg0_length = data.readInt();
if ((_arg0_length<0)) {
_arg0 = null;
}
else {
_arg0 = new int[_arg0_length];
}
int _result = this.getBattaryParam(_arg0);
reply.writeNoException();
reply.writeInt(_result);
reply.writeIntArray(_arg0);
return true;
}
case TRANSACTION_setBattaryParam:
{
data.enforceInterface(DESCRIPTOR);
int[] _arg0;
_arg0 = data.createIntArray();
int _result = this.setBattaryParam(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getBattaryChargingParam:
{
data.enforceInterface(DESCRIPTOR);
int[] _arg0;
int _arg0_length = data.readInt();
if ((_arg0_length<0)) {
_arg0 = null;
}
else {
_arg0 = new int[_arg0_length];
}
int _result = this.getBattaryChargingParam(_arg0);
reply.writeNoException();
reply.writeInt(_result);
reply.writeIntArray(_arg0);
return true;
}
case TRANSACTION_setBattaryChargingParam:
{
data.enforceInterface(DESCRIPTOR);
int[] _arg0;
_arg0 = data.createIntArray();
int _result = this.setBattaryChargingParam(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getBCUParam:
{
data.enforceInterface(DESCRIPTOR);
int[] _arg0;
int _arg0_length = data.readInt();
if ((_arg0_length<0)) {
_arg0 = null;
}
else {
_arg0 = new int[_arg0_length];
}
int _result = this.getBCUParam(_arg0);
reply.writeNoException();
reply.writeInt(_result);
reply.writeIntArray(_arg0);
return true;
}
case TRANSACTION_setBCUParam:
{
data.enforceInterface(DESCRIPTOR);
int[] _arg0;
_arg0 = data.createIntArray();
int _result = this.setBCUParam(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_resetBCURelayStick:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.resetBCURelayStick(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getMotorControlerParam:
{
data.enforceInterface(DESCRIPTOR);
int[] _arg0;
int _arg0_length = data.readInt();
if ((_arg0_length<0)) {
_arg0 = null;
}
else {
_arg0 = new int[_arg0_length];
}
int _result = this.getMotorControlerParam(_arg0);
reply.writeNoException();
reply.writeInt(_result);
reply.writeIntArray(_arg0);
return true;
}
case TRANSACTION_setMotorControlerParam:
{
data.enforceInterface(DESCRIPTOR);
int[] _arg0;
_arg0 = data.createIntArray();
int _result = this.setMotorControlerParam(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_StartUpdataHex:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
boolean _arg2;
_arg2 = (0!=data.readInt());
this.StartUpdataHex(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_GetUpdataStatus:
{
data.enforceInterface(DESCRIPTOR);
int[] _arg0;
int _arg0_length = data.readInt();
if ((_arg0_length<0)) {
_arg0 = null;
}
else {
_arg0 = new int[_arg0_length];
}
int _result = this.GetUpdataStatus(_arg0);
reply.writeNoException();
reply.writeInt(_result);
reply.writeIntArray(_arg0);
return true;
}
case TRANSACTION_GetTBoxStatus:
{
data.enforceInterface(DESCRIPTOR);
int[] _arg0;
int _arg0_length = data.readInt();
if ((_arg0_length<0)) {
_arg0 = null;
}
else {
_arg0 = new int[_arg0_length];
}
int _result = this.GetTBoxStatus(_arg0);
reply.writeNoException();
reply.writeInt(_result);
reply.writeIntArray(_arg0);
return true;
}
case TRANSACTION_GetCarVin:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.GetCarVin();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getCar_EcoEnergy:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getCar_EcoEnergy();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getCar_SportEnergy:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getCar_SportEnergy();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getPowerManager:
{
data.enforceInterface(DESCRIPTOR);
int[] _arg0;
int _arg0_length = data.readInt();
if ((_arg0_length<0)) {
_arg0 = null;
}
else {
_arg0 = new int[_arg0_length];
}
int _result = this.getPowerManager(_arg0);
reply.writeNoException();
reply.writeInt(_result);
reply.writeIntArray(_arg0);
return true;
}
case TRANSACTION_requestCarVin:
{
data.enforceInterface(DESCRIPTOR);
this.requestCarVin();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.driverlayer.kdos_driverServer.IECarDriver
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
/**
	 * 获取接口实现的版本号
	 * @return 版本号
	 */
@Override public java.lang.String getVersion() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getVersion, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 获取整车控制设备BCU的基本信息
	 * @param 			null	
	 * @return			返回BCU设备信息字符串
	 * 
	 * BCU设备信息字符串格式：
	 * 	BCU供应商名称,
	 * 	硬件版本号,
	 * 	软件版本号,
	 */
@Override public java.lang.String getBaseInfo_Bcu() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getBaseInfo_Bcu, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/*******************************************************************
	*ECOC通讯接口
	*//**
	 * 获取电池概要信息
	 * @param batNum		电池编号，正常编号介于0～3，其他值无效
	 * @param array_info	返回电池概要信息中的数字信息数组；
	 * @return				返回电池编号；
	 * 
	 * 电池概要整形数据信息顺序:
	 *	array_info[0]	获取成功返回0，失败返回负值错误代码
	 * 	array_info[1]	电池电压，范围(0~110.0V),分辨率0.1V
	 * 	array_info[2]	电池电流，范围(-1600.0~1600.0A),分辨率0.1A
	 *	array_info[3]	最大单体电压(单位1mV),
	 *	array_info[4]	最低单体电压(单位1mV),
	 *	array_info[5]	最大温度(分辨率1摄氏度),
	 *	array_info[6]	最小温度(分辨率1摄氏度),
	 * 	array_info[7]	电池绝缘电阻(低值),
	 * 	array_info[8]	SOC剩余电量(0.0~100.0%),
	 * 	array_info[9]	SOH电池健康度 (0.0~100.0%),
	 * 	array_info[10]	完整充电次数,
	 * 	array_info[11]	硬件版本 
	 * 	array_info[12]	软件版本
	 *	array_info[13]	电池充电状态(0：放电；1：充电；2：搁置)
	 *	array_info[14]	电池安全性(0:安全，1：报警，2：故障)
	 */
@Override public java.lang.String Ecoc_getGeneral_Battery(int batNum, int[] array_info) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(batNum);
if ((array_info==null)) {
_data.writeInt(-1);
}
else {
_data.writeInt(array_info.length);
}
mRemote.transact(Stub.TRANSACTION_Ecoc_getGeneral_Battery, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
_reply.readIntArray(array_info);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 获取整车概要信息
	 * @param array_info	整形数据采用数组方式顺序排列上传。
	 * @return				获取成功返回0，失败返回负值错误代码
	 * 
	 * 整车概要信息数据顺序：
	 * 	array_info[0]	整车SOC,
	 * 	array_info[1]	剩余里程,KM
	 * 	array_info[2]	控制器转速(范围：0—65536 rpm),
	 * 	array_info[3]	正极绝缘电阻(0—200000 KOhm),
	 * 	array_info[4]	负极绝缘电阻(0—200000 KOhm),
	 * 	array_info[5]	电池就位数(0-4), 
	 * 	array_info[6]	剩余电量,1KWH, 
	 * 	array_info[7]	车速,1km/h,  
	 * 	array_info[8]	充电线连接状态:0未连接，1连接, 
	 * 	array_info[9]	充电状态:0:停止；1:启动 2:故障停止    
	 */
@Override public int Ecoc_getGeneral_Car(int[] array_info) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((array_info==null)) {
_data.writeInt(-1);
}
else {
_data.writeInt(array_info.length);
}
mRemote.transact(Stub.TRANSACTION_Ecoc_getGeneral_Car, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
_reply.readIntArray(array_info);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/*********************Ecoc专用接口End*//**
	 * 获取历史趋势图数据
	 * @param nTrendType	曲线类型：1:电流(A)/2:电量(%)/3:发动机功率(kw) 0xffff 当前时间节点无数据
	 * @param nDataLen		所获取采样数量，从当前时间算起的历史数据
	 * @param nInterval		采样间隔ms
	 * @param nValueArr		数据值数组，数组长度 >= nDataLen
	 * @return				实际返回的采样量
	 */
@Override public int Ecoc_getTrendData(int nTrendType, int nDataLen, int nInterval, int[] nValueArr) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(nTrendType);
_data.writeInt(nDataLen);
_data.writeInt(nInterval);
if ((nValueArr==null)) {
_data.writeInt(-1);
}
else {
_data.writeInt(nValueArr.length);
}
mRemote.transact(Stub.TRANSACTION_Ecoc_getTrendData, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
_reply.readIntArray(nValueArr);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 获取电池概要信息
	 * @param batNum		电池编号，正常编号介于0～3，其他值无效
	 * @param array_info	返回电池概要信息中的数字信息数组；
	 * @return				返回电池概要信息中的一些字符串数据，各参数用逗号分隔；
	 * 
	 * 电池概要信息字串顺序:
	 * 	厂商名称,
	 *	电池序列号,
	 *	硬件版本号,
	 *	软件版本号,
	 * 
	 * 电池概要整形数据信息顺序:
	 *	array_info[0]	获取成功返回0，失败返回负值错误代码
	 * 	array_info[1]	电池电压范围(0~110.0V),分辨率0.1V
	 * 	array_info[2]	电池电流范围(-1600.0~1600.0A),分辨率0.1A
	 * 	array_info[3]	SOC剩余电量(0.0~100.0%),
	 * 	array_info[4]	SOH电池健康度 (0.0~100.0%),
	 * 	array_info[5]	完整充放电次数,
	 * 	array_info[6]	累计输入kwh(>=0),分辨率0.1kwh  
	 * 	array_info[7]	累计输出kwh(>=0),分辨率0.1kwh
	 * 	array_info[8]	本次输入kwh(0~65536),分辨率0.1kwh
	 * 	array_info[9]	本次输出kwh(0~65536),分辨率0.1kwh
	 * 
	 * 	array_info[10]	电池单体数量,
	 * 	array_info[11]	最高单体电压编号,
	 *	array_info[12]	最低单体电压编号,
	 *	array_info[13]	最大单体电压(单位1mV),
	 *	array_info[14]	最低单体电压(单位1mV),
	 *	array_info[15]	单体平均值	(单位1mV),
	 *
	 * 	array_info[16]	电池内部温度探头数量,
	 *	array_info[17]	最大温度探头标号,
	 *	array_info[18]	最小温度探头标号,
	 *	array_info[19]	最大温度(分辨率1摄氏度),
	 *	array_info[20]	最小温度(分辨率1摄氏度),
	 *
	 *	array_info[21]	电池加热状态(1:on/0:off),
	 *	array_info[22]	均衡状态(1:on/0:off),
	 *	array_info[23]	单体均衡启动数目,
	 *	array_info[24]	单体均衡状态掩码(int型，每bit代表一个单体均衡状态开关，1:on/0:off),
	 *	array_info[25]	电池充电状态(0：放电；1：充电；2：搁置)
	 *	array_info[26]	电池安全性(int 0:安全，1：报警，2：故障)
	 * 	array_info[27]	硬件版本 
	 * 	array_info[28]	软件版本	 
	 * 	array_info[29]	正极绝缘电阻(0—200000 KOhm),
	 * 	array_info[30]	负极绝缘电阻(0—200000 KOhm),
	 */
@Override public java.lang.String getGeneral_Battery(int batNum, int[] array_info) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(batNum);
if ((array_info==null)) {
_data.writeInt(-1);
}
else {
_data.writeInt(array_info.length);
}
mRemote.transact(Stub.TRANSACTION_getGeneral_Battery, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
_reply.readIntArray(array_info);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 获取整车概要信息
	 * @param array_info	整形数据采用数组方式顺序排列上传。
	 * @return				获取成功返回0，失败返回负值错误代码
	 * 
	 * 整车概要信息数据顺序：
	 * 	array_info[0]	整车SOC,
	 * 	array_info[1]	汽车电池总电压(0~400.0V),分辨率100mV
	 * 	array_info[2]	电流环采样电流(-1600.0~1600.0A),分辨率100mA
	 * 	array_info[3]	剩余里程,KM
	 * 	array_info[4]	控制器转速(范围：0—65536 rpm),
	 * 	array_info[5]	输出扭矩(范围：0—65536Nm),
	 * 	array_info[6]	正极绝缘电阻(0—200000 KOhm),
	 * 	array_info[7]	负极绝缘电阻(0—200000 KOhm),
	 * 	array_info[8]	电池就位数(0-4),0表示电池空，车上没有电池
	 * 	array_info[9]	整车电池就位掩码(short，1:电池已就位), bit0-3对应电池ABCD
	 * 	
	 * 	array_info[10]	最大电压单体所在电池编号,
	 * 	array_info[11]	最大电压单体编号,
	 * 	array_info[12]	最大电压单体电压值(mV,整型),
	 * 
	 * 	array_info[13]	最小电压单体所在电池编号,
	 * 	array_info[14]	最小电压单体编号,
	 * 	array_info[15]	最小电压单体电压值(mV,整型),
	 * 
	 * 	array_info[16]	最高温度所在电池编号,
	 * 	array_info[17]	最高温度单体编号,
	 * 	array_info[18]	最高温度单体温度(摄氏度,整型),
	 * 
	 * 	array_info[19]	最低温度所在电池编号,
	 * 	array_info[20]	最低温度单体编号,
	 * 	array_info[21]	最低温度单体温度(摄氏度,整型),
	 * 
	 */
@Override public int getGeneral_Car(int[] array_info) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((array_info==null)) {
_data.writeInt(-1);
}
else {
_data.writeInt(array_info.length);
}
mRemote.transact(Stub.TRANSACTION_getGeneral_Car, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
_reply.readIntArray(array_info);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/***********************************************
	 * 电动汽车状态获取接口
	 *//**
	 * 获取电池单体电压信息
	 * @param batNum	电池编号，正常编号介于0～3，其他值无效
	 * @param cell[]	返回电池单体电压信息数组，电压单位mv，单体电压值范围：0—4500mV
	 * @return			返回电池单体数量,上限为50;
	 */
@Override public int getDetial_BatCellVol(int batNum, int[] cell) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(batNum);
if ((cell==null)) {
_data.writeInt(-1);
}
else {
_data.writeInt(cell.length);
}
mRemote.transact(Stub.TRANSACTION_getDetial_BatCellVol, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
_reply.readIntArray(cell);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 获取电池详细温度信息
	 * @param batNum	电池编号，正常编号介于0～3，其他值无效
	 * @param temp[]	返回电池内部温度采样值数组，温度单位：摄氏度。
	 * @return			返回电池内部温度探头数量,上限为50；
	 */
@Override public int getDetial_BatTemp(int batNum, int[] temp) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(batNum);
if ((temp==null)) {
_data.writeInt(-1);
}
else {
_data.writeInt(temp.length);
}
mRemote.transact(Stub.TRANSACTION_getDetial_BatTemp, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
_reply.readIntArray(temp);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 获取电池SOC信息
	 * @param array_info	返回电池数量和SOC信息；
	 * @return				获取成功返回0，失败返回负值错误代码；
	 * 
	 * 电池概要整形数据信息顺序:
	 *	array_info[0]	电池数量(最大为4)，正常为2箱电池
	 * 	array_info[1]	第1箱电池SOC
	 * 	array_info[2]	第2箱电池SOC
	 *	array_info[3]	第3箱电池SOC
	 *	array_info[4]	第4箱电池SOC
	 */
@Override public int getAllBat_Soc(int[] array_info) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((array_info==null)) {
_data.writeInt(-1);
}
else {
_data.writeInt(array_info.length);
}
mRemote.transact(Stub.TRANSACTION_getAllBat_Soc, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
_reply.readIntArray(array_info);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 获取充电机状态信息
	 * @param array_info	返回充电机信息
	 * @return				返回充电机厂商信息
	 *
	 * 充电机整形数据顺序：
	 * 	array_info[0]	充电电压(V),范围(0~110.0V),分辨率0.1V
	 * 	array_info[1]	充电电流(A),范围(-1600.0~1600.0A),分辨率0.1A
	 * 	array_info[2]	本次充电电量(kwh),范围(0~65536),分辨率0.1kwh
	 * 	array_info[3]	本次充电容量(Ah),范围(0~1000),分辨率0.1Ah
	 *	array-info[4]	充电机状态，0:停止；1:启动 2:故障停止
	 */
@Override public java.lang.String getGeneral_Charger(int[] array_info) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((array_info==null)) {
_data.writeInt(-1);
}
else {
_data.writeInt(array_info.length);
}
mRemote.transact(Stub.TRANSACTION_getGeneral_Charger, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
_reply.readIntArray(array_info);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 获取系统错误信息
	 * @param num			实际返回的错误信息条数，num[0]有效
	 * @return				返回错误信息字符串数组
	 *
	 *
	 */
@Override public java.lang.String[] getError_Car() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getError_Car, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 清除系统所有历史错误信息
	 * @return				被清除的错误信息数
	 */
@Override public int cleanError_Car() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_cleanError_Car, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 获取系统警告信息
	 * @param num	实际返回的警告信息条数
	 * @return		返回警告信息字符串数组		
	 *
	 *
	 */
@Override public java.lang.String[] getWarning_Car() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getWarning_Car, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 清除系统所有历史警告信息
	 * @return		被清除的警告信息数
	 */
@Override public int cleanWarning_Car() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_cleanWarning_Car, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/******************************************************
	 * 						电动汽车设置接口
	 *//**
	 * 设置按钮状态
	 * @param buttonState	默认0一键开窗；
	 * @return				设置成功返回0，失败返回负值错误代码
	 */
@Override public int set_OneKeyOpenWindow(int buttonState) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(buttonState);
mRemote.transact(Stub.TRANSACTION_set_OneKeyOpenWindow, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 设置汽车档位
	 * @param dnrState	包含三个状态，前进(0x01)，空档(0x02)，倒车(0x03)；
	 * @return			设置成功返回0，失败返回负值错误代码
	 */
@Override public int setCar_DNR(int dnrState) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(dnrState);
mRemote.transact(Stub.TRANSACTION_setCar_DNR, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 读取车辆档位	
	 * @param null																	
	 * @return			返回档位信息 1:D档,2:N档,3:R档；
	 */
@Override public int getCar_DNR() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCar_DNR, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 设置汽车运动模式
	 * @param motor_mode 包含2个模式，经济模式（0x01）,运动模式（0x02）;
	 * @return			 设置成功返回0，失败返回负值错误代码
	 */
@Override public int setCar_WorkMode(int motor_mode) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(motor_mode);
mRemote.transact(Stub.TRANSACTION_setCar_WorkMode, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 读取车辆模式参数	
	 * @param null																	
	 * @return			返回车辆模式 1:运动模式,0:经济模式；
	 */
@Override public int getCar_WorkMode() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCar_WorkMode, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 设置EPS助力模式
	 * @param EPS_mode  包含3个模式，助力轻（0x01）,助力中（0x02）,助力重（0x03）;
	 * @return			 设置成功返回0，失败返回负值错误代码
	 */
@Override public int setCar_EPSassistance(int EPS_mode) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(EPS_mode);
mRemote.transact(Stub.TRANSACTION_setCar_EPSassistance, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 读取EPS助力模式	
	 * @param null																	
	 * @return			返回EPS助力模式 1:助力轻,2:助力中,3:助力重；
	 */
@Override public int getCar_EPSassistance() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCar_EPSassistance, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 设置汽车空调工作参数
	 * 接口参数中没有被按下的按钮下发无效值0xffff;
	 * @param isOpenAc	0x01:关闭；0x02:开启
	 * 		  isOpenPtc 0x01:关闭；0x02:开启
	 * 		  
	 * @param mode  	bit0-3空调工作模式:1:吹面；2：吹面+吹脚；3：吹脚；4：吹脚+除霜;5:除霜
	 					bit4: 为1表示开启内循环，为0默认为外循环；
	 					--------------------------------------------------
	 					--bit7--bit6--bit5--bit4--bit3--bit2--bit1--bit0--
	 					-- 保留 --保留  -- 保留--内循环--[     空调工作模式选择            ]--
	 					--------------------------------------------------
	 * @param temp  	设置温度，范围1-16档 (18-34摄氏度)；
	 * @param windSpeed	风速，1到8，共8档； 9表示关闭风机；
	 * @return			设置成功返回0，失败返回负值错误代码 
	 */
@Override public int setAirCon_Para(int isOpenAc, int isOpenPtc, int mode, int temp, int windSpeed) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(isOpenAc);
_data.writeInt(isOpenPtc);
_data.writeInt(mode);
_data.writeInt(temp);
_data.writeInt(windSpeed);
mRemote.transact(Stub.TRANSACTION_setAirCon_Para, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 读取汽车空调工作参数	
	 * @param status	数组长度为3，数据定义：
	 					status[0]	车内环境温度:单位1摄氏度，范围：-40-128
					  	status[1]	车外环境温度:(同上)
						status[2]	车内湿度：0-100%，分辨率1；
						status[3]	设定温度：范围1-16档 (18-34摄氏度)	
						status[4]	0x00:AC+PTC关闭;	
					 				bit0-bit1:1开启AC，0关闭AC;
				 					bit2-bit3:1开启PTC，0关闭PTC; 
					 				bit4-bit5:1开启风扇，0关闭风扇;  					
				 					--------------------------------------------------
				 					--bit7--bit6--bit5--bit4--bit3--bit2--bit1--bit0--
				 					-- 保留 --保留  --[	风扇控制    ]--[	PTC控制    ]--[  AC控制    ]--
				 					--------------------------------------------------	
					    status[5]  	bit0-3空调工作模式:1:吹面；2：吹面+吹脚；3：吹脚；4：吹脚+除霜;5:除霜
				 					bit4: 为1表示开启内循环，为0默认为外循环；
				 					--------------------------------------------------
				 					--bit7--bit6--bit5--bit4--bit3--bit2--bit1--bit0--
				 					-- 保留 --保留  -- 保留--内循环--[     空调工作模式选择            ]--
				 					--------------------------------------------------				 					
				 		status[6]	风速，1到8，共8档； 																	
	 * @return			1空调离线,0空调正常；
	 */
@Override public int GetAirCon_Status(int[] status) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((status==null)) {
_data.writeInt(-1);
}
else {
_data.writeInt(status.length);
}
mRemote.transact(Stub.TRANSACTION_GetAirCon_Status, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
_reply.readIntArray(status);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 汽车门窗以及大灯状态通知
	 * @param actType	控制对象类型
	 * @param actNum	控制对象编号
	 * @param actState	动作状态设置
	 * 
	 * 设备名称 actType值 actNum编号范围	actState状态定义
	 *  中控门锁	0x01	1			0x01:使能锁止/0x02:解锁
	 *  车窗		0x02	1~7			0x01:升/0x02:降/0x03:停止
	 *  后备箱  	0x03	1			0x01:使能锁止/0x02:解锁
	 *  充电盖  	0x04	1			0x01:开启/0x02:关闭
	 *  大灯		0x05	1			0x01:远光灯/0x02:近光灯/0x03:关闭
	 *  双跳		0x06	1			0x01:开启/0x02:关闭
	 *  前雾灯	0x07	1			0x01:开启/0x02:关闭
	 *  小灯		0x08	1			0x01:开启/0x02:关闭
	 *  电池仓门	0x09	1			0x01:升/0x02:降/0x03：停止
	 *  后雾灯	0x0a	1			0x01:开启/0x02:关闭	 
	 *  
	 *  上述值除了状态定义中的值，其他值都无效。车窗和车门编号顺序一致，
	 *  从驾驶员侧按照之字形计数，前左/前右/后左/后右...天窗编号为7。
	 */
@Override public void setCar_Action(int actType, int actNum, int actState) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(actType);
_data.writeInt(actNum);
_data.writeInt(actState);
mRemote.transact(Stub.TRANSACTION_setCar_Action, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * 获取汽车门窗以及大灯状态
	 * @param array_info 获取车身设备状态
	 * @return 			返回值：0表示BCM正常;1表示BCM掉线
	 * 
	 *	
	 * 车身状态定义：
	 *array_info[0] 车窗1状态:0x01:升/0x02:降/0x03:停止  
	 *array_info[1]	车窗2状态:0x01:升/0x02:降/0x03:停止
	 *array_info[2]	车窗3状态:0x01:升/0x02:降/0x03:停止
	 *array_info[3]	车窗4状态:0x01:升/0x02:降/0x03:停止
	 *array_info[4] 车窗5状态:0x01:升/0x02:降/0x03:停止
	 *array_info[5]	车窗6状态:0x01:升/0x02:降/0x03:停止
	 *array_info[6]	车窗7状态:0x01:升/0x02:降/0x03:停止
	 *array_info[7]	车门锁状态：0x01:使能锁止/0x02:解锁
	 *array_info[8] 后备箱状态：0x01:使能锁止/0x02:解锁
	 *array_info[9]	充电盖状态：0x01:开启/0x02:关闭
	 *array_info[10] 大灯状态：0x01:远光灯/0x02:近光灯/0x03:关闭
	 *array_info[11] 小灯状态：0x01:开启/0x02:关闭
	 *array_info[12]	前雾灯状态：0x01:开启/0x02:关闭
	 *array_info[13]	后雾灯状态：0x01:开启/0x02:关闭
	 *array_info[14]	双跳状态：0x01:开启/0x02:关闭
	 *array_info[15]	电池舱门状态：0x01:升/0x02:降/0x03:停止
	 *  
	 *  从驾驶员侧按照之字形计数，前左/前右/后左/后右...天窗编号为7。
	 */
@Override public int getCarState(int[] array_info) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((array_info==null)) {
_data.writeInt(-1);
}
else {
_data.writeInt(array_info.length);
}
mRemote.transact(Stub.TRANSACTION_getCarState, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
_reply.readIntArray(array_info);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/****************************************************
	 * 电池充电设置接口
	 *//**
	 * 获取当前电池充电设置参数
	 * @param param		参数数组
	 * @return 			成功返回0，失败返回负值错误代码
	 * 
	 * 参数数组定义
	 *		param[0]	充电停止总压			(单位 0.1V)
	 *		param[1]	充电停止单体电压			(单位 1mV)
	 *		param[2]	充电停止温度			(单位 1度)
	 *		param[3]	充电停止电流			(单位 0.1A)
	 *		param[4]	降流单体电压			(单位 1mV)
	 *		param[5]	降流延时时间			(单位 1S)
	 *		
	 *		param[6]	Step0_Charger_temp			(单位 1度)
	 *		param[7]	Step0_Charger_cellv			(单位:mV)
	 *		param[8]	Step0_Charger_current		(单位：0.1A)
	 *		param[9]	Step1_Charger_temp			(单位 1度)
	 *		param[10]	Step1_Charger_cellv			(单位:mV)
	 *		param[11]	Step1_Charger_current		(单位：0.1A)
	 *		param[12]	Step2_Charger_temp			(单位 1度)
	 *		param[13]	Step2_Charger_cellv			(单位:mV)
	 *		param[14]	Step2_Charger_current		(单位：0.1A)
	 *		
	 *		param[15]	Step0_Dis_temp		(单位 1度)
	 *		param[16]	Step0_Dis_cellw		(单位:mV)
	 *		param[17]	Step0_Dis_celle		(单位：mV)
	 *		param[18]	Step1_Dis_temp		(单位 1度)
	 *		param[19]	Step1_Dis_cellw		(单位:mV)
	 *		param[20]	Step1_Dis_celle		(单位：mV)
	 *		param[21]	Step2_Dis_temp		(单位 1度)
	 *		param[22]	Step2_Dis_cellw		(单位:mV)
	 *		param[23]	Step1_Dis_celle		(单位：mV)
	 */
@Override public int getBattaryParam(int[] param) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((param==null)) {
_data.writeInt(-1);
}
else {
_data.writeInt(param.length);
}
mRemote.transact(Stub.TRANSACTION_getBattaryParam, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
_reply.readIntArray(param);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 设置当前电池充电设置参数
	 * @param param		参数数组
	 * @return 			成功返回0，失败返回负值错误代码
	 * 
	 * 参数数组定义
	 *		param[0]	充电停止总压			(单位 0.1V)
	 *		param[1]	充电停止单体电压			(单位 1mV)
	 *		param[2]	充电停止温度			(单位 1度)
	 *		param[3]	充电停止电流			(单位 0.1A)
	 *		param[4]	降流单体电压			(单位 1mV)
	 *		param[5]	降流延时时间			(单位 1S)
	 *		
	 *		param[6]	Step0_Charger_temp			(单位 1度)
	 *		param[7]	Step0_Charger_cellv			(单位:mV)
	 *		param[8]	Step0_Charger_current		(单位：0.1A)
	 *		param[9]	Step1_Charger_temp			(单位 1度)
	 *		param[10]	Step1_Charger_cellv			(单位:mV)
	 *		param[11]	Step1_Charger_current		(单位：0.1A)
	 *		param[12]	Step2_Charger_temp			(单位 1度)
	 *		param[13]	Step2_Charger_cellv			(单位:mV)
	 *		param[14]	Step2_Charger_current		(单位：0.1A)
	 *		
	 *		param[15]	Step0_Dis_temp		(单位 1度)
	 *		param[16]	Step0_Dis_cellw		(单位:mV)
	 *		param[17]	Step0_Dis_celle		(单位：mV)
	 *		param[18]	Step1_Dis_temp		(单位 1度)
	 *		param[19]	Step1_Dis_cellw		(单位:mV)
	 *		param[20]	Step1_Dis_celle		(单位：mV)
	 *		param[21]	Step2_Dis_temp		(单位 1度)
	 *		param[22]	Step2_Dis_cellw		(单位:mV)
	 *		param[23]	Step1_Dis_celle		(单位：mV)
	 */
@Override public int setBattaryParam(int[] param) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeIntArray(param);
mRemote.transact(Stub.TRANSACTION_setBattaryParam, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 获取充电机参数
	 * @param param		参数数组
	 * @return 			成功返回0，失败返回负值错误代码
	 * 
	 * 参数数组定义
	 *		param[0]	充电机最高充电电压			(单位 0.1V)
	 *		param[1]	充电机最高充电电流			(单位 0.1A)
	 *		param[2]	均衡使能					(0:禁用，1:使能)
	 */
@Override public int getBattaryChargingParam(int[] param) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((param==null)) {
_data.writeInt(-1);
}
else {
_data.writeInt(param.length);
}
mRemote.transact(Stub.TRANSACTION_getBattaryChargingParam, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
_reply.readIntArray(param);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 设置充电机参数
	 * @param param		参数数组
	 * @return 			成功返回0，失败返回负值错误代码
	 * 
	 * 参数数组定义
	 *		param[0]	充电机最高充电电压			(单位 0.1V)
	 *		param[1]	充电机最高充电电流			(单位 0.1A)
	 *		param[2]	均衡使能					(0:禁用，1:使能)
	 */
@Override public int setBattaryChargingParam(int[] param) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeIntArray(param);
mRemote.transact(Stub.TRANSACTION_setBattaryChargingParam, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 获取电池组控制单元(BCU)参数
	 * @param param		参数数组
	 * @return 			成功返回0，失败返回负值错误代码
	 * 
	 * 参数数组定义
	 *		param[0]	电池粘连检测周期			(单位 秒)
	 *		param[1]	电流环选型					(#电流环编号 0~255)
	 *		param[2]	最小电池数目				(0~255)
	 *		param[3]	协议使能					(位掩码 bit0:康迪协议，bit1:大有协议，bit3:万向协议  0:禁用，1:使能)
	 *		param[4]	RTC低功耗使能				(0:禁用，1:使能)
	 *		param[5]	RTC低功耗CAN使能			(0:禁用，1:使能)
	 *		param[6]	电流环自动校准使能			(0:禁用，1:使能)
	 *		param[7]	均衡使能					(0:禁用，1:使能)
	 *		param[8]	清除电池粘连标志				(0:禁用，1:使能)
	 *		param[9]	绝缘报警值					(单位 KR)
	 *		param[10]	绝缘故障值					(单位 KR)	 
	 */
@Override public int getBCUParam(int[] param) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((param==null)) {
_data.writeInt(-1);
}
else {
_data.writeInt(param.length);
}
mRemote.transact(Stub.TRANSACTION_getBCUParam, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
_reply.readIntArray(param);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 设置电池组控制单元(BCU)参数
	 * @param param		参数数组
	 * @return 			成功返回0，失败返回负值错误代码
	 * 
	 * 参数数组定义
	 *		param[0]	电池粘连检测周期			(单位 秒)
	 *		param[1]	电流环选型					(#电流环编号 0~255)
	 *		param[2]	最小电池数目				(0~255)
	 *		param[3]	协议使能					(位掩码 bit0:康迪协议，bit1:万向协议，bit2:大有协议. 0:禁用，1:使能)
	 *		param[4]	RTC低功耗使能				(0:禁用，1:使能)
	 *		param[5]	RTC低功耗CAN使能			(0:禁用，1:使能)
	 *		param[6]	电流环自动校准使能			(0:禁用，1:使能)
	 *		param[7]	均衡使能					(0:禁用，1:使能)
	 *		param[8]	清除电池粘连标志				(0:禁用，1:使能)
	 *		param[9]	绝缘报警值					(单位 KR)
	 *		param[10]	绝缘故障值					(单位 KR)
	 */
@Override public int setBCUParam(int[] param) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeIntArray(param);
mRemote.transact(Stub.TRANSACTION_setBCUParam, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * BCU继电器粘连状态清除
	 * @param param		预留，默认0
	 * @return 			成功返回0，失败返回负值错误代码
	 * 
	 */
@Override public int resetBCURelayStick(int param) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(param);
mRemote.transact(Stub.TRANSACTION_resetBCURelayStick, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 获取电机控制机参数
	 * @param param		参数数组
	 * @return 			成功返回0，失败返回负值错误代码
	 * 
	 * 参数数组定义
	 *		param[0]	最高转数限定				(单位 RPM)
	 *		param[1]	怠速转矩限定				(单位 牛/米, -600 ~ 600)
	 *		param[2]	SOC							(单位 千分比)
	 */
@Override public int getMotorControlerParam(int[] param) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((param==null)) {
_data.writeInt(-1);
}
else {
_data.writeInt(param.length);
}
mRemote.transact(Stub.TRANSACTION_getMotorControlerParam, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
_reply.readIntArray(param);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 设置电机控制机参数
	 * @param param		参数数组
	 * @return 			成功返回0，失败返回负值错误代码
	 * 
	 * 参数数组定义
	 *		param[0]	最高转数限定				(单位 RPM)
	 *		param[1]	怠速转矩限定				(单位 牛/米, -600 ~ 600)
	 *		param[2]	SOC						(单位 0.1%)
	 */
@Override public int setMotorControlerParam(int[] param) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeIntArray(param);
mRemote.transact(Stub.TRANSACTION_setMotorControlerParam, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/*******************************************************************
	*设备升级通讯接口
	*//*
	 * @param path	升级固件位置;
	 * 		  flag  true:启动升级；false:停止升级；
	 * 		  addr_offset	升级地址（0-20）地址偏移,默认为0,对于多个相同设备的需要选择地址附加偏移，例如BMS
	 * return null	
	 */
@Override public void StartUpdataHex(java.lang.String path, int addr_offset, boolean falg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(path);
_data.writeInt(addr_offset);
_data.writeInt(((falg)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_StartUpdataHex, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/*
	 * @param 参数数组;
	 * @return 升级进度值，百分比；
	 * 
	 * param 定义：
	 * 	param[0]		厂商信息；
	 * 	param[1..2]		硬件版本号，高字节在前，低字节在后
	 * 	param[3..4]		软件版本号，高字节在前，低字节在后
	 * 	param[5..6]		升级文件大小（kb）
	 *	param[7]		升级状态 0x55进入升级,0xAA升级完成,01启动信息错误,02升级失败
	 */
@Override public int GetUpdataStatus(int[] param) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((param==null)) {
_data.writeInt(-1);
}
else {
_data.writeInt(param.length);
}
mRemote.transact(Stub.TRANSACTION_GetUpdataStatus, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
_reply.readIntArray(param);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/*
	 * @param 参数数组;
	 * 
	 * param 定义：
	 * 	param[0]		tbox防拆状态：0->默认状态;1->设备防拆;2->天线防拆;3->正常状态
	 * 	param[1]		tbox授权状态：0->默认状态;1->授权;2->未授权；
	 */
@Override public int GetTBoxStatus(int[] param) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((param==null)) {
_data.writeInt(-1);
}
else {
_data.writeInt(param.length);
}
mRemote.transact(Stub.TRANSACTION_GetTBoxStatus, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
_reply.readIntArray(param);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/*
	 * 返回空字符串为未获得全车架号;
	 */
@Override public java.lang.String GetCarVin() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_GetCarVin, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 读取车辆经济模式能量等级	
	 * @param null																	
	 * @return			返回能量等级 0:低,1:中,2:高；
	 */
@Override public int getCar_EcoEnergy() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCar_EcoEnergy, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 读取车辆运动模式能量等级		
	 * @param null																	
	 * @return			返回能量等级 0:低,1:中,2:高；
	 */
@Override public int getCar_SportEnergy() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCar_SportEnergy, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 读取能量管理概要信息
	 * @param 参数数组;
	 * @return
	 * 
	 * param 定义：
	 * 	param[0]		剩余电量；
	 * 	param[1]		总电压
	 * 	param[2]		总电流
	 * 	param[3]		单体最高
	 *	param[4]		单体最低
	 *	param[5]		最高温度
	 *	param[6]		最低温度
	 *	param[7]		正绝缘阻值
	 *	param[8]		负绝缘阻值
	 *	param[9]		电池箱数
	 *	param[10]		soc
	 *  param[11]		剩余里程
	 */
@Override public int getPowerManager(int[] param) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((param==null)) {
_data.writeInt(-1);
}
else {
_data.writeInt(param.length);
}
mRemote.transact(Stub.TRANSACTION_getPowerManager, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
_reply.readIntArray(param);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	* 请求车架号
	*/
@Override public void requestCarVin() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_requestCarVin, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_getVersion = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getBaseInfo_Bcu = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_Ecoc_getGeneral_Battery = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_Ecoc_getGeneral_Car = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_Ecoc_getTrendData = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getGeneral_Battery = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_getGeneral_Car = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_getDetial_BatCellVol = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_getDetial_BatTemp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_getAllBat_Soc = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_getGeneral_Charger = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_getError_Car = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_cleanError_Car = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_getWarning_Car = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_cleanWarning_Car = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_set_OneKeyOpenWindow = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_setCar_DNR = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_getCar_DNR = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_setCar_WorkMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
static final int TRANSACTION_getCar_WorkMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
static final int TRANSACTION_setCar_EPSassistance = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
static final int TRANSACTION_getCar_EPSassistance = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
static final int TRANSACTION_setAirCon_Para = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
static final int TRANSACTION_GetAirCon_Status = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
static final int TRANSACTION_setCar_Action = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
static final int TRANSACTION_getCarState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
static final int TRANSACTION_getBattaryParam = (android.os.IBinder.FIRST_CALL_TRANSACTION + 26);
static final int TRANSACTION_setBattaryParam = (android.os.IBinder.FIRST_CALL_TRANSACTION + 27);
static final int TRANSACTION_getBattaryChargingParam = (android.os.IBinder.FIRST_CALL_TRANSACTION + 28);
static final int TRANSACTION_setBattaryChargingParam = (android.os.IBinder.FIRST_CALL_TRANSACTION + 29);
static final int TRANSACTION_getBCUParam = (android.os.IBinder.FIRST_CALL_TRANSACTION + 30);
static final int TRANSACTION_setBCUParam = (android.os.IBinder.FIRST_CALL_TRANSACTION + 31);
static final int TRANSACTION_resetBCURelayStick = (android.os.IBinder.FIRST_CALL_TRANSACTION + 32);
static final int TRANSACTION_getMotorControlerParam = (android.os.IBinder.FIRST_CALL_TRANSACTION + 33);
static final int TRANSACTION_setMotorControlerParam = (android.os.IBinder.FIRST_CALL_TRANSACTION + 34);
static final int TRANSACTION_StartUpdataHex = (android.os.IBinder.FIRST_CALL_TRANSACTION + 35);
static final int TRANSACTION_GetUpdataStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 36);
static final int TRANSACTION_GetTBoxStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 37);
static final int TRANSACTION_GetCarVin = (android.os.IBinder.FIRST_CALL_TRANSACTION + 38);
static final int TRANSACTION_getCar_EcoEnergy = (android.os.IBinder.FIRST_CALL_TRANSACTION + 39);
static final int TRANSACTION_getCar_SportEnergy = (android.os.IBinder.FIRST_CALL_TRANSACTION + 40);
static final int TRANSACTION_getPowerManager = (android.os.IBinder.FIRST_CALL_TRANSACTION + 41);
static final int TRANSACTION_requestCarVin = (android.os.IBinder.FIRST_CALL_TRANSACTION + 42);
}
/**
	 * 获取接口实现的版本号
	 * @return 版本号
	 */
public java.lang.String getVersion() throws android.os.RemoteException;
/**
	 * 获取整车控制设备BCU的基本信息
	 * @param 			null	
	 * @return			返回BCU设备信息字符串
	 * 
	 * BCU设备信息字符串格式：
	 * 	BCU供应商名称,
	 * 	硬件版本号,
	 * 	软件版本号,
	 */
public java.lang.String getBaseInfo_Bcu() throws android.os.RemoteException;
/*******************************************************************
	*ECOC通讯接口
	*//**
	 * 获取电池概要信息
	 * @param batNum		电池编号，正常编号介于0～3，其他值无效
	 * @param array_info	返回电池概要信息中的数字信息数组；
	 * @return				返回电池编号；
	 * 
	 * 电池概要整形数据信息顺序:
	 *	array_info[0]	获取成功返回0，失败返回负值错误代码
	 * 	array_info[1]	电池电压，范围(0~110.0V),分辨率0.1V
	 * 	array_info[2]	电池电流，范围(-1600.0~1600.0A),分辨率0.1A
	 *	array_info[3]	最大单体电压(单位1mV),
	 *	array_info[4]	最低单体电压(单位1mV),
	 *	array_info[5]	最大温度(分辨率1摄氏度),
	 *	array_info[6]	最小温度(分辨率1摄氏度),
	 * 	array_info[7]	电池绝缘电阻(低值),
	 * 	array_info[8]	SOC剩余电量(0.0~100.0%),
	 * 	array_info[9]	SOH电池健康度 (0.0~100.0%),
	 * 	array_info[10]	完整充电次数,
	 * 	array_info[11]	硬件版本 
	 * 	array_info[12]	软件版本
	 *	array_info[13]	电池充电状态(0：放电；1：充电；2：搁置)
	 *	array_info[14]	电池安全性(0:安全，1：报警，2：故障)
	 */
public java.lang.String Ecoc_getGeneral_Battery(int batNum, int[] array_info) throws android.os.RemoteException;
/**
	 * 获取整车概要信息
	 * @param array_info	整形数据采用数组方式顺序排列上传。
	 * @return				获取成功返回0，失败返回负值错误代码
	 * 
	 * 整车概要信息数据顺序：
	 * 	array_info[0]	整车SOC,
	 * 	array_info[1]	剩余里程,KM
	 * 	array_info[2]	控制器转速(范围：0—65536 rpm),
	 * 	array_info[3]	正极绝缘电阻(0—200000 KOhm),
	 * 	array_info[4]	负极绝缘电阻(0—200000 KOhm),
	 * 	array_info[5]	电池就位数(0-4), 
	 * 	array_info[6]	剩余电量,1KWH, 
	 * 	array_info[7]	车速,1km/h,  
	 * 	array_info[8]	充电线连接状态:0未连接，1连接, 
	 * 	array_info[9]	充电状态:0:停止；1:启动 2:故障停止    
	 */
public int Ecoc_getGeneral_Car(int[] array_info) throws android.os.RemoteException;
/*********************Ecoc专用接口End*//**
	 * 获取历史趋势图数据
	 * @param nTrendType	曲线类型：1:电流(A)/2:电量(%)/3:发动机功率(kw) 0xffff 当前时间节点无数据
	 * @param nDataLen		所获取采样数量，从当前时间算起的历史数据
	 * @param nInterval		采样间隔ms
	 * @param nValueArr		数据值数组，数组长度 >= nDataLen
	 * @return				实际返回的采样量
	 */
public int Ecoc_getTrendData(int nTrendType, int nDataLen, int nInterval, int[] nValueArr) throws android.os.RemoteException;
/**
	 * 获取电池概要信息
	 * @param batNum		电池编号，正常编号介于0～3，其他值无效
	 * @param array_info	返回电池概要信息中的数字信息数组；
	 * @return				返回电池概要信息中的一些字符串数据，各参数用逗号分隔；
	 * 
	 * 电池概要信息字串顺序:
	 * 	厂商名称,
	 *	电池序列号,
	 *	硬件版本号,
	 *	软件版本号,
	 * 
	 * 电池概要整形数据信息顺序:
	 *	array_info[0]	获取成功返回0，失败返回负值错误代码
	 * 	array_info[1]	电池电压范围(0~110.0V),分辨率0.1V
	 * 	array_info[2]	电池电流范围(-1600.0~1600.0A),分辨率0.1A
	 * 	array_info[3]	SOC剩余电量(0.0~100.0%),
	 * 	array_info[4]	SOH电池健康度 (0.0~100.0%),
	 * 	array_info[5]	完整充放电次数,
	 * 	array_info[6]	累计输入kwh(>=0),分辨率0.1kwh  
	 * 	array_info[7]	累计输出kwh(>=0),分辨率0.1kwh
	 * 	array_info[8]	本次输入kwh(0~65536),分辨率0.1kwh
	 * 	array_info[9]	本次输出kwh(0~65536),分辨率0.1kwh
	 * 
	 * 	array_info[10]	电池单体数量,
	 * 	array_info[11]	最高单体电压编号,
	 *	array_info[12]	最低单体电压编号,
	 *	array_info[13]	最大单体电压(单位1mV),
	 *	array_info[14]	最低单体电压(单位1mV),
	 *	array_info[15]	单体平均值	(单位1mV),
	 *
	 * 	array_info[16]	电池内部温度探头数量,
	 *	array_info[17]	最大温度探头标号,
	 *	array_info[18]	最小温度探头标号,
	 *	array_info[19]	最大温度(分辨率1摄氏度),
	 *	array_info[20]	最小温度(分辨率1摄氏度),
	 *
	 *	array_info[21]	电池加热状态(1:on/0:off),
	 *	array_info[22]	均衡状态(1:on/0:off),
	 *	array_info[23]	单体均衡启动数目,
	 *	array_info[24]	单体均衡状态掩码(int型，每bit代表一个单体均衡状态开关，1:on/0:off),
	 *	array_info[25]	电池充电状态(0：放电；1：充电；2：搁置)
	 *	array_info[26]	电池安全性(int 0:安全，1：报警，2：故障)
	 * 	array_info[27]	硬件版本 
	 * 	array_info[28]	软件版本	 
	 * 	array_info[29]	正极绝缘电阻(0—200000 KOhm),
	 * 	array_info[30]	负极绝缘电阻(0—200000 KOhm),
	 */
public java.lang.String getGeneral_Battery(int batNum, int[] array_info) throws android.os.RemoteException;
/**
	 * 获取整车概要信息
	 * @param array_info	整形数据采用数组方式顺序排列上传。
	 * @return				获取成功返回0，失败返回负值错误代码
	 * 
	 * 整车概要信息数据顺序：
	 * 	array_info[0]	整车SOC,
	 * 	array_info[1]	汽车电池总电压(0~400.0V),分辨率100mV
	 * 	array_info[2]	电流环采样电流(-1600.0~1600.0A),分辨率100mA
	 * 	array_info[3]	剩余里程,KM
	 * 	array_info[4]	控制器转速(范围：0—65536 rpm),
	 * 	array_info[5]	输出扭矩(范围：0—65536Nm),
	 * 	array_info[6]	正极绝缘电阻(0—200000 KOhm),
	 * 	array_info[7]	负极绝缘电阻(0—200000 KOhm),
	 * 	array_info[8]	电池就位数(0-4),0表示电池空，车上没有电池
	 * 	array_info[9]	整车电池就位掩码(short，1:电池已就位), bit0-3对应电池ABCD
	 * 	
	 * 	array_info[10]	最大电压单体所在电池编号,
	 * 	array_info[11]	最大电压单体编号,
	 * 	array_info[12]	最大电压单体电压值(mV,整型),
	 * 
	 * 	array_info[13]	最小电压单体所在电池编号,
	 * 	array_info[14]	最小电压单体编号,
	 * 	array_info[15]	最小电压单体电压值(mV,整型),
	 * 
	 * 	array_info[16]	最高温度所在电池编号,
	 * 	array_info[17]	最高温度单体编号,
	 * 	array_info[18]	最高温度单体温度(摄氏度,整型),
	 * 
	 * 	array_info[19]	最低温度所在电池编号,
	 * 	array_info[20]	最低温度单体编号,
	 * 	array_info[21]	最低温度单体温度(摄氏度,整型),
	 * 
	 */
public int getGeneral_Car(int[] array_info) throws android.os.RemoteException;
/***********************************************
	 * 电动汽车状态获取接口
	 *//**
	 * 获取电池单体电压信息
	 * @param batNum	电池编号，正常编号介于0～3，其他值无效
	 * @param cell[]	返回电池单体电压信息数组，电压单位mv，单体电压值范围：0—4500mV
	 * @return			返回电池单体数量,上限为50;
	 */
public int getDetial_BatCellVol(int batNum, int[] cell) throws android.os.RemoteException;
/**
	 * 获取电池详细温度信息
	 * @param batNum	电池编号，正常编号介于0～3，其他值无效
	 * @param temp[]	返回电池内部温度采样值数组，温度单位：摄氏度。
	 * @return			返回电池内部温度探头数量,上限为50；
	 */
public int getDetial_BatTemp(int batNum, int[] temp) throws android.os.RemoteException;
/**
	 * 获取电池SOC信息
	 * @param array_info	返回电池数量和SOC信息；
	 * @return				获取成功返回0，失败返回负值错误代码；
	 * 
	 * 电池概要整形数据信息顺序:
	 *	array_info[0]	电池数量(最大为4)，正常为2箱电池
	 * 	array_info[1]	第1箱电池SOC
	 * 	array_info[2]	第2箱电池SOC
	 *	array_info[3]	第3箱电池SOC
	 *	array_info[4]	第4箱电池SOC
	 */
public int getAllBat_Soc(int[] array_info) throws android.os.RemoteException;
/**
	 * 获取充电机状态信息
	 * @param array_info	返回充电机信息
	 * @return				返回充电机厂商信息
	 *
	 * 充电机整形数据顺序：
	 * 	array_info[0]	充电电压(V),范围(0~110.0V),分辨率0.1V
	 * 	array_info[1]	充电电流(A),范围(-1600.0~1600.0A),分辨率0.1A
	 * 	array_info[2]	本次充电电量(kwh),范围(0~65536),分辨率0.1kwh
	 * 	array_info[3]	本次充电容量(Ah),范围(0~1000),分辨率0.1Ah
	 *	array-info[4]	充电机状态，0:停止；1:启动 2:故障停止
	 */
public java.lang.String getGeneral_Charger(int[] array_info) throws android.os.RemoteException;
/**
	 * 获取系统错误信息
	 * @param num			实际返回的错误信息条数，num[0]有效
	 * @return				返回错误信息字符串数组
	 *
	 *
	 */
public java.lang.String[] getError_Car() throws android.os.RemoteException;
/**
	 * 清除系统所有历史错误信息
	 * @return				被清除的错误信息数
	 */
public int cleanError_Car() throws android.os.RemoteException;
/**
	 * 获取系统警告信息
	 * @param num	实际返回的警告信息条数
	 * @return		返回警告信息字符串数组		
	 *
	 *
	 */
public java.lang.String[] getWarning_Car() throws android.os.RemoteException;
/**
	 * 清除系统所有历史警告信息
	 * @return		被清除的警告信息数
	 */
public int cleanWarning_Car() throws android.os.RemoteException;
/******************************************************
	 * 						电动汽车设置接口
	 *//**
	 * 设置按钮状态
	 * @param buttonState	默认0一键开窗；
	 * @return				设置成功返回0，失败返回负值错误代码
	 */
public int set_OneKeyOpenWindow(int buttonState) throws android.os.RemoteException;
/**
	 * 设置汽车档位
	 * @param dnrState	包含三个状态，前进(0x01)，空档(0x02)，倒车(0x03)；
	 * @return			设置成功返回0，失败返回负值错误代码
	 */
public int setCar_DNR(int dnrState) throws android.os.RemoteException;
/**
	 * 读取车辆档位	
	 * @param null																	
	 * @return			返回档位信息 1:D档,2:N档,3:R档；
	 */
public int getCar_DNR() throws android.os.RemoteException;
/**
	 * 设置汽车运动模式
	 * @param motor_mode 包含2个模式，经济模式（0x01）,运动模式（0x02）;
	 * @return			 设置成功返回0，失败返回负值错误代码
	 */
public int setCar_WorkMode(int motor_mode) throws android.os.RemoteException;
/**
	 * 读取车辆模式参数	
	 * @param null																	
	 * @return			返回车辆模式 1:运动模式,0:经济模式；
	 */
public int getCar_WorkMode() throws android.os.RemoteException;
/**
	 * 设置EPS助力模式
	 * @param EPS_mode  包含3个模式，助力轻（0x01）,助力中（0x02）,助力重（0x03）;
	 * @return			 设置成功返回0，失败返回负值错误代码
	 */
public int setCar_EPSassistance(int EPS_mode) throws android.os.RemoteException;
/**
	 * 读取EPS助力模式	
	 * @param null																	
	 * @return			返回EPS助力模式 1:助力轻,2:助力中,3:助力重；
	 */
public int getCar_EPSassistance() throws android.os.RemoteException;
/**
	 * 设置汽车空调工作参数
	 * 接口参数中没有被按下的按钮下发无效值0xffff;
	 * @param isOpenAc	0x01:关闭；0x02:开启
	 * 		  isOpenPtc 0x01:关闭；0x02:开启
	 * 		  
	 * @param mode  	bit0-3空调工作模式:1:吹面；2：吹面+吹脚；3：吹脚；4：吹脚+除霜;5:除霜
	 					bit4: 为1表示开启内循环，为0默认为外循环；
	 					--------------------------------------------------
	 					--bit7--bit6--bit5--bit4--bit3--bit2--bit1--bit0--
	 					-- 保留 --保留  -- 保留--内循环--[     空调工作模式选择            ]--
	 					--------------------------------------------------
	 * @param temp  	设置温度，范围1-16档 (18-34摄氏度)；
	 * @param windSpeed	风速，1到8，共8档； 9表示关闭风机；
	 * @return			设置成功返回0，失败返回负值错误代码 
	 */
public int setAirCon_Para(int isOpenAc, int isOpenPtc, int mode, int temp, int windSpeed) throws android.os.RemoteException;
/**
	 * 读取汽车空调工作参数	
	 * @param status	数组长度为3，数据定义：
	 					status[0]	车内环境温度:单位1摄氏度，范围：-40-128
					  	status[1]	车外环境温度:(同上)
						status[2]	车内湿度：0-100%，分辨率1；
						status[3]	设定温度：范围1-16档 (18-34摄氏度)	
						status[4]	0x00:AC+PTC关闭;	
					 				bit0-bit1:1开启AC，0关闭AC;
				 					bit2-bit3:1开启PTC，0关闭PTC; 
					 				bit4-bit5:1开启风扇，0关闭风扇;  					
				 					--------------------------------------------------
				 					--bit7--bit6--bit5--bit4--bit3--bit2--bit1--bit0--
				 					-- 保留 --保留  --[	风扇控制    ]--[	PTC控制    ]--[  AC控制    ]--
				 					--------------------------------------------------	
					    status[5]  	bit0-3空调工作模式:1:吹面；2：吹面+吹脚；3：吹脚；4：吹脚+除霜;5:除霜
				 					bit4: 为1表示开启内循环，为0默认为外循环；
				 					--------------------------------------------------
				 					--bit7--bit6--bit5--bit4--bit3--bit2--bit1--bit0--
				 					-- 保留 --保留  -- 保留--内循环--[     空调工作模式选择            ]--
				 					--------------------------------------------------				 					
				 		status[6]	风速，1到8，共8档； 																	
	 * @return			1空调离线,0空调正常；
	 */
public int GetAirCon_Status(int[] status) throws android.os.RemoteException;
/**
	 * 汽车门窗以及大灯状态通知
	 * @param actType	控制对象类型
	 * @param actNum	控制对象编号
	 * @param actState	动作状态设置
	 * 
	 * 设备名称 actType值 actNum编号范围	actState状态定义
	 *  中控门锁	0x01	1			0x01:使能锁止/0x02:解锁
	 *  车窗		0x02	1~7			0x01:升/0x02:降/0x03:停止
	 *  后备箱  	0x03	1			0x01:使能锁止/0x02:解锁
	 *  充电盖  	0x04	1			0x01:开启/0x02:关闭
	 *  大灯		0x05	1			0x01:远光灯/0x02:近光灯/0x03:关闭
	 *  双跳		0x06	1			0x01:开启/0x02:关闭
	 *  前雾灯	0x07	1			0x01:开启/0x02:关闭
	 *  小灯		0x08	1			0x01:开启/0x02:关闭
	 *  电池仓门	0x09	1			0x01:升/0x02:降/0x03：停止
	 *  后雾灯	0x0a	1			0x01:开启/0x02:关闭	 
	 *  
	 *  上述值除了状态定义中的值，其他值都无效。车窗和车门编号顺序一致，
	 *  从驾驶员侧按照之字形计数，前左/前右/后左/后右...天窗编号为7。
	 */
public void setCar_Action(int actType, int actNum, int actState) throws android.os.RemoteException;
/**
	 * 获取汽车门窗以及大灯状态
	 * @param array_info 获取车身设备状态
	 * @return 			返回值：0表示BCM正常;1表示BCM掉线
	 * 
	 *	
	 * 车身状态定义：
	 *array_info[0] 车窗1状态:0x01:升/0x02:降/0x03:停止  
	 *array_info[1]	车窗2状态:0x01:升/0x02:降/0x03:停止
	 *array_info[2]	车窗3状态:0x01:升/0x02:降/0x03:停止
	 *array_info[3]	车窗4状态:0x01:升/0x02:降/0x03:停止
	 *array_info[4] 车窗5状态:0x01:升/0x02:降/0x03:停止
	 *array_info[5]	车窗6状态:0x01:升/0x02:降/0x03:停止
	 *array_info[6]	车窗7状态:0x01:升/0x02:降/0x03:停止
	 *array_info[7]	车门锁状态：0x01:使能锁止/0x02:解锁
	 *array_info[8] 后备箱状态：0x01:使能锁止/0x02:解锁
	 *array_info[9]	充电盖状态：0x01:开启/0x02:关闭
	 *array_info[10] 大灯状态：0x01:远光灯/0x02:近光灯/0x03:关闭
	 *array_info[11] 小灯状态：0x01:开启/0x02:关闭
	 *array_info[12]	前雾灯状态：0x01:开启/0x02:关闭
	 *array_info[13]	后雾灯状态：0x01:开启/0x02:关闭
	 *array_info[14]	双跳状态：0x01:开启/0x02:关闭
	 *array_info[15]	电池舱门状态：0x01:升/0x02:降/0x03:停止
	 *  
	 *  从驾驶员侧按照之字形计数，前左/前右/后左/后右...天窗编号为7。
	 */
public int getCarState(int[] array_info) throws android.os.RemoteException;
/****************************************************
	 * 电池充电设置接口
	 *//**
	 * 获取当前电池充电设置参数
	 * @param param		参数数组
	 * @return 			成功返回0，失败返回负值错误代码
	 * 
	 * 参数数组定义
	 *		param[0]	充电停止总压			(单位 0.1V)
	 *		param[1]	充电停止单体电压			(单位 1mV)
	 *		param[2]	充电停止温度			(单位 1度)
	 *		param[3]	充电停止电流			(单位 0.1A)
	 *		param[4]	降流单体电压			(单位 1mV)
	 *		param[5]	降流延时时间			(单位 1S)
	 *		
	 *		param[6]	Step0_Charger_temp			(单位 1度)
	 *		param[7]	Step0_Charger_cellv			(单位:mV)
	 *		param[8]	Step0_Charger_current		(单位：0.1A)
	 *		param[9]	Step1_Charger_temp			(单位 1度)
	 *		param[10]	Step1_Charger_cellv			(单位:mV)
	 *		param[11]	Step1_Charger_current		(单位：0.1A)
	 *		param[12]	Step2_Charger_temp			(单位 1度)
	 *		param[13]	Step2_Charger_cellv			(单位:mV)
	 *		param[14]	Step2_Charger_current		(单位：0.1A)
	 *		
	 *		param[15]	Step0_Dis_temp		(单位 1度)
	 *		param[16]	Step0_Dis_cellw		(单位:mV)
	 *		param[17]	Step0_Dis_celle		(单位：mV)
	 *		param[18]	Step1_Dis_temp		(单位 1度)
	 *		param[19]	Step1_Dis_cellw		(单位:mV)
	 *		param[20]	Step1_Dis_celle		(单位：mV)
	 *		param[21]	Step2_Dis_temp		(单位 1度)
	 *		param[22]	Step2_Dis_cellw		(单位:mV)
	 *		param[23]	Step1_Dis_celle		(单位：mV)
	 */
public int getBattaryParam(int[] param) throws android.os.RemoteException;
/**
	 * 设置当前电池充电设置参数
	 * @param param		参数数组
	 * @return 			成功返回0，失败返回负值错误代码
	 * 
	 * 参数数组定义
	 *		param[0]	充电停止总压			(单位 0.1V)
	 *		param[1]	充电停止单体电压			(单位 1mV)
	 *		param[2]	充电停止温度			(单位 1度)
	 *		param[3]	充电停止电流			(单位 0.1A)
	 *		param[4]	降流单体电压			(单位 1mV)
	 *		param[5]	降流延时时间			(单位 1S)
	 *		
	 *		param[6]	Step0_Charger_temp			(单位 1度)
	 *		param[7]	Step0_Charger_cellv			(单位:mV)
	 *		param[8]	Step0_Charger_current		(单位：0.1A)
	 *		param[9]	Step1_Charger_temp			(单位 1度)
	 *		param[10]	Step1_Charger_cellv			(单位:mV)
	 *		param[11]	Step1_Charger_current		(单位：0.1A)
	 *		param[12]	Step2_Charger_temp			(单位 1度)
	 *		param[13]	Step2_Charger_cellv			(单位:mV)
	 *		param[14]	Step2_Charger_current		(单位：0.1A)
	 *		
	 *		param[15]	Step0_Dis_temp		(单位 1度)
	 *		param[16]	Step0_Dis_cellw		(单位:mV)
	 *		param[17]	Step0_Dis_celle		(单位：mV)
	 *		param[18]	Step1_Dis_temp		(单位 1度)
	 *		param[19]	Step1_Dis_cellw		(单位:mV)
	 *		param[20]	Step1_Dis_celle		(单位：mV)
	 *		param[21]	Step2_Dis_temp		(单位 1度)
	 *		param[22]	Step2_Dis_cellw		(单位:mV)
	 *		param[23]	Step1_Dis_celle		(单位：mV)
	 */
public int setBattaryParam(int[] param) throws android.os.RemoteException;
/**
	 * 获取充电机参数
	 * @param param		参数数组
	 * @return 			成功返回0，失败返回负值错误代码
	 * 
	 * 参数数组定义
	 *		param[0]	充电机最高充电电压			(单位 0.1V)
	 *		param[1]	充电机最高充电电流			(单位 0.1A)
	 *		param[2]	均衡使能					(0:禁用，1:使能)
	 */
public int getBattaryChargingParam(int[] param) throws android.os.RemoteException;
/**
	 * 设置充电机参数
	 * @param param		参数数组
	 * @return 			成功返回0，失败返回负值错误代码
	 * 
	 * 参数数组定义
	 *		param[0]	充电机最高充电电压			(单位 0.1V)
	 *		param[1]	充电机最高充电电流			(单位 0.1A)
	 *		param[2]	均衡使能					(0:禁用，1:使能)
	 */
public int setBattaryChargingParam(int[] param) throws android.os.RemoteException;
/**
	 * 获取电池组控制单元(BCU)参数
	 * @param param		参数数组
	 * @return 			成功返回0，失败返回负值错误代码
	 * 
	 * 参数数组定义
	 *		param[0]	电池粘连检测周期			(单位 秒)
	 *		param[1]	电流环选型					(#电流环编号 0~255)
	 *		param[2]	最小电池数目				(0~255)
	 *		param[3]	协议使能					(位掩码 bit0:康迪协议，bit1:大有协议，bit3:万向协议  0:禁用，1:使能)
	 *		param[4]	RTC低功耗使能				(0:禁用，1:使能)
	 *		param[5]	RTC低功耗CAN使能			(0:禁用，1:使能)
	 *		param[6]	电流环自动校准使能			(0:禁用，1:使能)
	 *		param[7]	均衡使能					(0:禁用，1:使能)
	 *		param[8]	清除电池粘连标志				(0:禁用，1:使能)
	 *		param[9]	绝缘报警值					(单位 KR)
	 *		param[10]	绝缘故障值					(单位 KR)	 
	 */
public int getBCUParam(int[] param) throws android.os.RemoteException;
/**
	 * 设置电池组控制单元(BCU)参数
	 * @param param		参数数组
	 * @return 			成功返回0，失败返回负值错误代码
	 * 
	 * 参数数组定义
	 *		param[0]	电池粘连检测周期			(单位 秒)
	 *		param[1]	电流环选型					(#电流环编号 0~255)
	 *		param[2]	最小电池数目				(0~255)
	 *		param[3]	协议使能					(位掩码 bit0:康迪协议，bit1:万向协议，bit2:大有协议. 0:禁用，1:使能)
	 *		param[4]	RTC低功耗使能				(0:禁用，1:使能)
	 *		param[5]	RTC低功耗CAN使能			(0:禁用，1:使能)
	 *		param[6]	电流环自动校准使能			(0:禁用，1:使能)
	 *		param[7]	均衡使能					(0:禁用，1:使能)
	 *		param[8]	清除电池粘连标志				(0:禁用，1:使能)
	 *		param[9]	绝缘报警值					(单位 KR)
	 *		param[10]	绝缘故障值					(单位 KR)
	 */
public int setBCUParam(int[] param) throws android.os.RemoteException;
/**
	 * BCU继电器粘连状态清除
	 * @param param		预留，默认0
	 * @return 			成功返回0，失败返回负值错误代码
	 * 
	 */
public int resetBCURelayStick(int param) throws android.os.RemoteException;
/**
	 * 获取电机控制机参数
	 * @param param		参数数组
	 * @return 			成功返回0，失败返回负值错误代码
	 * 
	 * 参数数组定义
	 *		param[0]	最高转数限定				(单位 RPM)
	 *		param[1]	怠速转矩限定				(单位 牛/米, -600 ~ 600)
	 *		param[2]	SOC							(单位 千分比)
	 */
public int getMotorControlerParam(int[] param) throws android.os.RemoteException;
/**
	 * 设置电机控制机参数
	 * @param param		参数数组
	 * @return 			成功返回0，失败返回负值错误代码
	 * 
	 * 参数数组定义
	 *		param[0]	最高转数限定				(单位 RPM)
	 *		param[1]	怠速转矩限定				(单位 牛/米, -600 ~ 600)
	 *		param[2]	SOC						(单位 0.1%)
	 */
public int setMotorControlerParam(int[] param) throws android.os.RemoteException;
/*******************************************************************
	*设备升级通讯接口
	*//*
	 * @param path	升级固件位置;
	 * 		  flag  true:启动升级；false:停止升级；
	 * 		  addr_offset	升级地址（0-20）地址偏移,默认为0,对于多个相同设备的需要选择地址附加偏移，例如BMS
	 * return null	
	 */
public void StartUpdataHex(java.lang.String path, int addr_offset, boolean falg) throws android.os.RemoteException;
/*
	 * @param 参数数组;
	 * @return 升级进度值，百分比；
	 * 
	 * param 定义：
	 * 	param[0]		厂商信息；
	 * 	param[1..2]		硬件版本号，高字节在前，低字节在后
	 * 	param[3..4]		软件版本号，高字节在前，低字节在后
	 * 	param[5..6]		升级文件大小（kb）
	 *	param[7]		升级状态 0x55进入升级,0xAA升级完成,01启动信息错误,02升级失败
	 */
public int GetUpdataStatus(int[] param) throws android.os.RemoteException;
/*
	 * @param 参数数组;
	 * 
	 * param 定义：
	 * 	param[0]		tbox防拆状态：0->默认状态;1->设备防拆;2->天线防拆;3->正常状态
	 * 	param[1]		tbox授权状态：0->默认状态;1->授权;2->未授权；
	 */
public int GetTBoxStatus(int[] param) throws android.os.RemoteException;
/*
	 * 返回空字符串为未获得全车架号;
	 */
public java.lang.String GetCarVin() throws android.os.RemoteException;
/**
	 * 读取车辆经济模式能量等级	
	 * @param null																	
	 * @return			返回能量等级 0:低,1:中,2:高；
	 */
public int getCar_EcoEnergy() throws android.os.RemoteException;
/**
	 * 读取车辆运动模式能量等级		
	 * @param null																	
	 * @return			返回能量等级 0:低,1:中,2:高；
	 */
public int getCar_SportEnergy() throws android.os.RemoteException;
/**
	 * 读取能量管理概要信息
	 * @param 参数数组;
	 * @return
	 * 
	 * param 定义：
	 * 	param[0]		剩余电量；
	 * 	param[1]		总电压
	 * 	param[2]		总电流
	 * 	param[3]		单体最高
	 *	param[4]		单体最低
	 *	param[5]		最高温度
	 *	param[6]		最低温度
	 *	param[7]		正绝缘阻值
	 *	param[8]		负绝缘阻值
	 *	param[9]		电池箱数
	 *	param[10]		soc
	 *  param[11]		剩余里程
	 */
public int getPowerManager(int[] param) throws android.os.RemoteException;
/**
	* 请求车架号
	*/
public void requestCarVin() throws android.os.RemoteException;
}
