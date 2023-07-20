package com.example.alarmapp.service;

import static com.example.alarmapp.utils.AlarmUtils.scheduleAlarm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleService;


import com.example.alarmapp.data.AlarmRepository;
import com.example.alarmapp.model.Alarm;

public class RescheduleAlarmService extends LifecycleService {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        AlarmRepository alarmRepository = new AlarmRepository(getApplication());
        // Use LiveData to observe database changes
        alarmRepository.getAllAlarms().observe(this, alarms -> {
            for (Alarm alarm : alarms) {
                if (alarm.isEnable()) {
                    scheduleAlarm(getApplicationContext(), alarm);
                }
            }
        });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }
}