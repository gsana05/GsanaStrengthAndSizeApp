package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.DataModelResult;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.User;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserModelSingleton;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserProfileModelSingleton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class FragmentToolBar extends Fragment {

    TextView userName;
    TextView userResult;
    private DataModelResult<User> callback;
    private DataModelResult<User> callbackTest;
    private ImageView logout;
    private TextView totalStrengthScore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_fragment_tool_bar, container, false);


        totalStrengthScore = view.findViewById(R.id.fragment_tool_bar_account_strength_label);
        totalStrengthScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("This is the total of your bench press, squat, deadlift and over head press ")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.show();
            }
        });

        callback = new DataModelResult<User>() {

            @Override
            public void onComplete(User data, Exception exception) {
                if(data != null){
                    userName = view.findViewById(R.id.fragment_tool_bar_account_name_text);
                    userName.setText(data.getGymName());

                    userResult = view.findViewById(R.id.fragment_tool_bar_account_strength_result);
                    Float bench = data.getBenchPress();
                    Float deadlift = data.getDeadlift();
                    Float squat = data.getSquat();
                    Float ohp = data.getOverHeadPress();

                    Float total = bench + deadlift + squat + ohp;

                    userResult.setText(String.format(Locale.UK,"%.02f KG", total));
                }
                else {
                    userName = view.findViewById(R.id.fragment_tool_bar_account_name_text);
                    userName.setText("null");
                }
            }
        };

        // logout user
        final DataModelResult<Boolean> callbackLogout = new DataModelResult<Boolean>(){

            @Override
            public void onComplete(Boolean data, Exception exception) {
                if(data){
                    Intent intent = new Intent(getActivity(), ActivityLogIn.class);
                    startActivity(intent);
                }
            }
        };

        logout = view.findViewById(R.id.fragment_tool_bar_account_icon);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityProfile.class);
                startActivity(intent);
                //UserModelSingleton userModelSingleton = UserModelSingleton.getInstance();
                //userModelSingleton.logout(callbackLogout);
            }
        });



        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        String id = FirebaseAuth.getInstance().getUid();
        if(id != null){
            UserProfileModelSingleton userProfileModelSingleton = UserProfileModelSingleton.getInstance();
            userProfileModelSingleton.removeUserProfileListener(id, callback);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        String id = FirebaseAuth.getInstance().getUid();
        if(id != null){
            UserProfileModelSingleton userProfileModelSingleton = UserProfileModelSingleton.getInstance();
            userProfileModelSingleton.addUserProfileListener(id, callback);
        }
    }
}
