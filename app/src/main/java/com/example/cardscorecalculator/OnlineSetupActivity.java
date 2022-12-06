package com.example.cardscorecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.appbar.MaterialToolbar;

public class OnlineSetupActivity extends AppCompatActivity {
    Button b_startOL, b_joinOL;
    MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_setup);

        // connect vars to views
        b_startOL = findViewById(R.id.b_startOL);
        b_joinOL = findViewById(R.id.b_joinOL);
        topAppBar = findViewById(R.id.topAppBarOnlineSetup);

        // sets the app bar back button function
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // sets player as host
        b_startOL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open HostStartActivity
                Intent in = new Intent(OnlineSetupActivity.this, HostStartActivity.class);
                startActivity(in);
            }
        });

        // opens player join activity
        b_joinOL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open PlayerJoinActivity
                Intent in = new Intent(OnlineSetupActivity.this, PlayerJoinActivity.class);
                startActivity(in);
            }
        });
    }
}