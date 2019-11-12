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
    private String pin;
    private String email;

    private String proofBenchLink;
    private String proofSquatLink;
    private String proofDeadliftLink;
    private String proofOhpLink;

    public User(String gymName, Float benchPress, Float squat, Float deadlift, Float overHeadPress, Date date, String pin, String email, String proofBenchLink, String proofSquatLink, String proofDeadliftLink, String proofOhpLink) {
        this.gymName = gymName;
        this.benchPress = benchPress;
        this.squat = squat;
        this.deadlift = deadlift;
        this.overHeadPress = overHeadPress;
        this.date = date;
        this.pin = pin;
        this.email = email;
        this.proofBenchLink = proofBenchLink;
        this.proofSquatLink = proofSquatLink;
        this.proofDeadliftLink = proofDeadliftLink;
        this.proofOhpLink = proofOhpLink;
    }

    public String getProofBenchLink() {
        return proofBenchLink;
    }

    public void setProofBenchLink(String proofBenchLink) {
        this.proofBenchLink = proofBenchLink;
    }

    public String getProofSquatLink() {
        return proofSquatLink;
    }

    public void setProofSquatLink(String proofSquatLink) {
        this.proofSquatLink = proofSquatLink;
    }

    public String getProofDeadliftLink() {
        return proofDeadliftLink;
    }

    public void setProofDeadliftLink(String proofDeadliftLink) {
        this.proofDeadliftLink = proofDeadliftLink;
    }

    public String getProofOhpLink() {
        return proofOhpLink;
    }

    public void setProofOhpLink(String proofOhpLink) {
        this.proofOhpLink = proofOhpLink;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
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
