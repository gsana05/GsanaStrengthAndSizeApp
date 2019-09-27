package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.R;


import java.util.ArrayList;
import java.util.HashMap;

public class LeagueRecyclerViewAdapter extends RecyclerView.Adapter<LeagueRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<String> createdLeagues;
    private TextView leagueName;

    public LeagueRecyclerViewAdapter(ArrayList<String> createdLeagues) {
        this.createdLeagues = createdLeagues;
    }

    @NonNull
    @Override
    public LeagueRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.league_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LeagueRecyclerViewAdapter.MyViewHolder holder, int position) {

        String leaguePin = createdLeagues.get(position);


      leagueName.setText(leaguePin);
    }

    @Override
    public int getItemCount() {
        return createdLeagues.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            leagueName = itemView.findViewById(R.id.league_list_item_league_name);

        }
    }
}
