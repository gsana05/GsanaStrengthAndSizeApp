package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class League {

    String leagueMasterId;
    ArrayList<String> leaguesCreated;
    private ArrayList<String> flag;
    private ArrayList<String> onlyLeaguesYouCreated;

    public League(String leagueMasterId, ArrayList<String> leaguesCreated, ArrayList<String> flag, ArrayList<String> onlyLeaguesYouCreated ) {
        this.leagueMasterId = leagueMasterId;
        this.leaguesCreated = leaguesCreated;
        this.flag = flag;
        this.onlyLeaguesYouCreated = onlyLeaguesYouCreated;
    }

    public ArrayList<String> getOnlyLeaguesYouCreated() {
        return onlyLeaguesYouCreated;
    }

    public void setOnlyLeaguesYouCreated(ArrayList<String> onlyLeaguesYouCreated) {
        this.onlyLeaguesYouCreated = onlyLeaguesYouCreated;
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
