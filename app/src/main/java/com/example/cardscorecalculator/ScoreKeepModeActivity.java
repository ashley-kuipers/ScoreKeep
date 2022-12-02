package com.example.cardscorecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ScoreKeepModeActivity extends AppCompatActivity {
    Button b_local, b_online, b_sports;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_keep_mode);

        b_local = findViewById(R.id.b_local);
        b_online = findViewById(R.id.b_online);
        b_sports = findViewById(R.id.b_sports);

        b_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ScoreKeepModeActivity.this, LocalSetup.class);
                startActivity(in);
            }
        });

        b_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ScoreKeepModeActivity.this, OnlineSetupActivity.class);
                startActivity(in);
            }
        });

        b_sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ScoreKeepModeActivity.this, SportSetupActivity.class);
                startActivity(in);
            }
        });


    }
}