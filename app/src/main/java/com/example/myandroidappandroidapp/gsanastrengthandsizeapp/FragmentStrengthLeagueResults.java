package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
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
import android.widget.Toast;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.adapters.LeagueRecyclerViewAdapter;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.CreatedLeague;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.DataModelResult;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.LeagueModelSingleton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;


public class FragmentStrengthLeagueResults extends Fragment {

    private Button createLeague;
    private Button joinLeague;
    private EditText leagueName;
    private RecyclerView leagueRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DataModelResult<ArrayList<String>> callback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_fragment_strength_league_results, container, false);

        createLeague = view.findViewById(R.id.fragment_strength_league_results_create_league_btn);
        createLeague.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog();
            }
        });

        joinLeague = view.findViewById(R.id.fragment_strength_league_results_join_league_btn);
        joinLeague.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        leagueRecyclerView = view.findViewById(R.id.fragment_strength_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        leagueRecyclerView.setLayoutManager(layoutManager);


        final DataModelResult<ArrayList<CreatedLeague>> callba = new DataModelResult<ArrayList<CreatedLeague>>() {
            @Override
            public void onComplete(ArrayList<CreatedLeague> data, Exception exception) {
                if(data != null){
                    mAdapter = new LeagueRecyclerViewAdapter(data);
                    leagueRecyclerView.setAdapter(mAdapter);
                }
            }
        };

        callback = new DataModelResult<ArrayList<String>>() {
            @Override
            public void onComplete(ArrayList<String> data, Exception exception) {
                if(data != null && data.size() > 0){
                    LeagueModelSingleton leagueModelSingleton = LeagueModelSingleton.getInstance();
                    leagueModelSingleton.addAllLeagueListener(data, callba);

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
        }
    }

    public void alertDialog(){
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        View inflatedLayout = getLayoutInflater().inflate(R.layout.custom_dialog_create_league, null);
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
        createLeague.setOnClickListener(new View.OnClickListener() {
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

    public void dismissKeyboard(){
        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(leagueName.getWindowToken(), 0);
    }
}
