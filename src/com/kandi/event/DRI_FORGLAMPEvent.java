package com.kandi.event;

import android.os.Bundle;

public class DRI_FORGLAMPEvent {
	public String text;
	public Bundle bundle;

    public DRI_FORGLAMPEvent() {
        super();
    }

    public DRI_FORGLAMPEvent(String text) {
        super();
        this.text = text;
    }
    public DRI_FORGLAMPEvent(Bundle bundle) {
        super();
        this.bundle = bundle;
    }
}
