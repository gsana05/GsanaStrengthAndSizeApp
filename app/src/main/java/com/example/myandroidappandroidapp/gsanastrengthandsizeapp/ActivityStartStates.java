package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserModel;

public class ActivityStartStates extends AppCompatActivity {

    private Button button;
    private ProgressBar progress;
    private EditText benchPress;
    private EditText squat;
    private EditText deadlift;
    private EditText overHeadPress;
    private EditText gymName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_states);

        button = this.findViewById(R.id.start_states_ohp_submit_btn);
        progress = this.findViewById(R.id.start_states_ohp_submit_btn_progress);

        benchPress = this.findViewById(R.id.start_states_bench_press_input);
        squat =  this.findViewById(R.id.start_states_squat_input);
        deadlift =  this.findViewById(R.id.start_states_deadlift_input);
        overHeadPress =  this.findViewById(R.id.start_states_ohp_input);
        gymName = this.findViewById(R.id.start_states_user_name_input);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isEmpty(gymName)){
                    alert("User name");
                    return;
                }

                if(isEmpty(benchPress)){
                    alert("Bench press");
                    return;
                }

                if(isEmpty(squat)){
                    alert("Squat");
                    return;
                }

                if(isEmpty(deadlift)){
                    alert("Deadlift");
                    return;
                }

                if(isEmpty(overHeadPress)){
                    alert("over head press");
                    return;
                }

                saveUserStats();
            }
        });
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    public void alert(String response){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setMessage(response)
                .setCancelable(false)
                .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.show();
    }

    public void saveUserStats(){
       alert("Data has been filled in correctly");

        UserModel userModel = new UserModel();
        userModel.saveUserStats(gymName.getText().toString(),benchPress.getText().toString(), squat.getText().toString(),
                deadlift.getText().toString(), overHeadPress.getText().toString()
                );

    }

    @Override
    public void onBackPressed() {
        //disable
    }
}
