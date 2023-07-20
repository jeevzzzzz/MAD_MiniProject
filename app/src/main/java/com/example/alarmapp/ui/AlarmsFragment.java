package com.example.alarmapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.alarmapp.R;
import com.example.alarmapp.adapter.AlarmAdapter;
import com.example.alarmapp.model.Alarm;
import com.example.alarmapp.utils.AlarmUtils;
import com.example.alarmapp.utils.DatabaseHelper;
import com.example.alarmapp.viewmodel.AlarmViewModel;

public class AlarmsFragment extends Fragment implements DatabaseHelper {

    RecyclerView recyclerView;
    private AlarmViewModel alarmViewModel = null;
    private AlarmAdapter adapter;
    private SearchView searchView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new AlarmAdapter(this.getContext(), this);
        alarmViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);
        alarmViewModel.getAllAlarms().observe(this, adapter::setAlarms);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_alarms, container, false);

        recyclerView = view.findViewById(R.id.listAlarmRecyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        addSwipAndDragItem();
        // manage search view
        searchView = view.findViewById(R.id.search_recipe);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    private void addSwipAndDragItem() {
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        adapter.moveAlarm(viewHolder.getAdapterPosition(),
                                target.getAdapterPosition());
                        return true;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Alarm alarm = adapter.getAlarmAt(position);
                        Toast.makeText(AlarmsFragment.this.getContext(),
                                "Deleted " + alarm.getTitle(), Toast.LENGTH_SHORT).show();
                        onDelete(alarm);
                    }
                }
        );
        helper.attachToRecyclerView(this.recyclerView);
    }

    @Override
    public void onToggle(Alarm alarm) {
        // switch turn on/off alarm
        if (alarm.isEnable()) {
            alarm.setEnable(false);
            AlarmUtils.cancelAlarm(getContext(), alarm);
        } else {
            alarm.setEnable(true);
            AlarmUtils.scheduleAlarm(getContext(), alarm);
        }

        alarmViewModel.update(alarm);
    }

    @Override
    public void onDelete(Alarm alarm) {
        alarmViewModel.delete(alarm);
        AlarmUtils.cancelAlarm(getContext(), alarm);
    }

    @Override
    public void onUpdate(Alarm alarm) {
        alarmViewModel.update(alarm);
        AlarmUtils.scheduleAlarm(getContext(), alarm);
    }
}