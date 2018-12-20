package com.kandi.event;

import com.kandi.event.base.BaseEvent;

import android.os.Bundle;

public class DRI_CAR_BCM_Event extends BaseEvent {

	public DRI_CAR_BCM_Event(String text) {
		super(text);
	}

	public DRI_CAR_BCM_Event(Bundle bundle) {
		super(bundle);
	}

}
