package com.example.alarmapp.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.alarmapp.R;
import com.example.alarmapp.model.Alarm;
import com.example.alarmapp.ui.RingActivity;


import java.io.IOException;
import java.util.Calendar;

public class AlarmService extends Service {
    public static final String CHANNEL_ID = "ALARM_SERVICE_CHANNEL";
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    Alarm alarm;
    Uri ringtone;
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(true);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        ringtone= RingtoneManager.getActualDefaultRingtoneUri(this.getBaseContext(), RingtoneManager.TYPE_ALARM);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle=intent.getBundleExtra(getString(R.string.bundle_alarm_obj));
        if (bundle!=null)
            alarm =(Alarm)bundle.getSerializable(getString(R.string.arg_alarm_obj));
        Intent notificationIntent = new Intent(this, RingActivity.class);
        notificationIntent.putExtra(getString(R.string.bundle_alarm_obj),bundle);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addNextIntentWithParentStack(notificationIntent);
//        // Get the PendingIntent containing the entire back stack
//        PendingIntent pendingIntent =
//                stackBuilder.getPendingIntent(0,
//                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        String alarmTitle=getString(R.string.alarm_title);
        if(alarm!=null) {
            alarmTitle = alarm.getTitle();

            try {
                mediaPlayer.setDataSource(this.getBaseContext(), Uri.parse(alarm.getRingtoneUri()));
                mediaPlayer.prepareAsync();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else{

            try {
                mediaPlayer.setDataSource(this.getBaseContext(),ringtone);
                mediaPlayer.prepareAsync();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        // After Android Oreo, all notification has to belong to a notification channel
        String channelName = "Alarm Background Service";
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, channelName,
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(alarmTitle)
                .setContentText(alarm.getDescription())
                .setSmallIcon(R.drawable.wall_clock)
                .setSound(null)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                //.setFullScreenIntent(pendingIntent, true)
                .setContentIntent(pendingIntent)
                .build();

        // Play ring tone asynchronously
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });

        // Vibrate when alarm or not
        if (alarm.isVibrateMode()) {
            long[] pattern = {0, 100, 1000};
            vibrator.vibrate(pattern, 0);
        }
        // Run this foreground service
        startForeground(1, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
        vibrator.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}