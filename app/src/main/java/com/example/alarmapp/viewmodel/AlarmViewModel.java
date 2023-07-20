package com.example.alarmapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.alarmapp.data.AlarmRepository;
import com.example.alarmapp.model.Alarm;


import java.util.List;

public class AlarmViewModel extends AndroidViewModel {
    final private AlarmRepository repository;
    private LiveData<List<Alarm>> allAlarms;

    public AlarmViewModel(@NonNull Application application) {
        super(application);
        repository = new AlarmRepository(application);
        allAlarms = repository.getAllAlarms();
    }

    public LiveData<List<Alarm>> getAllAlarms() {
        return this.allAlarms;
    }

    public void insert(Alarm alarm) {
        repository.insert(alarm);
    }

    public Alarm getLastAlarm() {
        return repository.getLastAlarm()[0];
    }

    public void delete(Alarm alarm) {
        repository.delete(alarm);
    }

    public void update(Alarm alarm) {
        repository.update(alarm);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    LiveData<List<Alarm>> searchByTitle(String title) {
        return repository.searchByTitle(title);
    }

    public String getNearbyRange() {
        return repository.getNearbyRange();
    }

}
