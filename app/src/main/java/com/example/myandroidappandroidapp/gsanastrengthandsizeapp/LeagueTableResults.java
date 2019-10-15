package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.adapters.LeagueTableRecyclerViewAdapter;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.comparator.SortUserLeague;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.DataModelResult;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.User;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserLeagueTableModelSingleton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;

import static java.security.AccessController.getContext;

public class LeagueTableResults extends AppCompatActivity {

    private TextView nameOfLeague;
    final UserLeagueTableModelSingleton userLeagueTableModelSingleton = UserLeagueTableModelSingleton.getInstance();
    DataModelResult<ArrayList<User>> usersCallback;

    private RecyclerView leagueTableRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_table_results);

        Intent intent = getIntent();
        String leaguePin = intent.getStringExtra("LeaguePin"); // passed into function
        String leagueName = intent.getStringExtra("LeagueName");
        nameOfLeague = this.findViewById(R.id.league_table_results_heading_view_league_name);
        nameOfLeague.setText(leagueName);

        leagueTableRecyclerView = this.findViewById(R.id.league_table_results_main_view_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        leagueTableRecyclerView.setLayoutManager(layoutManager);

        usersCallback = new DataModelResult<ArrayList<User>>() {
            @Override
            public void onComplete(ArrayList<User> data, Exception exception) {
                if(data != null){
                    // sort data by total
                    Collections.sort(data, new SortUserLeague().reversed());

                    mAdapter = new LeagueTableRecyclerViewAdapter(data);
                    leagueTableRecyclerView.setAdapter(mAdapter);
                    Log.v("", "");
                }
                else {
                    Log.v("", "");
                }
            }
        };

        // gets the leagueMasterId
        DataModelResult<ArrayList<String>> callback = new DataModelResult<ArrayList<String>>() {
            @Override
            public void onComplete(ArrayList<String> data, Exception exception) {
                if(data != null){
                    //pin.setText(data.toString());
                    String id = FirebaseAuth.getInstance().getUid();
                    if(id != null){
                        // adding listener
                        userLeagueTableModelSingleton.addLeagueTableUserProfileListener(id, data, usersCallback);
                    }

                    //userLeagueTableModelSingleton.getUsersForCurrentLeague("", data, usersCallback);
                }
                else {
                    nameOfLeague.setText("no data");
                }
            }
        };

        userLeagueTableModelSingleton.getUsersWithTheSamePin(leaguePin, callback); // passes in leaguePin and returns the leagueMasterId


        ImageView backBtn = this.findViewById(R.id.league_table_results_heading_view_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView filterBtn = this.findViewById(R.id.league_table_results_heading_view_filter);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FilterActivity.class);
                startActivityForResult(intent, 999);
                //alertDialog("filter");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 999) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                String resulet=data.getStringExtra("name");

                Log.v("", "");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.v("", "");
                //Write your code if there's no result
            }
        }
    }//onActivityResult

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

    @Override
    protected void onResume() {
        super.onResume();
        String id = FirebaseAuth.getInstance().getUid();
        if(id != null){

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        String id = FirebaseAuth.getInstance().getUid();
        if(id != null){
            //remove listener
            userLeagueTableModelSingleton.removeLeagueTableUserProfileListener(id, usersCallback);
        }
    }
}
