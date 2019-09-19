package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models;

import java.util.Date;

public class League {

    String leagueName;
    String leaguePin;
    Date leagueCreatedDate;

    public League(String leagueName, String leaguePin, Date leagueCreatedDate) {
        this.leagueName = leagueName;
        this.leaguePin = leaguePin;
        this.leagueCreatedDate = leagueCreatedDate;
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

    public Date getLeagueCreatedDate() {
        return leagueCreatedDate;
    }

    public void setLeagueCreatedDate(Date leagueCreatedDate) {
        this.leagueCreatedDate = leagueCreatedDate;
    }
}
