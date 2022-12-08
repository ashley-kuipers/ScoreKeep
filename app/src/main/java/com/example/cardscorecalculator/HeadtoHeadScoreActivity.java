package com.example.cardscorecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;

public class HeadtoHeadScoreActivity extends AppCompatActivity {
    String teamL, teamR;
    int points = 1, win = 999999999;
    TextView t_teamL, t_teamR, t_teamLscore, t_teamRscore;
    Button b_endGame;
    FloatingActionButton b_teamLAdd, b_teamRAdd, b_teamLSub, b_teamRSub;
    HashMap<Integer, String> scoresMap = new HashMap<Integer, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_sport_score);

        // get data from intents
        Bundle bun = getIntent().getExtras();
        teamL = bun.getString("teamL");
        teamR = bun.getString("teamR");
        points = bun.getInt("points");
        win = bun.getInt("win");

        // update team names on screen
        t_teamL = findViewById(R.id.t_teamL);
        t_teamR = findViewById(R.id.t_teamR);
        t_teamL.setText(teamL.toUpperCase());
        t_teamR.setText(teamR.toUpperCase());

        // connect vars to views
        b_teamLAdd = findViewById(R.id.fab_addL);
        b_teamRAdd = findViewById(R.id.fab_addR);
        b_teamLSub = findViewById(R.id.fab_minusL);
        b_teamRSub = findViewById(R.id.fab_minusR);
        b_endGame = findViewById(R.id.b_endGameScore);
        t_teamLscore = findViewById(R.id.t_teamLScore);
        t_teamRscore = findViewById(R.id.t_teamRScore);

        // adds to teamL's score and checks for win
        b_teamLAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newScore = points + Integer.parseInt((String) t_teamLscore.getText());
                t_teamLscore.setText(newScore + "");
                if(newScore >= win){
                    b_endGame.performClick();
                }
            }
        });

        // adds to teamR's score and checks for win
        b_teamRAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newScore = points + Integer.parseInt((String) t_teamRscore.getText());
                t_teamRscore.setText(newScore + "");
                if(newScore >= win){
                    b_endGame.performClick();
                }
            }
        });

        // subtract from teamL's score
        b_teamLSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newScore = Integer.parseInt((String) t_teamLscore.getText()) - points ;
                t_teamLscore.setText(newScore + "");
            }
        });

        // subtract from teamR's score
        b_teamRSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newScore = Integer.parseInt((String) t_teamRscore.getText()) - points;
                t_teamRscore.setText(newScore + "");
            }
        });

        // ends the game and sends the score to EndGame activity
        b_endGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoresMap.put(Integer.parseInt((String) t_teamLscore.getText()), teamL.toUpperCase());
                scoresMap.put(Integer.parseInt((String) t_teamRscore.getText()), teamR.toUpperCase());

                if(win != 999999999){
                    openDialog("We have a winner!");
                    Log.d("TAG", "winner");
                } else {
                    leaveActivity();
                }

            }
        });
    }

    // opens an alert dialog when someone wins if there is a win condition chosen
    public void openDialog(String text){
        AlertSportWinner wd = new AlertSportWinner(text);
        wd.show(getSupportFragmentManager(), "dialog");
    }

    // opens the end game activity and sends the scores to it
    public void leaveActivity(){
        Intent in = new Intent(HeadtoHeadScoreActivity.this, EndGame.class);
        in.putExtra("scores", scoresMap);
        startActivity(in);
    }
}