package com.kandi.event;



public class BottomFloatOptionMenuSwitchEvent {
	String text;

    public BottomFloatOptionMenuSwitchEvent() {
        super();
    }

    public BottomFloatOptionMenuSwitchEvent(String text) {
        super();
        this.text = text;
    }

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
    
}
