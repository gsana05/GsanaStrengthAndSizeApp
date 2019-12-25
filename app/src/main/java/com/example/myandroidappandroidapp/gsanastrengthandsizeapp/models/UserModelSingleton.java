package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.UUID;

import static androidx.constraintlayout.widget.Constraints.TAG;

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

    private UserProfileModelSingleton userProfileModelSingleton  = UserProfileModelSingleton.getInstance();

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

    public void saveUserStats(final String gymName, final Float benchPress, final Float squat, final Float deadlift, final Float overHeadPress, final Uri benchProof, final Uri squatProof, final Uri deadliftProof, final Uri ohpProof, final String pushNotificationToken, final DataModelResult<Boolean> callback){
        final String id = FirebaseAuth.getInstance().getUid();

        if(id != null){

            StorageReference fb = FirebaseStorage.getInstance().getReference().child("gymBenchVideos").child(id);
            fb.putFile(benchProof).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageMetadata sm = taskSnapshot.getMetadata();
                    if(sm != null){
                        StorageReference storage = taskSnapshot.getStorage();
                        if(storage != null){

                            Task<Uri> benchUri = storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    final Uri benchProofUri = uri;

                                    StorageReference fb = FirebaseStorage.getInstance().getReference().child("gymSquatVideos").child(id);
                                    fb.putFile(squatProof).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            StorageMetadata sm = taskSnapshot.getMetadata();
                                            if(sm != null){
                                                StorageReference storage = taskSnapshot.getStorage();
                                                if(storage != null){

                                                    Task<Uri> squatUri = storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {

                                                            final Uri squatProofUri = uri;

                                                            StorageReference fb = FirebaseStorage.getInstance().getReference().child("gymDeadliftVideos").child(id);
                                                            fb.putFile(deadliftProof).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                @Override
                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                    StorageMetadata sm = taskSnapshot.getMetadata();
                                                                    if(sm != null){
                                                                        StorageReference storage = taskSnapshot.getStorage();
                                                                        if(storage != null){

                                                                            Task<Uri> deadliftUri = storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                @Override
                                                                                public void onSuccess(Uri uri) {

                                                                                    final Uri deadliftProofUri = uri;

                                                                                    StorageReference fb = FirebaseStorage.getInstance().getReference().child("gymOhpVideos").child(id);
                                                                                    fb.putFile(ohpProof).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                        @Override
                                                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                            StorageMetadata sm = taskSnapshot.getMetadata();
                                                                                            if(sm != null){
                                                                                                StorageReference storage = taskSnapshot.getStorage();
                                                                                                if(storage != null){

                                                                                                    Task<Uri> ohpUri = storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Uri uri) {

                                                                                                            Uri ohpProofUri = uri;


                                                                                                            uploadUserProfile(id, gymName, benchPress, squat, deadlift, overHeadPress, benchProofUri, squatProofUri, deadliftProofUri, ohpProofUri,pushNotificationToken, callback);
                                                                                                        }
                                                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                                                        @Override
                                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                                            callback.onComplete(null, e);
                                                                                                        }
                                                                                                    });
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            callback.onComplete(null, e);
                                                                                        }
                                                                                    });

                                                                                    //uploadUserProfile(id, gymName, benchPress, squat, deadlift, overHeadPress, uri, squatProof, deadliftProof, ohpProof, callback);
                                                                                }
                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    callback.onComplete(null, e);
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    callback.onComplete(null, e);
                                                                }
                                                            });

                                                            //uploadUserProfile(id, gymName, benchPress, squat, deadlift, overHeadPress, uri, squatProof, deadliftProof, ohpProof, callback);
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            callback.onComplete(null, e);
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            callback.onComplete(null, e);
                                        }
                                    });

                                    //uploadUserProfile(id, gymName, benchPress, squat, deadlift, overHeadPress, uri, squatProof, deadliftProof, ohpProof, callback);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    callback.onComplete(null, e);
                                }
                            });
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    callback.onComplete(null, e);
                }
            });
        }
    }

    public void uploadUserProfile(final String id, final String gymName, final Float benchPress, final Float squat, final Float deadlift, final Float overHeadPress, final Uri benchProof, final Uri squatProof, final Uri deadliftProof, final Uri ohpProof, String pushNotificationToken, final DataModelResult<Boolean> callback){
        if(benchProof != null){
            Date date = new Date(System.currentTimeMillis());
            UUID uuid = UUID.randomUUID();
            //String pin = uuid.toString().substring(0,8);

            Float compoundTotalLift = benchPress + squat + deadlift + overHeadPress;
            User user = new User(gymName, benchPress, squat, deadlift, overHeadPress, date, id, userEmail, benchProof.toString(), squatProof.toString(), deadliftProof.toString(), ohpProof.toString(), pushNotificationToken, compoundTotalLift);

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


                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (!task.isSuccessful()) {
                                        Log.w(TAG, "getInstanceId failed", task.getException());
                                        return;
                                    }

                                    // Get new Instance ID token and store in workout profiles
                                    String token = task.getResult().getToken();
                                    String userId = FirebaseAuth.getInstance().getUid();
                                    if(userId != null){
                                        getDatabaseRef().document(userId).update("pushNotificationToken", token)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                callback.onComplete(true, null);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //log in but you have no push notification id
                                                callback.onComplete(true, e);
                                            }
                                        });
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

