package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.DataModelResult;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserModelSingleton;

public class ActivityStartStates extends AppCompatActivity {

    private EditText benchPress;
    private EditText squat;
    private EditText deadlift;
    private EditText overHeadPress;
    private EditText gymName;
    private Button button;
    private ProgressBar progress;
    private boolean mSaveData = false;

    private Button benchProofLink;
    private EditText squatProofLink;
    private EditText deadliftProofLink;
    private EditText ohpProofLink;

    private static final int PICK_IMAGE_REQUEST = 1;

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

        benchProofLink = this.findViewById(R.id.start_states_bench_proof_input);
        benchProofLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });



        squatProofLink = this.findViewById(R.id.start_states_squat_proof_input);
        deadliftProofLink = this.findViewById(R.id.start_states_deadlift_proof_input);
        ohpProofLink = this.findViewById(R.id.start_states_ohp_proof_input);


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

                /*if(isEmpty(benchProofLink)){
                    alert("Bench press proof link");
                    return;
                }*/


                if(isEmpty(squat)){
                    alert("Squat");
                    return;
                }

                if(isEmpty(squatProofLink)){
                    alert("Squat proof link");
                    return;
                }

                if(isEmpty(deadlift)){
                    alert("Deadlift");
                    return;
                }

                if(isEmpty(deadliftProofLink)){
                    alert("Deadlift proof link");
                    return;
                }

                if(isEmpty(overHeadPress)){
                    alert("over head press");
                    return;
                }

                if(isEmpty(ohpProofLink)){
                    alert("Over Head Press proof link");
                    return;
                }

                mSaveData = true;
                updateUI();
                saveUserStats();
            }
        });
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri uri = data.getData();

            VideoView video = this.findViewById(R.id.start_states_bench_video);
            MediaController mediaController = new MediaController(this);
            video.setVideoURI(uri);
            video.setMediaController(mediaController);
            mediaController.setAnchorView(video);
            video.start();
        }
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() <= 0;
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
       //alert("Data has been filled in correctly");

        DataModelResult<Boolean> callback = new DataModelResult<Boolean>(){
            @Override
            public void onComplete(Boolean data, Exception exception) {
                if(data){
                    mSaveData = false;
                    updateUI();
                    Toast.makeText(getApplicationContext(),"Data saved successfully",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    mSaveData = false;
                    updateUI();
                    Toast.makeText(getApplicationContext(),"Data did not save",Toast.LENGTH_SHORT).show();
                }
            }
        };

        UserModelSingleton userModelSingleton = UserModelSingleton.getInstance();
        userModelSingleton.saveUserStats(
                gymName.getText().toString(),
                Float.valueOf(benchPress.getText().toString()),
                Float.valueOf(squat.getText().toString()),
                Float.valueOf(deadlift.getText().toString()),
                Float.valueOf(overHeadPress.getText().toString()),
                benchProofLink.getText().toString(),
                squatProofLink.getText().toString(),
                deadliftProofLink.getText().toString(),
                ohpProofLink.getText().toString(),
                callback);
    }

    public void updateUI(){

        if(mSaveData){
            progress.setVisibility(View.VISIBLE);
            button.setVisibility(View.INVISIBLE);
        }
        else {
            progress.setVisibility(View.INVISIBLE);
            button.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        //disable
    }
}
