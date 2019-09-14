package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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

public class FragmentToolBar extends Fragment {

    TextView userName;
    private DataModelResult<User> callback;
    private ImageView logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_fragment_tool_bar, container, false);

        callback = new DataModelResult<User>() {

            @Override
            public void onComplete(User data, Exception exception) {
                if(data != null){
                    userName = view.findViewById(R.id.fragment_tool_bar_account_name_text);
                    userName.setText(data.getGymName());
                }
                else {
                    userName = view.findViewById(R.id.fragment_tool_bar_account_name_text);
                    userName.setText("null");
                }
            }
        };

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
                UserModelSingleton userModelSingleton = UserModelSingleton.getInstance();
                userModelSingleton.logout(callbackLogout);
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        String id = FirebaseAuth.getInstance().getUid();
        if(id != null){
            UserProfileModelSingleton userProfileModelSingleton = UserProfileModelSingleton.getInstance();
            userProfileModelSingleton.getUserData(id, callback);
        }
    }
}
