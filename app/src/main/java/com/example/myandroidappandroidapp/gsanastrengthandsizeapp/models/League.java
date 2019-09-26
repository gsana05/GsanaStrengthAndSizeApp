package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models;

import java.util.ArrayList;
import java.util.Date;

public class League {

    String leagueMasterId;
    ArrayList<String> leaguesCreated;

    public League(String leagueMasterId, ArrayList<String> leaguesCreated) {
        this.leagueMasterId = leagueMasterId;
        this.leaguesCreated = leaguesCreated;
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
