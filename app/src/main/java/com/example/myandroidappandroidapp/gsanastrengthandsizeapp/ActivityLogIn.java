package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.DataModelResult;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserModelSingleton;

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

    public void doLogin(){

        EditText email = this.findViewById(R.id.log_in_email_input);
        EditText password = this.findViewById(R.id.log_in_password_input);

        DataModelResult<Boolean> callback = new DataModelResult<Boolean>() {
            @Override
            public void onComplete(Boolean data, Exception exception) {
                if(data){
                    startMainActivity();
                }else {
                    Toast.makeText(getApplicationContext(), exception.getMessage() , Toast.LENGTH_LONG).show();
                }
            }
        };

        UserModelSingleton userModelSingleton = UserModelSingleton.getInstance();
        userModelSingleton.logIn(email.getText().toString(), password.getText().toString(), callback);

    }

    public void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void startActivity(){
        Intent intent = new Intent(this, ActivitySignUp.class);
        startActivity(intent);
    }
}
