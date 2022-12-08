package com.example.cardscorecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

public class HeadtoHeadSetupActivity extends AppCompatActivity {
    Button b_start;
    TextView teamL, teamR, points, winPoints;
    MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_setup);

        // connect vars to view
        b_start = findViewById(R.id.b_startSport);
        teamL = findViewById(R.id.et_teamLName);
        teamR = findViewById(R.id.et_teamRName);
        points = findViewById(R.id.et_points);
        winPoints = findViewById(R.id.et_win);
        topAppBar = findViewById(R.id.topAppBarSportSetup);

        // sets the app bar back button function
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // starts the game with the inputted values
        b_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(HeadtoHeadSetupActivity.this, HeadtoHeadScoreActivity.class);
                in.putExtra("teamL", teamL.getText().toString());
                in.putExtra("teamR", teamR.getText().toString());
                in.putExtra("points", Integer.parseInt(points.getText().toString()));
                String str = winPoints.getText().toString();
                if(str.length() > 0){
                    in.putExtra("win", Integer.parseInt(str));
                } else {
                    in.putExtra("win", 999999999);
                }
                startActivity(in);
            }
        });
    }

    // when you turn the phone, this function is called to save any data you wish to save
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        // save roll and current et_numSides
        outState.putString("teamLName", teamL.getText().toString());
        outState.putString("teamRName", teamR.getText().toString());
        outState.putString("points", points.getText().toString());
        outState.putString("winCond", winPoints.getText().toString());

        super.onSaveInstanceState(outState);
    }

    // when phone is done turning, this function is called to restore any of that data you saved
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle saved) {
        // get values from saved state
        teamL.setText(saved.getString("teamLName"));
        teamR.setText(saved.getString("teamRName"));
        points.setText(saved.getString("points"));
        winPoints.setText(saved.getString("winCond"));

        super.onRestoreInstanceState(saved);
    }
}