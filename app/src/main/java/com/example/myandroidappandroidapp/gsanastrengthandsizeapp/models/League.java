package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models;

import java.util.ArrayList;
import java.util.Date;

public class League {

    String leagueName;
    String leaguePin;
    Date leagueCreatedDate;
    ArrayList<String> listOfUiid;

    public League(String leagueName, String leaguePin, Date leagueCreatedDate, ArrayList<String> listOfUiid) {
        this.leagueName = leagueName;
        this.leaguePin = leaguePin;
        this.leagueCreatedDate = leagueCreatedDate;
        this.listOfUiid = listOfUiid;
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

    public ArrayList<String> getListOfUiid() {
        return listOfUiid;
    }

    public void setListOfUiid(ArrayList<String> listOfUiid) {
        this.listOfUiid = listOfUiid;
    }
}
