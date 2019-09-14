package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class UserProfileModelSingleton {

    private static UserProfileModelSingleton ourInstance;

    private UserProfileModelSingleton() {
    }

    public static UserProfileModelSingleton getInstance() {
        if(ourInstance == null){//if there is no instance available... create new one
            ourInstance = new UserProfileModelSingleton();

        }
        return ourInstance;
    }

    //private HashMap<String, ArrayList<DataModelResult<User>>> mProfileCallbacks;
    private HashMap<String, ArrayList<DataModelResult<User>>> mProfileCallbacks = new HashMap<>();
    private HashMap<String, User> mCachedProfile = new HashMap<>();
    private HashMap<String, ListenerRegistration> mRefs = new HashMap<>();

    public void getUserData(final String userId, final DataModelResult<User> callback){

        ArrayList<DataModelResult<User>> callbacks = null;

        if(mProfileCallbacks.containsKey(userId)){ // if the user data has been cached
            if(mCachedProfile.get(userId) != null ){
                callbacks = mProfileCallbacks.get(userId);
            }
        }
        else {
            callbacks = new ArrayList<>(); // first time getting user data
        }

        if(callbacks != null){
            if(!callbacks.contains(callback)){ // add the callback to the cache
                callbacks.add(callback);
                mProfileCallbacks.put(userId, callbacks);
            }
        }


        if(mCachedProfile.containsKey(userId)){ // if user data has been cached return the cached values do NOT go to database
            User user = mCachedProfile.get(userId);
            callback.onComplete(user, null);
        }

        if(!mRefs.containsKey(userId)){
            ListenerRegistration ref = getDatabaseRef().document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {

                    ArrayList<DataModelResult<User>> callbackList = mProfileCallbacks.get(userId);

                    if (snapshot != null && snapshot.exists()) {
                        String gymName = snapshot.getString("gymName");
                        Double bench = snapshot.getDouble("benchPress");
                        Double deadlift = snapshot.getDouble("deadlift");
                        Double squat = snapshot.getDouble("squat");
                        Double ohp = snapshot.getDouble("overHeadPress");
                        String pin = snapshot.getString("pin");
                        String email = snapshot.getString("email");
                        Date date = snapshot.getDate("date");

                        User user = new User(gymName, bench.floatValue(), squat.floatValue(), deadlift.floatValue(), ohp.floatValue(), date, pin, email);

                        if(user != null){
                            mCachedProfile.put(userId, user); // cache data
                        }

                        // return the same user data the same amount of times as the length of callBackList
                        if(callbackList != null){
                            for(DataModelResult<User> i : callbackList){
                                callback.onComplete(user, null);
                            }
                        }

                    }
                }
            });
            mRefs.put(userId, ref);
        }
    }

    private CollectionReference getDatabaseRef(){
        return FirebaseFirestore.getInstance().collection("workoutProfiles");
    }
}
