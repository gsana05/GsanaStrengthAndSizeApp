package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SettingsLeagueRecyclerView extends RecyclerView.Adapter<SettingsLeagueRecyclerView.MyLeagueListViewHolder>  {

    @NonNull
    @Override
    public MyLeagueListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyLeagueListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyLeagueListViewHolder extends RecyclerView.ViewHolder {
        public MyLeagueListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
