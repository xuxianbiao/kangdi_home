package com.kandi.event;

import android.os.Bundle;

public class DRI_ASSISTEDSEvent {
	public String text;
	public Bundle bundle;
    public DRI_ASSISTEDSEvent() {
        super();
    }

    public DRI_ASSISTEDSEvent(String text) {
        super();
        this.text = text;
    }
    public DRI_ASSISTEDSEvent(Bundle bundle) {
        super();
        this.bundle = bundle;
    }
}
