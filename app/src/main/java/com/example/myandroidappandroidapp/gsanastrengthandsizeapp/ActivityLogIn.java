package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.DataModelResult;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserModelSingleton;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityLogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Button logInButton = this.findViewById(R.id.sign_up_button);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               doLogin();
            }
        });

        Button createAccount = this.findViewById(R.id.log_in_create_account_btn);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity();
            }
        });

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

    public void doLogin(){

        EditText email = this.findViewById(R.id.log_in_email_input);
        EditText password = this.findViewById(R.id.log_in_password_input);

        if(email.getText().toString().isEmpty()){
            alertDialog("Please enter email");
            return;
        }

        if(password.getText().toString().isEmpty()){
            alertDialog("Please enter password");
            return;
        }

        DataModelResult<Boolean> callback = new DataModelResult<Boolean>() {
            @Override
            public void onComplete(Boolean data, Exception exception) {
                if(data){
                    finish();
                }else {
                    alertDialog(exception.getMessage());
                }
            }
        };

        UserModelSingleton userModelSingleton = UserModelSingleton.getInstance();
        userModelSingleton.logIn(email.getText().toString(), password.getText().toString(), callback);

    }

    public void startActivity(){
        Intent intent = new Intent(this, ActivitySignUp.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String id = FirebaseAuth.getInstance().getUid();
        if(id != null){
            finish();
        }
    }
}
