package com.kandi.event;

public class CarChargingEvent {
	public String text;
	public int carChargingNum ;
    public CarChargingEvent() {
        super();
    }

    public CarChargingEvent(String text) {
        super();
        this.text = text;
    }
    public CarChargingEvent(int carChargingNum) {
        super();
        this.carChargingNum = carChargingNum;
    }
}
