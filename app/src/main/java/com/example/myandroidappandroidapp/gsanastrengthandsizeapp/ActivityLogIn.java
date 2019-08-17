package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityLogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        EditText email = (EditText) this.findViewById(R.id.log_in_email_input);
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
                Pattern pattern = Pattern.compile("^[A-Za-z0-9._]{1,16}+@{1}+[a-z]{1,7}\\.[a-z]{1,3}$");
                Matcher mail = pattern.matcher(s);
                if(mail.find()){
                    ImageView email = (ImageView) findViewById(R.id.log_in_email_check);
                    email.setVisibility(View.VISIBLE);
                }
                else {
                    ImageView email = (ImageView) findViewById(R.id.log_in_email_check);
                    email.setVisibility(View.INVISIBLE);
                }

                //Toast.makeText(getApplicationContext(), "3" ,Toast.LENGTH_LONG).show();
            }
        });

        Button logInButton = (Button) this.findViewById(R.id.log_in_button);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });
    }

    public void logIn(){
        EditText email = (EditText) this.findViewById(R.id.log_in_email_input);
        String emailUser = email.getText().toString();




        EditText password = (EditText) this.findViewById(R.id.log_in_password_input);
        String passwordUser = password.getText().toString();



       // Toast.makeText(getApplicationContext(), "" ,Toast.LENGTH_LONG).show();
    }
}
