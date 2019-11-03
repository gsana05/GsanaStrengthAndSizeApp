package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.adapters.LeagueRecyclerViewAdapter;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.adapters.LeagueTableRecyclerViewAdapter;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.adapters.SettingsLeagueRecyclerView;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.CreatedLeague;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.DataModelResult;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.LeagueModelSingleton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ActivitySettings extends AppCompatActivity {

    private RecyclerView leagueTableSettingsRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private DataModelResult<ArrayList<String>> callback;

    private LeagueModelSingleton leagueModelSingleton = LeagueModelSingleton.getInstance();
    private DataModelResult<ArrayList<CreatedLeague>> callbackCreatedLeagues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        leagueTableSettingsRecyclerView = this.findViewById(R.id.settings_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        leagueTableSettingsRecyclerView.setLayoutManager(layoutManager);

        callbackCreatedLeagues = new DataModelResult<ArrayList<CreatedLeague>>() {
            @Override
            public void onComplete(ArrayList<CreatedLeague> data, Exception exception) {
                if(data != null){
                    if(data.size() >= 1){
                        //tv.setVisibility(View.GONE);
                        mAdapter = new LeagueRecyclerViewAdapter(data, getApplicationContext());
                        leagueTableSettingsRecyclerView.setAdapter(mAdapter);
                    }
                    else{
                        //tv.setVisibility(View.VISIBLE);
                    }
                }
            }
        };

        // get the the list of leagues this user is part of
        callback = new DataModelResult<ArrayList<String>>() {
            @Override
            public void onComplete(ArrayList<String> data, Exception exception) {
                if(data != null){ // data is all league pins for that user
                    LeagueModelSingleton leagueModelSingleton = LeagueModelSingleton.getInstance();
                    leagueModelSingleton.addAllLeagueListener(data, callbackCreatedLeagues);
                }
                else {
                    alertDialog("data == null");
                    // clear recycler in case user has a league and then deletes it
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        String userId = FirebaseAuth.getInstance().getUid();
        if(userId != null){
            leagueModelSingleton.addLeagueListener(userId, callback);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        String userId = FirebaseAuth.getInstance().getUid();
        if(userId != null){
            leagueModelSingleton.removeLeagueListener(userId, callback);
        }
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
}
