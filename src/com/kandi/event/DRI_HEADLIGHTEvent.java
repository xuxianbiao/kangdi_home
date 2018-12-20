package com.kandi.event;

import android.os.Bundle;

public class DRI_HEADLIGHTEvent {
	public String text;
	public Bundle bundle;

    public DRI_HEADLIGHTEvent() {
        super();
    }

    public DRI_HEADLIGHTEvent(String text) {
        super();
        this.text = text;
    }
    public DRI_HEADLIGHTEvent(Bundle bundle) {
        super();
        this.bundle = bundle;
    }
}
