package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models;

import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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

    public void addUserProfileListener(final String userId, final DataModelResult<User> callback){

        ArrayList<DataModelResult<User>> callbacks = null;

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
            User user = mCachedProfile.get(userId);
            callback.onComplete(user, null);
        }

        if(!mRefs.containsKey(userId)){// listener gets data and real time updates if anything changes
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

                        User user = new User(gymName, bench.floatValue(), squat.floatValue(), deadlift.floatValue(), ohp.floatValue(), date, pin, email, null, null,null, null);

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
            mRefs.put(userId, ref); // add firebase listener
        }
    }

    public void removeUserProfileListener(final String userId, final DataModelResult<User> callback){
        ArrayList<DataModelResult<User>> callbackList =  mProfileCallbacks.get(userId);
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

    public void getCurrentUser(final String userId, final DataModelResult<User> callback){
        getDatabaseRef().document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot != null)
                    documentSnapshot.get("GymName");
                    documentSnapshot.get("bench");
                    documentSnapshot.get("squat");
                    documentSnapshot.get("deadlift");
                    documentSnapshot.get("overHeadPress");
                    documentSnapshot.get("email");
                    documentSnapshot.get("date");
                    documentSnapshot.get("pin");


                    String gymName = documentSnapshot.getString("gymName");
                    Double bench = documentSnapshot.getDouble("benchPress");
                    Double deadlift = documentSnapshot.getDouble("deadlift");
                    Double squat = documentSnapshot.getDouble("squat");
                    Double ohp = documentSnapshot.getDouble("overHeadPress");
                    String pin = documentSnapshot.getString("pin");
                    String email = documentSnapshot.getString("email");
                    Date date = documentSnapshot.getDate("date");
                    String benchLink = documentSnapshot.getString("proofBenchLink");
                    String deadliftLink = documentSnapshot.getString("proofDeadliftLink");
                    String proofSquatLink = documentSnapshot.getString("proofSquatLink");
                    String proofOhpLink = documentSnapshot.getString("proofOhpLink");

                    User user = new User(
                            gymName,
                            bench.floatValue(),
                            squat.floatValue(),
                            deadlift.floatValue(),
                            ohp.floatValue(),
                            date,
                            pin,
                            email,
                            benchLink,
                            proofSquatLink,
                            proofOhpLink,
                            deadliftLink
                            );

                    if(user != null){
                        callback.onComplete(user, null);
                    }
                    else {
                        callback.onComplete(null, null);
                    }

                }
            }
        });
    }

    private String mNewBenchPbUri;
    private String mNewSquatPbUri;
    private String mNewDeadliftPbUri;
    private String mNewOhpPbUri;

    public void deleteVideoAndSaveNewOne(final String userId, final Uri benchLink, final String storageArea, final DataModelResult<Boolean> callback){
        //delete current bench from storage
        StorageReference fb = FirebaseStorage.getInstance().getReference().child(storageArea).child(userId);
        fb.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                StorageReference fb = FirebaseStorage.getInstance().getReference().child(storageArea).child(userId);
                fb.putFile(benchLink).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        StorageMetadata sm = taskSnapshot.getMetadata();
                        if(sm != null){
                            StorageReference storage = taskSnapshot.getStorage();
                            if(storage != null){
                                storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        switch (storageArea){
                                            case "gymBenchVideos": mNewBenchPbUri = uri.toString();
                                            break;
                                            case "gymSquatVideos": mNewSquatPbUri = uri.toString();
                                            break;
                                            case "gymDeadliftVideos": mNewDeadliftPbUri = uri.toString();
                                            break;
                                            case "gymOhpVideos": mNewOhpPbUri = uri.toString();
                                            break;
                                        }

                                        callback.onComplete(true, null);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        callback.onComplete(false, e);
                                        //todo unable to get download url
                                    }
                                });
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onComplete(false, e);
                        // todo unable to save new video
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onComplete(false, e);
                //todo unable to delete video
            }
        });
    }

    public void checkLift(String userId, Uri link, String storageArea, final DataModelResult<Boolean> callback){

        if(link == null){
           callback.onComplete(false, null);
        }
        else{
            DataModelResult<Boolean> benchCallback = new DataModelResult<Boolean>() {
                @Override
                public void onComplete(Boolean data, Exception exception) {
                    if(data){
                        //todo success delete old and saved new video
                        callback.onComplete(true, null);
                    }
                    else{

                        if(exception != null){
                            callback.onComplete(null, exception);
                        }
                        else{
                            callback.onComplete(false, null);
                        }

                        //todo something went wrong when trying to delete and save a new video
                    }
                }
            };

            deleteVideoAndSaveNewOne(userId, link,storageArea, benchCallback);
        }
    }

    public void updateUser(final String gymName, final Float bench, final Float squat, final Float deadlift, final Float ohp, final Uri benchLink, final Uri squatLink, final Uri deadliftLink, final Uri ohpLink, final DataModelResult<Boolean> callback){

        final String userId = FirebaseAuth.getInstance().getUid();

        if(userId != null){
            DataModelResult<User> userCallback = new DataModelResult<User>() {
                @Override
                public void onComplete(User data, Exception exception) {
                    if(data != null){
                        final String email = data.getEmail();
                        final String pin = data.getPin();
                        final Date date = data.getDate();
                        final String currentBenchLink = data.getProofBenchLink();
                        final String currentSquatLink = data.getProofSquatLink();
                        final String currentDeadlift = data.getProofDeadliftLink();
                        final String currentOhp = data.getProofOhpLink();

                        final DataModelResult<Boolean> callbackBench = new DataModelResult<Boolean>() {
                            @Override
                            public void onComplete(Boolean data, Exception exception) {

                                // If there is ever an exception
                                if(exception != null){
                                    callback.onComplete(null, exception);
                                }

                                // when user has either deleted and saved new video OR does not want to upload a new video

                                DataModelResult<Boolean> callbackSquat = new DataModelResult<Boolean>() {
                                    @Override
                                    public void onComplete(Boolean data, Exception exception) {
                                        // If there is ever an exception
                                        if(exception != null){
                                            callback.onComplete(null, exception);
                                        }

                                        DataModelResult<Boolean> callbackDeadlift = new DataModelResult<Boolean>() {
                                            @Override
                                            public void onComplete(Boolean data, Exception exception) {
                                                // If there is ever an exception
                                                if(exception != null){
                                                    callback.onComplete(null, exception);
                                                }

                                                DataModelResult<Boolean> callbackOhp = new DataModelResult<Boolean>() {
                                                    @Override
                                                    public void onComplete(Boolean data, Exception exception) {
                                                        // If there is ever an exception
                                                        if(exception != null){
                                                            callback.onComplete(null, exception);
                                                        }

                                                        if(mNewBenchPbUri == null){
                                                            mNewBenchPbUri = currentBenchLink;
                                                        }

                                                        if(mNewSquatPbUri == null){
                                                            mNewSquatPbUri = currentSquatLink;
                                                        }

                                                        if(mNewDeadliftPbUri == null){
                                                            mNewDeadliftPbUri = currentDeadlift;
                                                        }

                                                        if(mNewOhpPbUri == null){
                                                            mNewOhpPbUri = currentOhp;
                                                        }

                                                        //update database
                                                        User user = new User(gymName, bench, squat, deadlift, ohp, date, pin, email, mNewBenchPbUri, mNewSquatPbUri,mNewDeadliftPbUri, mNewOhpPbUri);

                                                        // setOptions - only changes the field that now has a different value
                                                        getDatabaseRef().document(userId).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    callback.onComplete(true, null);
                                                                }
                                                                else {
                                                                    callback.onComplete(false, null);
                                                                }
                                                            }
                                                        });

                                                    }
                                                };

                                                checkLift(userId, ohpLink,"gymOhpVideos", callbackOhp );

                                            }
                                        };

                                        checkLift(userId, deadliftLink, "gymDeadliftVideos", callbackDeadlift);
                                    }
                                };

                                checkLift(userId, squatLink, "gymSquatVideos",callbackSquat);
                            }
                        };

                        checkLift(userId, benchLink,"gymBenchVideos", callbackBench);

                      /*  String mDeadliftLink;
                        if(deadliftLink == null){
                            mDeadliftLink = "";
                        }
                        else{
                            mDeadliftLink = deadliftLink.toString();
                        }

                        String mOhpLink;
                        if(ohpLink == null){
                            mOhpLink = "";
                        }
                        else{
                            mOhpLink = ohpLink.toString();
                        }*/

                      /*  getDatabaseRef().document(userId).update(
                                "benchPress", bench,
                                "squat", squat,
                                "deadlift", deadlift,
                                "overHeadPress", ohp,
                                "proofBenchLink", mBenchLink,
                                "proofSquatLink", mSquatLink,
                                "proofDeadliftLink", mDeadliftLink,
                                "proofOhpLink", mOhpLink

                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });*/

                       /* User user = new User(gymName, bench, squat, deadlift, ohp, date, pin, email, mBenchLink, squatLink.toString(),deadliftLink.toString(), ohpLink.toString());

                        // setOptions - only changes the field that now has a different value
                        getDatabaseRef().document(userId).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    callback.onComplete(true, null);
                                }
                                else {
                                    callback.onComplete(false, null);
                                }
                            }
                        });*/
                    }
                    else {
                        Log.v("User", "NULL");
                    }
                }
            };
            getCurrentUser(userId, userCallback);
        }
        else {
            callback.onComplete(false, null);
        }
    }

    private CollectionReference getDatabaseRef(){
        return FirebaseFirestore.getInstance().collection("workoutProfiles");
    }
}
