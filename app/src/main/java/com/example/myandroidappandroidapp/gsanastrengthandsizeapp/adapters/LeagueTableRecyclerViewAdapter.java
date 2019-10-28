package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.R;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.User;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserLeagueTableModelSingleton;

import java.util.ArrayList;

public class LeagueTableRecyclerViewAdapter extends RecyclerView.Adapter<LeagueTableRecyclerViewAdapter.MyLeagueViewHolder> {

    private ArrayList<User> leagueList;
    private int type;
    private TextView gymName;
    private TextView leaguePos;
    private TextView bench;
    private TextView deadlift;
    private TextView squat;
    private TextView ohp;
    private TextView total;

    public LeagueTableRecyclerViewAdapter(ArrayList<User> leagueList, int type) {
        this.leagueList = leagueList;
        this.type = type;
    }

    @NonNull
    @Override
    public LeagueTableRecyclerViewAdapter.MyLeagueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyLeagueViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.league_table_list_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull LeagueTableRecyclerViewAdapter.MyLeagueViewHolder holder, int position) {
        User user = leagueList.get(position);

        int pos = position + 1;

        gymName.setText(user.getGymName());
        leaguePos.setText(Integer.toString(pos));

        int col = ContextCompat.getColor(holder.itemView.getContext(), R.color.ssRed);

        if(type == UserLeagueTableModelSingleton.benchPress){
            TextView tv = holder.itemView.findViewById(R.id.league_table_list_item_bench_label);
            tv.setTextColor(col);
            bench.setTextColor(col);
        }
        bench.setText(user.getBenchPress().toString());


        if(type == UserLeagueTableModelSingleton.deadlift){
            TextView tv = holder.itemView.findViewById(R.id.league_table_list_item_deadlift_label);
            tv.setTextColor(col);
            deadlift.setTextColor(col);
        }
        deadlift.setText(user.getDeadlift().toString());


        if(type == UserLeagueTableModelSingleton.squat){
            TextView tv = holder.itemView.findViewById(R.id.league_table_list_item_squat_label);
            tv.setTextColor(col);
            squat.setTextColor(col);
        }
        squat.setText(user.getSquat().toString());


        if(type == UserLeagueTableModelSingleton.ohp){
            TextView tv = holder.itemView.findViewById(R.id.league_table_list_item_ohp_label);
            tv.setTextColor(col);
            ohp.setTextColor(col);
        }
        ohp.setText(user.getOverHeadPress().toString());

        if(type == -1){
            TextView tv = holder.itemView.findViewById(R.id.league_table_list_item_total_label);
            tv.setTextColor(col);
            total.setTextColor(col);
        }
        Float totalScore = user.getBenchPress() + user.getDeadlift() + user.getSquat() + user.getOverHeadPress();
        total.setText(totalScore.toString());
    }

    @Override
    public int getItemCount() {
        return leagueList.size();
    }

    public class MyLeagueViewHolder extends RecyclerView.ViewHolder {
        public MyLeagueViewHolder(@NonNull View itemView) {
            super(itemView);

            gymName = itemView.findViewById(R.id.league_table_list_item_gym_name);
            leaguePos = itemView.findViewById(R.id.league_table_list_item_league_pos);
            bench = itemView.findViewById(R.id.league_table_list_item_bench_result);
            deadlift = itemView.findViewById(R.id.league_table_list_item_deadlift_result);
            squat = itemView.findViewById(R.id.league_table_list_item_squat_result);
            ohp = itemView.findViewById(R.id.league_table_list_item_ohp_result);
            total = itemView.findViewById(R.id.league_table_list_item_total_result);

        }
    }
}
