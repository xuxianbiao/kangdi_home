package com.kandi.event;

import android.os.Bundle;

import com.kandi.event.base.BaseEvent;


public class DRI_ERREvent extends BaseEvent{
	public DRI_ERREvent(String text) {
		super(text);
	}

	public DRI_ERREvent(Bundle bundle) {
		super(bundle);
	}
}
