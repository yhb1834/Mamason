package com.example.mamason.ui.dashboard;

public class emalarm_data {
    public String getAmpm() {
        return ampm;
    }

    public void setAmpm(String ampm) {
        this.ampm = ampm;
    }

    private String ampm;
    private int date;
    private int hour;
    private int min;
    private int repeat;
    private String name;
    private String content;
    private int alarmid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAlarmid() {
        return alarmid;
    }

    public void setAlarmid(int alarmid) {
        this.alarmid = alarmid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String contet) {
        this.content = contet;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public emalarm_data(int alarmid, String name,int date, int hour, int min, int repeat, String content) {
        this.date = date;
        this.hour = hour;
        this.min = min;
        this.repeat = repeat;
        this.name = name;
        this.alarmid = alarmid;
        this.content = content;
    }
    public emalarm_data(String ampm,int hour, int min, String content) {
        this.ampm = ampm;
        this.hour = hour;
        this.min = min;
        this.content = content;
    }
}

