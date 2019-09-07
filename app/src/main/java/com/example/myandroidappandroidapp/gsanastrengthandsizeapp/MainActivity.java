package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button btnLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogOut = this.findViewById(R.id.btn);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity();
            }
        });
    }

    public void startActivity(){
        Intent intent = new Intent(this, ActivityLogIn.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String id = FirebaseAuth.getInstance().getUid();
        if(id == null){
            startActivity();
        }
    }
}
