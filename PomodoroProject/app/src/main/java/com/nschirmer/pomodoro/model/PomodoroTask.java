package com.nschirmer.pomodoro.model;

import java.sql.Timestamp;

public class PomodoroTask {

    private String title;
    private Timestamp whenEnded;
    private Long timeSpentDoing;
    private Long taskMaxTime;

    public PomodoroTask(){}

    public PomodoroTask(String title, Timestamp whenEnded, Long timeSpentDoing, Long taskMaxTime) {
        this.title = title;
        this.whenEnded = whenEnded;
        this.timeSpentDoing = timeSpentDoing;
        this.taskMaxTime = taskMaxTime;
    }

    public Long getID(){
        return whenEnded.getTime();
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

    public Long getTimeSpentDoing() {
        return timeSpentDoing;
    }

    public void setTimeSpentDoing(Long timeSpentDoing) {
        this.timeSpentDoing = timeSpentDoing;
    }

    public Long getTaskMaxTime() {
        return taskMaxTime;
    }

    public void setTaskMaxTime(Long taskMaxTime) {
        this.taskMaxTime = taskMaxTime;
    }

    public boolean hasValidTaskToSave(){
        return title != null && whenEnded != null && timeSpentDoing != null && taskMaxTime != null;
    }
}
