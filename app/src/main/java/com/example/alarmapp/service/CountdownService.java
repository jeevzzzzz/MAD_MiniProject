package com.example.alarmapp.service;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.example.alarmapp.R;
import com.example.alarmapp.receiver.CountdownBroadcastReceiver;

import java.util.Locale;

public class CountdownService extends Service {
    private static final String NOTIFICATION_CHANEL_ID = "CountdownChanel";
    private static final  int NOTIFICATION_ID = 1;
    private final long Time_Countdown = 1 * 60 * 1000;
    private final int Time_Interval = 1000;

    private CountDownTimer countDownTimer;
    RemoteViews notificationLayout ;
    PendingIntent pendingIntent;

    @Override
    public void onCreate() {
        super.onCreate();



        // Đăng kí kênh thông báo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANEL_ID,
                    "Channel Notification Service", NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(null,null);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if (intent.getAction().equals("STOP")) {
            stopSelf();
        } else
        if (intent.getAction().equals("START")) {
            notificationLayout = new RemoteViews(getPackageName(), R.drawable.notification_small);
            Intent intent_stop = new Intent(this, CountdownBroadcastReceiver.class);
            intent_stop.setAction("Stop_CountdownService");

            pendingIntent = PendingIntent.getBroadcast(this, 0, intent_stop,
                    PendingIntent.FLAG_IMMUTABLE);

            //Rung
            final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            final VibrationEffect vibrationEffect1;

            // this is the only type of the vibration which requires system version Oreo (API 26)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                // this effect creates the vibration of default amplitude for 1000ms(1 sec)
                vibrationEffect1 = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE);

                // it is safe to cancel other vibrations currently taking place
                Log.i(" TAG vibrator", "vibrating with pattern: " );
                vibrator.vibrate(vibrationEffect1);

            }
            // Thong bao
            callNotification();
            // hien thi thoi gian troi
            long time = Time_Countdown;
            startCountdown(time);

        }

        return START_NOT_STICKY;
    }




    private void startCountdown(long duration) {
        countDownTimer = new CountDownTimer(duration, Time_Interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Xử lý mỗi giây trong quá trình đếm ngược
                long remainingSeconds = (Time_Countdown - millisUntilFinished) / 1000;

                int remainingHours = (int) (remainingSeconds / 3600);
                remainingSeconds %= 3600;
                int remainingMinutes = (int) (remainingSeconds / 60);
                remainingSeconds %= 60;

                // Update the UI with the remaining time
                String time = String.format(Locale.getDefault(),"- %02d:%02d:%02d",
                        remainingHours, remainingMinutes, remainingSeconds);
                updateNotification(time);

            }

            @Override
            public void onFinish() {
                // Xử lý khi kết thúc đếm ngược
                stopSelf();
            }
        };

        countDownTimer.start();
    }


    public void callNotification() {

        // Apply the layouts to the notification
        NotificationCompat.Builder customNotification = new NotificationCompat.Builder(getBaseContext(),
                "CountdownChanel")
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setSound(null)
                //.setCustomBigContentView(notificationLayoutExpanded)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                //.setFullScreenIntent(pendingIntent,true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getBaseContext());
        notificationManager.notify(1, customNotification.build());

    }




    private  void updateNotification(String time) {
        //Toast.makeText(this, "couting douwn", Toast.LENGTH_SHORT).show();

        notificationLayout.setTextViewText(R.id.notification_message, time);

        // cập nhật thời gian đếm ngược trong thông báo
        NotificationCompat.Builder customNotification = new NotificationCompat.Builder(getBaseContext(),
                "CountdownChanel")
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                //.setCustomBigContentView(notificationLayoutExpanded)
                .setAutoCancel(true)
                .setSound(null)
                .setContentIntent(pendingIntent)
                //.setFullScreenIntent(pendingIntent, true)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // cập nhật thông báo
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, customNotification.build());
        }

    }


    private PendingIntent stopCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        stopSelf();
        return null;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Hủy bỏ đếm ngược khi dịch vụ bị hủy
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

}

