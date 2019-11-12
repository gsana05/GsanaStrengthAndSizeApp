package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Date;
import java.util.UUID;

public class UserModelSingleton {

    private String userEmail;

    private static UserModelSingleton ourInstance;

    private UserModelSingleton() {
    }

    public static UserModelSingleton getInstance() {
        if(ourInstance == null){//if there is no instance available... create new one
            ourInstance = new UserModelSingleton();

        }

        return ourInstance;
    }

    public void signUp(final String email, String password, final DataModelResult<Boolean> callback){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    userEmail = email;
                    callback.onComplete(true, null);
                }
                else {
                    callback.onComplete(false, task.getException());
                }

            }
        });
    }

    public void saveUserStats(String gymName, Float benchPress, Float squat, Float deadlift, Float overHeadPress, String benchProof, String squatProof, String deadliftProof, String ohpProof, final DataModelResult<Boolean> callback){
        String id = FirebaseAuth.getInstance().getUid();

        if(id != null){
            Date date = new Date(System.currentTimeMillis());
            UUID uuid = UUID.randomUUID();
            //String pin = uuid.toString().substring(0,8);

            User user = new User(gymName, benchPress, squat, deadlift, overHeadPress, date, id, userEmail, benchProof, squatProof, deadliftProof, ohpProof);

            getDatabaseRef().document(id).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    public void logIn(String email, String password, final DataModelResult<Boolean> callback){

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    callback.onComplete(true, null);
                }
                else {
                    callback.onComplete(false, task.getException());
                }
            }
        });

    }

    public void logout(final DataModelResult<Boolean> callback){
        final LeagueModelSingleton leagueModelSingleton = LeagueModelSingleton.getInstance();
        String id = FirebaseAuth.getInstance().getUid();

        if(id != null){

            final DataModelResult<Boolean> callbackLeaguesCreated = new DataModelResult<Boolean>() {
                @Override
                public void onComplete(Boolean data, Exception exception) {
                    if(data){
                        FirebaseAuth.getInstance().signOut();
                        callback.onComplete(true, null);
                    }
                    else {
                        callback.onComplete(false, null);
                    }
                }
            };

            DataModelResult<Boolean> callbackLeaguePins = new DataModelResult<Boolean>() {
                @Override
                public void onComplete(Boolean data, Exception exception) {
                    if(data){

                        leagueModelSingleton.clearAllLeaguesCreated(callbackLeaguesCreated);

                    }
                    else{
                        callback.onComplete(false, null);
                    }
                }
            };


            leagueModelSingleton.clearLeaguePinsFromCache(callbackLeaguePins);
        }
        else {
            callback.onComplete(false, null);
        }
    }

    private CollectionReference getDatabaseRef(){
        return FirebaseFirestore.getInstance().collection("workoutProfiles");
    }
}

