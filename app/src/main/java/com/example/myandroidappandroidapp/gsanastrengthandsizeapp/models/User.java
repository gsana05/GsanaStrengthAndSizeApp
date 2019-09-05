package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;

public class User {

    private String gymName;
    private Float benchPress;
    private Float squat;
    private Float deadlift;
    private Float overHeadPress;
    private Date date;

    public User(String gymName, Float benchPress, Float squat, Float deadlift, Float overHeadPress, Date date) {
        this.gymName = gymName;
        this.benchPress = benchPress;
        this.squat = squat;
        this.deadlift = deadlift;
        this.overHeadPress = overHeadPress;
        this.date = date;
    }


    public String getGymName() {
        return gymName;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
    }

    public Float getBenchPress() {
        return benchPress;
    }

    public void setBenchPress(Float benchPress) {
        this.benchPress = benchPress;
    }

    public Float getSquat() {
        return squat;
    }

    public void setSquat(Float squat) {
        this.squat = squat;
    }

    public Float getDeadlift() {
        return deadlift;
    }

    public void setDeadlift(Float deadlift) {
        this.deadlift = deadlift;
    }

    public Float getOverHeadPress() {
        return overHeadPress;
    }

    public void setOverHeadPress(Float overHeadPress) {
        this.overHeadPress = overHeadPress;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
