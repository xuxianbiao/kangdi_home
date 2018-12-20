package com.kandi.event;

import android.os.Bundle;

public class DRI_CHARGER_ONOFFEvent {
	public String text;
	public Bundle bundle;

    public DRI_CHARGER_ONOFFEvent() {
        super();
    }

    public DRI_CHARGER_ONOFFEvent(String text) {
        super();
        this.text = text;
    }
    public DRI_CHARGER_ONOFFEvent(Bundle bundle) {
        super();
        this.bundle = bundle;
    }
}
