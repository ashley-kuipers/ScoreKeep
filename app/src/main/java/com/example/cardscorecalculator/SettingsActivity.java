package com.example.cardscorecalculator;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {
    SwitchCompat switch_darkmode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        switch_darkmode = findViewById(R.id.switch_darkmode);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            switch_darkmode.setChecked(false);
            switch_darkmode.getTrackDrawable().setColorFilter(ContextCompat.getColor(SettingsActivity.this, R.color.pink_dark), PorterDuff.Mode.SRC_IN);
        } else {
            switch_darkmode.setChecked(true);
            switch_darkmode.getTrackDrawable().setColorFilter(ContextCompat.getColor(SettingsActivity.this, R.color.pink_light), PorterDuff.Mode.SRC_IN);
        }

        switch_darkmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    switch_darkmode.getTrackDrawable().setColorFilter(ContextCompat.getColor(SettingsActivity.this, R.color.pink_light), PorterDuff.Mode.SRC_IN);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    switch_darkmode.getTrackDrawable().setColorFilter(ContextCompat.getColor(SettingsActivity.this, R.color.pink_dark), PorterDuff.Mode.SRC_IN);
                }
            }
        });
    }

}