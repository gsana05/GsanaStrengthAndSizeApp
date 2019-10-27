package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.comparator;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.User;

import java.util.Comparator;

public class SortUserLeague implements Comparator<User> {

    @Override
    public int compare(User o1, User o2) {
        Float user1 = o1.getBenchPress() + o1.getDeadlift() + o1.getSquat() + o1.getOverHeadPress();
        Float user2 = o2.getBenchPress() + o2.getOverHeadPress() + o2.getSquat() + o2.getDeadlift();
        return  user1.compareTo(user2);
    }
}

