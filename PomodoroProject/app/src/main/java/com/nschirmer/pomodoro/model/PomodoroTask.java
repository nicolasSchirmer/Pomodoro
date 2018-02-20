package com.nschirmer.pomodoro.model;

import java.sql.Timestamp;

public class PomodoroTask {

    private String title;
    private Timestamp whenEnded;

    public PomodoroTask(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getWhenEnded() {
        return whenEnded;
    }

    public void setWhenEnded(Timestamp whenEnded) {
        this.whenEnded = whenEnded;
    }
}
