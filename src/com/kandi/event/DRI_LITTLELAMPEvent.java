package com.kandi.event;

import android.os.Bundle;

public class DRI_LITTLELAMPEvent {
	public String text;
	public Bundle bundle;

    public DRI_LITTLELAMPEvent() {
        super();
    }

    public DRI_LITTLELAMPEvent(String text) {
        super();
        this.text = text;
    }
    public DRI_LITTLELAMPEvent(Bundle bundle) {
        super();
        this.bundle = bundle;
    }
}
