package com.example.alarmapp.ui;

import static com.example.alarmapp.utils.AlarmUtils.cancelAlarm;
import static com.example.alarmapp.utils.AlarmUtils.cancelAlarmWhenRing;
import static com.example.alarmapp.utils.AlarmUtils.scheduleAlarm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.alarmapp.R;
import com.example.alarmapp.databinding.ActivityRingBinding;
import com.example.alarmapp.model.Alarm;
import com.example.alarmapp.service.AlarmService;
import com.example.alarmapp.viewmodel.AlarmViewModel;

import java.util.Calendar;
import java.util.Random;

public class RingActivity extends AppCompatActivity {
    private Alarm alarm;
    private ActivityRingBinding binding;
    private AlarmViewModel alarmViewModel;
    private Intent intentService;
    private static final int MY_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);

        intentService = new Intent(getApplicationContext(), AlarmService.class);
        binding = ActivityRingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                            | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            );
        }

        alarmViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);
        Bundle bundle = getIntent().getBundleExtra(getString(R.string.bundle_alarm_obj));
        if (bundle != null)
            alarm = (Alarm) bundle.getSerializable(getString(R.string.arg_alarm_obj));

        binding.btnDimiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissAlarm();
            }
        });

        binding.btnSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snoozeAlarm();
            }
        });

        binding.txtAlarmTitle.setText(alarm.getTitle());
    }

    public  void dismissAlarm() {

        if (alarm != null) {
            if(!alarm.isHardMode()) {
                cancelAlarmWhenRing(getBaseContext(), alarm);
                alarmViewModel.update(alarm);
                getApplicationContext().stopService(intentService);
                finish();
            }
            else {
                Intent intent = new Intent(RingActivity.this, MathProblemActivity.class);
                startActivityForResult(intent, MY_REQUEST_CODE);
            }
        }

    }

    private void snoozeAlarm() {

        cancelAlarmWhenRing(getBaseContext(), alarm);
        alarmViewModel.update(alarm);
        getApplicationContext().stopService(intentService);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 1);



        Alarm newAlarm = new Alarm(
                1,
                calendar.getTimeInMillis(),
                alarm.getTitle(),
                alarm.getDescription(),
                alarm.getRingtoneUri(),
                true,
                alarm.isHardMode(),
                alarm.isVibrateMode(),
                false,
                (byte) 0);
        scheduleAlarm(getApplicationContext(), newAlarm);


        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            cancelAlarmWhenRing(getBaseContext(), alarm);
            alarmViewModel.update(alarm);
            getApplicationContext().stopService(intentService);
            finish();
        }
    }
}