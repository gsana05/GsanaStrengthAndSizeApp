package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.DataModelResult;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.User;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserModelSingleton;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserProfileModelSingleton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class ActivityProfile extends AppCompatActivity {

    private DataModelResult<User> callbackUserData;
    private EditText gymName;
    private EditText benchPress;
    private EditText deadlift;
    private EditText squat;
    private EditText ohp;
    private ImageView backBtn;
    private Button saveChangesBtn;
    private Button logoutUser;
    private ProgressBar logoutUserSpinner;
    private Boolean isLoggedIn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
                    deadlift.setText(data.getDeadlift().toString());
                    squat.setText(data.getSquat().toString());
                    ohp.setText(data.getOverHeadPress().toString());
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

        backBtn = this.findViewById(R.id.profile_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        saveChangesBtn = this.findViewById(R.id.profile_save_btn);
        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog("Save Button Pressed");
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
                UserModelSingleton userModelSingleton = UserModelSingleton.getInstance();
                userModelSingleton.logout(callbackLogout);
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
            UserProfileModelSingleton userProfileModelSingleton = UserProfileModelSingleton.getInstance();
            userProfileModelSingleton.removeUserProfileListener(id, callbackUserData);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        String id = FirebaseAuth.getInstance().getUid();
        if(id != null){
            UserProfileModelSingleton userProfileModelSingleton = UserProfileModelSingleton.getInstance();
            userProfileModelSingleton.addUserProfileListener(id, callbackUserData);
        }
    }
}
