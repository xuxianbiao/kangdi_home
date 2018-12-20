package com.kandi.event;

import android.os.Bundle;

public class DRI_CHARGERTOPEvent {
	public String text;
	public Bundle bundle;

    public DRI_CHARGERTOPEvent() {
        super();
    }

    public DRI_CHARGERTOPEvent(String text) {
        super();
        this.text = text;
    }
    public DRI_CHARGERTOPEvent(Bundle bundle) {
        super();
        this.bundle = bundle;
    }
}
