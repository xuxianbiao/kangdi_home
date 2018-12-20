package com.kandi.event;

import android.os.Bundle;

import com.kandi.event.base.BaseEvent;


public class DRI_PARA_Event extends BaseEvent {

	public DRI_PARA_Event(String text) {
		super(text);
	}

	public DRI_PARA_Event(Bundle bundle) {
		super(bundle);
	}

}