package com.kandi.util;

import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

public class WifiAdmin {
	public WifiManager mWifiManager;
	private WifiInfo mWifiInfo;
	private List<ScanResult> mWifiList;
	private List<WifiConfiguration> mWifiConfigurations;
	WifiLock mWifiLock;
	public WifiAdmin(Context context){
		mWifiManager=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		mWifiInfo=mWifiManager.getConnectionInfo();
	} 
	public void openWifi(){
		if(!mWifiManager.isWifiEnabled()){
			mWifiManager.setWifiEnabled(true);
		}
	}
	public void closeWifi(){
		if(mWifiManager.isWifiEnabled()){
			mWifiManager.setWifiEnabled(false);
		}
	}
    public int checkState() {  
        return mWifiManager.getWifiState();  
    }  
	public void acquireWifiLock(){
		mWifiLock.acquire();
	}
	public void releaseWifiLock(){
		if(mWifiLock.isHeld()){
			mWifiLock.acquire();
		}
	}
	public void createWifiLock(){
		mWifiLock=mWifiManager.createWifiLock("test");
	}
	public List<WifiConfiguration> getConfiguration(){
		return mWifiConfigurations;
	}
	public void connetionConfiguration(int index){
		if(index>mWifiConfigurations.size()){
			return ;
		}
		mWifiManager.enableNetwork(mWifiConfigurations.get(index).networkId, true);
	}
	public void startScan(){
		mWifiManager.startScan();
		mWifiList=mWifiManager.getScanResults();
		mWifiConfigurations=mWifiManager.getConfiguredNetworks();
	}
	public List<ScanResult> getWifiList(){
		return mWifiList;
	}
	public StringBuffer lookUpScan(){
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<mWifiList.size();i++){
			sb.append("Index_" + new Integer(i + 1).toString() + ":");
			sb.append((mWifiList.get(i)).toString()).append("\n");
		}
		return sb;	
	}
	public String getMacAddress(){
		return (mWifiInfo==null)?"NULL":mWifiInfo.getMacAddress();
	}
	public String getBSSID(){
		return (mWifiInfo==null)?"NULL":mWifiInfo.getBSSID();
	}
	public int getIpAddress(){
		return (mWifiInfo==null)?0:mWifiInfo.getIpAddress();
	}
	public int getNetWordId(){
		return (mWifiInfo==null)?0:mWifiInfo.getNetworkId();
	}
	public String getWifiInfo(){
		return (mWifiInfo==null)?"NULL":mWifiInfo.toString();
	}
	public void addNetWork(WifiConfiguration configuration){
		int wcgId=mWifiManager.addNetwork(configuration);
		mWifiManager.enableNetwork(wcgId, true);
	}
	public void disConnectionWifi(int netId){
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
	}
	public void removeConfig(int netId){
		mWifiManager.removeNetwork(netId);
	}
	public boolean wifiIsConnect(Context context){
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiNetworkInfo.isConnected()){
            return true ;
        }
        return false ;
	}
	private int GetSecurity(String capabilities){
		if(capabilities.contains("WEP")) {  
	        return 1;  
	    } else if (capabilities.contains("PSK")) {  
	        return 2;  
	    } else if (capabilities.contains("EAP")) {  
	        return 3;  
	    }
		return 0;
	}
	public WifiConfiguration CreateWifiInfo(String SSID, String Password, String Type) 
    { 
		int sec = GetSecurity(Type);
        WifiConfiguration config = new WifiConfiguration();   
		config.allowedAuthAlgorithms.clear(); 
		config.allowedGroupCiphers.clear(); 
		config.allowedKeyManagement.clear(); 
		config.allowedPairwiseCiphers.clear(); 
		config.allowedProtocols.clear(); 
		config.SSID = "\"" + SSID + "\"";   
        if(sec == 0){ 
            config.wepKeys[0] = ""; 
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE); 
            config.wepTxKeyIndex = 0; 
        }else if(sec == 1){ 
            config.hiddenSSID = true;
            config.wepKeys[0]= "\""+Password+"\""; 
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED); 
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP); 
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP); 
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40); 
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104); 
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE); 
            config.wepTxKeyIndex = 0; 
        }else if(sec == 2){ 
	        config.preSharedKey = "\""+Password+"\""; 
	        config.hiddenSSID = true;   
	        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);   
	        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);                         
	        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);                         
	        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);                    
	        config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);                      
	        config.status = WifiConfiguration.Status.ENABLED;   
        }
        addNetWork(config);
        return config; 
    }
}

