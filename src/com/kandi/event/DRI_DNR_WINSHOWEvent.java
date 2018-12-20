package com.kandi.event;

import android.os.Bundle;

public class DRI_DNR_WINSHOWEvent {
	public String text;
	public Bundle bundle;

    public DRI_DNR_WINSHOWEvent() {
        super();
    }

    public DRI_DNR_WINSHOWEvent(String text) {
        super();
        this.text = text;
    }
    public DRI_DNR_WINSHOWEvent(Bundle bundle) {
        super();
        this.bundle = bundle;
    }
}
