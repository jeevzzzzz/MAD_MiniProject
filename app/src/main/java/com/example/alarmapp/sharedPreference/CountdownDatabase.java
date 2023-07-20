package com.example.alarmapp.sharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CountdownDatabase {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String nameDataBase;


    public CountdownDatabase(String nameDataBase, Context context) {
        this.sharedPreferences = context.getSharedPreferences(nameDataBase, context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.nameDataBase = nameDataBase;
    }

    public List<String> getDataBase() {
        String savedDataString =  sharedPreferences.getString(nameDataBase, "");

        List<String> dataList =  new ArrayList<>(Arrays.asList(savedDataString.split(";")));

        if(dataList.get(0) == "") {
            return new ArrayList<>();
        }

        for (String data : dataList) {
            Log.d("get database", data);
        }

        return dataList;
    }

    public void setDataBase(List<String> dataList) {
        for (String data : dataList) {
            Log.d("put database", data);
        }
        this.editor.putString(nameDataBase, TextUtils.join(";", dataList));
        this.editor.apply();
    }

}

