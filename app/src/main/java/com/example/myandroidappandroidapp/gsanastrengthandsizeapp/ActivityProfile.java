package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
    private Button cancelClaim;
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
    private ProgressBar saveChangesBtnProgress;
    private Boolean mSubmitProgress = false;
    private Button logout;
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

    private static final int BENCH_REQUEST = 1;
    private static final int SQUAT_REQUEST = 2;
    private static final int DEADLIFT_REQUEST = 3;
    private static final int OHP_REQUEST = 4;


    private Boolean mClaim = false;

    private Button benchProofBtn;
    private TextView benchTextView;
    private ImageView benchImageView;
    private Uri mBenchPressUri;

    private Button squatProofBtn;
    private TextView squatTextView;
    private ImageView squatImageView;
    private Uri mSquatUri;

    private Button deadliftProofBtn;
    private TextView deadliftTextView;
    private ImageView deadliftImageView;
    private Uri mDeadliftUri;

    private Button ohpProofBtn;
    private TextView ohpTextView;
    private ImageView ohpImageView;
    private Uri mOhpUri;


    private static final String MY_PREFS_NAME = "MyPrefsFile";
    private static final String BENCH_TEXT = "bench_text";
    private static final String SQUAT_TEXT = "squat_text";
    private static final String DEADLIFT_TEXT = "deadlift_text";
    private static final String OHP_TEXT = "ohp_text";

    private String mBenchSavedLocally;
    private String mSquatSavedLocally;
    private String mDeadliftSavedLocally;
    private String mOhpSavedLocally;

    private User mCurrentUser;

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

        benchProofBtn = this.findViewById(R.id.profile_bench_proof_input);
        benchProofBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInputOnDevice();
                openFileChooser(BENCH_REQUEST);
            }
        });
        benchTextView = this.findViewById(R.id.profile_bench_proof_link);
        benchImageView = this.findViewById(R.id.profile_bench_proof_link_remove);
        benchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBenchPressUri = null;
                updateUI();
            }
        });



        squatProofBtn = this.findViewById(R.id.profile_squat_proof_input);
        squatProofBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInputOnDevice();
                openFileChooser(SQUAT_REQUEST);
            }
        });
        squatTextView = this.findViewById(R.id.profile_squat_proof_link);
        squatImageView = this.findViewById(R.id.profile_squat_proof_link_remove);
        squatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSquatUri = null;
                updateUI();
            }
        });

        deadliftProofBtn = this.findViewById(R.id.profile_deadlift_proof_input);
        deadliftProofBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInputOnDevice();
                openFileChooser(DEADLIFT_REQUEST);
            }
        });
        deadliftTextView = this.findViewById(R.id.profile_deadlift_proof_link);
        deadliftImageView = this.findViewById(R.id.profile_deadlift_proof_link_remove);
        deadliftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeadliftUri = null;
                updateUI();
            }
        });

        ohpProofBtn = this.findViewById(R.id.profile_ohp_proof_input);
        ohpProofBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInputOnDevice();
                openFileChooser(OHP_REQUEST);
            }
        });
        ohpTextView = this.findViewById(R.id.profile_ohp_proof_link);
        ohpImageView = this.findViewById(R.id.profile_ohp_proof_link_remove);
        ohpImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOhpUri = null;
                updateUI();
            }
        });


        callbackUserData = new DataModelResult<User>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(User data, Exception exception) {
               mCurrentUser = data;
               updateUI();
            }
        };

        ImageView backBtn = this.findViewById(R.id.profile_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clear out shared preferences
                deleteInputOnDevice();
                finish();
            }
        });


        claimBtn = this.findViewById(R.id.profile_claim_btn);
        claimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mClaim = true;
                updateUI();

                /*bench1 = Float.valueOf(benchPress.getText().toString());
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
                }*/
                /*claim = true;
                updateUI();*/
            }
        });

        saveChangesBtnProgress = this.findViewById(R.id.profile_save_btn_progress);
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

                }if(mBenchPressUri == null && mSquatUri == null && mDeadliftUri == null && mOhpUri == null){
                    alertDialog("You have not uploaded any new videos");
                    return;
                }

                mSubmitProgress = true;
                updateUI();

                DataModelResult<Boolean> callback = new DataModelResult<Boolean>() {
                    @Override
                    public void onComplete(Boolean data, Exception exception) {


                        if(exception != null){
                            alertDialog(exception.getMessage());
                            mSubmitProgress = false;
                            updateUI();
                        }
                        else if(data != null && data){
                            deleteInputOnDevice();
                            alertDialog("Data saved");
                            mSubmitProgress = false;
                            updateUI();
                            finish();
                        }
                        else if(data == null){
                            alertDialog("Data has not been saved");
                            mSubmitProgress = false;
                            updateUI();
                        }
                        else{
                            alertDialog("Something has gone wrong");
                            mSubmitProgress = false;
                            updateUI();
                        }

                    }
                };

                userProfileModelSingleton.updateUser(
                        gymName.getText().toString(),
                        Float.valueOf(benchPress.getText().toString()),
                        Float.valueOf(squat.getText().toString()),
                        Float.valueOf(deadlift.getText().toString()),
                        Float.valueOf(ohp.getText().toString()),
                        mBenchPressUri,
                        mSquatUri,
                        mDeadliftUri,
                        mOhpUri,
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

        logout = this.findViewById(R.id.profile_logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userModelSingleton.logout(callbackLogout);
            }
        });

        cancelClaim = this.findViewById(R.id.profile_sign_out_btn);
        logoutUserSpinner = this.findViewById(R.id.profile_sign_out_progress);
        cancelClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClaim = false;
                mBenchPressUri = null;
                mSquatUri = null;
                mDeadliftUri = null;
                mOhpUri = null;
                deleteInputOnDevice();
                updateUI();
            }
        });


        ImageView settingsBtn = this.findViewById(R.id.profile_image_settings);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInputOnDevice();
                Intent intent = new Intent(v.getContext(), ActivitySettings.class);
                startActivity(intent);
            }
        });

        updateUI();
    }

    public void deleteInputOnDevice(){
        SharedPreferences sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    public void saveInputOnDevice(){
        SharedPreferences sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BENCH_TEXT, benchPress.getText().toString());
        editor.putString(SQUAT_TEXT, squat.getText().toString());
        editor.putString(DEADLIFT_TEXT, deadlift.getText().toString());
        editor.putString(OHP_TEXT, ohp.getText().toString());

        editor.apply();
    }

    private void openFileChooser(int exercise){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, exercise);
    }

    public void updateUI(){

        if(mCurrentUser != null && !mSubmitProgress){

            gymName.setText(mCurrentUser.getGymName());

            SharedPreferences sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            mBenchSavedLocally = sharedPreferences.getString(BENCH_TEXT, "");
            if(mBenchSavedLocally.isEmpty()){
                benchPress.setText(mCurrentUser.getBenchPress().toString());
            }
            else{
                benchPress.setText(mBenchSavedLocally);
            }
            benchPressValue = mCurrentUser.getBenchPress();


            mDeadliftSavedLocally = sharedPreferences.getString(DEADLIFT_TEXT, "");
            if(mDeadliftSavedLocally.isEmpty()){
                deadlift.setText(mCurrentUser.getDeadlift().toString());
            }
            else{
                deadlift.setText(mDeadliftSavedLocally);
            }
            deadliftValue = mCurrentUser.getDeadlift();


            mSquatSavedLocally = sharedPreferences.getString(SQUAT_TEXT, "");
            if(mSquatSavedLocally.isEmpty()){
                squat.setText(mCurrentUser.getSquat().toString());
            }
            else{
                squat.setText(mSquatSavedLocally);
            }
            squatValue = mCurrentUser.getSquat();

            mOhpSavedLocally = sharedPreferences.getString(OHP_TEXT, "");
            if(mOhpSavedLocally.isEmpty()){
                ohp.setText(mCurrentUser.getOverHeadPress().toString());
            }
            else{
                ohp.setText(mOhpSavedLocally);
            }
            ohpValue = mCurrentUser.getOverHeadPress();

            benchProofLink = mCurrentUser.getProofBenchLink();
            squatProofLink = mCurrentUser.getProofSquatLink();
            ohpLink = mCurrentUser.getProofOhpLink();
            deadliftLink = mCurrentUser.getProofDeadliftLink();
        }

        if(mClaim){

            gymName.setEnabled(false);

            benchProofBtn.setVisibility(View.VISIBLE);
            if(mBenchPressUri != null){
                benchTextView.setVisibility(View.VISIBLE);
                benchTextView.setText(mBenchPressUri.toString());
                benchImageView.setVisibility(View.VISIBLE);
                benchPress.setEnabled(true);
                benchProofBtn.setEnabled(false);
            }
            else{
                benchTextView.setVisibility(View.GONE);
                benchImageView.setVisibility(View.GONE);
                benchPress.setText(benchPressValue.toString());
                benchPress.setEnabled(false);
                benchProofBtn.setEnabled(true);
            }

            squatProofBtn.setVisibility(View.VISIBLE);
            if(mSquatUri != null){
                squatTextView.setVisibility(View.VISIBLE);
                squatTextView.setText(mSquatUri.toString());
                squatImageView.setVisibility(View.VISIBLE);
                squat.setEnabled(true);
                squatProofBtn.setEnabled(false);
            }
            else {
                squatTextView.setVisibility(View.GONE);
                squatImageView.setVisibility(View.GONE);
                squat.setText(squatValue.toString());
                squat.setEnabled(false);
                squatProofBtn.setEnabled(true);
            }


            deadliftProofBtn.setVisibility(View.VISIBLE);
            if(mDeadliftUri != null){
                deadliftTextView.setVisibility(View.VISIBLE);
                deadliftTextView.setText(mDeadliftUri.toString());
                deadliftImageView.setVisibility(View.VISIBLE);
                deadlift.setEnabled(true);
                deadliftProofBtn.setEnabled(false);
            }
            else {
                deadliftTextView.setVisibility(View.GONE);
                deadliftImageView.setVisibility(View.GONE);
                deadlift.setText(deadliftValue.toString());
                deadlift.setEnabled(false);
                deadliftProofBtn.setEnabled(true);
            }


            ohpProofBtn.setVisibility(View.VISIBLE);
            if(mOhpUri != null){
                ohpTextView.setVisibility(View.VISIBLE);
                ohpTextView.setText(mOhpUri.toString());
                ohpImageView.setVisibility(View.VISIBLE);
                ohp.setEnabled(true);
                ohpProofBtn.setEnabled(false);
            }
            else {
                ohpTextView.setVisibility(View.GONE);
                ohpImageView.setVisibility(View.GONE);
                ohp.setText(ohpValue.toString());
                ohp.setEnabled(false);
                ohpProofBtn.setEnabled(true);
            }

            if(mSubmitProgress){
                saveChangesBtn.setVisibility(View.INVISIBLE);
                saveChangesBtnProgress.setVisibility(View.VISIBLE);
            }
            else{
                saveChangesBtn.setVisibility(View.VISIBLE);
                saveChangesBtnProgress.setVisibility(View.INVISIBLE);
            }


            cancelClaim.setVisibility(View.VISIBLE);
            claimBtn.setVisibility(View.INVISIBLE);
            logout.setVisibility(View.INVISIBLE);

        }
        else{

            gymName.setEnabled(false);
            benchPress.setEnabled(false);
            squat.setEnabled(false);
            deadlift.setEnabled(false);
            ohp.setEnabled(false);

            benchProofBtn.setVisibility(View.GONE);
            benchTextView.setVisibility(View.GONE);
            benchImageView.setVisibility(View.GONE);

            squatProofBtn.setVisibility(View.GONE);
            squatTextView.setVisibility(View.GONE);
            squatImageView.setVisibility(View.GONE);

            deadliftProofBtn.setVisibility(View.GONE);
            deadliftTextView.setVisibility(View.GONE);
            deadliftImageView.setVisibility(View.GONE);

            ohpProofBtn.setVisibility(View.GONE);
            ohpTextView.setVisibility(View.GONE);
            ohpImageView.setVisibility(View.GONE);

            saveChangesBtn.setVisibility(View.INVISIBLE);
            cancelClaim.setVisibility(View.GONE);
            claimBtn.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
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
