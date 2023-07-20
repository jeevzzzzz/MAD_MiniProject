package com.example.alarmapp.service;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.alarmapp.R;


public class CountdownSoundService extends Service {
    MediaPlayer mediaPlayer;
    final long Time_Countdown = 1 * 60 * 1000;
    final  long Time_Interval = 1000;



    @Override
    public void onCreate() {
        super.onCreate();
        //Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ring2);
        mediaPlayer = MediaPlayer.create(this,R.raw.ring2 );
        mediaPlayer.setVolume(100,100);
        mediaPlayer.setLooping(true);
        //Toast.makeText(this, "create service!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals("START")) {
            mediaPlayer.start();
            remaining();
            // Toast.makeText(this, "start service!", Toast.LENGTH_SHORT).show();
        } else


        if (intent.getAction().equals("STOP")) {
            stopSelf();
        }

        return START_NOT_STICKY;
    }

    private void remaining() {
        CountDownTimer countDownTimer = new CountDownTimer(Time_Countdown, Time_Interval) {
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {

                stopSelf();
            }
        };

        countDownTimer.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}

