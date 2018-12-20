package com.kandi.event;

import android.os.Bundle;

public class DRI_BACKFOGEvent {
	public String text;
	public Bundle bundle;

    public DRI_BACKFOGEvent() {
        super();
    }

    public DRI_BACKFOGEvent(String text) {
        super();
        this.text = text;
    }
    public DRI_BACKFOGEvent(Bundle bundle) {
        super();
        this.bundle = bundle;
    }
}
