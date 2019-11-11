package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserLeagueTableModelSingleton;

public class ActivityProveLift extends AppCompatActivity {

    private int liftType;
    private TextView proveLiftType;
    private int liftName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prove_lift);

        Intent intent = getIntent();
        liftType = intent.getIntExtra("lift", -1); // passed into function
        proveLiftType = this.findViewById(R.id.prove_lift_type);
        updateUI();

    }

    private void updateUI(){
        switch(liftType){
            case UserLeagueTableModelSingleton.benchPress: liftName = R.string.bench_press;
                break;
            case UserLeagueTableModelSingleton.deadlift: liftName = R.string.deadlift;
                break;
            case UserLeagueTableModelSingleton.ohp: liftName = R.string.over_head_press_kg;
                break;
            case UserLeagueTableModelSingleton.squat: liftName = R.string.squat;
                break;
            default: liftName = R.string.app_name;
        }

        proveLiftType.setText(liftName);
    }
}
