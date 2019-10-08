package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.ActivityLogIn;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.LeagueTableResults;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.R;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.CreatedLeague;


import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LeagueRecyclerViewAdapter extends RecyclerView.Adapter<LeagueRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<CreatedLeague> createdLeagues;
    private Context context;
    private TextView leagueDate;
    private TextView leaguePin;
    private TextView leagueName;

    public LeagueRecyclerViewAdapter(ArrayList<CreatedLeague> createdLeagues, Context context) {
        this.createdLeagues = createdLeagues;
        this.context = context;
    }

    @NonNull
    @Override
    public LeagueRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.league_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final LeagueRecyclerViewAdapter.MyViewHolder holder, int position) {
        final CreatedLeague createdLeague = createdLeagues.get(position);

        Long i = createdLeague.getLeagueStartDate();
        Date d = new Date(i);
        String kok =  DateFormat.getDateInstance(DateFormat.MEDIUM).format(d);

        if(createdLeague.getLeagueStartDate() != null){
            leagueDate.setText(kok);
        }
        else {
            leagueDate.setText("No Data");
        }

        leaguePin.setText(createdLeague.getLeaguePin());
        leagueName.setText(createdLeague.getLeagueName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (v.getContext(), LeagueTableResults.class);
                intent.putExtra("LeaguePin", createdLeague.getLeaguePin());
                intent.putExtra("LeagueName", createdLeague.getLeagueName());
                v.getContext().startActivity(intent);

              /*  AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(createdLeague.getLeaguePin())
                        .setCancelable(false)
                        .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(holder.itemView.getContext(), ActivityLogIn.class);
                                intent.putExtra("LeaguePin", createdLeague.getLeaguePin());
                                holder.itemView.getContext().startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.show();*/
            }
        });
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
