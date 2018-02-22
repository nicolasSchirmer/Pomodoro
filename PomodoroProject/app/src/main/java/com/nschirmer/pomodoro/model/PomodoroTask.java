package com.nschirmer.pomodoro.model;

import java.sql.Timestamp;

public class PomodoroTask {

    private String title;
    private Timestamp whenEnded;
    private Long timeSpentDoing;
    private Long taskMaxTime;

    public PomodoroTask(){}

    public String getID(){
        return String.valueOf(whenEnded.getTime());
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
        return isCompleted() ? taskMaxTime : timeSpentDoing;
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

    public Boolean isCompleted() {
        return taskMaxTime - timeSpentDoing < 1000;
    }

}
