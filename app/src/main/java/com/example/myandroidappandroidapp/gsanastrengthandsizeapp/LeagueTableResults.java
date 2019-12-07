package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.adapters.LeagueTableRecyclerViewAdapter;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.comparator.SortUserLeague;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.comparator.SortUserLeagueByBench;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.comparator.SortUserLeagueByDeadlift;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.comparator.SortUserLeagueByOverHeadPress;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.comparator.SortUserLeagueBySquat;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.DataModelResult;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.User;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserLeagueTableModelSingleton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;

public class LeagueTableResults extends AppCompatActivity {

    private TextView nameOfLeague;
    final UserLeagueTableModelSingleton userLeagueTableModelSingleton = UserLeagueTableModelSingleton.getInstance();
    DataModelResult<ArrayList<User>> usersCallback;

    private RecyclerView leagueTableRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private int filterId;
    private String leaguePin;
    private int mSortValue;
    private Boolean IsLeagueCreator;
    private int type = -1;
    DataModelResult<ArrayList<String>> leagueFlagsList;
    ArrayList<DataModelResult<ArrayList<String>>> flagCallbacks = new ArrayList<>();
    ArrayList<Integer> positionList = new ArrayList<>();

    private final static int BENCH_PRESS =1;
    private final static int SQUAT =2;
    private final static int DEADLIFT =3;
    private final static int OVER_HEAD_PRESS =4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_table_results);

        if(filterId > 0){
            Log.v("", "");
        }

        Intent intent = getIntent();
        IsLeagueCreator = intent.getBooleanExtra("IsLeagueCreator", false);
        leaguePin = intent.getStringExtra("LeaguePin"); // passed into function
        String leagueName = intent.getStringExtra("LeagueName");
        nameOfLeague = this.findViewById(R.id.league_table_results_heading_view_league_name);
        nameOfLeague.setText(leagueName);

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
                mSortValue = data.getIntExtra("result", -1);
                String resulet=data.getStringExtra("name");
                TextView tv = this.findViewById(R.id.league_table_results_main_view_sort_result);
                tv.setText(resulet);
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
        final String id = FirebaseAuth.getInstance().getUid();
        if(id != null){
            leagueTableRecyclerView = this.findViewById(R.id.league_table_results_main_view_recycler_view);
            layoutManager = new LinearLayoutManager(this);
            leagueTableRecyclerView.setLayoutManager(layoutManager);
            usersCallback = new DataModelResult<ArrayList<User>>() {
                @Override
                public void onComplete(final ArrayList<User> data, Exception exception) {
                    if(data != null){
                        // sort data by total

                        switch(mSortValue){
                            case UserLeagueTableModelSingleton.benchPress:
                                Log.v("", "");
                                Collections.sort(data, new SortUserLeagueByBench().reversed());

                                getLeaguePosition(data, BENCH_PRESS);

                                type = UserLeagueTableModelSingleton.benchPress;
                                break;
                            case UserLeagueTableModelSingleton.deadlift:
                                Log.v("", "");
                                Collections.sort(data, new SortUserLeagueByDeadlift().reversed());

                                getLeaguePosition(data, DEADLIFT);

                                type = UserLeagueTableModelSingleton.deadlift;
                                break;
                            case UserLeagueTableModelSingleton.squat:
                                Log.v("", "");
                                Collections.sort(data, new SortUserLeagueBySquat().reversed());

                                getLeaguePosition(data, SQUAT);

                                type = UserLeagueTableModelSingleton.squat;
                                break;
                            case UserLeagueTableModelSingleton.ohp:
                                Log.v("", "");
                                Collections.sort(data, new SortUserLeagueByOverHeadPress().reversed());

                                getLeaguePosition(data, OVER_HEAD_PRESS);

                                type = UserLeagueTableModelSingleton.ohp;
                                break;
                            default:
                                //Log.v("", ""); // sorts by all lifts added together
                                Collections.sort(data, new SortUserLeague().reversed());

                                break;
                        }

                        DataModelResult<ArrayList<String>> leagueFlags = new DataModelResult<ArrayList<String>>() {
                            @Override
                            public void onComplete(ArrayList<String> leagueFlags, Exception exception) {

                                mAdapter = new LeagueTableRecyclerViewAdapter(data, type, IsLeagueCreator, leagueFlags, leaguePin, positionList);
                                leagueTableRecyclerView.setAdapter(mAdapter);
                                Log.v("", "");
                            }
                        };

                        userLeagueTableModelSingleton.addFlagListener(leaguePin, leagueFlags);

                        /*leagueFlagsList = new DataModelResult<ArrayList<String>>() {
                            @Override
                            public void onComplete(ArrayList<String> leagueFlags, Exception exception) {
                                flagCallbacks.add(leagueFlagsList);
                                mAdapter = new LeagueTableRecyclerViewAdapter(data, type, IsLeagueCreator, leagueFlags, leaguePin);
                                leagueTableRecyclerView.setAdapter(mAdapter);
                                Log.v("", "");
                            }
                        };

                        userLeagueTableModelSingleton.addFlagListener(id,leaguePin ,leagueFlagsList);*/

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
        }
    }

    public void getLeaguePosition(ArrayList<User> data, int exerciseType){
        positionList.clear();
        Float mUserExercise = null;
        int pos = 0;
        Float compouneMove = null;

        for(User user : data){

            switch(exerciseType){
                case BENCH_PRESS: compouneMove = user.getBenchPress();
                break;
                case SQUAT: compouneMove = user.getSquat();
                break;
                case DEADLIFT: compouneMove = user.getDeadlift();
                break;
                case OVER_HEAD_PRESS: compouneMove = user.getOverHeadPress();
                break;
                default:compouneMove = null;
            }

            if(compouneMove != null){
                if(compouneMove.equals(mUserExercise)){
                    // the same league position
                    positionList.add(pos);
                }
                else{
                    pos++;
                    positionList.add(pos);
                }

                mUserExercise = compouneMove;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        String id = FirebaseAuth.getInstance().getUid();
        if(id != null){
            //remove listener
           /* flagCallbacks.size();
            for(DataModelResult<ArrayList<String>> flagListener : flagCallbacks){
                userLeagueTableModelSingleton.removeFlagListener(id, flagListener);
            }*/
            userLeagueTableModelSingleton.removeLeagueTableUserProfileListener(id, usersCallback);

        }
    }
}
