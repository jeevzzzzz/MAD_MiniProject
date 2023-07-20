package com.example.alarmapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.alarmapp.utils.WeekDays;

import java.io.Serializable;

@Entity(tableName = "alarm_table")
public class Alarm implements Serializable {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int ID;
    @ColumnInfo(name = "time")
    private long time; // hour:min in millis
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "ringtone")
    private String ringtoneUri;
    @ColumnInfo(name = "is_enable")
    private boolean isEnable; // if alarm is set
    @ColumnInfo(name = "hard_mode")
    private boolean hardMode; // if true, user has to solve math problem to dismiss alarm
    @ColumnInfo(name = "vibrate_mode")
    private boolean vibrateMode; // true if alarm vibrate
    @ColumnInfo(name = "repeat_mode")
    private boolean repeatMode; // false if only one time
    @ColumnInfo(name = "days_in_week")
    private byte daysInWeek; // bit i set 1 if day i repeated, Monday is 0th, Sunday is 6th



    public Alarm(int ID, long time, String title, String description, String ringtoneUri,
                 boolean isEnable, boolean hardMode, boolean vibrateMode, boolean repeatMode,
                 byte daysInWeek) {
        this.ID = ID;
        this.time = time;
        this.title = title;
        this.description = description;
        this.ringtoneUri = ringtoneUri;
        this.isEnable = isEnable;
        this.hardMode = hardMode;
        this.vibrateMode = vibrateMode;
        this.repeatMode = repeatMode;
        this.daysInWeek = daysInWeek;

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRingtoneUri() {
        return ringtoneUri;
    }

    public void setRingtoneUri(String ringtoneUri) {
        this.ringtoneUri = ringtoneUri;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public boolean isHardMode() {
        return hardMode;
    }

    public void setHardMode(boolean hardMode) {
        this.hardMode = hardMode;
    }
    // ------------ setter --------------------

    public boolean isVibrateMode() {
        return vibrateMode;
    }

    public void setVibrateMode(boolean vibrateMode) {
        this.vibrateMode = vibrateMode;
    }

    public boolean isRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(boolean repeatMode) {
        this.repeatMode = repeatMode;
    }

    public byte getDaysInWeek() {
        return daysInWeek;
    }

    public void setDaysInWeek(byte daysInWeek) {
        this.daysInWeek = daysInWeek;
    }

    public boolean isRepeatAt(WeekDays day) {
        int x = day.ordinal();
        return (daysInWeek >> x & 1) == 1; //if bit x on then it repeated
    }

    public boolean isRepeatAt(int dayIndex) {
        return (daysInWeek >> dayIndex & 1) == 1; //if bit x on then it repeated
    }



    public void setRepeatAt(WeekDays day, boolean isRepeat) {
        int x = day.ordinal();
        if (isRepeat)
            this.daysInWeek |= 1 << x;
        else
            this.daysInWeek &= ~(1 << x);
    }


}
