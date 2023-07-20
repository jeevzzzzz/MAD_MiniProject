package com.example.alarmapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.alarmapp.service.CountdownService;
import com.example.alarmapp.service.CountdownSoundService;

public class CountdownBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Lấy hành động của Intent
        String action = intent.getAction();

        if (action != null && action.equals("Stop_CountdownService")) {

            Intent stop_countdown = new Intent(context, CountdownService.class);
            stop_countdown.setAction("STOP");
            context.stopService(stop_countdown);

            Intent stop_sound = new Intent(context, CountdownSoundService.class);
            stop_sound.setAction("STOP");
            context.stopService(stop_sound);
        }
    }
}

