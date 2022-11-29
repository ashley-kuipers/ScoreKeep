package com.example.cardscorecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HostStartActivity extends AppCompatActivity {
    String roomCode = "", userName;
    Button b_openRoom;
    TextView t_roomCode, t_roomCodeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_start);

        b_openRoom = findViewById(R.id.b_openRoom);
        t_roomCode = findViewById(R.id.t_roomCode);
        t_roomCodeTitle = findViewById(R.id.t_roomCodeTitle);

        // get data from intent
        Bundle bun = getIntent().getExtras();
        userName = bun.getString("enteredName");

        String out = "Hello " + userName.toUpperCase() + ", your room code will be:";
        t_roomCodeTitle.setText(out);

        // create room code
        Random rand = new Random();
        String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

        for(int i = 0; i<4; i++){
            int randNum = rand.nextInt(26);
            roomCode += alphabet[randNum];
        }

        t_roomCode.setText(roomCode);

        DAORoom dao = new DAORoom();
        b_openRoom.setOnClickListener( v->{
            Player player = new Player(userName, 0);
            dao.addPlayer(roomCode, player);

            // Open score list intent
            Intent in = new Intent(HostStartActivity.this, OnlineScoreboardActivity.class);
            in.putExtra("roomCode", roomCode);
            startActivity(in);
        });

    }
}