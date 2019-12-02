package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models;

import java.util.ArrayList;
import java.util.Date;

public class CreatedLeague {

    String leagueName;
    String leaguePin;
    Long leagueStartDate;
    String userId;
    ArrayList<String> flags;

    public CreatedLeague(String leagueName, String leaguePin, Long leagueStartDate, String userId, ArrayList<String> flags) {
        this.leagueName = leagueName;
        this.leaguePin = leaguePin;
        this.leagueStartDate = leagueStartDate;
        this.userId = userId;
        this.flags = flags;
    }

    public ArrayList<String> getFlags() {
        return flags;
    }

    public void setFlags(ArrayList<String> flags) {
        this.flags = flags;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getLeaguePin() {
        return leaguePin;
    }

    public void setLeaguePin(String leaguePin) {
        this.leaguePin = leaguePin;
    }

    public Long getLeagueStartDate() {
        return leagueStartDate;
    }

    public void setLeagueStartDate(Long leagueStartDate) {
        this.leagueStartDate = leagueStartDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
