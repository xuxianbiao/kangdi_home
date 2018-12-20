package com.kandi.event;

import android.os.Bundle;

public class DRI_DOUBLELAMPEvent {
	public String text;
	public Bundle bundle;

    public DRI_DOUBLELAMPEvent() {
        super();
    }

    public DRI_DOUBLELAMPEvent(String text) {
        super();
        this.text = text;
    }
    public DRI_DOUBLELAMPEvent(Bundle bundle) {
        super();
        this.bundle = bundle;
    }
}
