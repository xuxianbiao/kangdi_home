package com.kandi.event;

public class ChangeMainBgEvent {
	String text;

    public ChangeMainBgEvent() {
        super();
    }

    public ChangeMainBgEvent(String text) {
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
