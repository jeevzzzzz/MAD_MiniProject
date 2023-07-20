package com.example.alarmapp.ui.huan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alarmapp.R;
import com.example.alarmapp.service.CountdownService;
import com.example.alarmapp.service.CountdownSoundService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CountdownActivity extends AppCompatActivity {
    TextView time_remaining;

    Button button_exit, button_continue;

    private CountDownTimer countDownTimer;

    private static final  int NOTIFICATION_ID = 1;

    boolean stop = false;

    final int  timeInterval = 10;

    private long remainingTime;

    ProgressBar progressBar;

    private long totalTimeInMillis; // tong  tg dem nguoc

    private TextView finishTimeView;

    private NotificationChannel channel; // Kenh thong bao

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        //set progressBar
        progressBar = findViewById(R.id.progressBar);

        // set time remaining
        time_remaining = findViewById(R.id.timeRemainingTextView);

        //time finish
        finishTimeView = findViewById(R.id.finishTimeTextView);

        Intent intent = getIntent();
        totalTimeInMillis = intent.getLongExtra("time",0);

        addTime(totalTimeInMillis);

        remainingTime = totalTimeInMillis;

        button_exit = findViewById(R.id.button_exit);
        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // countdown
        countDownTimer = new CountDownTimer(totalTimeInMillis, timeInterval) {
            @Override
            public void onTick(long millisUntilFinished) {

                remainingTime = millisUntilFinished;
                updateTimeText(remainingTime);
            }
            @Override
            public void onFinish() {
                remainingTime = 0;
                updateTimeText(remainingTime);

                // call notification
                callNotification();

                // Back
                finish();
            }
        };

        countDownTimer.start();

        //setButon continue

        button_continue = findViewById(R.id.button_continue);
        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stop) {
                    resumeTimer();
                    button_continue.setText("Stop");
                    button_continue.setBackgroundResource(R.drawable.button_stop);

                } else {
                    pauseTimer();
                    button_continue.setText("Continue");
                    button_continue.setBackgroundResource(R.drawable.button_continue);

                }

                stop = !stop;
            }
        });
    }

    void updateProgressBar(){
        progressBar.setProgress((int) ((double)remainingTime / (double) totalTimeInMillis * 100));
    }

    void updateTimeText(long millisUntilFinished) {
        long remainingSeconds = millisUntilFinished / 1000;

        int remainingHours = (int) (remainingSeconds / 3600);
        remainingSeconds %= 3600;
        int remainingMinutes = (int) (remainingSeconds / 60);
        remainingSeconds %= 60;

        // Update the UI with the remaining time
        String time = String.format(Locale.getDefault(),"%02d:%02d:%02d",
                remainingHours, remainingMinutes, remainingSeconds);
        time_remaining.setText(time);
        updateProgressBar();

    }

    private  void startTime() {
        if(stop) {
            countDownTimer.start();
        }
    }

    private void pauseTimer() {
        if(!stop) {
            countDownTimer.cancel();
        }
    }

    private void resumeTimer() {
        if(stop) {
            addTime(remainingTime);
            countDownTimer = new CountDownTimer(remainingTime, timeInterval) {
                @Override
                public void onTick(long l) {
                    remainingTime = l;
                    updateTimeText(l);
                }
                @Override

                public void onFinish() {
                    remainingTime = 0;
                    updateTimeText(remainingTime);

                    // call notification
                    callNotification();

                    // Back
                    finish();
                }
            };
            startTime();
        }
    }


    public void addTime(long millisUntilFinished) {
        int remainingSeconds = (int) (millisUntilFinished / 1000);

        int remainingHours = (int) (remainingSeconds / 3600);
        remainingSeconds %= 3600;
        int remainingMinutes = (int) (remainingSeconds / 60);
        remainingSeconds %= 60;


        Calendar currentTime = Calendar.getInstance();
        currentTime.add(Calendar.HOUR_OF_DAY, remainingHours);
        currentTime.add(Calendar.MINUTE, remainingMinutes);
        currentTime.add(Calendar.SECOND, remainingSeconds);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");

        finishTimeView.setText(sdf.format(currentTime.getTime()));
    }


    public void callNotification() {


        Intent notificationIntent = new Intent(getBaseContext(), CountdownService.class);
        notificationIntent.setAction("START");
        startService(notificationIntent) ;
        //Call sound
        Intent soundIntent = new Intent(getBaseContext(), CountdownSoundService.class);
        soundIntent.setAction("START");
        startService(soundIntent);
    }

    private void startRing() {
        Intent intent = new Intent(this, CountdownSoundService.class);
        String action = "START";
        intent.setAction(action);
        startService(intent);
    }

    private void stopRing() {
        Intent intent = new Intent(this, CountdownSoundService.class);
        String action = "STOP";
        intent.setAction(action);
        stopService(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Hủy bỏ đếm ngược khi Activity bị hủy
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

    }
}