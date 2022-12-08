package com.example.cardscorecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.appbar.MaterialToolbar;

public class OfflineSetup extends AppCompatActivity {
    EditText e_numPlayers;
    Button b_start;
    LinearLayout layout_setup;
    MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_setup);

        // connect vars to views
        e_numPlayers = findViewById(R.id.et_numPlayers);
        b_start = findViewById(R.id.b_start);
        layout_setup = findViewById(R.id.layout_setup);

        topAppBar = findViewById(R.id.topAppBarLocalSetup);

        // sets the app bar back button function
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        b_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: input validation here
                int numPlayers = Integer.parseInt(e_numPlayers.getText().toString());

                // set up new fragment manager
                FragmentManager fr = getSupportFragmentManager();
                FragmentTransaction ft = fr.beginTransaction();
                OfflineEnterPlayerNamesFragment names = new OfflineEnterPlayerNamesFragment();

                // package info and send to fragment
                Bundle bundle = new Bundle();
                bundle.putInt("numPlayers", numPlayers);
                names.setArguments(bundle);

                // actually brings up cityInfo (replaces any instance of cityInfo fragment currently up)
                ft.replace(R.id.frame_enterNames, names);
                ft.commit();

                layout_setup.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            }
        });

    }

    // when you turn the phone, this function is called to save any data you wish to save
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        // save roll and current et_numSides
        outState.putString("numPlayers", e_numPlayers.getText().toString());

        super.onSaveInstanceState(outState);
    }

    // when phone is done turning, this function is called to restore any of that data you saved
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle saved) {
        // get values from saved state
        e_numPlayers.setText(saved.getString("numPlayers"));

        super.onRestoreInstanceState(saved);
    }
}