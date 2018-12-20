/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\AndroidStudioProjects\\QY_K22\\kangdi_home\\src\\com\\driverlayer\\kdos_driverServer\\BlueDriver.aidl
 */
package com.driverlayer.kdos_driverServer;
public interface BlueDriver extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.driverlayer.kdos_driverServer.BlueDriver
{
private static final java.lang.String DESCRIPTOR = "com.driverlayer.kdos_driverServer.BlueDriver";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.driverlayer.kdos_driverServer.BlueDriver interface,
 * generating a proxy if needed.
 */
public static com.driverlayer.kdos_driverServer.BlueDriver asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.driverlayer.kdos_driverServer.BlueDriver))) {
return ((com.driverlayer.kdos_driverServer.BlueDriver)iin);
}
return new com.driverlayer.kdos_driverServer.BlueDriver.Stub.Proxy(obj);
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
case TRANSACTION_startCountService:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String[] _arg0;
_arg0 = data.createStringArray();
this.startCountService(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_stopCountService:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String[] _arg0;
_arg0 = data.createStringArray();
this.stopCountService(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getCountState:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String[] _arg0;
_arg0 = data.createStringArray();
int[] _arg1;
int _arg1_length = data.readInt();
if ((_arg1_length<0)) {
_arg1 = null;
}
else {
_arg1 = new int[_arg1_length];
}
this.getCountState(_arg0, _arg1);
reply.writeNoException();
reply.writeIntArray(_arg1);
return true;
}
case TRANSACTION_getCountData:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String[] _arg0;
int _arg0_length = data.readInt();
if ((_arg0_length<0)) {
_arg0 = null;
}
else {
_arg0 = new java.lang.String[_arg0_length];
}
this.getCountData(_arg0);
reply.writeNoException();
reply.writeStringArray(_arg0);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.driverlayer.kdos_driverServer.BlueDriver
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
/*
	*接口功能：开始计时服务；
	*入口参数：param[2]
	*		param[0]:number
	*		param[1]:which 0为sim，1为blue
	*返回值：开始计时服务
	*/
@Override public void startCountService(java.lang.String[] param) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStringArray(param);
mRemote.transact(Stub.TRANSACTION_startCountService, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/*
	*接口功能：销毁计时服务程序
	*入口参数：无
	*返回值：销毁计时服务程序
	*入口参数：param[0]:which 0为sim，1为blue
	*/
@Override public void stopCountService(java.lang.String[] param) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStringArray(param);
mRemote.transact(Stub.TRANSACTION_stopCountService, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/*
	*接口功能：获取计时状态
	*入口参数：state[1],btorsim[1]
	*输出参数：state[0]:1计时中，0停止计时
	*       btorsim[0]:which 0为sim，1为blue
	*返回值：获取计时状态
	*/
@Override public void getCountState(java.lang.String[] btorsim, int[] state) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStringArray(btorsim);
if ((state==null)) {
_data.writeInt(-1);
}
else {
_data.writeInt(state.length);
}
mRemote.transact(Stub.TRANSACTION_getCountState, _data, _reply, 0);
_reply.readException();
_reply.readIntArray(state);
}
finally {
_reply.recycle();
_data.recycle();
}
}
/*
	*接口功能：获取计时数据
	*入口参数：data[2]
	*输出参数： data[0]:蓝牙计时时长
	*		data[1]:sim计时时长
	*返回值：获取计时数据
	*/
@Override public void getCountData(java.lang.String[] data) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((data==null)) {
_data.writeInt(-1);
}
else {
_data.writeInt(data.length);
}
mRemote.transact(Stub.TRANSACTION_getCountData, _data, _reply, 0);
_reply.readException();
_reply.readStringArray(data);
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_startCountService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_stopCountService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getCountState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getCountData = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
/*
	*接口功能：开始计时服务；
	*入口参数：param[2]
	*		param[0]:number
	*		param[1]:which 0为sim，1为blue
	*返回值：开始计时服务
	*/
public void startCountService(java.lang.String[] param) throws android.os.RemoteException;
/*
	*接口功能：销毁计时服务程序
	*入口参数：无
	*返回值：销毁计时服务程序
	*入口参数：param[0]:which 0为sim，1为blue
	*/
public void stopCountService(java.lang.String[] param) throws android.os.RemoteException;
/*
	*接口功能：获取计时状态
	*入口参数：state[1],btorsim[1]
	*输出参数：state[0]:1计时中，0停止计时
	*       btorsim[0]:which 0为sim，1为blue
	*返回值：获取计时状态
	*/
public void getCountState(java.lang.String[] btorsim, int[] state) throws android.os.RemoteException;
/*
	*接口功能：获取计时数据
	*入口参数：data[2]
	*输出参数： data[0]:蓝牙计时时长
	*		data[1]:sim计时时长
	*返回值：获取计时数据
	*/
public void getCountData(java.lang.String[] data) throws android.os.RemoteException;
}
