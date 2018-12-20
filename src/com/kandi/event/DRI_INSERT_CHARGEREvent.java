package com.kandi.event;

import android.os.Bundle;

public class DRI_INSERT_CHARGEREvent {
	public String text;
	public Bundle bundle;

    public DRI_INSERT_CHARGEREvent() {
        super();
    }

    public DRI_INSERT_CHARGEREvent(String text) {
        super();
        this.text = text;
    }
    public DRI_INSERT_CHARGEREvent(Bundle bundle) {
        super();
        this.bundle = bundle;
    }
}
