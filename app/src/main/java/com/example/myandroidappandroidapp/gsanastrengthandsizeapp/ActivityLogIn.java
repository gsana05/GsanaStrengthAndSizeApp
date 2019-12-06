package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.DataModelResult;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.MyFirebaseMessagingService;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserModelSingleton;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Functions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

public class ActivityLogIn extends AppCompatActivity {

    private Boolean mSignIn = false;
    private final static String CHANNEL_ID = "CHANNEL_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Button logInButton = this.findViewById(R.id.log_in_button);
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

        mSignIn = true;
        updateUI();
        DataModelResult<Boolean> callback = new DataModelResult<Boolean>() {
            @Override
            public void onComplete(Boolean data, Exception exception) {
                if(data){
                    Task<String> i = sendCallToFirebaseFunctionForPushNotifications("Gareth Sanashee");
                    i.addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            mSignIn = false;
                            updateUI();
                            finish();
                        }
                    });
                    i.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // test code
                            mSignIn = true;
                            updateUI();
                        }
                    });

                }else {
                    mSignIn = false;
                    updateUI();
                    alertDialog(exception.getMessage());
                }
            }
        };

        UserModelSingleton userModelSingleton = UserModelSingleton.getInstance();
        userModelSingleton.logIn(email.getText().toString(), password.getText().toString(), callback);

    }

    private Task<String> sendCallToFirebaseFunctionForPushNotifications(String pushNotificationToken) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("pushNotificationToken", pushNotificationToken);

        FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();

        return mFunctions
                .getHttpsCallable("sendNotification")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        String result = (String) task.getResult().getData();
                        return result;
                    }
                });
    }

    /*public void notification(String title, String body){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_accounticon)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }*/

    public void updateUI(){
        ProgressBar progress = this.findViewById(R.id.log_in_button_progress);
        Button progressBtn = this.findViewById(R.id.log_in_button);
        if(mSignIn){
            progress.setVisibility(View.VISIBLE);
            progressBtn.setVisibility(View.INVISIBLE);
        }
        else {
            progress.setVisibility(View.INVISIBLE);
            progressBtn.setVisibility(View.VISIBLE);
        }
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
