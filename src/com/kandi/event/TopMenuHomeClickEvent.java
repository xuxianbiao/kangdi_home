package com.kandi.event;

public class TopMenuHomeClickEvent {
	private boolean _isClickHomeBtn;

    public TopMenuHomeClickEvent() {
        super();
    	this._isClickHomeBtn = false;
    }

    public TopMenuHomeClickEvent(boolean isClickHomeBtn) {
        super();
    	this._isClickHomeBtn = isClickHomeBtn;
    }
    
    public boolean isClickHomeBtn() {
    	return _isClickHomeBtn;
    }
}
