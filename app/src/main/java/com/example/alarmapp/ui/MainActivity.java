package com.example.alarmapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.alarmapp.R;
import com.wwdablu.soumya.lottiebottomnav.FontBuilder;
import com.wwdablu.soumya.lottiebottomnav.FontItem;
import com.wwdablu.soumya.lottiebottomnav.ILottieBottomNavCallback;
import com.wwdablu.soumya.lottiebottomnav.LottieBottomNav;
import com.wwdablu.soumya.lottiebottomnav.MenuItem;
import com.wwdablu.soumya.lottiebottomnav.MenuItemBuilder;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ILottieBottomNavCallback {

    FragmentTransaction transaction = null;
    LottieBottomNav bottomNav;
    ArrayList<MenuItem> list;

    AlarmsFragment alarmsFragment = null;
    StopwatchFragment stopwatchFragment = null;
    PickTimeFragment pickTimeFragment = null;
    CurrentTimeFragment settingFragment = null;
    private ImageView virtualButton = null;
    // help click on add button
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav   = findViewById(R.id.bottomAppBar);

        //viewPager = findViewById(R.id.viewPager);

        //setUpViewPager();
        setupVirtualAddButton();

        //Set font item
        FontItem fontItem = FontBuilder.create("Home")
                .selectedTextColor(Color.BLACK)
                .unSelectedTextColor(Color.GRAY)
                .selectedTextSize(16) //SP
                .unSelectedTextSize(12) //SP
                .setTypeface(Typeface.createFromAsset(getAssets(), "coconutz.ttf"))
                .build();

        //Menu Home
        MenuItem homeItem = MenuItemBuilder.create("home.json", MenuItem.Source.Assets, fontItem, "dash")
                .pausedProgress(1f)
                .build();

        //Menu Alarms List
        fontItem = FontBuilder.create(fontItem).setTitle("Alarms").build();
        MenuItem alarmsItem = MenuItemBuilder.createFrom(homeItem, fontItem)
                .selectedLottieName("alarm.json")
                .unSelectedLottieName("alarm.json")
                .pausedProgress(1f)
                .loop(true)
                .build();

        //Menu Timer
        fontItem = FontBuilder.create(fontItem).setTitle("Timer").build();
        MenuItem timerItem = MenuItemBuilder.createFrom(homeItem, fontItem)
                .selectedLottieName("timer.json")
                .unSelectedLottieName("timer.json")
                .pausedProgress(1f)
                .loop(true)
                .build();

        //Menu Add
        fontItem = FontBuilder.create(fontItem).setTitle("").build();
        MenuItem addItem = MenuItemBuilder.createFrom(homeItem, fontItem)
                .selectedLottieName("add.json")
                .unSelectedLottieName("add.json")
                .pausedProgress(1f)
                .loop(true)
                .build();

        //Menu Stopwatch
        fontItem = FontBuilder.create(fontItem).setTitle("Stopwatch").build();
        MenuItem stopwatchItem = MenuItemBuilder.createFrom(homeItem, fontItem)
                .selectedLottieName("stopwatch.json")
                .unSelectedLottieName("stopwatch.json")
                .pausedProgress(1f)
                .loop(true)
                .build();

        //Menu Settings
        fontItem = FontBuilder.create(fontItem).setTitle("Current").build();
        MenuItem currentTimeItem = MenuItemBuilder.createFrom(homeItem, fontItem)
                .selectedLottieName("current_time.json")
                .unSelectedLottieName("current_time.json")
                .pausedProgress(1f)
                .loop(true)
                .build();

        list = new ArrayList<>(4);
        list.add(alarmsItem);
        list.add(timerItem);
        list.add(addItem);
        list.add(stopwatchItem);
        list.add(currentTimeItem);

        bottomNav.setCallback(this);
        bottomNav.setMenuItemList(list);
        bottomNav.setSelectedIndex(0); //first selected index

        //First selected fragment
        setFragment(new AlarmsFragment());

    }

    @Override
    public void onMenuSelected(int oldIndex, int newIndex, MenuItem menuItem) {
        switch (newIndex) {
            case 0: {
                setFragment(getAlarmsFragment());
                //viewPager.setCurrentItem(0);
                break;
            }
            case 1: {
                setFragment(getPickTimeFragment());
                //viewPager.setCurrentItem(1);
                break;
            }
            case 2: {
                startActivity(new Intent(MainActivity.this, AddAlarmActivity.class));
                virtualButton.setVisibility(View.VISIBLE);
                //setFragment(new AlarmsFragment());
                break;
            }
            case 3: {
                setFragment(getStopwatchFragment());
                //viewPager.setCurrentItem(2);
                break;
            }
            case 4: {
                setFragment(getSettingFragment());
                //viewPager.setCurrentItem(2);
                break;
            }
        }
        // if switch tab then
        if (newIndex != 2) {
            virtualButton.setVisibility(View.GONE);
        }
    }



    @Override
    public void onAnimationStart(int index, MenuItem menuItem) {

    }

    @Override
    public void onAnimationEnd(int index, MenuItem menuItem) {

    }

    @Override
    public void onAnimationCancel(int index, MenuItem menuItem) {

    }

    private void setFragment(Fragment fragment) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment).commit();
    }

    private AlarmsFragment getAlarmsFragment() {
        if (alarmsFragment == null)
            alarmsFragment = new AlarmsFragment();
        return alarmsFragment;
    }

    private PickTimeFragment getPickTimeFragment() {
        if (pickTimeFragment == null)
            pickTimeFragment = new PickTimeFragment();
        return pickTimeFragment;
    }

    private StopwatchFragment getStopwatchFragment() {
        return new StopwatchFragment();
    }

    private Fragment getSettingFragment() {
        if (settingFragment == null)
            settingFragment = new CurrentTimeFragment();
        return settingFragment;
    }
    // virtual button allow multiple click on add button
    private void setupVirtualAddButton() {
        virtualButton = findViewById(R.id.virtualAddButton);
        virtualButton.setVisibility(View.GONE);
        virtualButton.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AddAlarmActivity.class));
        });
    }
}