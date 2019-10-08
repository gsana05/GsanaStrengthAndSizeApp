package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.R;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.User;

import java.util.ArrayList;

public class LeagueTableRecyclerViewAdapter extends RecyclerView.Adapter<LeagueTableRecyclerViewAdapter.MyLeagueViewHolder> {

    private ArrayList<User> leagueList;
    private TextView leagueDate;
    private TextView leaguePin;
    private TextView leagueName;

    public LeagueTableRecyclerViewAdapter(ArrayList<User> leagueList) {
        this.leagueList = leagueList;
    }

    @NonNull
    @Override
    public LeagueTableRecyclerViewAdapter.MyLeagueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyLeagueViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.league_table_list_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull LeagueTableRecyclerViewAdapter.MyLeagueViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return leagueList.size();
    }

    public class MyLeagueViewHolder extends RecyclerView.ViewHolder {
        public MyLeagueViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
