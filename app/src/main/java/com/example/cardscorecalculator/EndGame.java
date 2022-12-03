package com.example.cardscorecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.TreeMap;

public class EndGame extends AppCompatActivity {
    HashMap<Integer, String> map;
//    TreeMap<Integer, String> tm;
    LinearLayout results;
    TextView t_winner;
    Button b_playAgain;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        map = (HashMap<Integer, String>) getIntent().getSerializableExtra("scores");
        results = findViewById(R.id.results);
        t_winner = findViewById(R.id.t_winner);
        b_playAgain = findViewById(R.id.b_playAgain);
        context = getBaseContext();

        // Copy all data from hashMap into TreeMap (sorts it by score)
        TreeMap<Integer, String> sorted = new TreeMap<>(map);

        Object[] sortedNames = sorted.values().toArray();
        Object[] sortedScores = sorted.keySet().toArray();

        // Update winner textview
        String winnerName = String.valueOf(sortedNames[sortedNames.length-1]);
        Integer winnerScore = Integer.parseInt(String.valueOf(sortedScores[sortedScores.length - 1]));
        String winOutput = "\uD83C\uDFC6 " + winnerName + " . . . " + winnerScore + " \uD83C\uDFC6";
        t_winner.setText(winOutput);

        // Add second place textview
        if(sortedNames.length > 1){
            TextView secondPlace = new TextView(this);
            String secondOutput = "\uD83E\uDD48 " + String.valueOf(sortedNames[sortedNames.length-2]) + " . . . " + String.valueOf(sortedScores[sortedScores.length-2]) + " \uD83E\uDD48";
            secondPlace.setText(secondOutput);
            secondPlace.setTextAppearance(this, R.style.h2);
            secondPlace.setPadding(0, 20, 0, 20);
            results.addView(secondPlace);
        }

        // Add third place textview
        if(sortedNames.length > 2){
            TextView thirdPlace = new TextView(this);
            String thirdOutput = "\uD83E\uDD49 " + String.valueOf(sortedNames[sortedNames.length-3]) + " . . . " + String.valueOf(sortedScores[sortedScores.length-3]) + " \uD83E\uDD49";
            thirdPlace.setText(thirdOutput);
            thirdPlace.setTextAppearance(this, R.style.h2);
            results.addView(thirdPlace);
        }

        if(sortedNames.length>3){
            for(int i = sortedNames.length-4; i >= 0; i--){
                Log.d("TAG", String.valueOf(sortedNames[i]));
                TextView place = new TextView(this);
                String placeOutput = String.valueOf(sortedNames[i]) + " . . . " + String.valueOf(sortedScores[i]);
                place.setText(placeOutput);
                place.setTextAppearance(this, R.style.h2);
                results.addView(place);
            }
        }

        b_playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, MainActivity.class);
                startActivity(in);
            }
        });

    }
}