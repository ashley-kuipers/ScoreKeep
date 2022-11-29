package com.example.cardscorecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class OnlineSetupActivity extends AppCompatActivity {
    Button b_startOL, b_joinOL;
    EditText et_playerNameOL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_setup);

        b_startOL = findViewById(R.id.b_startOL);
        b_joinOL = findViewById(R.id.b_joinOL);
        et_playerNameOL = findViewById(R.id.et_playerNameOL);

        b_startOL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open HostStartActivity
                String output = et_playerNameOL.getText().toString().toLowerCase();

                Intent in = new Intent(OnlineSetupActivity.this, HostStartActivity.class);
                in.putExtra("enteredName", output);
                startActivity(in);
            }
        });

        b_joinOL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open PlayerJoinActivity
                String output = et_playerNameOL.getText().toString().toLowerCase();

                Intent in = new Intent(OnlineSetupActivity.this, PlayerJoinActivity.class);
                in.putExtra("enteredName", output);
                startActivity(in);
            }
        });
    }
}