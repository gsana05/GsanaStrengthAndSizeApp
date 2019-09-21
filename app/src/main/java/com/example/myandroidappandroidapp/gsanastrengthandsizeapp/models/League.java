package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models;

import java.util.ArrayList;
import java.util.Date;

public class League {

    String leagueMasterId;
    ArrayList<CreatedLeague> leaguesCreated;

    public League(String leagueMasterId, ArrayList<CreatedLeague> leaguesCreated) {
        this.leagueMasterId = leagueMasterId;
        this.leaguesCreated = leaguesCreated;
    }

    public String getLeagueMasterId() {
        return leagueMasterId;
    }

    public void setLeagueMasterId(String leagueMasterId) {
        this.leagueMasterId = leagueMasterId;
    }

    public ArrayList<CreatedLeague> getLeaguesCreated() {
        return leaguesCreated;
    }

    public void setLeaguesCreated(ArrayList<CreatedLeague> leaguesCreated) {
        this.leaguesCreated = leaguesCreated;
    }
}
