package com.example.alarmapp.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alarmapp.R;

import java.util.Locale;

public class StopwatchFragment extends Fragment {


    private ImageButton btn_pause, btn_run, btn_reset, btn_lap;

    Handler handler = new Handler();
    TextView timeView, milisView;
    LinearLayout container;
    Animation move_in, move_out;
    int i = 1;

    long startTime = 0L, milis = 0L, timeSwapBuff = 0L, updateTime = 0L;

    public StopwatchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        timeView = v.findViewById(R.id.textView);
        milisView = v.findViewById(R.id.milisView);
        container = v.findViewById(R.id.container);
        btn_pause = v.findViewById(R.id.btn_pause);
        btn_run = v.findViewById(R.id.btn_run);
        btn_reset = v.findViewById(R.id.btn_reset);
        btn_lap = v.findViewById(R.id.btn_lap);

        btn_pause.setVisibility(View.INVISIBLE);
        btn_lap.setVisibility(View.INVISIBLE);
        btn_reset.setVisibility(View.INVISIBLE);

        btn_run.setOnClickListener(view -> {

            startTime = SystemClock.uptimeMillis();
            handler.postDelayed(updateTimerThread, 0);
            btn_run.setVisibility(View.INVISIBLE);
            btn_pause.setVisibility(View.VISIBLE);
            btn_lap.setVisibility(View.VISIBLE);
            btn_reset.setVisibility(View.VISIBLE);
            if (milis == 0) {
                move_in = AnimationUtils.loadAnimation(getContext().getApplicationContext(), R.anim.fade_in);
                btn_reset.startAnimation(move_in);
                move_in = AnimationUtils.loadAnimation(getContext().getApplicationContext(), R.anim.fade_in);
                btn_lap.startAnimation(move_in);
            }
        });

        btn_pause.setOnClickListener(view -> {
            timeSwapBuff += milis;
            handler.removeCallbacks(updateTimerThread);
            btn_run.setVisibility(View.VISIBLE);
            btn_pause.setVisibility(View.INVISIBLE);

        });

        btn_lap.setOnClickListener(view -> {
            LayoutInflater inflater1 = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View addView = inflater1.inflate(R.layout.row, null);
            TextView txtValue = (TextView) addView.findViewById(R.id.txtContent);
            TextView txt2Value = (TextView) addView.findViewById(R.id.txtContent2);

            txtValue.setText("#" + (i++) + "    " + timeView.getText());
            txt2Value.setText(milisView.getText());
            container.addView(addView, 0);

        });

        btn_reset.setOnClickListener(view -> {

            btn_reset.clearAnimation();
            btn_lap.clearAnimation();
            btn_reset.setVisibility(View.GONE);
            btn_lap.setVisibility(View.GONE);

            btn_run.setVisibility(View.VISIBLE);
            btn_pause.setVisibility(View.INVISIBLE);


            startTime = SystemClock.uptimeMillis();
            milis = 0L;
            timeSwapBuff = 0L;
            updateTime = 0L;
            container.removeAllViews();
            i = 1;

            timeView.setText("00:00:00");
            milisView.setText(".00");
            handler.removeCallbacks(updateTimerThread);
        });

        return v;

    }


    Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            milis = SystemClock.uptimeMillis() - startTime;
            updateTime = timeSwapBuff + milis;
            int secs = (int) (updateTime / 1000);
            int minutes = secs / 60;
            int hours = minutes / 60;
            secs %= 60;
            int mili_secs = (int) (updateTime % 100);
            String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs);
            String milis = String.format(Locale.getDefault(), ".%02d", mili_secs);
            timeView.setText(time);
            milisView.setText(milis);

            handler.postDelayed(this, 0);
        }

    };
}