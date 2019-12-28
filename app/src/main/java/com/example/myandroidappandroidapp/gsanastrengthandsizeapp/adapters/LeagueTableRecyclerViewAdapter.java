package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.ActivityCreatorMonitorLeague;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.ActivityProfile;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.LeagueTableResults;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.R;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.DataModelResult;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.LeagueModelSingleton;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.User;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.UserLeagueTableModelSingleton;
import com.google.firebase.auth.FirebaseAuth;

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
    private Boolean isLeagueCreator;
    private ArrayList<String> flags;
    private String leaguePin;
    private ArrayList<Integer> leaguePosition;
    private ProgressBar benchProofBtnProgress;
    private Button benchProofBtn;
    private ProgressBar deadliftProofBtnProgress;
    private Button deadliftProofBtn;
    private ProgressBar squatProofBtnProgress;
    private Button squatProofBtn;
    private ProgressBar ohpProofBtnProgress;
    private Button ohpProofBtn;

    public LeagueTableRecyclerViewAdapter(ArrayList<User> leagueList, int type, Boolean isLeagueCreator, ArrayList<String> flags, String leaguePin, ArrayList<Integer> leaguePosition) {
        this.leagueList = leagueList;
        this.type = type;
        this.isLeagueCreator = isLeagueCreator;
        this.flags = flags;
        this.leaguePin = leaguePin;
        this.leaguePosition =leaguePosition;
    }

    @NonNull
    @Override
    public LeagueTableRecyclerViewAdapter.MyLeagueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyLeagueViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.league_table_list_item, parent, false));

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final LeagueTableRecyclerViewAdapter.MyLeagueViewHolder holder, int position) {
        final LeagueModelSingleton leagueModelSingleton = LeagueModelSingleton.getInstance();

        final User user = leagueList.get(position);

        int pos = position + 1;

        gymName.setText(user.getGymName());
        if(leaguePosition.size() > 0){
            leaguePos.setText(Integer.toString(leaguePosition.get(position)));
        }
        else{
            leaguePos.setText(Integer.toString(pos));
        }


        int col = ContextCompat.getColor(holder.itemView.getContext(), R.color.ssGreen);

        if(type == UserLeagueTableModelSingleton.benchPress){
            TextView tv = holder.itemView.findViewById(R.id.league_table_list_item_bench_label);
            tv.setTextColor(col);
            bench.setTextColor(col);
        }
        bench.setText(user.getBenchPress().toString());
        benchProofBtn = holder.itemView.findViewById(R.id.league_table_list_item_bench_proof_btn);
        benchProofBtnProgress = holder.itemView.findViewById(R.id.league_table_list_item_bench_proof_btn_progress);
        benchProofBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                benchProofBtn.setVisibility(View.INVISIBLE);
                benchProofBtnProgress.setVisibility(View.VISIBLE);
                alertVideoDialog(user.getProofBenchLink(), v, UserLeagueTableModelSingleton.benchPress);
            }
        });

        if(type == UserLeagueTableModelSingleton.deadlift){
            TextView tv = holder.itemView.findViewById(R.id.league_table_list_item_deadlift_label);
            tv.setTextColor(col);
            deadlift.setTextColor(col);
        }
        deadlift.setText(user.getDeadlift().toString());

        deadliftProofBtnProgress = holder.itemView.findViewById(R.id.league_table_list_item_deadlift_proof_btn_progress);
        deadliftProofBtn = holder.itemView.findViewById(R.id.league_table_list_item_deadlift_proof_btn);
        deadliftProofBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deadliftProofBtn.setVisibility(View.INVISIBLE);
                deadliftProofBtnProgress.setVisibility(View.VISIBLE);
                alertVideoDialog(user.getProofDeadliftLink(), v, UserLeagueTableModelSingleton.deadlift);
            }
        });


        if(type == UserLeagueTableModelSingleton.squat){
            TextView tv = holder.itemView.findViewById(R.id.league_table_list_item_squat_label);
            tv.setTextColor(col);
            squat.setTextColor(col);
        }
        squat.setText(user.getSquat().toString());
        squatProofBtnProgress = holder.itemView.findViewById(R.id.league_table_list_item_squat_proof_btn_progress);
        squatProofBtn = holder.itemView.findViewById(R.id.league_table_list_item_squat_proof_btn);
        squatProofBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                squatProofBtn.setVisibility(View.INVISIBLE);
                squatProofBtnProgress.setVisibility(View.VISIBLE);
                alertVideoDialog(user.getProofSquatLink(), v, UserLeagueTableModelSingleton.squat);
            }
        });


        if(type == UserLeagueTableModelSingleton.ohp){
            TextView tv = holder.itemView.findViewById(R.id.league_table_list_item_ohp_label);
            tv.setTextColor(col);
            ohp.setTextColor(col);
        }
        ohp.setText(user.getOverHeadPress().toString());

        ohpProofBtnProgress = holder.itemView.findViewById(R.id.league_table_list_item_ohp_proof_btn_progress);
        ohpProofBtn = holder.itemView.findViewById(R.id.league_table_list_item_ohp_proof_btn);
        ohpProofBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ohpProofBtn.setVisibility(View.INVISIBLE);
                ohpProofBtnProgress.setVisibility(View.VISIBLE);
                alertVideoDialog(user.getProofOhpLink(), v, UserLeagueTableModelSingleton.ohp);
            }
        });

        if(type == -1 || type == UserLeagueTableModelSingleton.totalAllLifts){
            TextView tv = holder.itemView.findViewById(R.id.league_table_list_item_total_label);
            tv.setTextColor(col);
            total.setTextColor(col);
        }
        float totalScore = user.getBenchPress() + user.getDeadlift() + user.getSquat() + user.getOverHeadPress();
        total.setText(Float.toString(totalScore));

        ImageView unflag = holder.itemView.findViewById(R.id.league_table_list_item_league_unflag);
        unflag.setVisibility(View.GONE);
        ImageView flag = holder.itemView.findViewById(R.id.league_table_list_item_league_flag);
        flag.setVisibility(View.GONE);


        // get the league created and flagged users in this league
        if(flags != null && flags.contains(user.getPin())){
            ConstraintLayout layout = holder.itemView.findViewById(R.id.league_table_list_item_constraint);
            Drawable myIcon = holder.itemView.getContext().getApplicationContext().getResources().getDrawable(R.drawable.red_border);
            layout.setBackground(myIcon);
        }

        // only do this if user created the league
        if(isLeagueCreator){

            if(flags != null && flags.contains(user.getPin())){
                unflag.setVisibility(View.VISIBLE);
                unflag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {

                        Intent intent = new Intent (v.getContext(), ActivityCreatorMonitorLeague.class);
                        intent.putExtra("gymName", user.getGymName());
                        intent.putExtra("benchPress", user.getBenchPress());
                        intent.putExtra("squat", user.getSquat());
                        intent.putExtra("deadlift", user.getDeadlift());
                        intent.putExtra("ohp", user.getOverHeadPress());
                        intent.putExtra("userPin", user.getPin());
                        intent.putExtra("leaguePin", leaguePin);
                        intent.putExtra("flag", true);
                        v.getContext().startActivity(intent);

                     /*   DataModelResult<Boolean> removeFlag = new DataModelResult<Boolean>() {
                            @Override
                            public void onComplete(Boolean data, Exception exception) {
                                if(data){
                                    alertDialog("User flag has been removed", v);
                                }
                                else{
                                    alertDialog("Unable to remove flag", v);
                                }

                            }
                        };

                        leagueModelSingleton.deleteFlagToLeague(leaguePin,user.getPin() ,removeFlag);*/

                    }
                });

            }
            else{
                flag.setVisibility(View.VISIBLE);
                flag.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(final View v) {

                        Intent intent = new Intent (v.getContext(), ActivityCreatorMonitorLeague.class);
                        intent.putExtra("gymName", user.getGymName());
                        intent.putExtra("benchPress", user.getBenchPress());
                        intent.putExtra("squat", user.getSquat());
                        intent.putExtra("deadlift", user.getDeadlift());
                        intent.putExtra("ohp", user.getOverHeadPress());
                        intent.putExtra("userPin", user.getPin());
                        intent.putExtra("leaguePin", leaguePin);
                        v.getContext().startActivity(intent);
                    }
                });
            }



           /* holder.itemView.setOnClickListener(new View.OnClickListener() {
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
            });*/
        }
    }

    @Override
    public int getItemCount() {
        return leagueList.size();
    }

    class MyLeagueViewHolder extends RecyclerView.ViewHolder {
        MyLeagueViewHolder(@NonNull View itemView) {
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

    private void alertDialog(String comment, final View v){
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage(comment)
                .setCancelable(false)
                .setPositiveButton("close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        //((LeagueTableResults)v.getContext()).finish();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.show();
    }

    private void alertVideoDialog(String link, View v, int videoType){
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(v.getContext());
        LayoutInflater getLayoutInflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("InflateParams") View inflatedLayout = getLayoutInflater.inflate(R.layout.custom_bench_video, null);
        final VideoView video = inflatedLayout.findViewById(R.id.custom_bench_video_proof);
        //MediaController mediaController = new MediaController(v.getContext());

        if(link != null){

            Uri myUri = Uri.parse(link);

            video.setVideoURI(myUri);
            //video.setMediaController(mediaController);
            //mediaController.setAnchorView(video);
            video.start();

            builder.setView(inflatedLayout);
            builder.setCancelable(false);
            final AlertDialog alert = builder.create();
            alert.show();

            benchProofBtn.setVisibility(View.VISIBLE);
            benchProofBtnProgress.setVisibility(View.INVISIBLE);

            deadliftProofBtn.setVisibility(View.VISIBLE);
            deadliftProofBtnProgress.setVisibility(View.INVISIBLE);

            squatProofBtn.setVisibility(View.VISIBLE);
            squatProofBtnProgress.setVisibility(View.INVISIBLE);

            ohpProofBtn.setVisibility(View.VISIBLE);
            ohpProofBtnProgress.setVisibility(View.INVISIBLE);

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
