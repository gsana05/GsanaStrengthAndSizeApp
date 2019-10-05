package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.DataModelResult;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.User;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserLeagueTableModelSingleton;

import java.util.ArrayList;

public class LeagueTableResults extends AppCompatActivity {

    private TextView pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_table_results);

        Intent intent = getIntent();
        String leaguePin = intent.getStringExtra("LeaguePin");
        pin = this.findViewById(R.id.league_table_results_pin);
        pin.setText(leaguePin);

        final UserLeagueTableModelSingleton userLeagueTableModelSingleton = UserLeagueTableModelSingleton.getInstance();


        final DataModelResult<ArrayList<User>> usersCallback = new DataModelResult<ArrayList<User>>() {
            @Override
            public void onComplete(ArrayList<User> data, Exception exception) {
                if(data != null){
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
                    pin.setText(data.toString());

                    userLeagueTableModelSingleton.getUsersForCurrentLeague("", data, usersCallback);
                }
                else {
                    pin.setText("no data");
                }
            }
        };


        userLeagueTableModelSingleton.getUsersWithTheSamePin(leaguePin, callback); // passes in leaguePin and returns the leagueMasterId


    }
}
