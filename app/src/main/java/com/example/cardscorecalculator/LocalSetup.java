package com.example.cardscorecalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class LocalSetup extends AppCompatActivity {
    EditText e_numPlayers;
    Button b_start;
    LinearLayout layout_setup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_setup);

        e_numPlayers = findViewById(R.id.et_numPlayers);
        b_start = findViewById(R.id.b_start);
        layout_setup = findViewById(R.id.layout_setup);

        b_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: input validation here
                int numPlayers = Integer.parseInt(e_numPlayers.getText().toString());

                // set up new fragment manager
                FragmentManager fr = getSupportFragmentManager();
                FragmentTransaction ft = fr.beginTransaction();
                Frag_LocalEnterPlayerNames names = new Frag_LocalEnterPlayerNames();

                // package info and send to fragment
                Bundle bundle = new Bundle();
                bundle.putInt("numPlayers", numPlayers);
                names.setArguments(bundle);

                // actually brings up cityInfo (replaces any instance of cityInfo fragment currently up)
                ft.replace(R.id.frame_enterNames, names);
                ft.commit();

                layout_setup.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                Log.d("TAG", "Setup: add fragment");

            }
        });

    }
}