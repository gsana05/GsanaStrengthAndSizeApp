package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.DataModelResult;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.User;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserLeagueTableModelSingleton;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserModelSingleton;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserProfileModelSingleton;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityProfile extends AppCompatActivity {

    private DataModelResult<User> callbackUserData;
    private EditText gymName;
    private EditText benchPress;
    private EditText deadlift;
    private EditText squat;
    private EditText ohp;
    private Button logoutUser;
    private ProgressBar logoutUserSpinner;
    private Boolean isLoggedIn = true;
    private UserProfileModelSingleton userProfileModelSingleton;
    private UserModelSingleton userModelSingleton;

    private Float benchPressValue;
    private Float squatValue;
    private Float deadliftValue;
    private Float ohpValue;

    private Boolean claim = false;
    private Button saveChangesBtn;
    private Button claimBtn;
    private int liftType;

    private String benchProofLink;
    private String squatProofLink;
    private String deadliftLink;
    private String ohpLink;

    private float bench1;
    private float squat1;
    private float deadlift1;
    private float ohp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userModelSingleton = UserModelSingleton.getInstance();
        userProfileModelSingleton = UserProfileModelSingleton.getInstance();

        gymName = this.findViewById(R.id.profile_sub_heading_gym_name_input);
        benchPress = this.findViewById(R.id.profile_sub_heading_bench_input);
        deadlift = this.findViewById(R.id.profile_sub_heading_deadlift_input);
        squat = this.findViewById(R.id.profile_sub_heading_squat_input);
        ohp = this.findViewById(R.id.profile_sub_heading_ohp_input);

        callbackUserData = new DataModelResult<User>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(User data, Exception exception) {
                if(data != null){

                    gymName.setText(data.getGymName());

                    benchPress.setText(data.getBenchPress().toString());
                    benchPressValue = data.getBenchPress();

                    deadlift.setText(data.getDeadlift().toString());
                    deadliftValue = data.getDeadlift();

                    squat.setText(data.getSquat().toString());
                    squatValue = data.getSquat();

                    ohp.setText(data.getOverHeadPress().toString());
                    ohpValue = data.getOverHeadPress();

                    benchProofLink = data.getProofBenchLink();
                    squatProofLink = data.getProofSquatLink();
                    ohpLink = data.getProofOhpLink();
                    deadliftLink = data.getProofDeadliftLink();
                }
                else {
                    gymName.setText("no data");
                    benchPress.setText("no data");
                    deadlift.setText("no data");
                    squat.setText("no data");
                    ohp.setText("no data");
                }
            }
        };

        ImageView backBtn = this.findViewById(R.id.profile_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        claimBtn = this.findViewById(R.id.profile_claim_btn);
        claimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bench1 = Float.valueOf(benchPress.getText().toString());
                squat1 = Float.valueOf(squat.getText().toString());
                deadlift1 = Float.valueOf(deadlift.getText().toString());
                ohp1 = Float.valueOf(ohp.getText().toString());

                if(bench1 > benchPressValue){
                    // you need proof for the improvement
                    liftType = UserLeagueTableModelSingleton.benchPress;
                    alertDialogProof("You need proof");
                }
                else if(squat1 > squatValue){
                    liftType = UserLeagueTableModelSingleton.squat;
                    alertDialogProof("You need proof");
                }
                else if(deadlift1 > deadliftValue){
                    liftType = UserLeagueTableModelSingleton.deadlift;
                    alertDialogProof("You need proof");
                }
                else if(ohp1 > ohpValue){
                    liftType = UserLeagueTableModelSingleton.ohp;
                    alertDialogProof("You need proof");
                }
                else {
                    alertDialog("You have entered no improvements");
                }
                /*claim = true;
                updateUI();*/
            }
        });

        saveChangesBtn = this.findViewById(R.id.profile_save_btn);
        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(gymName.getText().toString().isEmpty()){
                    alertDialog("Please enter gymName");
                    return;
                }

                if(benchPress.getText().toString().isEmpty()){
                    alertDialog("Please enter bench press");
                    return;
                }

                if(squat.getText().toString().isEmpty()){
                    alertDialog("Please enter squat");
                    return;
                }

                if(deadlift.getText().toString().isEmpty()){
                    alertDialog("Please enter deadlift");
                    return;
                }

                if(ohp.getText().toString().isEmpty()){
                    alertDialog("Please enter over head press");
                    return;
                }

                DataModelResult<Boolean> callback = new DataModelResult<Boolean>() {
                    @Override
                    public void onComplete(Boolean data, Exception exception) {
                        if(data){
                            alertDialog("Data saved");
                            finish();
                        }
                        else {
                            alertDialog("Data has not been saved");
                        }

                    }
                };

                userProfileModelSingleton.updateUser(
                        gymName.getText().toString(),
                        Float.valueOf(benchPress.getText().toString()),
                        Float.valueOf(squat.getText().toString()),
                        Float.valueOf(deadlift.getText().toString()),
                        Float.valueOf(ohp.getText().toString()),
                        benchProofLink,
                        squatProofLink,
                        deadliftLink,
                        ohpLink,
                        callback
                );
            }
        });

        final DataModelResult<Boolean> callbackLogout = new DataModelResult<Boolean>(){

            @Override
            public void onComplete(Boolean data, Exception exception) {
                if(data){
                    finish(); // go back to main activity - in onResume check userId is logged in
                }
                isLoggedIn = true;
                updateUI();
            }
        };

        logoutUser = this.findViewById(R.id.profile_sign_out_btn);
        logoutUserSpinner = this.findViewById(R.id.profile_sign_out_progress);
        logoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoggedIn = false;
                updateUI();

                userModelSingleton.logout(callbackLogout);
            }
        });


        ImageView settingsBtn = this.findViewById(R.id.profile_image_settings);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivitySettings.class);
                startActivity(intent);
            }
        });

        updateUI();
    }

    public void updateUI(){
        if(isLoggedIn){
            logoutUser.setVisibility(View.VISIBLE);
            logoutUserSpinner.setVisibility(View.GONE);
        }
        else {
            logoutUser.setVisibility(View.INVISIBLE);
            logoutUserSpinner.setVisibility(View.VISIBLE);
        }

        if(claim){
            saveChangesBtn.setVisibility(View.VISIBLE);
            claimBtn.setVisibility(View.INVISIBLE);
        }
        else {
            saveChangesBtn.setVisibility(View.INVISIBLE);
            claimBtn.setVisibility(View.VISIBLE);
        }
    }

    public void alertDialogProof(String response){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setMessage(response)
                .setCancelable(false)
                .setPositiveButton("Prove", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplication(), ActivityProveLift.class);
                        intent.putExtra("lift", liftType);
                        startActivityForResult(intent, 999);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 999) {
            if(resultCode == Activity.RESULT_OK){
                String bench=data.getStringExtra("Bench");
                String squat = data.getStringExtra("squat");
                String deadlift = data.getStringExtra("deadlift");
                String ohp = data.getStringExtra("ohp");
                if(bench != null){
                    benchProofLink = bench;
                }

                if(squat != null){
                    squatProofLink = squat;
                }

                if(deadlift != null){
                    deadliftLink = deadlift;
                }

                if(ohp != null){
                    ohpLink = ohp;
                }

                claim = true;
                updateUI();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.v("", "");
                //Write your code if there's no result
            }
        }
    }

    public void alertDialog(String response){
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

    @Override
    public void onPause() {
        super.onPause();
        String id = FirebaseAuth.getInstance().getUid();
        if(id != null){
            userProfileModelSingleton.removeUserProfileListener(id, callbackUserData);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        String id = FirebaseAuth.getInstance().getUid();
        if(id != null){
            userProfileModelSingleton.addUserProfileListener(id, callbackUserData);
        }
    }
}
