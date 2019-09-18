package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;


public class FragmentStrengthLeagueResults extends Fragment {

    private Button createLeague;

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


        return view;
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
    }

    public void createLeagueDialog(){

    }
}
