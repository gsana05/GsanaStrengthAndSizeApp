package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.UUID;

public class LeagueModelSingleton {

    private static LeagueModelSingleton ourInstance;

    private LeagueModelSingleton() {

    }

    public static LeagueModelSingleton getInstance() {

        if(ourInstance == null){
            ourInstance = new LeagueModelSingleton();
        }

        return ourInstance;
    }

    public void setLeagueTable(String leagueName, final DataModelResult<Boolean> callback){
        String userId = FirebaseAuth.getInstance().getUid();
        Date leagueStartDate = new Date(System.currentTimeMillis());
        UUID uuid = UUID.randomUUID();
        String leaguePin = uuid.toString().substring(0,8);

        League league = new League(leagueName, leaguePin, leagueStartDate);

        if(userId != null){
            getDatabaseRef().document(leaguePin).set(userId).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){
                        callback.onComplete(true, null);
                    }
                    else {
                        callback.onComplete(false, task.getException());
                    }

                }
            });
        }

    }

    private CollectionReference getDatabaseRef(){
        return FirebaseFirestore.getInstance().collection("leagueTables");
    }

}
