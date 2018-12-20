package com.kandi.event;

import android.os.Bundle;

public class DRI_WARINGEvent {
	public String text;
	public Bundle bundle;

    public DRI_WARINGEvent() {
        super();
    }

    public DRI_WARINGEvent(String text) {
        super();
        this.text = text;
    }
    public DRI_WARINGEvent(Bundle bundle) {
        super();
        this.bundle = bundle;
    }
}
