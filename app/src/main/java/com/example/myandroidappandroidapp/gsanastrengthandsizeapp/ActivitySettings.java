package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.adapters.LeagueRecyclerViewAdapter;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.CreatedLeague;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.DataModelResult;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.LeagueModelSingleton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class ActivitySettings extends AppCompatActivity {

    private RecyclerView leagueTableSettingsRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private DataModelResult<ArrayList<String>> callback;

    private LeagueModelSingleton leagueModelSingleton = LeagueModelSingleton.getInstance();
    private DataModelResult<ArrayList<CreatedLeague>> callbackCreatedLeagues;

    private SwitchCompat switchCompat;
    private TextView tvRules;
    private TextView tvExitLeague;
    private TextView tvHeadingLeaguesYouAreIn;
    private WebView rules;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        rules = this.findViewById(R.id.settings_rules);
        tvHeadingLeaguesYouAreIn = this.findViewById(R.id.settings_sub_heading);
        tvRules = this.findViewById(R.id.settings_rules_switch_map);
        tvExitLeague = this.findViewById(R.id.settings_exit_league_switch_list);
        switchCompat = this.findViewById(R.id.settings_switch);
        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI();
            }
        });

        leagueTableSettingsRecyclerView = this.findViewById(R.id.settings_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        leagueTableSettingsRecyclerView.setLayoutManager(layoutManager);


        final TextView tv = this.findViewById(R.id.settings_no_league_label);

        callbackCreatedLeagues = new DataModelResult<ArrayList<CreatedLeague>>() {
            @Override
            public void onComplete(ArrayList<CreatedLeague> data, Exception exception) {
                if(data != null){
                    if(data.size() >= 1){
                        tv.setVisibility(View.GONE);
                        mAdapter = new LeagueRecyclerViewAdapter(data, getApplicationContext(), true);
                        leagueTableSettingsRecyclerView.setAdapter(mAdapter);
                    }
                    else{
                        tv.setVisibility(View.VISIBLE);
                        leagueTableSettingsRecyclerView.setAdapter(null);
                        Log.v("", "");
                    }
                }
            }
        };

        // get the the list of leagues this user is part of
        callback = new DataModelResult<ArrayList<String>>() {
            @Override
            public void onComplete(ArrayList<String> data, Exception exception) {
                if(data != null){ // data is all league pins for that user
                    leagueModelSingleton.addAllLeagueListener(data, callbackCreatedLeagues);
                }
                else {
                    alertDialog("data == null");
                    // clear recycler in case user has a league and then deletes it
                }
            }
        };

        ImageView settingBackBtn = this.findViewById(R.id.settings_back_btn);
        settingBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateUI();
    }

    public void updateUI(){
        if(switchCompat.isChecked()){ // rules
            tvRules.setVisibility(View.VISIBLE);
            tvExitLeague.setVisibility(View.INVISIBLE);
            tvHeadingLeaguesYouAreIn.setVisibility(View.VISIBLE);
            leagueTableSettingsRecyclerView.setVisibility(View.VISIBLE);
            rules.setVisibility(View.GONE);
        }
        else{ // exit leagues
            tvRules.setVisibility(View.INVISIBLE);
            tvExitLeague.setVisibility(View.VISIBLE);
            tvHeadingLeaguesYouAreIn.setVisibility(View.GONE);
            leagueTableSettingsRecyclerView.setVisibility(View.GONE);
            rules.setVisibility(View.VISIBLE);
            rules.loadUrl("file:///android_asset/rules.html");
        }
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
            leagueModelSingleton.removeAllLeagueListener(userId, callbackCreatedLeagues);
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
