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
                                String h = document.getString("leagueMasterId");

                                ArrayList<String> list = null;
                                Map hMap = document.getData();
                                list = (ArrayList<String>) hMap.get("leaguesCreated");

                                if(list != null){
                                    for(String pin : list){
                                        if(pin.equals(leaguePin)){
                                            leaguePinList.add(pin);
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
}
