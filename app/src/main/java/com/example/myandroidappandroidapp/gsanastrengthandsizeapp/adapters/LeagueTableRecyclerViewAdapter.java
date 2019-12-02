package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.R;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.DataModelResult;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.LeagueModelSingleton;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.User;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserLeagueTableModelSingleton;

import java.util.ArrayList;
import java.util.Objects;

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
    private Boolean isLeagueCreator;

    public LeagueTableRecyclerViewAdapter(ArrayList<User> leagueList, int type, Boolean isLeagueCreator) {
        this.leagueList = leagueList;
        this.type = type;
        this.isLeagueCreator = isLeagueCreator;
    }

    @NonNull
    @Override
    public LeagueTableRecyclerViewAdapter.MyLeagueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyLeagueViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.league_table_list_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull LeagueTableRecyclerViewAdapter.MyLeagueViewHolder holder, int position) {
        final User user = leagueList.get(position);

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
        bench.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertVideoDialog(user.getProofBenchLink(), v);
            }
        });


        if(type == UserLeagueTableModelSingleton.deadlift){
            TextView tv = holder.itemView.findViewById(R.id.league_table_list_item_deadlift_label);
            tv.setTextColor(col);
            deadlift.setTextColor(col);
        }
        deadlift.setText(user.getDeadlift().toString());
        deadlift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertVideoDialog(user.getProofDeadliftLink(), v);
            }
        });


        if(type == UserLeagueTableModelSingleton.squat){
            TextView tv = holder.itemView.findViewById(R.id.league_table_list_item_squat_label);
            tv.setTextColor(col);
            squat.setTextColor(col);
        }
        squat.setText(user.getSquat().toString());
        squat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertVideoDialog(user.getProofSquatLink(), v);
            }
        });


        if(type == UserLeagueTableModelSingleton.ohp){
            TextView tv = holder.itemView.findViewById(R.id.league_table_list_item_ohp_label);
            tv.setTextColor(col);
            ohp.setTextColor(col);
        }
        ohp.setText(user.getOverHeadPress().toString());
        ohp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertVideoDialog(user.getProofOhpLink(), v);
            }
        });

        if(type == -1){
            TextView tv = holder.itemView.findViewById(R.id.league_table_list_item_total_label);
            tv.setTextColor(col);
            total.setTextColor(col);
        }
        Float totalScore = user.getBenchPress() + user.getDeadlift() + user.getSquat() + user.getOverHeadPress();
        total.setText(totalScore.toString());

        ImageView flag = holder.itemView.findViewById(R.id.league_table_list_item_league_flag);
        flag.setVisibility(View.GONE);

        // only do this if user created the league
        if(isLeagueCreator){

            flag.setVisibility(View.VISIBLE);
            flag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DataModelResult<Boolean> addFlag = new DataModelResult<Boolean>() {
                        @Override
                        public void onComplete(Boolean data, Exception exception) {
                            if(data){
                                Log.v("", ""); // flagged success
                            }
                            else{
                                Log.v("", "");
                            }
                        }
                    };

                    LeagueModelSingleton leagueModelSingleton = LeagueModelSingleton.getInstance();
                    leagueModelSingleton.addFlagToLeague(user.getPin(), addFlag);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("League Creator")
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
            });
        }
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

    public void alertVideoDialog(String link, View v){
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(v.getContext());
        LayoutInflater getLayoutInflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View inflatedLayout = getLayoutInflater.inflate(R.layout.custom_bench_video, null);
        final VideoView video = inflatedLayout.findViewById(R.id.custom_bench_video_proof);
        MediaController mediaController = new MediaController(v.getContext());

        if(link != null){
            Uri myUri = Uri.parse(link);

            video.setVideoURI(myUri);
            video.setMediaController(mediaController);
            mediaController.setAnchorView(video);
            //video.start();

            builder.setView(inflatedLayout);
            builder.setCancelable(false);
            final AlertDialog alert = builder.create();
            alert.show();

            /*Button restart;
            restart = inflatedLayout.findViewById(R.id.custom_bench_video_pause);
            restart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    video.seekTo(0);
                    video.start();
                }
            });*/

            Button pause;
            pause = inflatedLayout.findViewById(R.id.custom_bench_video_pause);
            pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    video.pause();
                }
            });


            Button play;
            play = inflatedLayout.findViewById(R.id.custom_bench_video_play);
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    video.start();
                }
            });

            Button cancel;
            cancel = inflatedLayout.findViewById(R.id.custom_bench_video_cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });
        }
        else{
            builder.setMessage("No video found")
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

    }
}
