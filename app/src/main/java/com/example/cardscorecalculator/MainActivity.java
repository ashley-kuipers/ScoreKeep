package com.example.cardscorecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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

        b_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, TimerActivity.class);
                startActivity(in);
            }
        });

        b_dice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, DiceActivity.class);
                startActivity(in);
            }
        });

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

        // TODO: create help page
        b_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(in);
            }
        });

        // TODO: create settings page
        b_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, ScoreKeepModeActivity.class);
                startActivity(in);
            }
        });
    }
}