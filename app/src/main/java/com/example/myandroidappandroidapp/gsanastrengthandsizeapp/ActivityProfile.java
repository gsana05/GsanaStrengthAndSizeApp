package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.DataModelResult;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.User;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserModelSingleton;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserProfileModelSingleton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class ActivityProfile extends AppCompatActivity {

    private TextView logout;
    private DataModelResult<User> callbackUserData;
    private EditText gymName;
    private EditText benchPress;
    private EditText deadlift;
    private EditText squat;
    private EditText ohp;
    private Button saveChangesBtn;
    private Button logoutUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        gymName = this.findViewById(R.id.profile_sub_heading_gym_name_input);
        benchPress = this.findViewById(R.id.profile_sub_heading_bench_input);
        deadlift = this.findViewById(R.id.profile_sub_heading_deadlift_input);
        squat = this.findViewById(R.id.profile_sub_heading_squat_input);
        ohp = this.findViewById(R.id.profile_sub_heading_ohp_input);


        callbackUserData = new DataModelResult<User>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(User data, Exception exception) {
                if(data != null){
                    gymName.setText(data.getGymName());
                    benchPress.setText(data.getBenchPress().toString());
                    deadlift.setText(data.getDeadlift().toString());
                    squat.setText(data.getSquat().toString());
                    ohp.setText(data.getOverHeadPress().toString());
                }
                else {
                    gymName.setText("no data");
                    benchPress.setText("no data");
                    deadlift.setText("no data");
                    squat.setText("no data");
                    ohp.setText("no data");
                }
            }
        };


        final DataModelResult<Boolean> callbackLogout = new DataModelResult<Boolean>(){

            @Override
            public void onComplete(Boolean data, Exception exception) {
                if(data){
                    Intent intent = new Intent(getApplicationContext(), ActivityLogIn.class);
                    startActivity(intent);
                }
            }
        };

        logout = this.findViewById(R.id.profile_save_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModelSingleton userModelSingleton = UserModelSingleton.getInstance();
                userModelSingleton.logout(callbackLogout);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        String id = FirebaseAuth.getInstance().getUid();
        if(id != null){
            UserProfileModelSingleton userProfileModelSingleton = UserProfileModelSingleton.getInstance();
            userProfileModelSingleton.removeUserProfileListener(id, callbackUserData);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        String id = FirebaseAuth.getInstance().getUid();
        if(id != null){
            UserProfileModelSingleton userProfileModelSingleton = UserProfileModelSingleton.getInstance();
            userProfileModelSingleton.addUserProfileListener(id, callbackUserData);
        }
    }
}
