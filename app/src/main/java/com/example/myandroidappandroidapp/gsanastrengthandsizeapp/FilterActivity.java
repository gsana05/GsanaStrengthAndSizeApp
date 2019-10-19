package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

/*// custom spinner that takes null values
class CustomDropDownAdapter<T>(context: Context,listItemsTxt: List<T?>,  var bindView: (position: Int, item: T?, view: View) -> Unit) : ArrayAdapter<T>(context, -1, listItemsTxt) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getViewLayout(position, convertView , R.layout.custom_spinner_view)
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getViewLayout(position, convertView , R.layout.custom_spinner_view_selected)
        }

private fun getViewLayout(position: Int, convertView: View?, @LayoutRes layout : Int) : View{
        val view = convertView ?: LayoutInflater.from(context).inflate(layout, null)
        bindView(position, getItem(position), view)
        return view
        }
        }*/
