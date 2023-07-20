package com.example.alarmapp.utils;

import com.example.alarmapp.model.Alarm;

public interface DatabaseHelper {
    void onToggle(Alarm alarm);
    void onDelete(Alarm alarm);
    void onUpdate(Alarm alarm);
}
