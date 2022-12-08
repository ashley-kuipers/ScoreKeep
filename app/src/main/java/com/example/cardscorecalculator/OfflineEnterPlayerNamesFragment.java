package com.example.cardscorecalculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputType;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class OfflineEnterPlayerNamesFragment extends Fragment {
    LinearLayout layout_enterNames;
    Context context;
    Button b_addNames;
    ArrayList<EditText> inputs = new ArrayList<EditText>();

    public OfflineEnterPlayerNamesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_enter_player_names, container, false);
        context = getContext();

        // connect vars to views and initialize initial variables
        int numPlayers;
        layout_enterNames = view.findViewById(R.id.layout_enterNames);
        b_addNames = view.findViewById(R.id.b_addNames);
        ArrayList<String> names = new ArrayList<String>();

        // gets the number of players from the calling activity
        Bundle bundle = getArguments();
        if (bundle != null){
            numPlayers = bundle.getInt("numPlayers");
        } else {
            // defaults to 4 players if none specified
            numPlayers = 4;
        }

        // adds an editText asking for the player name based on number of players provided
        for(int i=0; i < numPlayers; i++){
            // creates an editText and adds to view
            EditText editText = new EditText(context, null, 0, R.style.et_filled);
            editText.setHint("Player " + (i + 1));
            editText.setPadding(150, 25, 150, 25);
            editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 10, 0, 30);
            editText.setLayoutParams(lp);
            layout_enterNames.addView(editText);

            // adds each editText to an arraylist to be accessed later
            inputs.add(editText);
        }

        // gets the players names from the editTexts and sends to scoreboard activity
        b_addNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // retrieves player names from each editText
                for(int i=0; i < inputs.size(); i++) {
                    names.add(inputs.get(i).getText().toString());
                }

                // opens scoreboard page with the provided player names
                Intent in = new Intent(context, OfflineScoreboard.class);
                in.putExtra("namesArray", names);
                startActivity(in);
            }
        });

        // retrieves data from instance state if it exists
        if (savedInstanceState != null) {
            for(int i=0; i < inputs.size(); i++) {
                inputs.get(i).setText(savedInstanceState.getString("player" + i));
            }
        }

        // Inflate the layout for this fragment
        return view;
    }

    // when you turn the phone, this function is called to save any data you wish to save
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // save data
        for(int i=0; i < inputs.size(); i++) {
            outState.putString("player" + i, inputs.get(i).getText().toString());
        }
    }
}