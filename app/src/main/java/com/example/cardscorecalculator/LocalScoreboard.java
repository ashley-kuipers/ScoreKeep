package com.example.cardscorecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class LocalScoreboard extends AppCompatActivity {
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<EditText> et_scores = new ArrayList<EditText>();
    ArrayList<TextView> tv_scores = new ArrayList<TextView>();
    ArrayList<Integer> scores = new ArrayList<Integer>();
    HashMap<Integer, String> map= new HashMap<Integer, String>();;
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

            //editText where user enters score
            EditText et = new EditText(context, null, 0, R.style.et_scores);
            et.setHint(R.string.et_newScore);
            et.setLayoutParams(lp);
            tr.addView(et);
            // add to array to retrieve inputted scores later
            et_scores.add(et);

            // add default score of 0 to array that keeps track of score
            scores.add(0);

            // Add row to layout
            tl.addView(tr);
        }

        b_addScores.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                for(int i = 0; i < names.size(); i++){
                    // calculate current score and find name of each player
                    int score = Integer.parseInt(et_scores.get(i).getText().toString()) + scores.get(i);
                    String name = names.get(i);

                    // save score to array
                    scores.set(i, score);

                    // put calculated score into the "current score" column of table on screen
                    tv_scores.get(i).setText(String.valueOf(score));

                    et_scores.get(i).setText("");
                }
            }
        });

        b_endGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // enter final scores and player names into a hashtable
                for(int i = 0; i < names.size(); i++) {
                    map.put(scores.get(i), names.get(i));
                }

                // start endgame activity and send hashtable
                LocalEndGame endGame = new LocalEndGame();
                Intent in = new Intent(context, LocalEndGame.class);
                in.putExtra("scores", map);
                startActivity(in);
            }
        });

    }
}