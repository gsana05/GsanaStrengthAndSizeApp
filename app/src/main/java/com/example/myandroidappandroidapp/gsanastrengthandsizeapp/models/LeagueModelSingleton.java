package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.protobuf.Any;
import com.google.protobuf.ByteString;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    private void getLeagueTable(final DataModelResult<ArrayList<String>> callback){
        final String userId = FirebaseAuth.getInstance().getUid();
        if(userId != null){

            getDatabaseRef().document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot snapshot = task.getResult();
                        if(snapshot != null && snapshot.exists()){
                            Date leagueStartDate = snapshot.getDate("leagueCreatedDate");
                            String leagueName = snapshot.getString("leagueName");
                            String leaguePin = snapshot.getString("leaguePin");

                            ArrayList<String> list = null;
                            Map hMap = snapshot.getData();
                            if (hMap != null) {
                                list = (ArrayList<String>) hMap.get("leaguesCreated");
                            }


                            //League league = new League(userId, list);
                            callback.onComplete(list, null);
                        }
                        else {
                            callback.onComplete(null, task.getException());
                        }
                    }
                    else {
                        callback.onComplete(null, task.getException());
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    callback.onComplete(null, null);
                }
            });
        }
    }

    // each time a league is created it gets the old data updates the arraylist of league and creates new object
    public void setLeagueTable(final String leagueName, final DataModelResult<Boolean> callback){

        DataModelResult<ArrayList<String>> leagueFromDatabase = new DataModelResult<ArrayList<String>>() {
            @Override
            public void onComplete(ArrayList<String> data, Exception exception) {

                ArrayList<String> officialList = new ArrayList<>();

                // if you have leagues created get them and make sure they are added to the new object
                if (data != null) {
                    officialList.addAll(data);
                }

                String userId = FirebaseAuth.getInstance().getUid();
                Date leagueStartDate = new Date(System.currentTimeMillis());
                UUID uuid = UUID.randomUUID();
                String leaguePin = uuid.toString().substring(0,8);

                //CreatedLeague createdLeague = new CreatedLeague(leagueName, leaguePin, leagueStartDate, userId);
                officialList.add(leaguePin);

                League officialLeague = new League(userId, officialList);

                if(userId != null){
                    getDatabaseRef().document(userId).set(officialLeague).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        };

        getLeagueTable(leagueFromDatabase);

    }

    private HashMap<String, ListenerRegistration> mRefs = new HashMap<>();
    private HashMap<String, ArrayList<DataModelResult<ArrayList<String>>>> mProfileCallbacks = new HashMap<>();
    private HashMap<String, ArrayList<String>> mCachedProfile = new HashMap<>();

    public void addLeagueListener(final String userId, final DataModelResult<ArrayList<String>> callback){

        ArrayList<DataModelResult<ArrayList<String>>> callbacks = null;

        if(mProfileCallbacks.containsKey(userId)){ // does the user already have callbacks
            if(mProfileCallbacks.get(userId) != null ){ // check if user callbacks is not null
                callbacks = mProfileCallbacks.get(userId); // gets the arrayList of callbacks and sets local variable
            }
        }
        else {
            callbacks = new ArrayList<>(); // first time getting user data aka has no callbacks
        }

        if(callbacks != null){
            if(!callbacks.contains(callback)){ // if it does not contain this callback then add it
                callbacks.add(callback);
                mProfileCallbacks.put(userId, callbacks);
            }
        }


        if(mCachedProfile.containsKey(userId)){ // if user data has been cached return the cached values do NOT go to database
            ArrayList<String> cacheList = mCachedProfile.get(userId);
            callback.onComplete(cacheList, null);
        }

        if(!mRefs.containsKey(userId)){
            ListenerRegistration ref = getDatabaseRef().document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {

                    ArrayList<DataModelResult<ArrayList<String>>> callbackList = mProfileCallbacks.get(userId);

                    if(snapshot != null && snapshot.exists()){

                        ArrayList<String> list = null;
                        Map hMap = snapshot.getData();
                        if (hMap != null) {
                            list = (ArrayList<String>) hMap.get("leaguesCreated");
                        }

                        if(list != null){
                            mCachedProfile.put(userId, list); // cache data
                        }

                        // return the same user data the same amount of times as the length of callBackList
                        if(callbackList != null){
                            for(DataModelResult<ArrayList<String>> i : callbackList){
                                callback.onComplete(list, null);
                            }
                        }

                    }
                    else {
                        callback.onComplete(null, null);
                    }
                }
            });
            mRefs.put(userId, ref);
        }
    }

    public void removeLeagueListener(final String userId, final DataModelResult<ArrayList<String>> callback){
        // remove firebase listener
        ArrayList<DataModelResult<ArrayList<String>>> callbackList =  mProfileCallbacks.get(userId);

        if(callbackList != null && callbackList.contains(callback)){
            callbackList.remove(callback); // remove all the callbacks one by one

            if(callbackList.size() == 0){ // when they are no callbacks left, its time to remove the firebase listener
                mProfileCallbacks.remove(userId); // remove the key from hashmap so there is no reference to it

                // remove firebase listener
                ListenerRegistration lRef = mRefs.get(userId);
                if(lRef != null){
                    lRef.remove();
                    mRefs.remove(userId);
                }
            }
            else {
                mProfileCallbacks.put(userId, callbackList);  // put the callback list back into list without the one just removed
            }
        }
    }

    private CollectionReference getDatabaseRef(){
        return FirebaseFirestore.getInstance().collection("leagueTables");
    }

}
