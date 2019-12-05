package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.DataModelResult;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserModelSingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.File;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ActivityStartStates extends AppCompatActivity {

    private EditText benchPress;
    private EditText squat;
    private EditText deadlift;
    private EditText overHeadPress;
    private EditText gymName;
    private Button button;
    private ProgressBar progress;
    private boolean mSaveData = false;

    private Uri mBenchPressUri;
    private TextView benchPressLink;
    private ImageView benchPressRemoveLink;

    private Uri mSquatUri;
    private TextView squatLink;
    private ImageView squatRemoveLink;

    private Uri mDeadliftUri;
    private TextView deadliftLink;
    private ImageView deadliftRemoveLink;

    private Uri mOhpUri;
    private TextView ohpLink;
    private ImageView ohpRemoveLink;

    private static final int BENCH_REQUEST = 1;
    private static final int SQUAT_REQUEST = 2;
    private static final int DEADLIFT_REQUEST = 3;
    private static final int OHP_REQUEST = 4;

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

        //BENCH
        benchPressLink = this.findViewById(R.id.start_states_bench_proof_link);
        benchPressRemoveLink = this.findViewById(R.id.start_states_bench_proof_link_remove);
        benchPressRemoveLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBenchPressUri = null;
                updateUI();
            }
        });

        Button benchProofSearchLink = this.findViewById(R.id.start_states_bench_proof_input);
        benchProofSearchLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser(BENCH_REQUEST);
            }
        });


        //SQUAT
        squatLink = this.findViewById(R.id.start_states_squat_proof_link);
        squatRemoveLink = this.findViewById(R.id.start_states_squat_proof_link_remove);
        squatRemoveLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSquatUri = null;
                updateUI();
            }
        });

        Button squatSearchLink = this.findViewById(R.id.start_states_squat_proof_label);
        squatSearchLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser(SQUAT_REQUEST);
            }
        });


        // DEADLIFT
        deadliftLink = this.findViewById(R.id.start_states_deadlift_proof_link);
        deadliftRemoveLink = this.findViewById(R.id.start_states_deadlift_proof_link_remove);
        deadliftRemoveLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeadliftUri = null;
                updateUI();
            }
        });

        Button deadliftSearchLink = this.findViewById(R.id.start_states_deadlift_proof_label);
        deadliftSearchLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser(DEADLIFT_REQUEST);
            }
        });

        //OVER HEAD PRESS
        ohpLink = this.findViewById(R.id.start_states_ohp_proof_link);
        ohpRemoveLink = this.findViewById(R.id.start_states_ohp_proof_link_remove);
        ohpRemoveLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOhpUri = null;
                updateUI();
            }
        });

        Button ohpSearchLink = this.findViewById(R.id.start_states_ohp_proof_label);
        ohpSearchLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser(OHP_REQUEST);
            }
        });





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

                if(mBenchPressUri == null){
                    alert("Bench press proof link");
                    return;
                }


                if(isEmpty(squat)){
                    alert("Squat");
                    return;
                }

                if(mSquatUri == null){
                    alert("Squat proof link");
                    return;
                }

                if(isEmpty(deadlift)){
                    alert("Deadlift");
                    return;
                }

                if(mDeadliftUri == null){
                    alert("Deadlift proof link");
                    return;
                }

                if(isEmpty(overHeadPress)){
                    alert("over head press");
                    return;
                }

                if(mOhpUri == null){
                    alert("Over Head Press proof link");
                    return;
                }

                mSaveData = true;
                updateUI();
                saveUserStats();
            }
        });

        updateUI();
    }

    private void openFileChooser(int exercise){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, exercise);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null && data.getData() != null){

            if(requestCode == BENCH_REQUEST){
                mBenchPressUri = data.getData();
            }
            else if(requestCode == SQUAT_REQUEST){
                mSquatUri = data.getData();
            }
            else if (requestCode == DEADLIFT_REQUEST){
                mDeadliftUri = data.getData();
            }
            else if(requestCode == OHP_REQUEST){
                mOhpUri = data.getData();
            }
            else  {
                Log.v("", "");
            }

            updateUI();



            /*VideoView video = this.findViewById(R.id.start_states_bench_video_link);
            MediaController mediaController = new MediaController(this);
            video.setVideoURI(uri);
            video.setMediaController(mediaController);
            mediaController.setAnchorView(video);
            video.start();*/
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

        final DataModelResult<Boolean> callback = new DataModelResult<Boolean>(){
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

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token and store in workout profiles
                        String pushNotificationToken = task.getResult().getToken();

                        UserModelSingleton userModelSingleton = UserModelSingleton.getInstance();
                        userModelSingleton.saveUserStats(
                                gymName.getText().toString(),
                                Float.valueOf(benchPress.getText().toString()),
                                Float.valueOf(squat.getText().toString()),
                                Float.valueOf(deadlift.getText().toString()),
                                Float.valueOf(overHeadPress.getText().toString()),
                                mBenchPressUri,
                                mSquatUri,
                                mDeadliftUri,
                                mSquatUri,
                                pushNotificationToken,
                                callback);


                    }
                });


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

        if(mBenchPressUri != null){
            benchPressLink.setVisibility(View.VISIBLE);
            benchPressRemoveLink.setVisibility(View.VISIBLE);
            benchPressLink.setText(mBenchPressUri.toString());
        }
        else {
            benchPressLink.setVisibility(View.GONE);
            benchPressRemoveLink.setVisibility(View.GONE);
        }

        if(mSquatUri != null){
            squatLink.setVisibility(View.VISIBLE);
            squatRemoveLink.setVisibility(View.VISIBLE);
            squatLink.setText(mSquatUri.toString());
        }
        else {
            squatLink.setVisibility(View.GONE);
            squatRemoveLink.setVisibility(View.GONE);
        }

        if(mDeadliftUri != null){
            deadliftLink.setVisibility(View.VISIBLE);
            deadliftRemoveLink.setVisibility(View.VISIBLE);
            deadliftLink.setText(mDeadliftUri.toString());
        }
        else {
            deadliftLink.setVisibility(View.GONE);
            deadliftRemoveLink.setVisibility(View.GONE);
        }

        if(mOhpUri != null){
            ohpLink.setVisibility(View.VISIBLE);
            ohpRemoveLink.setVisibility(View.VISIBLE);
            ohpLink.setText(mOhpUri.toString());
        }
        else{
            ohpLink.setVisibility(View.GONE);
            ohpRemoveLink.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        //disable
    }
}
