package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.CreatedLeague;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.DataModelResult;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.LeagueModelSingleton;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityLeaveLeague extends AppCompatActivity {

    final LeagueModelSingleton leagueModelSingleton = LeagueModelSingleton.getInstance();
    Boolean isLeaving = false;
    ProgressBar progressBar;
    Button leave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_league);
        Intent intent = getIntent();
        String leagueName = intent.getStringExtra("LeagueName");
        final String leaguePin = intent.getStringExtra("LeaguePin");


        TextView name = this.findViewById(R.id.leave_league_name);
        name.setText(leagueName);

        progressBar = this.findViewById(R.id.leave_league_button_progress);

        Button cancel = this.findViewById(R.id.leave_league_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        leave = this.findViewById(R.id.leave_league_button);
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                isLeaving = true;
                updateUI();

                final String userId = FirebaseAuth.getInstance().getUid();
                if(userId != null){
                    final DataModelResult<Boolean> callbackLeaveLeague = new DataModelResult<Boolean>() {
                        @Override
                        public void onComplete(Boolean data, Exception exception) {

                            if(exception != null){
                                Log.v("", "");
                            }

                            if(data){
                                Log.v("", "");
                                AlertDialog.Builder builder;
                                builder = new AlertDialog.Builder(v.getContext());
                                builder.setMessage("Success")
                                        .setCancelable(false)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                isLeaving = false;
                                                dialog.dismiss();
                                                finish();
                                            }
                                        });
                                //Creating dialog box
                                AlertDialog alert = builder.create();
                                //Setting the title manually
                                alert.show();
                            }
                            else{
                                Log.v("", "");
                                AlertDialog.Builder builder;
                                builder = new AlertDialog.Builder(v.getContext());
                                builder.setMessage("You can not delete a league you created until you are the only user left in the league")
                                        .setCancelable(false)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                isLeaving = false;
                                                updateUI();
                                                dialog.dismiss();
                                            }
                                        });
                                //Creating dialog box
                                AlertDialog alert = builder.create();
                                //Setting the title manually
                                alert.show();
                            }

                        }
                    };

                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Are you sure you want to leave this league")
                            .setCancelable(false)
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    leagueModelSingleton.leaveLeague(userId, leaguePin, callbackLeaveLeague);
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.show();
                }
            }
        });

        updateUI();
    }

    public void updateUI(){
        if(isLeaving){
            progressBar.setVisibility(View.VISIBLE);
            leave.setVisibility(View.INVISIBLE);
        }
        else{
            progressBar.setVisibility(View.INVISIBLE);
            leave.setVisibility(View.VISIBLE);
        }
    }
}
