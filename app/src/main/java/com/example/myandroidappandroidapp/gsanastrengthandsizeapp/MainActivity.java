package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.DataModelResult;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserModelSingleton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button btnLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* btnLogOut = this.findViewById(R.id.btn);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataModelResult<Boolean> callback = new DataModelResult<Boolean>() {
                    @Override
                    public void onComplete(Boolean data, Exception exception) {
                        if(data){
                            startActivity();
                        }
                        else {
                            Log.v("LOG OUT ERROR", "Something went wrong logging out");
                        }

                    }
                };

                UserModelSingleton userModelSingleton = UserModelSingleton.getInstance();
                userModelSingleton.logout(callback);


            }
        });*/
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
