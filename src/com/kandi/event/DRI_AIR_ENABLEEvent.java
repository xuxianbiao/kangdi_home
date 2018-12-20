package com.kandi.event;

import com.kandi.event.base.BaseEvent;

import android.os.Bundle;

public class DRI_AIR_ENABLEEvent extends BaseEvent {

	public DRI_AIR_ENABLEEvent(String text) {
		super(text);
	}

	public DRI_AIR_ENABLEEvent(Bundle bundle) {
		super(bundle);
	}

}
