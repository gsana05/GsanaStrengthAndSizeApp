package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

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
    }
}
