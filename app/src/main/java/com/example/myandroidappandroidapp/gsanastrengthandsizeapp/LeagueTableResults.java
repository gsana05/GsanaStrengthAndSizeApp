package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.adapters.LeagueTableRecyclerViewAdapter;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.DataModelResult;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.User;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserLeagueTableModelSingleton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class LeagueTableResults extends AppCompatActivity {

    private TextView nameOfLeague;
    final UserLeagueTableModelSingleton userLeagueTableModelSingleton = UserLeagueTableModelSingleton.getInstance();
    DataModelResult<ArrayList<User>> usersCallback;

    private RecyclerView leagueTableRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_table_results);

        Intent intent = getIntent();
        String leaguePin = intent.getStringExtra("LeaguePin"); // passed into function
        String leagueName = intent.getStringExtra("LeagueName");
        nameOfLeague = this.findViewById(R.id.league_table_results_heading_view_league_name);
        nameOfLeague.setText(leagueName);

        leagueTableRecyclerView = this.findViewById(R.id.league_table_results_main_view_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        leagueTableRecyclerView.setLayoutManager(layoutManager);

        usersCallback = new DataModelResult<ArrayList<User>>() {
            @Override
            public void onComplete(ArrayList<User> data, Exception exception) {
                if(data != null){
                    mAdapter = new LeagueTableRecyclerViewAdapter(data);
                    leagueTableRecyclerView.setAdapter(mAdapter);
                    Log.v("", "");
                }
                else {
                    Log.v("", "");
                }
            }
        };

        // gets the leagueMasterId
        DataModelResult<ArrayList<String>> callback = new DataModelResult<ArrayList<String>>() {
            @Override
            public void onComplete(ArrayList<String> data, Exception exception) {
                if(data != null){
                    //pin.setText(data.toString());
                    String id = FirebaseAuth.getInstance().getUid();
                    if(id != null){
                        // adding listener
                        userLeagueTableModelSingleton.addLeagueTableUserProfileListener(id, data, usersCallback);
                    }

                    //userLeagueTableModelSingleton.getUsersForCurrentLeague("", data, usersCallback);
                }
                else {
                    nameOfLeague.setText("no data");
                }
            }
        };


        userLeagueTableModelSingleton.getUsersWithTheSamePin(leaguePin, callback); // passes in leaguePin and returns the leagueMasterId


    }

    @Override
    protected void onPause() {
        super.onPause();
        String id = FirebaseAuth.getInstance().getUid();
        if(id != null){
            //remove listener
            userLeagueTableModelSingleton.removeLeagueTableUserProfileListener(id, usersCallback);
        }
    }
}
