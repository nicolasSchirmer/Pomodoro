package com.nschirmer.pomodoro.model;

import java.sql.Timestamp;

public class Pomodoro {

    private Timestamp whenEnded;

    public Pomodoro(){

    }

    public Timestamp getWhenEnded() {
        return whenEnded;
    }

    public void setWhenEnded(Timestamp whenEnded) {
        this.whenEnded = whenEnded;
    }
}
