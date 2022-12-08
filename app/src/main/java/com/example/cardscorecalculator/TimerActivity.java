package com.example.cardscorecalculator;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

public class TimerActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    ViewPagerAdapter viewPagerAdapter;
    MaterialToolbar topAppBar;
    boolean notification, notificationSetByUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        getSharedPreferences();

        // connect vars with views
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);
        topAppBar = findViewById(R.id.topAppBarTimerActivity);

        // sets the app bar back button function
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(TimerActivity.this, MainActivity.class);
                startActivity(in);
            }
        });

        // setting up tabLayout
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        // joins TabLayout with ViewPager.
        tabLayout.setupWithViewPager(viewPager);

        // asks for notification permissions if they haven't been asked already
        if (Build.VERSION.SDK_INT >= 32) {
            if (NotificationManagerCompat.from(this).areNotificationsEnabled() && !notificationSetByUser) {
                Log.d("TAG", "asked for permission");
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            notification = true;
        } else {
            notification = false;
        }
        notificationSetByUser = true;
        createSharedPreferences();
    });

    // Save current Data when app is closed
    public void createSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // save values to sharedPreferences
        myEdit.putBoolean("notificationSetting", notification);
        myEdit.putBoolean("notificationSetByUser", notificationSetByUser);

        // commit sharedPreferences
        myEdit.apply();

    }

    // Retrieve shared preferences file
    public void getSharedPreferences(){
        // get values from shared preferences
        SharedPreferences sh = this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        // retrieve variables from file
        notification = sh.getBoolean("notificationSetting", NotificationManagerCompat.from(this).areNotificationsEnabled());
        notificationSetByUser = sh.getBoolean("notificationSetByUser", false);

        Log.d("TAG", "are notifications enabled?? timerActivity " + NotificationManagerCompat.from(this).areNotificationsEnabled());
        Log.d("TAG", "notification timerActivity " + notification);
    }
}