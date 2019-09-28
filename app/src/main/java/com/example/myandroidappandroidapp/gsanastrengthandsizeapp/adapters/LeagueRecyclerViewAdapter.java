package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.R;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.CreatedLeague;


import java.util.ArrayList;

public class LeagueRecyclerViewAdapter extends RecyclerView.Adapter<LeagueRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<CreatedLeague> createdLeagues;
    private TextView leagueDate;
    private TextView leaguePin;
    private TextView leagueName;

    public LeagueRecyclerViewAdapter(ArrayList<CreatedLeague> createdLeagues) {
        this.createdLeagues = createdLeagues;
    }

    @NonNull
    @Override
    public LeagueRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.league_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LeagueRecyclerViewAdapter.MyViewHolder holder, int position) {
        CreatedLeague createdLeague = createdLeagues.get(position);

        if(createdLeague.getLeagueStartDate() != null){
            //leagueDate.setText(createdLeague.getLeagueStartDate().toString());
        }
        else {
            //leagueDate.setText("No Data");
        }

        leaguePin.setText(createdLeague.getLeaguePin());
        leagueName.setText(createdLeague.getLeagueName());
    }

    @Override
    public int getItemCount() {
        return createdLeagues.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            leagueDate = itemView.findViewById(R.id.league_list_item_date_name);
            leaguePin = itemView.findViewById(R.id.league_list_item_pin_name);
            leagueName = itemView.findViewById(R.id.league_list_item_league_name);

        }
    }
}
