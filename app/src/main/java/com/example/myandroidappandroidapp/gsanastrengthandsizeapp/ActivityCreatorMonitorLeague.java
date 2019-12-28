package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.DataModelResult;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.LeagueModelSingleton;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityCreatorMonitorLeague extends AppCompatActivity {

    final LeagueModelSingleton leagueModelSingleton = LeagueModelSingleton.getInstance();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator_monitor_league);

        Intent intent = getIntent();
        String gymName = intent.getStringExtra("gymName");
        float benchPress = intent.getFloatExtra("benchPress", 0f); // passed into function
        float squat = intent.getFloatExtra("squat", 0f);
        float deadlift = intent.getFloatExtra("deadlift", 0f); // passed into function
        float ohp = intent.getFloatExtra("ohp", 0f);
        final String userPin = intent.getStringExtra("userPin");
        final String leaguePin = intent.getStringExtra("leaguePin");
        final Boolean isFlagged = intent.getBooleanExtra("flag", false);

        final TextView nameTv = this.findViewById(R.id.custom_league_creator_edit_name);
        nameTv.setText(gymName);

        final EditText benchEt = this.findViewById(R.id.custom_league_creator_edit_bench_input);
        benchEt.setText(Float.toString(benchPress));
        benchEt.setEnabled(false);

        final EditText deadliftEt = this.findViewById(R.id.custom_league_creator_edit_deadlift_input);
        deadliftEt.setText(Float.toString(deadlift));
        deadliftEt.setEnabled(false);

        final EditText squatEt = this.findViewById(R.id.custom_league_creator_edit_squat_input);
        squatEt.setText(Float.toString(squat));
        squatEt.setEnabled(false);

        final EditText ohpEt = this.findViewById(R.id.custom_league_creator_edit_ohp_input);
        ohpEt.setText(Float.toString(ohp));
        ohpEt.setEnabled(false);

        Button flag = this.findViewById(R.id.custom_league_creator_edit_btn_flag);
        if(isFlagged){
            flag.setText("Remove flag");
            flag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    DataModelResult<Boolean> removeFlag = new DataModelResult<Boolean>() {
                        @Override
                        public void onComplete(Boolean data, Exception exception) {
                            if(data){
                                alertDialog("User flag has been removed", v);
                            }
                            else{
                                alertDialog("Unable to remove flag", v);
                            }

                        }
                    };

                    leagueModelSingleton.deleteFlagToLeague(leaguePin,userPin,removeFlag);
                }
            });
        }
        else{
            flag.setText("Flag");
            flag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    DataModelResult<Boolean> addFlag = new DataModelResult<Boolean>() {
                        @Override
                        public void onComplete(Boolean data, Exception exception) {
                            if(data){
                                Log.v("", ""); // flagged success
                                alertDialog("User has been flagged", v);
                            }
                            else{
                                alertDialog("User unable to be flagged", v);
                            }
                        }
                    };

                    leagueModelSingleton.addFlagToLeague(leaguePin,userPin ,addFlag); //todo push notification
                }
            });
        }




        Button removeUserBtn = this.findViewById(R.id.custom_league_creator_edit_btn_remove);
        removeUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if(userPin.equals(FirebaseAuth.getInstance().getUid())){
                    alertDialog("You can not remove yourself as you are the league creator", v);
                }
                else{
                    // remove user from this league
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
                                                finish();
                                            }
                                        });
                                //Creating dialog box
                                AlertDialog alert = builder.create();
                                //Setting the title manually
                                alert.show();
                            }

                        }
                    };

                    // league creator removing a user from the league
                    leagueModelSingleton.leaveLeague(userPin, leaguePin,callbackLeaveLeague);
                }
            }
        });

        Button cancel = this.findViewById(R.id.custom_league_creator_edit_btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void alertDialog(String comment, final View v){
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage(comment)
                .setCancelable(false)
                .setPositiveButton("close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        finish();
                        //((LeagueTableResults)v.getContext()).finish();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.show();
    }

}
