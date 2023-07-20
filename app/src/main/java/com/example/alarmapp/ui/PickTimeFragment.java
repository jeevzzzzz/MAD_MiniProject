package com.example.alarmapp.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.example.alarmapp.R;
import com.example.alarmapp.adapter.CountdownAdapter;
import com.example.alarmapp.myinterface.CountdownRecycleViewClickInterface;
import com.example.alarmapp.ui.huan.CountdownActivity;
import com.example.alarmapp.ui.huan.TimeInputDialogFragment;


public class PickTimeFragment extends Fragment implements CountdownRecycleViewClickInterface {

    Button startButton;
    private RecyclerView recyclerView;
    private CountdownAdapter recycleAdapter;
    private LinearLayoutManager layoutManager;
    NumberPicker hourPicker;


    NumberPicker minutePicker;
    NumberPicker secondPicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_pick_time, container, false);
        //setHasOptionsMenu(true);


        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        hourPicker = view.findViewById(R.id.hourPicker);

        minutePicker = view.findViewById(R.id.minutePicker);
        secondPicker = view.findViewById(R.id.secondPicker);

        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(99);
        hourPicker.setTextSize(85.0F);
        hourPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                // Định dạng số hiển thị theo ý muốn
                return String.format("%02d", value);
            }
        });


        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setTextSize(85.0F);
        minutePicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                // Định dạng số hiển thị theo ý muốn
                return String.format("%02d", value);
            }
        });

        secondPicker.setMinValue(0);
        secondPicker.setMaxValue(59);
        secondPicker.setTextSize(85.0F);
        secondPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                // Định dạng số hiển thị theo ý muốn
                return String.format("%02d", value);
            }
        });


        startButton = view.findViewById(R.id.ButtonStart);

        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CountdownActivity.class);
            int hours = hourPicker.getValue();
            int minutes = minutePicker.getValue();
            int seconds = secondPicker.getValue();

            long totalTimeInMillis = (hours * 3600 + minutes * 60 + seconds) * 1000;

            intent.putExtra("time", totalTimeInMillis);

            startActivity(intent);

        });


        recyclerView = view.findViewById(R.id.recycleView_listPresetTime);

        recycleAdapter = new CountdownAdapter(this);

        layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, true);

        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recycleAdapter);


        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.top_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {

            case R.id.add:
                // Khởi tạo Bundle và đặt dữ liệu vào
                Bundle bundle = new Bundle();
                bundle.putString("hour", Integer.toString(hourPicker.getValue()));
                bundle.putString("minute", Integer.toString(minutePicker.getValue()));
                bundle.putString("second", Integer.toString(secondPicker.getValue()));

                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                TimeInputDialogFragment dialogFragment = new TimeInputDialogFragment(recycleAdapter);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fragmentTransaction, "dialog_fragment_tag");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(String data) {
        doSth(data);
    }

    void doSth(String data) {
        String[] sub_str = (data.split("\\|"))[0].split(":");

        onSmoothToSetValue(hourPicker,Integer.parseInt(sub_str[0]));
        onSmoothToSetValue(minutePicker,Integer.parseInt(sub_str[1]));
        onSmoothToSetValue(secondPicker,Integer.parseInt(sub_str[2]));
    }


    public void onSmoothToSetValue(NumberPicker numberPicker, int targetValue) {

        int difference = Math.abs(numberPicker.getValue() - targetValue);

        final int interval = 10;

        final int duration = 500;
        int steps = difference > 0 ? difference : 1;
        final int minValue = numberPicker.getMinValue();
        final int maxValue = numberPicker.getMaxValue();

        final int stepsSize = (maxValue - minValue) / steps;
        int delay = duration / steps;

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            int currentValue = numberPicker.getValue();
            int currentStep = 0;

            @Override
            public void run() {

                if (currentValue != targetValue) {
                    if (currentValue < targetValue) {
                        currentValue = currentValue + stepsSize;
                        if (currentValue > targetValue) {
                            currentValue = targetValue;
                        }
                    } else {
                        currentValue = currentValue - stepsSize;
                        if (currentValue < targetValue) {
                            currentValue = targetValue;
                        }
                    }

                    currentStep += 1;
                    numberPicker.setValue(currentValue);
                    if (currentStep < steps) {
                        new Handler(Looper.getMainLooper()).postDelayed(this, delay);
                    }
                }
            }

        }, interval);
    }
}