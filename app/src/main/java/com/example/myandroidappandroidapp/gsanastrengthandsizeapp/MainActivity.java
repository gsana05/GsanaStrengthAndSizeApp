package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Intent intent = new Intent(this, ActivitySignUp.class);
            startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        String id = FirebaseAuth.getInstance().getUid();
        if(id == null){
            /*Intent intent = new Intent(this, ActivitySignUp.class);
            startActivity(intent);*/
        }
    }
}
