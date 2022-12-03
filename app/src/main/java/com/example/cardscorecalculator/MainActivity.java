package com.example.cardscorecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    Button b_scorekeep, b_timer, b_dice;
    ImageButton b_chat, b_help, b_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b_scorekeep = findViewById(R.id.b_scorekeep);
        b_timer = findViewById(R.id.b_timer);
        b_dice = findViewById(R.id.b_dice);
        b_chat = findViewById(R.id.b_chat);
        b_help = findViewById(R.id.b_help);
        b_settings = findViewById(R.id.b_settings);

        b_scorekeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, ScoreKeepModeActivity.class);
                startActivity(in);
            }
        });

        // TODO: create time activity
        b_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, TimerActivity.class);
                startActivity(in);
            }
        });

        // TODO: create dice activity
        b_dice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, DiceActivity.class);
                startActivity(in);
            }
        });

        // TODO: create google form to submit bugs
        b_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, ScoreKeepModeActivity.class);
                startActivity(in);
            }
        });

        // TODO: create google form to submit bugs
        b_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, ScoreKeepModeActivity.class);
                startActivity(in);
            }
        });

        // TODO: create google form to submit bugs
        b_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, ScoreKeepModeActivity.class);
                startActivity(in);
            }
        });
    }
}