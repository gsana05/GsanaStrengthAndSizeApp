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

import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityLogIn extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private ImageView emailChceck;
    private ImageView passwordCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        email = (EditText) this.findViewById(R.id.log_in_email_input);
        password = (EditText) this.findViewById(R.id.log_in_password_input);
        emailChceck = (ImageView) this.findViewById(R.id.log_in_email_check) ;
        passwordCheck = (ImageView) this.findViewById(R.id.log_in_password_check);


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
                    emailChceck.setVisibility(View.VISIBLE);
                }
                else {
                    emailChceck.setVisibility(View.INVISIBLE);
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
                }
                else {
                    passwordCheck.setVisibility(View.INVISIBLE);
                }

            }
        });


        Button logInButton = (Button) this.findViewById(R.id.log_in_button);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn(email.toString(), password.toString());
            }
        });
    }

    public  boolean isValidEmail(final String email){

        Pattern pattern;
        Matcher matcher;

        final String EMAIL_PATTERN = "^[A-Za-z0-9._]{1,16}+@{1}+[a-z]{1,7}\\.[a-z]{1,3}$";

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

    public void logIn(String email, String password){

        //FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password);

        /*EditText email = (EditText) this.findViewById(R.id.log_in_email_input);
        String emailUser = email.getText().toString();

        EditText password = (EditText) this.findViewById(R.id.log_in_password_input);
        String passwordUser = password.getText().toString();*/

       // Toast.makeText(getApplicationContext(), "" ,Toast.LENGTH_LONG).show();
    }
}
