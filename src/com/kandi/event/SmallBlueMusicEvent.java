package com.kandi.event;

public class SmallBlueMusicEvent {
    public String text;
    public int duration;
    public String musicName;
    public String time1;
    public String time2;
    public SmallBlueMusicEvent() {
        super();
    }

    public SmallBlueMusicEvent(String text) {
        super();
        this.text = text;
    }
    public SmallBlueMusicEvent(String text,String musicName,int duration,String time1,String time2) {
        super();
        this.text = text;
        this.duration = duration;
        this.musicName = musicName;
        this.time1 = time1;
        this.time2 = time2;
    }
    
    public SmallBlueMusicEvent(String text,String musicName,String time1) {
        super();
        this.text = text;
        this.musicName = musicName;
        this.time1 = time1;
    }
    
    
    
}
