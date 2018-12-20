package com.kandi.event;

import android.os.Bundle;

public class DRI_CARDOOREvent {
	public String text;
	public Bundle bundle;

    public DRI_CARDOOREvent() {
        super();
    }

    public DRI_CARDOOREvent(String text) {
        super();
        this.text = text;
    }
    public DRI_CARDOOREvent(Bundle bundle) {
        super();
        this.bundle = bundle;
    }
}
