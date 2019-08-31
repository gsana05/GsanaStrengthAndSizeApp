package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.DataModelResult;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityLogIn extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private ImageView emailCheck;
    private ImageView passwordCheck;
    private boolean validEmailLogIn;
    private boolean validPasswordLogIn;
    private boolean mLogInProgress;

    private final static int isValidEmail = 1;
    private final static int isValidPassword = 2;
    private final static int isLogInSuccess = 3;
    private final static int isLogInUnsuccessful = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mLogInProgress = false;

        email = this.findViewById(R.id.log_in_email_input);
        password = this.findViewById(R.id.log_in_password_input);
        emailCheck = this.findViewById(R.id.log_in_email_check) ;
        passwordCheck = this.findViewById(R.id.log_in_password_check);



        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Toast.makeText(getApplicationContext(), "1" ,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(getApplicationContext(), "2" ,Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable s) {

                boolean validEmail = isValidEmail(s.toString());
                if(validEmail){
                    emailCheck.setVisibility(View.VISIBLE);
                    validEmailLogIn = true;
                }
                else {
                    emailCheck.setVisibility(View.INVISIBLE);
                    validEmailLogIn = false;
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isValidPassword = isValidPassword(s.toString());
                if(isValidPassword){
                    passwordCheck.setVisibility(View.VISIBLE);
                    validPasswordLogIn = true;
                }
                else {
                    passwordCheck.setVisibility(View.INVISIBLE);
                    validPasswordLogIn = false;
                }

            }
        });


        Button logInButton = this.findViewById(R.id.log_in_button);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validEmailLogIn){
                    logInAlert(v.getContext(), isValidEmail, null);
                    return;
                }

                if(!validPasswordLogIn){
                    logInAlert(v.getContext(), isValidPassword, null);
                    return;
                }

                logIn(email.getText().toString(), password.getText().toString(), v.getContext());
            }
        });
    }

    public  boolean isValidEmail(final String email){

        Pattern pattern;
        Matcher matcher;

        final String EMAIL_PATTERN =  "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public void updateUI(){
        ProgressBar progress = this.findViewById(R.id.log_in_button_progress);
        Button progressBtn = this.findViewById(R.id.log_in_button);
        if(mLogInProgress){
            progress.setVisibility(View.VISIBLE);
            progressBtn.setVisibility(View.INVISIBLE);
        }
        else {
            progress.setVisibility(View.INVISIBLE);
            progressBtn.setVisibility(View.VISIBLE);
        }
    }

    public void logInAlert(Context context, int type, Exception exc){

        String response;
        if(exc != null){
            response = exc.getMessage();
        }
        else {
            switch(type){
                case isValidEmail: response = "Email is not valid";
                    break;
                case isValidPassword: response = "password is not valid";
                    break;
                case isLogInSuccess: response = "Account created successfully";
                    break;
                default: response = "Error";
            }
        }

        // only shows if you have not logged in successfully
        if(type != isLogInSuccess){
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(context);
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
    }

    public void logIn(String email, String password, final Context context){
        mLogInProgress = true;
        updateUI();

        UserModel userModel = new UserModel();

        DataModelResult<Boolean> callback = new DataModelResult<Boolean>(){
            @Override
            public void onComplete(Boolean data, Exception exception) {
                if(data){
                    logInAlert(context, isLogInSuccess, null);
                    mLogInProgress = false;
                    updateUI();
                    startStatsActivity();
                }
                else {
                    logInAlert(context, isLogInUnsuccessful, exception);
                    mLogInProgress = false;
                    updateUI();
                }
            }
        };

        userModel.signUp(email, password, callback);
    }

    public void startStatsActivity(){
        Intent intent = new Intent(this, ActivityStartStates.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String id = FirebaseAuth.getInstance().getUid();
        if(id != null){
            //startStatsActivity();
        }
    }
}

