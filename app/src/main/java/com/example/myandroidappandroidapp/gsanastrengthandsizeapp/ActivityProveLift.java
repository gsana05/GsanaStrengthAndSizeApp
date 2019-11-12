package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserLeagueTableModelSingleton;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserModelSingleton;

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

        final EditText et = this.findViewById(R.id.prove_lift_link_input);

        Button submitBtn = this.findViewById(R.id.prove_lift_submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et.getText().toString().isEmpty()){
                    return;
                }

                Intent returnIntent = new Intent();

                if(liftType == UserLeagueTableModelSingleton.benchPress){
                    returnIntent.putExtra("Bench", et.getText().toString());
                }

                if(liftType == UserLeagueTableModelSingleton.deadlift){
                    returnIntent.putExtra("deadlift", et.getText().toString());
                }

                if(liftType == UserLeagueTableModelSingleton.squat){
                    returnIntent.putExtra("squat", et.getText().toString());
                }

                if(liftType == UserLeagueTableModelSingleton.ohp){
                    returnIntent.putExtra("ohp", et.getText().toString());
                }

                //save link to database

                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            }
        });

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
