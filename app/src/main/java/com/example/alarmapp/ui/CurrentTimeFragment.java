package com.example.alarmapp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alarmapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class CurrentTimeFragment extends Fragment {

    String mDate, mTime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    TextView txtDate, txtTime;
    public CurrentTimeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_current_time, container, false);

        return v;
    }
}