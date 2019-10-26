package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.DataModelResult;
import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models.Filter;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity {

    private Spinner spinner;
    private SpinnerAdapter mAdapter;
    private Spinner customerSpinnerbench;
    private SpinnerAdapter customSpinnerAdapterBench;
    private ImageView spinnerDropDownBenchIcon;

    private Button customiseBtn;
    private Boolean isCustom = false;
    private ImageView spinnerDropDownIcon;
    private SwitchCompat switchCompat;
    private TextView tvStandard;
    private TextView tvCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        ArrayList<Filter> list = new ArrayList<>();
        list.add(null);
        list.add(new Filter(1, "Bench Press"));
        list.add(new Filter(2, "Squat"));
        list.add(new Filter(3, "Deadlift"));
        list.add(new Filter(4, "Over Head Press"));

        spinnerDropDownIcon = this.findViewById(R.id.filter_main_view_drop_down_arrow);
        spinner = this.findViewById(R.id.filter_main_view_drop_down);
        mAdapter = new CustomDropDownAdapter(this, list);
        spinner.setAdapter(mAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*int col = ContextCompat.getColor(parent.getContext(), R.color.ssWhite);
                TextView op = (TextView) parent.getChildAt(0);
                op.setTextColor(col);*/

                // disable the top item in list
                /*if (position == 0){
                    return;
                }*/

                Filter i = (Filter) parent.getItemAtPosition(position);


                //String user = String.valueOf(i.getId());
                if(i != null){
                    Toast.makeText(parent.getContext(), i.getName(), Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(parent.getContext(), "All", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<Filter> benchList = new ArrayList<>();
        benchList.add(null);
        benchList.add(new Filter(1, "Bench Press"));

        spinnerDropDownBenchIcon = this.findViewById(R.id.filter_main_view_drop_down_custom_bench_arrow);
        customerSpinnerbench = this.findViewById(R.id.filter_main_view_drop_down_custom_bench);
        customSpinnerAdapterBench = new CustomDropDownAdapter(this, benchList);
        customerSpinnerbench.setAdapter(customSpinnerAdapterBench);
        customerSpinnerbench.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Filter i = (Filter) parent.getItemAtPosition(position);
                if(i != null){
                    Toast.makeText(parent.getContext(), i.getName(), Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(parent.getContext(), "All", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


       /* spinner = this.findViewById(R.id.filter_main_view_drop_down);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sort_string_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int col = ContextCompat.getColor(parent.getContext(), R.color.ssWhite);
                TextView op = (TextView) parent.getChildAt(0);
                op.setTextColor(col);

                // disable the top item in list
                if (position == 0){
                    return;
                }

                String i = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), i, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        /*Button filterBtn = this.findViewById(R.id.filter_test_btn);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",999);
                returnIntent.putExtra("name", "Ronaldo");
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });*/

       /* customiseBtn = this.findViewById(R.id.filter_man_view_custom_btn);
        customiseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCustom = true;
                updateUI();
            }
        });*/

        switchCompat = this.findViewById(R.id.filter_main_view_switch);
        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI();
            }
        });

        tvStandard = this.findViewById(R.id.dashboard_map_toggle_switch_map);
        tvCustom = this.findViewById(R.id.dashboard_map_toggle_switch_list);

        updateUI();
    }

    public void updateUI(){

        if(switchCompat.isChecked()){
            tvStandard.setVisibility(View.INVISIBLE);
            tvCustom.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
            spinnerDropDownIcon.setVisibility(View.GONE);
            customerSpinnerbench.setVisibility(View.VISIBLE);
            spinnerDropDownBenchIcon.setVisibility(View.VISIBLE);
        }
        else {
            tvStandard.setVisibility(View.VISIBLE);
            tvCustom.setVisibility(View.INVISIBLE);
            spinner.setVisibility(View.VISIBLE);
            spinnerDropDownIcon.setVisibility(View.VISIBLE);

            customerSpinnerbench.setVisibility(View.GONE);
            spinnerDropDownBenchIcon.setVisibility(View.GONE);
        }
    }
}


class CustomDropDownAdapter extends ArrayAdapter<Filter>{

    public CustomDropDownAdapter(@NonNull Context context, ArrayList<Filter> values) {
        super(context, 0, values);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int i = R.layout.activity_dashboard;
        ViewGroup h = parent;
        return getViewLayout(position, convertView , R.layout.custom_spinner_item);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getViewLayout(position, convertView , R.layout.custom_spinner_item_two);
    }

    public View getViewLayout(int position, @Nullable View convertView, @LayoutRes int parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(parent, null);
        }

        bindData(getItem(position), convertView, position);

        return convertView;
    }

    public void bindData(Filter filter, View convertView, int position){

        TextView tv = convertView.findViewById(R.id.custom_spinner_text_view);
        if(filter != null){
            tv.setText(filter.getName());
        }
        else {
            tv.setText("All");
        }
    }
}
