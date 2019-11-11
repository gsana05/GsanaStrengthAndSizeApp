package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.adapters.LeagueRecyclerViewAdapter;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.CreatedLeague;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.DataModelResult;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.LeagueModelSingleton;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;


public class FragmentStrengthLeagues extends Fragment {

    private EditText leagueName;
    private RecyclerView leagueRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private DataModelResult<ArrayList<String>> callback;
    private DataModelResult<ArrayList<CreatedLeague>> callbackCreatedLeagues;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_fragment_strength_league_results, container, false);

        Button createLeague = view.findViewById(R.id.fragment_strength_league_results_create_league_btn);
        createLeague.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createLeague();
            }
        });

        Button joinLeague = view.findViewById(R.id.fragment_strength_league_results_join_league_btn);
        joinLeague.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinLeague();
            }
        });

        leagueRecyclerView = view.findViewById(R.id.fragment_strength_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        leagueRecyclerView.setLayoutManager(layoutManager);

        final TextView tv = view.findViewById(R.id.fragment_strength_recycler_view_no_league);

        callbackCreatedLeagues = new DataModelResult<ArrayList<CreatedLeague>>() {
            @Override
            public void onComplete(ArrayList<CreatedLeague> data, Exception exception) {
                if(data != null){
                    if(data.size() >= 1){
                        tv.setVisibility(View.GONE);
                        mAdapter = new LeagueRecyclerViewAdapter(data, getContext(), false);
                        leagueRecyclerView.setAdapter(mAdapter);
                    }
                    else{
                        tv.setVisibility(View.VISIBLE);
                        leagueRecyclerView.setAdapter(null);
                    }
                }
            }
        };

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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        final String userId = FirebaseAuth.getInstance().getUid();
        if(userId != null){
            LeagueModelSingleton leagueModelSingleton = LeagueModelSingleton.getInstance();
            leagueModelSingleton.addLeagueListener(userId, callback);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        final String userId = FirebaseAuth.getInstance().getUid();
        if(userId != null){
            LeagueModelSingleton leagueModelSingleton = LeagueModelSingleton.getInstance();
            leagueModelSingleton.removeLeagueListener(userId, callback);
            leagueModelSingleton.removeAllLeagueListener(userId, callbackCreatedLeagues);
        }
        else {
            leagueRecyclerView.setAdapter(null); // clears recycler view so next user doe snot see previous users data
        }
    }

    private void joinLeague(){
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        View inflatedLayout = getLayoutInflater().inflate(R.layout.custom_dialog_create_league, null);
        TextView header = inflatedLayout.findViewById(R.id.custom_dialog_create_league_team_label);
        header.setText("Please enter league pin");


        builder.setView(inflatedLayout);
        builder.setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.show();

        Button cancel;
        cancel = inflatedLayout.findViewById(R.id.custom_dialog_create_league_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        leagueName = inflatedLayout.findViewById(R.id.custom_dialog_create_league_team_input);

        Button enterLeague = inflatedLayout.findViewById(R.id.custom_dialog_create_league_create);
        enterLeague.setText("Join League");
        enterLeague.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(leagueName.getText().toString().isEmpty()){
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                    builder.setMessage("Please enter a league name")
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
                    return;
                }

                Log.v("", "");

                DataModelResult<Boolean> callback = new DataModelResult<Boolean>() {
                    @Override
                    public void onComplete(Boolean data, Exception exception) {

                        if(data){
                            Toast.makeText(getActivity(),"Joined League Successfully",Toast.LENGTH_SHORT).show();
                            dismissKeyboard();
                            alert.dismiss();
                        }
                        else {
                            //Toast.makeText(getActivity(),"Not Added",Toast.LENGTH_SHORT).show();
                            alertDialog("Already part of this league or invalid pin");
                            dismissKeyboard();
                            alert.dismiss();
                        }

                    }
                };

                LeagueModelSingleton leagueModelSingleton = LeagueModelSingleton.getInstance();
                leagueModelSingleton.addToLeague(leagueName.getText().toString(), callback);
            }
        });
    }



    private void createLeague(){
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        View inflatedLayout = getLayoutInflater().inflate(R.layout.custom_dialog_create_league, null);

        TextView header = inflatedLayout.findViewById(R.id.custom_dialog_create_league_team_label);
        header.setText("Please enter league name");

        builder.setView(inflatedLayout);
        builder.setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.show();

        Button cancel;
        cancel = inflatedLayout.findViewById(R.id.custom_dialog_create_league_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        leagueName = inflatedLayout.findViewById(R.id.custom_dialog_create_league_team_input);

        Button createLeague = inflatedLayout.findViewById(R.id.custom_dialog_create_league_create);
        createLeague.setText("Create League");
        createLeague.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(leagueName.getText().toString().isEmpty()){
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                    builder.setMessage("Please enter league pin")
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
                    return;
                }

                Log.v("", "");

                DataModelResult<Boolean> callback = new DataModelResult<Boolean>() {
                    @Override
                    public void onComplete(Boolean data, Exception exception) {

                        if(data){
                            Toast.makeText(getActivity(),"Data Saved",Toast.LENGTH_SHORT).show();
                            dismissKeyboard();
                            alert.dismiss();
                        }
                        else {
                            Toast.makeText(getActivity(),"Data NOT Saved",Toast.LENGTH_SHORT).show();
                            dismissKeyboard();
                            alert.dismiss();
                        }

                    }
                };

                LeagueModelSingleton leagueModelSingleton = LeagueModelSingleton.getInstance();
                leagueModelSingleton.setLeagueTable(leagueName.getText().toString(), callback);
            }
        });
    }

    private void alertDialog(String response){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getActivity());
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

    private void dismissKeyboard(){
        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(leagueName.getWindowToken(), 0);
    }
}
