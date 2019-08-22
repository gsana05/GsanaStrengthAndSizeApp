package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

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


        Button logInButton = (Button) this.findViewById(R.id.log_in_button);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validEmailLogIn){
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("You need a green tick")
                            .setCancelable(false)
                            .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Email is not valid");
                    alert.show();
                    return;
                }

                if(!validPasswordLogIn){
                    return;
                }

                logIn(email.toString(), password.toString());
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

    public void logIn(String email, String password){

        FirebaseAuth.getInstance().createUserWithEmailAndPassword("sanashee0555@hotmail.com", password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.v("Tag", "");
                }
                else {
                    Log.v("Tag", task.getException().toString());
                }

            }
        });

        /*EditText email = (EditText) this.findViewById(R.id.log_in_email_input);
        String emailUser = email.getText().toString();

        EditText password = (EditText) this.findViewById(R.id.log_in_password_input);
        String passwordUser = password.getText().toString();*/

       // Toast.makeText(getApplicationContext(), "" ,Toast.LENGTH_LONG).show();
    }
}
