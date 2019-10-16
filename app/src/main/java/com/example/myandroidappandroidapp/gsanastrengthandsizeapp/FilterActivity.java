package com.example.myandroidappandroidapp.gsanastrengthandsizeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.protobuf.Any;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity {

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        ArrayList<String> list = new ArrayList<>();
        list.add("hey");
        list.add("hi");


        spinner = this.findViewById(R.id.filter_main_view_drop_down);
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
        });

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

class CustomDropDownAdapter{
    public CustomDropDownAdapter(Context context, List<Any> list) {
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
