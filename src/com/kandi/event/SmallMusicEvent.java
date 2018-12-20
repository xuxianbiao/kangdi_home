package com.kandi.event;

public class SmallMusicEvent {
    public String text;
    public int duration;
    public String musicName;
    public String time1;
    public String time2;
    public SmallMusicEvent() {
        super();
    }

    public SmallMusicEvent(String text) {
        super();
        this.text = text;
    }
    public SmallMusicEvent(String text,String musicName,int duration,String time1,String time2) {
        super();
        this.text = text;
        this.duration = duration;
        this.musicName = musicName;
        this.time1 = time1;
        this.time2 = time2;
    }
    
    
    
}
