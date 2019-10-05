package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

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

    // leagueMasterId and returns Users
    public void getUsersForCurrentLeague(final String leaguePin, ArrayList<String> leagueMasterId,final DataModelResult<ArrayList<User>> callback){

        final ArrayList<User> userList = new ArrayList<>();

        getDatabaseRefWorkoutProfiles().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot data = task.getResult();
                    if(data != null){
                        for (QueryDocumentSnapshot snapshot : data) {
                            String gymName = snapshot.getString("gymName");
                            Double bench = snapshot.getDouble("benchPress");
                            Double deadlift = snapshot.getDouble("deadlift");
                            Double squat = snapshot.getDouble("squat");
                            Double ohp = snapshot.getDouble("overHeadPress");
                            String pin = snapshot.getString("pin");
                            String email = snapshot.getString("email");
                            Date date = snapshot.getDate("date");

                            User user = new User(gymName, bench.floatValue(), squat.floatValue(), deadlift.floatValue(), ohp.floatValue(), date, pin, email);

                            userList.add(user);
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





    //League pins and returns leagueMasterId
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

                                if(list != null){
                                    for(String pin : list){
                                        if(pin.equals(leaguePin)){
                                            leaguePinList.add(leagueMasterId);
                                        }
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
