package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models;

import android.content.SharedPreferences;

import com.example.myandroidappandroidapp.gsanastrengthandsizeapp.MyApp;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static android.content.Context.MODE_PRIVATE;

public class sharedPreferenceProfile {

    private HashMap<String, String> profileInputMap = new HashMap<>();
    private static final String MY_PREFS_NAME = "MyPrefsFile";
    private static final String LIST_DATA = "MyListData";

    private static sharedPreferenceProfile ourInstance;

    private sharedPreferenceProfile() {

    }

    public static sharedPreferenceProfile getInstance() {

        if(ourInstance == null){
            ourInstance = new sharedPreferenceProfile();
        }

        return ourInstance;
    }

    public void saveToDisk(HashMap<String, String> profileInputMap){

        MyApp myApp = MyApp.getInstance();

        SharedPreferences sharedPreferences = myApp.getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(profileInputMap);
        editor.putString(LIST_DATA, json);
        editor.apply();

    }


   /* public HashMap<String, String> getSavedInputs(){
        // read shared preferences into LHM from your device
        MyApp myApp = MyApp.getInstance();

        SharedPreferences sharedPreferences = myApp.getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(LIST_DATA, null);

        String type = new Gson().fromJson(json, new TypeToken<LinkedHashMap<String, String>>(){}.getType());
        HashMap map = new HashMap<>();
        if(json == null){
            map = new HashMap();
        }
        else{

        }


    }*/



}
