package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
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

    private void getLeagueTable(final DataModelResult<League> callback){
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

                            ArrayList<CreatedLeague> list = null;
                            Map hMap = snapshot.getData();
                            if (hMap != null) {
                                list = (ArrayList<CreatedLeague>) hMap.get("leaguesCreated");
                            }

                            League league = new League(userId, list);
                            callback.onComplete(league, null);
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

        DataModelResult<League> leagueFromDatabase = new DataModelResult<League>() {
            @Override
            public void onComplete(League data, Exception exception) {

                ArrayList<CreatedLeague> officialList = new ArrayList<>();

                // if you have leagues created get them and make sure they are added to the new object
                if (data != null) {
                    officialList.addAll(data.leaguesCreated);
                }

                String userId = FirebaseAuth.getInstance().getUid();
                Date leagueStartDate = new Date(System.currentTimeMillis());
                UUID uuid = UUID.randomUUID();
                String leaguePin = uuid.toString().substring(0,8);

                CreatedLeague createdLeague = new CreatedLeague(leagueName, leaguePin, leagueStartDate, userId);
                officialList.add(createdLeague);

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

    private CollectionReference getDatabaseRef(){
        return FirebaseFirestore.getInstance().collection("leagueTables");
    }

}
