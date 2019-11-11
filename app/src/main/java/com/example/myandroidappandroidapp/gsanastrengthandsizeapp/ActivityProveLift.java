package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserLeagueTableModelSingleton;

public class ActivityProveLift extends AppCompatActivity {

    private int liftType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prove_lift);

        Intent intent = getIntent();
        liftType = intent.getIntExtra("lift", -1); // passed into function

        if(liftType == UserLeagueTableModelSingleton.benchPress){


            Log.v("", "");
        }
        else{
            Log.v("", "");
        }
    }
}
