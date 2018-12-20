package com.kandi.event;

import android.os.Bundle;

public class DRI_TRUNKBOOTEvent {
	public String text;
	public Bundle bundle;

    public DRI_TRUNKBOOTEvent() {
        super();
    }

    public DRI_TRUNKBOOTEvent(String text) {
        super();
        this.text = text;
    }
    public DRI_TRUNKBOOTEvent(Bundle bundle) {
        super();
        this.bundle = bundle;
    }
}
