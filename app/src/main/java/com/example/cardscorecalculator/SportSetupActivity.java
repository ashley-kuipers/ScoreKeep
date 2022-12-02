package com.example.cardscorecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SportSetupActivity extends AppCompatActivity {
    Button b_start;
    TextView teamL, teamR, points, winPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_setup);

        b_start = findViewById(R.id.b_startSport);
        teamL = findViewById(R.id.et_teamLName);
        teamR = findViewById(R.id.et_teamRName);
        points = findViewById(R.id.et_points);
        winPoints = findViewById(R.id.et_win);

        b_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(SportSetupActivity.this, SportScoreActivity.class);
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
}