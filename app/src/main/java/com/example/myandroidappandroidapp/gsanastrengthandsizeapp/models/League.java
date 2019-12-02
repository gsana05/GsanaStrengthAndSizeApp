package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class League {

    String leagueMasterId;
    ArrayList<String> leaguesCreated;
    private ArrayList<String> flag;

    public League(String leagueMasterId, ArrayList<String> leaguesCreated, ArrayList<String> flag) {
        this.leagueMasterId = leagueMasterId;
        this.leaguesCreated = leaguesCreated;
        this.flag = flag;
    }

    public ArrayList<String> getFlag() {
        return flag;
    }

    public void setFlag(ArrayList<String> flag) {
        this.flag = flag;
    }

    public String getLeagueMasterId() {
        return leagueMasterId;
    }

    public void setLeagueMasterId(String leagueMasterId) {
        this.leagueMasterId = leagueMasterId;
    }

    public ArrayList<String> getLeaguesCreated() {
        return leaguesCreated;
    }

    public void setLeaguesCreated(ArrayList<String> leaguesCreated) {
        this.leaguesCreated = leaguesCreated;
    }
}
