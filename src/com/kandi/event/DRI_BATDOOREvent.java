package com.kandi.event;

import android.os.Bundle;

public class DRI_BATDOOREvent {
	public String text;
	public Bundle bundle;

    public DRI_BATDOOREvent() {
        super();
    }

    public DRI_BATDOOREvent(String text) {
        super();
        this.text = text;
    }
    public DRI_BATDOOREvent(Bundle bundle) {
        super();
        this.bundle = bundle;
    }
}
