package com.example.cardscorecalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    Button b_scorekeep, b_timer, b_dice;
    ImageButton b_chat, b_help, b_settings;
    boolean darkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // gets shared prefs
        getSharedPreferences();

        // connect vars to views
        b_scorekeep = findViewById(R.id.b_scorekeep);
        b_timer = findViewById(R.id.b_timer);
        b_dice = findViewById(R.id.b_dice);
        b_chat = findViewById(R.id.b_chat);
        b_help = findViewById(R.id.b_help);
        b_settings = findViewById(R.id.b_settings);

        // if darkmode was on, turn it on
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // opens page to choose scorekeep mode
        b_scorekeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, ScoreKeepModeActivity.class);
                startActivity(in);
            }
        });

        // opens timer/stopwatch activity
        b_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, TimerActivity.class);
                startActivity(in);
            }
        });

        // opens dice activity
        b_dice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, DiceActivity.class);
                startActivity(in);
            }
        });

        // opens bug submit form
        b_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSfRnSjD8cKusjZDtrJ1xJ1KVXtUtbndSewzRfz63HLrFsaB9A/viewform?usp=sf_link"));
                startActivity(intent);
            }
        });

        // opens help page
        b_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(in);
            }
        });

        // opens settings page
        b_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(in);
            }
        });

    }

    // retrieve variables from file
    // default value of darkmode is based on system settings
    public void getSharedPreferences(){
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        darkMode = sh.getBoolean("darkMode", isNightMode(this));
    }

    public boolean isNightMode(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
}