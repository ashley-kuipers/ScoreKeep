package com.example.cardscorecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

public class LocalScoreboard extends AppCompatActivity {
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<EditText> et_scores = new ArrayList<EditText>();
    ArrayList<TextView> tv_scores = new ArrayList<TextView>();
    ArrayList<Integer> scores = new ArrayList<Integer>();
    ArrayList<String> currentText = new ArrayList<String>();
    HashMap<Integer, String> map= new HashMap<Integer, String>();
    Context context;
    Button b_addScores, b_endGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_scoreboard);
        b_addScores = findViewById(R.id.b_addscores);
        b_endGame = findViewById(R.id.b_endgamelocal);
        names = getIntent().getStringArrayListExtra("namesArray");
        context = getBaseContext();

        // Create new row in scoreboard for every player
        for (int i = 0; i < names.size(); i++) {
            // Find Tablelayout defined in activity
            TableLayout tl = findViewById(R.id.local_scoreboardTable);

            // Create new row
            TableRow tr = new TableRow(this);
            if (i % 2 == 0) {
                tr.setBackgroundColor(getResources().getColor(R.color.pink_dark));
            } else {
                tr.setBackgroundColor(getResources().getColor(R.color.pink));
            }

            // Create content of row
            // Textview with player name
            TextView tvn = new TextView(this);
            tvn.setText(names.get(i));
            tvn.setTextAppearance(context, R.style.scoreboardNames);
            if (i % 2 == 0) {
                tvn.setBackgroundColor(getResources().getColor(R.color.pink_dark));
            } else {
                tvn.setBackgroundColor(getResources().getColor(R.color.pink));
            }
            tvn.setPadding(0, 20, 0, 0);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1);
            tvn.setLayoutParams(lp);
            tr.addView(tvn);

            // Textview with current score
            TextView tvs = new TextView(this);
            tvs.setText("0");
            tvs.setTextAppearance(context, R.style.scoreboardNames);
            tvs.setPadding(0, 20, 0, 0);
            if (i % 2 == 0) {
                tvs.setBackgroundColor(getResources().getColor(R.color.pink_dark));
            } else {
                tvs.setBackgroundColor(getResources().getColor(R.color.pink));
            }
            tvs.setLayoutParams(lp);
            tr.addView(tvs);
            // add to array to retrieve previous scores later
            tv_scores.add(tvs);

            // editText where user enters score
            EditText et = new EditText(context, null, 0, R.style.et_scores);
            if (i % 2 == 0) {
                et.setBackground(getResources().getDrawable(R.drawable.et_selector_opposite));
            }
            et.setHint(R.string.et_newScore);
            lp.setMargins(20, 20, 20, 20);
            et.setLayoutParams(lp);
            et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
            tr.addView(et);
            // add to array to retrieve inputted scores later
            et_scores.add(et);

            // add default score of 0 to array that keeps track of score
            scores.add(0);

            // Add row to layout
            tl.addView(tr);

        }

        // adds all the entered scores to everyone's current score
        b_addScores.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                for(int i = 0; i < names.size(); i++){
                    // calculate current score and find name of each player
                    int score = Integer.parseInt(et_scores.get(i).getText().toString()) + scores.get(i);

                    // save score to array
                    scores.set(i, score);

                    // put calculated score into the "current score" column of table on screen
                    tv_scores.get(i).setText(String.valueOf(score));

                    et_scores.get(i).setText("");
                }
            }
        });

        // ends the game and sends the scores to the EndGame activity
        b_endGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // enter final scores and player names into a hashtable
                for(int i = 0; i < names.size(); i++) {
                    map.put(scores.get(i), names.get(i));
                }

                // start endgame activity and send hashtable
                Intent in = new Intent(context, EndGame.class);
                in.putExtra("scores", map);
                startActivity(in);
            }
        });
    }

    // when you turn the phone, this function is called to save any data you wish to save
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        // save values in GameBoard
        outState.putIntegerArrayList("scores", scores);
        outState.putStringArrayList("names", names);


        for(int i = 0; i < names.size(); i++){
            // calculate current score and find name of each player
            currentText.add(i, et_scores.get(i).getText().toString());
        }
        outState.putStringArrayList("currentText", currentText);
        Log.d("TAG", "saved");
        super.onSaveInstanceState(outState);
    }

    // when phone is done turning, this function is called to restore any of that data you saved
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle saved) {
        // get values from saved state
        scores = saved.getIntegerArrayList("scores");
        names = saved.getStringArrayList("names");
        currentText = saved.getStringArrayList("currentText");

        for(int i = 0; i < names.size(); i++){
            // calculate current score and find name of each player
            int score = scores.get(i);

            // save score to array
            scores.set(i, score);

            tv_scores.get(i).setText(String.valueOf(scores.get(i)));

            et_scores.get(i).setText(currentText.get(i));
        }

        Log.d("TAG", "restored");

        super.onRestoreInstanceState(saved);
    }
}
