package com.example.cardscorecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.appbar.MaterialToolbar;

public class ScoreKeepModeActivity extends AppCompatActivity {
    Button b_local, b_online, b_sports;
    MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_keep_mode);

        // connect vars with views
        b_local = findViewById(R.id.b_local);
        b_online = findViewById(R.id.b_online);
        b_sports = findViewById(R.id.b_sports);
        topAppBar = findViewById(R.id.topAppBarScoreKeepMode);

        // sets the app bar back button function
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Opens offline score mode
        b_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ScoreKeepModeActivity.this, LocalSetup.class);
                startActivity(in);
            }
        });

        // opens online score mode
        b_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ScoreKeepModeActivity.this, OnlineSetupActivity.class);
                startActivity(in);
            }
        });

        // opens head to head mode
        b_sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ScoreKeepModeActivity.this, SportSetupActivity.class);
                startActivity(in);
            }
        });


    }
}