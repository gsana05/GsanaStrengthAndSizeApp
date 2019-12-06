package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models;

import androidx.annotation.NonNull;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.ActivityLogIn;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getNotification() != null){
            String title = remoteMessage.getNotification().getTitle();

        }
    }
}
