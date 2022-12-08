package com.example.cardscorecalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class SettingsActivity extends AppCompatActivity {
    SwitchCompat switch_darkmode, switch_notification, switch_sound;
    MaterialToolbar topAppBar;
    boolean darkMode, sound, notification, notificationSetByUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        topAppBar = findViewById(R.id.topAppBarSettings);
        switch_darkmode = findViewById(R.id.switch_darkmode);
        switch_notification = findViewById(R.id.switch_notification);
        switch_sound = findViewById(R.id.switch_sound);

        // checks shared preferences and sets default switch position accordingly
        getSharedPreferences();
        if (darkMode) {
            // set switch on
            switch_darkmode.setChecked(true);
            switch_darkmode.getTrackDrawable().setColorFilter(ContextCompat.getColor(SettingsActivity.this, R.color.pink_light), PorterDuff.Mode.SRC_IN);
        } else {
            // switch off
            switch_darkmode.setChecked(false);
            switch_darkmode.getTrackDrawable().setColorFilter(ContextCompat.getColor(SettingsActivity.this, R.color.pink_dark), PorterDuff.Mode.SRC_IN);
        }

        if (sound) {
            // set switch on
            switch_sound.setChecked(true);
            switch_sound.getTrackDrawable().setColorFilter(ContextCompat.getColor(SettingsActivity.this, R.color.pink_light), PorterDuff.Mode.SRC_IN);
        } else {
            // switch off
            switch_sound.setChecked(false);
            switch_sound.getTrackDrawable().setColorFilter(ContextCompat.getColor(SettingsActivity.this, R.color.pink_dark), PorterDuff.Mode.SRC_IN);
        }

        if (notification) {
            // set switch on
            switch_notification.setChecked(true);
            switch_notification.getTrackDrawable().setColorFilter(ContextCompat.getColor(SettingsActivity.this, R.color.pink_light), PorterDuff.Mode.SRC_IN);
        } else {
            // switch off
            switch_notification.setChecked(false);
            switch_notification.getTrackDrawable().setColorFilter(ContextCompat.getColor(SettingsActivity.this, R.color.pink_dark), PorterDuff.Mode.SRC_IN);
        }

        // sets what happen when dark mode switch is clicked
        switch_darkmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // dark mode on
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    switch_darkmode.getTrackDrawable().setColorFilter(ContextCompat.getColor(SettingsActivity.this, R.color.pink_light), PorterDuff.Mode.SRC_IN);
                    darkMode = true;
                } else {
                    // dark mode off
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    switch_darkmode.getTrackDrawable().setColorFilter(ContextCompat.getColor(SettingsActivity.this, R.color.pink_dark), PorterDuff.Mode.SRC_IN);
                    darkMode = false;
                }
                createSharedPreferences();
            }
        });

        // sets what happen when switch is clicked
        switch_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // sound on
                    switch_sound.getTrackDrawable().setColorFilter(ContextCompat.getColor(SettingsActivity.this, R.color.pink_light), PorterDuff.Mode.SRC_IN);
                    sound = true;
                } else {
                    // sound off
                    switch_sound.getTrackDrawable().setColorFilter(ContextCompat.getColor(SettingsActivity.this, R.color.pink_dark), PorterDuff.Mode.SRC_IN);
                    sound = false;
                }
                createSharedPreferences();
            }
        });

        // sets what happen when switch is clicked
        switch_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // notifications on
                    switch_notification.getTrackDrawable().setColorFilter(ContextCompat.getColor(SettingsActivity.this, R.color.pink_light), PorterDuff.Mode.SRC_IN);
                    notification = true;
                } else {
                    // notifications off
                    switch_notification.getTrackDrawable().setColorFilter(ContextCompat.getColor(SettingsActivity.this, R.color.pink_dark), PorterDuff.Mode.SRC_IN);
                    notification = false;
                }
                notificationSetByUser = true;
                createSharedPreferences();
            }
        });

        // sets the app bar back button function
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSharedPreferences();
                finish();
            }
        });
    }

    // Save current Data when app is closed
    public void createSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // save values to sharedPreferences
        myEdit.putBoolean("darkMode", darkMode);
        myEdit.putBoolean("soundSetting", sound);
        myEdit.putBoolean("notificationSetting", notification);
        myEdit.putBoolean("notificationSetByUser", notificationSetByUser);

        // commit sharedPreferences
        myEdit.apply();

    }

    // Retrieve shared preferences file
    public void getSharedPreferences(){
        // get values from shared preferences
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        // retrieve variables from file
        // default value of darkmode is based on system settings
        darkMode = sh.getBoolean("darkMode", isNightMode(this));
        sound = sh.getBoolean("soundSetting", true);
        notification = sh.getBoolean("notificationSetting", NotificationManagerCompat.from(this).areNotificationsEnabled());
        notificationSetByUser = sh.getBoolean("notificationSetByUser", false);

    }

    public boolean isNightMode(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }

}