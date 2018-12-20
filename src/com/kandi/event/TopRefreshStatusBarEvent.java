package com.kandi.event;

public class TopRefreshStatusBarEvent {
    public int soc;
    public int mileage;
    public boolean bluetooth;
    public int wifiLevel;
    public String sTime;
    public String networkType;

    public TopRefreshStatusBarEvent() {
        super();
    }

    public TopRefreshStatusBarEvent(int soc, int mileage, boolean bluetooth, int wifiLevel, String time,String networkType) {
        super();
        this.soc = soc;
        this.mileage = mileage;
        this.bluetooth = bluetooth;
        this.wifiLevel = wifiLevel;
        this.sTime = time;
        this.networkType = networkType;
    }
    
}