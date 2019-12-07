package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models;

import android.provider.ContactsContract;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class UserLeagueTableModelSingleton {

    private static UserLeagueTableModelSingleton ourInstance;

    private UserLeagueTableModelSingleton() {
    }

    public static UserLeagueTableModelSingleton getInstance() {
        if(ourInstance == null){//if there is no instance available... create new one
            ourInstance = new UserLeagueTableModelSingleton();

        }

        return ourInstance;
    }

    public final static int benchPress = 1;
    public final static int deadlift = 2;
    public final static int squat = 3;
    public final static int ohp = 4;


    private HashMap<String, ArrayList<DataModelResult<ArrayList<User>>>> mProfileCallbacks = new HashMap<>();
    private HashMap<String, ArrayList<User>> mCachedProfile = new HashMap<>();
    private HashMap<String, ListenerRegistration> mRefs = new HashMap<>();

    public void addLeagueTableUserProfileListener(final String userId, final ArrayList<String> leagueMasterId, final DataModelResult<ArrayList<User>> callback){

        ArrayList<DataModelResult<ArrayList<User>>> callbacks = null;

        if(mProfileCallbacks.containsKey(userId)){ // does the user already have callbacks
            if(mProfileCallbacks.get(userId) != null ){ // check if user callbacks is not null
                callbacks = mProfileCallbacks.get(userId); // gets the arrayList of callbacks and sets local variable
            }
        }
        else {
            callbacks = new ArrayList<>(); // first time getting user data aka has no callbacks
        }

        if(callbacks != null){
            Log.v("T", "kd");
            if(!callbacks.contains(callback)){ // if it does not contain this callback then add it
                callbacks.add(callback);
                mProfileCallbacks.put(userId, callbacks);
            }
        }


        if(mCachedProfile.containsKey(userId)){ // if user data has been cached return the cached values do NOT go to database
            ArrayList<User> user = mCachedProfile.get(userId);
            callback.onComplete(user, null);
        }

        if(!mRefs.containsKey(userId)){// listener gets data and real time updates if anything changes

            ListenerRegistration ref = getDatabaseRefWorkoutProfiles().addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    ArrayList<DataModelResult<ArrayList<User>>> callbackList = mProfileCallbacks.get(userId);
                    ArrayList<User> listOfUser = new ArrayList<>();

                    if(queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) { // iterate through every document to see if pins aka leagueMasterId match

                            for (String id : leagueMasterId) {
                                Map<String, Object> lp = snapshot.getData();
                                String p = (String) lp.get("pin");
                                String pin = snapshot.getString("pin");
                                if (pin != null) {
                                    if (id.equals(pin)) {
                                        String gymName = snapshot.getString("gymName");
                                        Double bench = snapshot.getDouble("benchPress");
                                        Double deadlift = snapshot.getDouble("deadlift");
                                        Double squat = snapshot.getDouble("squat");
                                        Double ohp = snapshot.getDouble("overHeadPress");

                                        String email = snapshot.getString("email");
                                        Date date = snapshot.getDate("date");

                                        String benchLink = snapshot.getString("proofBenchLink");
                                        String squatLink = snapshot.getString("proofSquatLink");
                                        String deadliftLink = snapshot.getString("proofDeadliftLink");
                                        String ohpLink = snapshot.getString("proofOhpLink");
                                        String pushNotificationToken = snapshot.getString("pushNotificationToken");

                                        Float compoundTotalLift = bench.floatValue() + squat.floatValue() + deadlift.floatValue() + ohp.floatValue();

                                        User user = new User(gymName, bench.floatValue(), squat.floatValue(), deadlift.floatValue(), ohp.floatValue(), date, pin, email, benchLink, squatLink, deadliftLink, ohpLink, pushNotificationToken, compoundTotalLift);

                                        listOfUser.add(user);
                                    }
                                }
                                else {
                                    callback.onComplete(null, null);
                                }
                            }
                        }
                        if (listOfUser != null && !listOfUser.isEmpty()) {
                            mCachedProfile.put(userId, listOfUser); // cache data
                        }

                        // return the same user data the same amount of times as the length of callBackList
                        if (callbackList != null) {
                            for (DataModelResult<ArrayList<User>> i : callbackList) {
                                callback.onComplete(listOfUser, null);
                            }
                        }
                    }
                    else{
                        callback.onComplete(null, null);
                    }

                }
            });
            mRefs.put(userId, ref); // add firebase listener
        }
    }

    public void removeLeagueTableUserProfileListener(final String userId, final DataModelResult<ArrayList<User>> callback){

        ArrayList<DataModelResult<ArrayList<User>>> callbackList =  mProfileCallbacks.get(userId);
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


    // leagueMasterId and returns Users
    // iterates through workoutProfiles collection pins and iterates through passed in pins and finds matches if they match create user object and add to list
    public void getUsersForCurrentLeague(final String leaguePin, final ArrayList<String> leagueMasterId, final DataModelResult<ArrayList<User>> callback){

        final ArrayList<User> userList = new ArrayList<>();

        getDatabaseRefWorkoutProfiles().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot data = task.getResult();
                    if(data != null){
                        for (QueryDocumentSnapshot snapshot : data) { // iterate through every document to see if pins aka leagueMasterId match

                            for(String id : leagueMasterId){
                                String pin = snapshot.getString("pin");
                                if(id.equals(pin)){
                                    String gymName = snapshot.getString("gymName");
                                    Double bench = snapshot.getDouble("benchPress");
                                    Double deadlift = snapshot.getDouble("deadlift");
                                    Double squat = snapshot.getDouble("squat");
                                    Double ohp = snapshot.getDouble("overHeadPress");

                                    String email = snapshot.getString("email");
                                    Date date = snapshot.getDate("date");

                                    //User user = new User(gymName, bench.floatValue(), squat.floatValue(), deadlift.floatValue(), ohp.floatValue(), date, pin, email);

                                    //userList.add(user);
                                }
                            }
                        }
                        callback.onComplete(userList, null);
                    }
                    else {
                        callback.onComplete(null, null);
                    }
                }
                else {
                    callback.onComplete(null, task.getException());
                }

            }
        });
    }



    private HashMap<String, ListenerRegistration> mRefsPin = new HashMap<>();
    private HashMap<String, ArrayList<DataModelResult<ArrayList<String>>>> mProfileCallbacksPin = new HashMap<>();
    private HashMap<String, ArrayList<String>> mCachedProfilePin = new HashMap<>();

    // returns all the league pins(UUID) for that user
    public void addListenerUsersWithTheSamePin(final String userId, final String leaguePin, final DataModelResult<ArrayList<String>> callback){

        ArrayList<DataModelResult<ArrayList<String>>> callbacks = null;
        final ArrayList<String> leaguePinList = new ArrayList<>();

        if(mProfileCallbacksPin.containsKey(userId)){ // does the user already have callbacks
            if(mProfileCallbacksPin.get(userId) != null ){ // check if user callbacks is not null
                callbacks = mProfileCallbacksPin.get(userId); // gets the arrayList of callbacks and sets local variable
            }
        }
        else {
            callbacks = new ArrayList<>(); // first time getting user data aka has no callbacks
        }

        if(callbacks != null){
            if(!callbacks.contains(callback)){ // if it does not contain this callback then add it
                callbacks.add(callback);
                mProfileCallbacksPin.put(userId, callbacks);
            }
        }


        if(mCachedProfile.containsKey(userId)){ // if user data has been cached return the cached values do NOT go to database
            ArrayList<String> cacheList = mCachedProfilePin.get(userId);
            callback.onComplete(cacheList, null);
        }

        if(!mRefs.containsKey(userId)){

            ListenerRegistration ref = getDatabaseRef().whereEqualTo("leaguesCreated", leaguePin).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if(queryDocumentSnapshots != null){
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String leagueMasterId = document.getString("leagueMasterId");

                            ArrayList<String> list = null;
                            Map hMap = document.getData();
                            list = (ArrayList<String>) hMap.get("leaguesCreated");

                            // look at every user list and see if the pin passed in matches any in the list
                            // if yes then that users is in the league

                            if(list != null){
                                if(list.contains(leaguePin)){
                                    leaguePinList.add(leagueMasterId);
                                }
                            }
                            Log.d(TAG, document.getId() + " => " + list);
                        }
                        callback.onComplete(leaguePinList, null);

                    }else {
                        callback.onComplete(null, null);
                    }
                }
            });
            mRefsPin.put(userId, ref);

            /*ListenerRegistration ref = getDatabaseRef().document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {

                    ArrayList<DataModelResult<ArrayList<String>>> callbackList = mProfileCallbacksPin.get(userId);

                    if(snapshot != null && snapshot.exists()){

                        ArrayList<String> list = null;
                        Map hMap = snapshot.getData();
                        if (hMap != null) {
                            list = (ArrayList<String>) hMap.get("leaguesCreated");
                        }

                        if(list != null){
                            mCachedProfilePin.put(userId, list); // cache data
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
            mRefsPin.put(userId, ref);*/
        }
    }


    public void addFlagListener(String leaguePin, final DataModelResult<ArrayList<String>> callback){
        ListenerRegistration ref = getDatabaseRefAllLeagues().document(leaguePin).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if(snapshot != null && snapshot.exists()){
                    ArrayList<String> list = null;
                    list = (ArrayList<String>) snapshot.get("flags");
                    if(list != null){
                        callback.onComplete(list, null);
                    }
                    else{
                        callback.onComplete(null, null);
                    }
                }
                else{
                    callback.onComplete(null, e);
                }
            }
        });
        mRefsFlags.put(leaguePin, ref);
    }

    private HashMap<String, ListenerRegistration> mRefsFlags = new HashMap<>();
    private HashMap<String, ArrayList<DataModelResult<ArrayList<String>>>> mProfileCallbacksFalgs = new HashMap<>();
    private HashMap<String, ArrayList<String>> mCachedProfileFlags = new HashMap<>();

    // returns all the flags for that league
    public void addFlagListener(final String userId, String pin, final DataModelResult<ArrayList<String>> callback){

        ArrayList<DataModelResult<ArrayList<String>>> callbacks = null;

        if(mProfileCallbacksFalgs.containsKey(userId)){ // does the user already have callbacks
            if(mProfileCallbacksFalgs.get(userId) != null ){ // check if user callbacks is not null
                callbacks = mProfileCallbacksFalgs.get(userId); // gets the arrayList of callbacks and sets local variable
            }
        }
        else {
            callbacks = new ArrayList<>(); // first time getting user data aka has no callbacks
        }

        if(callbacks != null){
            if(!callbacks.contains(callback)){ // if it does not contain this callback then add it
                callbacks.add(callback);
                mProfileCallbacksFalgs.put(userId, callbacks);
            }
        }


        if(mCachedProfileFlags.containsKey(userId)){ // if user data has been cached return the cached values do NOT go to database
            ArrayList<String> cacheList = mCachedProfileFlags.get(userId);
            callback.onComplete(cacheList, null);
        }

        if(!mRefsFlags.containsKey(userId)){
            ListenerRegistration ref = getDatabaseRefAllLeagues().document(pin).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {

                    ArrayList<DataModelResult<ArrayList<String>>> callbackList = mProfileCallbacksFalgs.get(userId);

                    if(snapshot != null && snapshot.exists()){

                        ArrayList<String> list = null;
                        Map hMap = snapshot.getData();
                        if (hMap != null) {
                            list = (ArrayList<String>) hMap.get("flags");
                        }

                        if(list != null){
                            mCachedProfileFlags.put(userId, list); // cache data
                        }

                        // return the same user data the same amount of times as the length of callBackList
                        if(callbackList != null){
                            for(DataModelResult<ArrayList<String>> i : callbackList){
                                callback.onComplete(list, null);
                            }
                        }

                    }
                    else {
                        callback.onComplete(new ArrayList<String>(), null);
                    }
                }
            });
            mRefsFlags.put(userId, ref);
        }
    }

    public void removeFlagListener(final String userId, final DataModelResult<ArrayList<String>> callback){
        // remove firebase listener
        ArrayList<DataModelResult<ArrayList<String>>> callbackList =  mProfileCallbacksFalgs.get(userId);

        if(callbackList != null && callbackList.contains(callback)){
            callbackList.remove(callback); // remove all the callbacks one by one

            if(callbackList.size() == 0){ // when they are no callbacks left, its time to remove the firebase listener
                mProfileCallbacksFalgs.remove(userId); // remove the key from hashmap so there is no reference to it

                // remove firebase listener
                ListenerRegistration lRef = mRefsFlags.get(userId);
                if(lRef != null){
                    lRef.remove();
                    mRefsFlags.remove(userId);
                }
            }
            else {
                mProfileCallbacksFalgs.put(userId, callbackList);  // put the callback list back into list without the one just removed
            }
        }
    }

    //League pins and returns leagueMasterId // turn that into a listener if you live update to the leagues for new players
    public void getUsersWithTheSamePin(final String leaguePin, final DataModelResult<ArrayList<String>> callback){

        final ArrayList<String> leaguePinList = new ArrayList<>();

        // getting all documents in a collection
        getDatabaseRef().get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot pop = task.getResult();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String leagueMasterId = document.getString("leagueMasterId");

                                ArrayList<String> list = null;
                                Map hMap = document.getData();
                                list = (ArrayList<String>) hMap.get("leaguesCreated");

                                // look at every user list and see if the pin passed in matches any in the list
                                // if yes then that users is in the league

                                if(list != null){
                                    if(list.contains(leaguePin)){
                                        leaguePinList.add(leagueMasterId);
                                    }
                                }
                                Log.d(TAG, document.getId() + " => " + list);
                            }

                            callback.onComplete(leaguePinList, null);
                        } else {
                            callback.onComplete(null, null);
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private CollectionReference getDatabaseRef(){
        return FirebaseFirestore.getInstance().collection("userLeagueTables");
    }

    private CollectionReference getDatabaseRefAllLeagues(){
        return FirebaseFirestore.getInstance().collection("allLeagueTables");
    }

    private CollectionReference getDatabaseRefWorkoutProfiles(){
        return FirebaseFirestore.getInstance().collection("workoutProfiles");
    }
}
