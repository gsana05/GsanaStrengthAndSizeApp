package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Any;
import com.google.protobuf.ByteString;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static androidx.constraintlayout.widget.Constraints.TAG;


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
                            /*Date leagueStartDate = snapshot.getDate("leagueCreatedDate");
                            String leagueName = snapshot.getString("leagueName");
                            String leaguePin = snapshot.getString("leaguePin");*/

                            ArrayList<String> list = null;
                            Map hMap = snapshot.getData();
                            if (hMap != null) {
                                list = (ArrayList<String>) hMap.get("leaguesCreated");
                            }


                            //League league = new League(userId, list);
                            callback.onComplete(list, null);
                        }
                        else {
                            callback.onComplete(new ArrayList(), task.getException());
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

    public void isUserLeagueCreator(final String userId, final String leaguePin, final DataModelResult<Boolean> callback){
        // check if the league being deleted is by the creator - if so not allowed to delete
        getDatabaseRefAllLeagues().document(leaguePin).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot result = task.getResult();
                    String userIdFromDatabase = null;
                    if (result != null) {
                        userIdFromDatabase = result.getString("userId");
                    }

                    if(userIdFromDatabase.equals(userId)){
                        callback.onComplete(true, null); // they are the league creator
                    }
                    else{
                        callback.onComplete(false, null); // they are NOT the league creator
                    }

                }
            }
        });
    }

    public void leaveLeague(final String userId, final String leaveLeaguePin, final DataModelResult<Boolean> callback){

        DataModelResult<ArrayList<String>> callbackLeagues = new DataModelResult<ArrayList<String>>() {
            @Override
            public void onComplete(final ArrayList<String> data, Exception exception) {
                if(data != null){ // data = list of createdLeagues ids

                    // check if the league being deleted is by the creator - if so not allowed to delete
                    getDatabaseRefAllLeagues().document(leaveLeaguePin).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot result = task.getResult();
                                String userIdFromDatabase = null;
                                if (result != null) {
                                    userIdFromDatabase = result.getString("userId"); //id of who created this league
                                }

                                // check if you are the league creator
                                if( userIdFromDatabase != null && userIdFromDatabase.equals(userId)){
                                    // you created the league and you can not leave
                                    callback.onComplete(false, null);
                                }
                                else{
                                    // if you are not the league creator and want to leave you may
                                    //data.remove(leaveLeaguePin);
                                    updateLeagueMembers(userId, data, leaveLeaguePin, callback);
                                }


                                // allow the league creator to leave the league if only they are in it
                              /*  if(data.size() > 2){ // id there are more than you the creator in the league, you can not leave
                                    if (userIdFromDatabase != null) {
                                        if(userIdFromDatabase.equals(userId)){ // you can not leave a league you created
                                            callback.onComplete(false, null);
                                        }
                                        else{
                                            data.remove(leaveLeaguePin); // you can leave a league that you JOINED but you can not if you created league
                                            updateLeagueMembers(userId, data, false, callback);
                                        }
                                    }
                                }
                                else { // only one user left in league and that is the league creator
                                    //data.remove(leaveLeaguePin); // you can leave a league that you joined
                                    updateLeagueMembers(userId, data, true, callback);
                                }*/
                            }
                        }
                    });
                }
            }
        };

        getLeagueTable(callbackLeagues);
    }

    public void updateLeagueMembers(String userId, final ArrayList<String> data, String leaguePin,  final DataModelResult<Boolean> callback){

        if(data.size() < 2){ // if there is only 1 league this user is part and that is about to be deleted, just delete the whole document
            getDatabaseRef().document(userId).delete() /// problem delete the whole node, just delete the one league in the node
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // delete document from userLeagueTables
                            callback.onComplete(true, null);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    callback.onComplete(false, e);
                }
            });
        }
        else{
            data.remove(leaguePin);
            getDatabaseRef().document(userId).update("leaguesCreated",data )
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                            callback.onComplete(true, null);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error updating document", e);
                    callback.onComplete(false, e);
                }
            });
        }



        // write data into database
    /*    if(!lastMember){
            getDatabaseRef().document(userId).update("leaguesCreated",data )
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                            callback.onComplete(true, null);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error updating document", e);
                    callback.onComplete(false, e);
                }
            });
        }
        else { // if the last user in league delete document

            getDatabaseRef().document(userId).delete() /// problem delete the whole node, just delete the one league in the node
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            // delete the league
                            if(data.size() < 2){
                                deleteLeague(data.get(0), callback);
                            }
                            else {
                                callback.onComplete(false, null);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    callback.onComplete(false, e);
                }
            });
        }*/
    }

    private void deleteLeague(String leaguePin, final DataModelResult<Boolean> callback){
        getDatabaseRefAllLeagues().document(leaguePin).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onComplete(true, null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onComplete(false, null);
                    }
                });
    }

    // check if user already is part of this league
    public void checkForDuplicates(final String leaguePin, final DataModelResult<Boolean> callback){
        DataModelResult<ArrayList<String>> userAlreadyLeagues = new DataModelResult<ArrayList<String>>() {
            @Override
            public void onComplete(ArrayList<String> data, Exception exception) {
                if(data != null){
                    if(data.contains(leaguePin)){
                        callback.onComplete(false, null);
                    }
                    else {
                        callback.onComplete(true, null);
                    }
                }
                else {
                    // user does not have a league list - not part of any leagues

                }
            }
        };

        getLeagueTable(userAlreadyLeagues);
    }

    public void addToLeague(final String leaguePin, final DataModelResult<Boolean> callback){

        DataModelResult<Boolean> kok = new DataModelResult<Boolean>() {
            @Override
            public void onComplete(Boolean data, Exception exception) {
                if(data){
                    //loop through all the league tables and see if pin matches
                    getDatabaseRefAllLeagues().document(leaguePin).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot data = task.getResult();

                                if(data != null){
                                    // checking for duplicates - incase they are already part of the league
                                    if(data.getData() != null){
                                        //success
                                        DataModelResult<Boolean> value = new DataModelResult<Boolean>() {
                                            @Override
                                            public void onComplete(Boolean data, Exception exception) {
                                                if(data){
                                                    callback.onComplete(true, null);
                                                }
                                                else {
                                                    callback.onComplete(false, null);
                                                }
                                            }
                                        };
                                        addLeaguePinToUser(leaguePin, value);
                                    }
                                    else {
                                        callback.onComplete(false, null);
                                    }
                                }
                                else {
                                    callback.onComplete(false, null);
                                }

                            }
                            else {
                                callback.onComplete(false, task.getException());
                            }
                        }
                    });
                }
                else{
                    callback.onComplete(false, null);
                }
            }
        };

      checkForDuplicates(leaguePin, kok);
    }

    public void getFlags(final String pin, final DataModelResult<ArrayList<String>> callback){
        final String userId = FirebaseAuth.getInstance().getUid();
        if(userId != null){

            getDatabaseRef().document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot snapshot = task.getResult();
                        if(snapshot != null && snapshot.exists()){
                            /*Date leagueStartDate = snapshot.getDate("leagueCreatedDate");
                            String leagueName = snapshot.getString("leagueName");
                            String leaguePin = snapshot.getString("leaguePin");*/

                            ArrayList<String> list = null;
                            Map hMap = snapshot.getData();
                            if (hMap != null) {
                                list = (ArrayList<String>) hMap.get("flag");
                            }

                            if(list != null){
                                if(list.contains(pin)){
                                    //todo stop duplicate requests
                                    callback.onComplete(null, null);
                                }
                                else{
                                    callback.onComplete(list, null);
                                }
                            }
                            else{
                                callback.onComplete(new ArrayList<String>(), null);
                            }

                            //League league = new League(userId, list);

                        }
                        else {
                            callback.onComplete(new ArrayList(), task.getException());
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

    public void getLeague(String pin, final DataModelResult<CreatedLeague> callback){
        getDatabaseRefAllLeagues().document(pin).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){

                            DocumentSnapshot document = task.getResult();

                            if(document != null){
                                String leaguePinDatabase = (String) document.get("leaguePin");
                                String leagueNameDatabase = (String) document.get("leagueName");
                                Long leagueStartDate = (Long) document.get("leagueStartDate");
                                String userIdDatabase = (String) document.get("userId");
                                ArrayList<String> flags = (ArrayList<String>) document.get("flags");

                                CreatedLeague createdLeague = new CreatedLeague(leagueNameDatabase, leaguePinDatabase, leagueStartDate, userIdDatabase, flags);

                                callback.onComplete(createdLeague, null);
                            }
                            else{
                                callback.onComplete(null, null);
                            }
                        }
                        else{
                            callback.onComplete(null, null);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onComplete(null, e);
            }
        });
    }

    public void deleteFlagToLeague(final String pin, final String userId, final DataModelResult<Boolean> callback){
        DataModelResult<CreatedLeague> createdLeague = new DataModelResult<CreatedLeague>() {
            @Override
            public void onComplete(CreatedLeague data, Exception exception) {

                if(data != null){
                    ArrayList<String> flags = data.flags;
                    if(flags.contains(userId)){
                        flags.remove(userId);
                        CreatedLeague newCreatedLeague = new CreatedLeague(data.leagueName, data.leaguePin, data.leagueStartDate, data.userId, flags);

                        getDatabaseRefAllLeagues().document(pin).set(newCreatedLeague)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        callback.onComplete(true, null);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                callback.onComplete(false, null);
                            }
                        });

                    }
                    else{
                        // this user is is not in the flagged list
                        callback.onComplete(false, null);
                    }
                }

            }
        };

        getLeague(pin,createdLeague);
    }

    public void addFlagToLeague(final String pin, final String userId, final DataModelResult<Boolean> callback){

        DataModelResult<CreatedLeague> createdLeague = new DataModelResult<CreatedLeague>() {
            @Override
            public void onComplete(CreatedLeague data, Exception exception) {

                if(data != null){
                    ArrayList<String> flags = data.flags;
                    if(flags.contains(userId)){
                        callback.onComplete(false, null);
                    }
                    else{
                        flags.add(userId);
                        CreatedLeague newCreatedLeague = new CreatedLeague(data.leagueName, data.leaguePin, data.leagueStartDate, data.userId, flags);

                        getDatabaseRefAllLeagues().document(pin).set(newCreatedLeague)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        callback.onComplete(true, null);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                callback.onComplete(false, null);
                            }
                        });
                    }
                }

            }
        };

        getLeague(pin,createdLeague);
    }


   /* public void addFlagToLeague(final String pin, final DataModelResult<Boolean> callback){

        final String userId = FirebaseAuth.getInstance().getUid();


        DataModelResult<ArrayList<String>> data = new DataModelResult<ArrayList<String>>() {
            @Override
            public void onComplete(final ArrayList<String> officialList, Exception exception) {

                if(officialList != null){

                    DataModelResult<ArrayList<String>> flags = new DataModelResult<ArrayList<String>>() {
                        @Override
                        public void onComplete(ArrayList<String> leagueFlags, Exception exception) {
                            if(leagueFlags != null){

                                leagueFlags.add(pin);
                                ArrayList<String> officialListFlags = new ArrayList<>(leagueFlags);

                                if (userId != null) {

                                    final League officialLeague = new League(userId, officialList, officialListFlags);

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
                            else{
                                callback.onComplete(false, null);
                            }
                        }
                    };


                    getFlags(pin, flags);
                }
                else {
                    callback.onComplete(false, null);
                }
            }
        };

        getLeagueTable(data);

    }*/

    public void addLeaguePinToUser(final String leaguePin, final DataModelResult<Boolean> callback){

        final String userId = FirebaseAuth.getInstance().getUid();


        DataModelResult<ArrayList<String>> data = new DataModelResult<ArrayList<String>>() {
            @Override
            public void onComplete(ArrayList<String> data, Exception exception) {

                if(data != null){
                    data.add(leaguePin);
                    final ArrayList<String> officialList = new ArrayList<>(data);

                    if (userId != null) {



                        DataModelResult<ArrayList<String>> getFlags = new DataModelResult<ArrayList<String>>() {
                            @Override
                            public void onComplete(ArrayList<String> data, Exception exception) {
                                final League officialLeague = new League(userId, officialList, data);

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
                        };

                        getFlags(null, getFlags);
                    }

                }
                else {
                    callback.onComplete(false, null);
                }
            }
        };

        getLeagueTable(data);

    }

    // CREATE LEAGUE
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

                final String userId = FirebaseAuth.getInstance().getUid();
                Date leagueStartDate = new Date(System.currentTimeMillis());

                UUID uuid = UUID.randomUUID();
                String leaguePin = uuid.toString().substring(0,8);
                officialList.add(leaguePin);
                //todo write league data in a new collection

                CreatedLeague createdLeague = new CreatedLeague(leagueName, leaguePin, leagueStartDate.getTime(), userId, new ArrayList<String>()); // all leagues

                ArrayList<String> flags = new ArrayList<>();
                final League officialLeague = new League(userId, officialList, flags); // user leagues

                if(userId != null){

                    getDatabaseRefAllLeagues().document(leaguePin).set(createdLeague).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

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

    private HashMap<String, ListenerRegistration> mRefsAllLeagues = new HashMap<>();
    private HashMap<String, ArrayList<DataModelResult<ArrayList<CreatedLeague>>>> mProfileCallbacksAllLeagues = new HashMap<>();
    private HashMap<String, ArrayList<CreatedLeague>> mCachedProfileAllLeagues = new HashMap<>();

    public void addAllLeagueListener(final ArrayList<String> listLeaguePin, final DataModelResult<ArrayList<CreatedLeague>> callback){

        final String userId = FirebaseAuth.getInstance().getUid();

        final ArrayList<CreatedLeague> list = new ArrayList<>();
        ArrayList<DataModelResult<ArrayList<CreatedLeague>>> callbacks = null;

        // get callbacks if they have some already
        if(mProfileCallbacksAllLeagues.containsKey(userId)){
            if(mProfileCallbacksAllLeagues.get(userId) != null){
                callbacks = mProfileCallbacksAllLeagues.get(userId);
            }
        } // create a new list if they have no callbacks
        else {
            callbacks = new ArrayList<>();
        }

        // add the callback to the list
        if(callbacks != null){
            if(!callbacks.contains(callback)){ // if it does not contain this callback then add it
                callbacks.add(callback);
                mProfileCallbacksAllLeagues.put(userId, callbacks);
            }
        }

        if(mCachedProfileAllLeagues.containsKey(userId)){ // if user data has been cached return the cached values do NOT go to database
            ArrayList<CreatedLeague> cacheList = mCachedProfileAllLeagues.get(userId);
            callback.onComplete(cacheList, null);
        }

        // gets all leagues
        ListenerRegistration ref = getDatabaseRefAllLeagues().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots != null){
                    ArrayList<DataModelResult<ArrayList<CreatedLeague>>> callbackList = mProfileCallbacksAllLeagues.get(userId);
                    for(DocumentSnapshot i : queryDocumentSnapshots){
                        HashMap<String, Object> map = (HashMap<String, Object>)i.getData();

                        String leaguePinDatabase = (String) map.get("leaguePin");
                        if(leaguePinDatabase != null){
                            for(String pin : listLeaguePin){
                                if(leaguePinDatabase.equals(pin)){
                                    String leagueNameDatabase = (String) map.get("leagueName");
                                    Long leagueStartDate = (Long) map.get("leagueStartDate");
                                    String userIdDatabase = (String) map.get("userId");
                                    ArrayList<String> flags = (ArrayList<String>) map.get("flags");

                                    CreatedLeague createdLeague = new CreatedLeague(leagueNameDatabase, leaguePinDatabase, leagueStartDate, userIdDatabase, flags);
                                    list.add(createdLeague);
                                }
                            }

                            if(list != null){
                                mCachedProfileAllLeagues.put(userId, list); // cache data
                            }

                            // return the same user data the same amount of times as the length of callBackList
                            if(callbackList != null){
                                for(DataModelResult<ArrayList<CreatedLeague>> createdLeague : callbackList){
                                    callback.onComplete(list, null);
                                }
                            }
                        }
                        else {
                            callback.onComplete(null, null);
                        }
                    }
                }
                else {
                    callback.onComplete(null, null);
                }
            }
        });
        mRefsAllLeagues.put(userId,ref);
    }

    public void removeAllLeagueListener(final String userId, final DataModelResult<ArrayList<CreatedLeague>> callback){
        // remove firebase listener
        ArrayList<DataModelResult<ArrayList<CreatedLeague>>> callbackList =  mProfileCallbacksAllLeagues.get(userId);

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
                mProfileCallbacksAllLeagues.put(userId, callbackList);  // put the callback list back into list without the one just removed
            }
        }
    }

    public void clearAllLeaguesCreated(final DataModelResult<Boolean> callback){
        mCachedProfileAllLeagues.clear();
        if(mCachedProfileAllLeagues.isEmpty()){
            callback.onComplete(true, null);
        }
        else {
            callback.onComplete(false, null);
        }

    }

    private HashMap<String, ListenerRegistration> mRefs = new HashMap<>();
    private HashMap<String, ArrayList<DataModelResult<ArrayList<String>>>> mProfileCallbacks = new HashMap<>();
    private HashMap<String, ArrayList<String>> mCachedProfile = new HashMap<>();

    // returns all the league pins(UUID) for that user
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
                        callback.onComplete(new ArrayList<String>(), null);
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

    public void clearLeaguePinsFromCache(final DataModelResult<Boolean> callback){
        mCachedProfile.clear();
        if(mCachedProfile.isEmpty()){
            callback.onComplete(true, null);
        }
        else{
            callback.onComplete(false, null);
        }
    }


    private CollectionReference getDatabaseRef(){
        return FirebaseFirestore.getInstance().collection("userLeagueTables");
    }

    private CollectionReference getDatabaseRefAllLeagues(){
        return FirebaseFirestore.getInstance().collection("allLeagueTables");
    }

}
