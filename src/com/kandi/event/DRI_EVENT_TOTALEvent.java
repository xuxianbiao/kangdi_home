package com.kandi.event;

import android.os.Bundle;

public class DRI_EVENT_TOTALEvent {
	public String text;
	public Bundle bundle;

    public DRI_EVENT_TOTALEvent() {
        super();
    }

    public DRI_EVENT_TOTALEvent(String text) {
        super();
        this.text = text;
    }
    public DRI_EVENT_TOTALEvent(Bundle bundle) {
        super();
        this.bundle = bundle;
    }
}
