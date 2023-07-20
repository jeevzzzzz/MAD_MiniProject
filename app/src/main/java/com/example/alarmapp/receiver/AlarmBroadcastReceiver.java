package com.example.alarmapp.receiver;




import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.alarmapp.R;
import com.example.alarmapp.model.Alarm;
import com.example.alarmapp.service.AlarmService;
import com.example.alarmapp.service.RescheduleAlarmService;
import com.example.alarmapp.utils.WeekDays;

import java.util.Calendar;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    Alarm alarm;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            String toastText = String.format("Alarm Reboot");
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            //startRescheduleAlarmsService(context);
        }
        else {
            Bundle bundle=intent.getBundleExtra(context.getString(R.string.bundle_alarm_obj));
            Log.i("AlarmTest", "bundle ="+bundle);
            if (bundle!=null)
                alarm =(Alarm) bundle.getSerializable(context.getString(R.string.arg_alarm_obj));
            String toastText = String.format("Alarm Received");
            // Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            if(alarm!=null) {
                Toast.makeText(context, "Start alarm service", Toast.LENGTH_SHORT).show();
                startAlarmService(context, alarm);
            }
        }
    }

    private void startAlarmService(Context context, Alarm alarm1) {
        Intent intentService = new Intent(context, AlarmService.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable(context.getString(R.string.arg_alarm_obj),alarm1);
        intentService.putExtra(context.getString(R.string.bundle_alarm_obj),bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }
    private void startRescheduleAlarmsService(Context context) {
        Intent intentService = new Intent(context, RescheduleAlarmService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }
}
