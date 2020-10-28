package com.myapplication.module;


import org.litepal.crud.DataSupport;

public class SettingEntry extends DataSupport {
    private String typeName;
    private int speed;
    private float threshold;
    private int interval;
    private int duration;
    private int type;

    public SettingEntry(String typeName, int speed, float threshold, int interval, int duration, int type) {
        this.typeName = typeName;
        this.speed = speed;
        this.threshold = threshold;
        this.interval = interval;
        this.duration = duration;
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public float getThreshold() {
        return threshold;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
