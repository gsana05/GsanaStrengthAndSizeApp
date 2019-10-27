package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.comparator;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.User;

import java.util.Comparator;

public class SortUserLeagueBySquat implements Comparator<User> {
    @Override
    public int compare(User o1, User o2) {
        return o1.getSquat().compareTo(o2.getSquat());
    }
}
