package com.example.alarmapp.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.example.alarmapp.R;
import com.example.alarmapp.model.Alarm;
import com.example.alarmapp.receiver.AlarmBroadcastReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlarmUtils {
    private static final Calendar calendar = Calendar.getInstance();
    private static SimpleDateFormat TIME_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.getDefault());

    public static String getHourMinute(long time) {
        return TIME_FORMAT.format(time);
    }

    public static long getTimeMillis(int hour, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTimeInMillis();
    }

    public static String getDaysInWeek(int mask) {
        StringBuilder result = new StringBuilder();
        for (WeekDays w : WeekDays.values()) {
            int x = w.ordinal();
            if ((mask >> x & 1) == 1) {
                result.append(w.toString());
                result.append(" ");
            }
        }
        return result.toString();
    }

    public static byte getBitFormat(WeekDays... days) {
        byte result = 0;
        for (WeekDays w : days) {
            int x = w.ordinal();
            result |= 1 << x;
        }
        return result;
    }

    public static byte getBitFormat(boolean... days) {
        byte result = 0;
        for (int i = 0; i < 7; ++i) {
            result |= (days[i] ? 1 : 0) << i;
        }
        return result;
    }

    public static String toWeekDay(int day) throws Exception {
        // Get week day name in String
        switch (day) {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
        }
        throw new Exception("Could not locate day");
    }

    public static void scheduleAlarm(Context context, Alarm alarm){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(context.getString(R.string.arg_alarm_obj), alarm);
        intent.putExtra(context.getString( R.string.bundle_alarm_obj), bundle);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context,
                alarm.getID(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        Log.i("AlarmTest", "bundle send =" + bundle);
        Calendar alarmCalendar = Calendar.getInstance();
        alarmCalendar.setTimeInMillis(alarm.getTime());

        Calendar tmpCalendar = Calendar.getInstance();
        tmpCalendar.setTimeInMillis(System.currentTimeMillis());
        Log.i("CURRENTtmpAlamr", "tmpAlamr: "
                + "Hour " + tmpCalendar.get(Calendar.HOUR_OF_DAY) + " "
                + "Minute " + tmpCalendar.get(Calendar.MINUTE) + " "
                + "Day of month "+ tmpCalendar.get(Calendar.DAY_OF_MONTH) + " "
                + "AlarmID " + alarm.getID() + " "
                + "Month " + tmpCalendar.get(Calendar.MONTH) );

        tmpCalendar.set(Calendar.HOUR_OF_DAY, alarmCalendar.get(Calendar.HOUR_OF_DAY));
        tmpCalendar.set(Calendar.MINUTE, alarmCalendar.get(Calendar.MINUTE));
        tmpCalendar.set(Calendar.SECOND, 0);
        tmpCalendar.set(Calendar.MILLISECOND, 0);

        String toastText = null;
        if (!alarm.isRepeatMode()) {

            try {
                if (hasTimeAlreadyPassed(alarm)) {
                    tmpCalendar.set(Calendar.DAY_OF_MONTH, tmpCalendar.get(Calendar.DAY_OF_MONTH) + 1);
                }

                toastText = String.format("One-time Alarm %s scheduled for %s at %02d:%02d",
                        alarm.getTitle(),
                        toWeekDay(tmpCalendar.get(Calendar.DAY_OF_WEEK)),
                        tmpCalendar.get(Calendar.HOUR_OF_DAY),
                        tmpCalendar.get(Calendar.MINUTE));

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            toastText = String.format("Repeating Alarm %s scheduled for %s at %02d:%02d",
                    alarm.getTitle(),
                    getRecurringDaysText(alarm),
                    tmpCalendar.get(Calendar.HOUR_OF_DAY),
                    tmpCalendar.get(Calendar.MINUTE));

            int alarmDay = findAlarmDay(alarm);
            int today = tmpCalendar.get(Calendar.DAY_OF_WEEK);
            tmpCalendar.set(Calendar.DAY_OF_WEEK, alarmDay );
            if(alarmDay < today)
                tmpCalendar.add(Calendar.DAY_OF_MONTH, 7);
            else if(alarmDay == today && hasTimeAlreadyPassed(alarm))
                tmpCalendar.add(Calendar.DAY_OF_MONTH, 7);
            try {
                Toast.makeText(context, "nextAlarmDay " + toWeekDay(alarmDay), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.i("ScheduletmpAlamr", "tmpAlamr: "
                + "Hour " + tmpCalendar.get(Calendar.HOUR_OF_DAY) + " "
                + "Minute " + tmpCalendar.get(Calendar.MINUTE) + " "
                + "Day of month "+ tmpCalendar.get(Calendar.DAY_OF_MONTH) + " "
                + "AlarmID " + alarm.getID() + " "
                + "Month " + tmpCalendar.get(Calendar.MONTH) );
        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
        alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                tmpCalendar.getTimeInMillis(),
                alarmPendingIntent);
        alarm.setEnable(true);
    }

    public static int findAlarmDay(Alarm alarm) {
        Calendar curCalendar = Calendar.getInstance();
        curCalendar.setTimeInMillis(System.currentTimeMillis());
        Log.i("findAlarmDay", Integer.toString(calendar.get(Calendar.DAY_OF_WEEK)));
        int today = curCalendar.get(Calendar.DAY_OF_WEEK) - 2;
        int res = 0;
        int numAlarmDay = 0;
        if (today == -1)
            today = 6;
        for (int i = 0; i < 7; ++i) {
            int nxtDay = today + i;
            if (nxtDay >= 7)
                nxtDay = nxtDay % 7;

            if (alarm.isRepeatAt(nxtDay)) {
                numAlarmDay++;
                if (nxtDay == today && hasTimeAlreadyPassed(alarm))
                    continue;
                res = nxtDay;
                break;
            }
        }
        if(numAlarmDay == 1)
            res = today;
        res += 2;
        if (res == 8)
            res = 1;
        return res;
    }

    public static boolean hasTimeAlreadyPassed(Alarm alarm) {
        Calendar alarmCalendar = Calendar.getInstance();
        alarmCalendar.setTimeInMillis(alarm.getTime());

        Calendar tmpCalendar = Calendar.getInstance();
        tmpCalendar.setTimeInMillis(System.currentTimeMillis());
        tmpCalendar.set(Calendar.HOUR_OF_DAY, alarmCalendar.get(Calendar.HOUR_OF_DAY));
        tmpCalendar.set(Calendar.MINUTE, alarmCalendar.get(Calendar.MINUTE));
        tmpCalendar.set(Calendar.SECOND, 0);
        tmpCalendar.set(Calendar.MILLISECOND, 0);

        // if alarm time has already passed, increment day by 1
        if (tmpCalendar.getTimeInMillis() / 1000 <= System.currentTimeMillis()/1000) {
            return true;
        }
        return false;
    }

    public static String getRecurringDaysText(Alarm alarm) {
        if (!alarm.isRepeatMode()) {
            return null;
        }

        String days = "";
        if (alarm.isRepeatAt(WeekDays.MON)) {
            days += "Mo ";
        }
        if (alarm.isRepeatAt(WeekDays.TUE)) {
            days += "Tu ";
        }
        if (alarm.isRepeatAt(WeekDays.WED)) {
            days += "We ";
        }
        if (alarm.isRepeatAt(WeekDays.THU)) {
            days += "Th ";
        }
        if (alarm.isRepeatAt(WeekDays.FRI)) {
            days += "Fr ";
        }
        if (alarm.isRepeatAt(WeekDays.SAT)) {
            days += "Sa ";
        }
        if (alarm.isRepeatAt(WeekDays.SUN)) {
            days += "Su ";
        }

        return days;
    }

    public static void cancelAlarm(Context context, Alarm alarm) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context,
                alarm.getID(),
                intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(alarmPendingIntent);
        alarm.setEnable(false);
        String toastText = String.format("Alarm %s cancelled", alarm.getTitle());
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
    }

    public static void cancelAlarmWhenRing(Context context, Alarm alarm) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context,
                alarm.getID(),
                intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(alarmPendingIntent);
        String toastText = String.format("Alarm %s cancelled", alarm.getTitle());
        if (alarm.isRepeatMode() == false) {
            alarm.setEnable(false);

        } else {
            scheduleAlarm(context, alarm);
        }
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
    }

}
