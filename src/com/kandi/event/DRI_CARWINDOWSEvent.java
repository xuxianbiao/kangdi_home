package com.kandi.event;

import android.os.Bundle;

public class DRI_CARWINDOWSEvent {
	public String text;
	public Bundle bundle;

    public DRI_CARWINDOWSEvent() {
        super();
    }

    public DRI_CARWINDOWSEvent(String text) {
        super();
        this.text = text;
    }
    public DRI_CARWINDOWSEvent(Bundle bundle) {
        super();
        this.bundle = bundle;
    }
}
