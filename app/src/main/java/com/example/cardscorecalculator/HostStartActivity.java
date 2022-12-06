package com.example.cardscorecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class HostStartActivity extends AppCompatActivity {
    String roomCode = "", userName;
    Button b_openRoom;
    TextView t_roomCode;
    EditText et_name;
    MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_start);

        // connect vars to views
        b_openRoom = findViewById(R.id.b_openRoom);
        t_roomCode = findViewById(R.id.t_roomCode);
        et_name = findViewById(R.id.et_name);
        topAppBar = findViewById(R.id.topAppBarHostStart);

        // sets the app bar back button function
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(HostStartActivity.this, OnlineSetupActivity.class);
                startActivity(in);
            }
        });

        // create room code
        Random rand = new Random();
        String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        for(int i = 0; i<4; i++){
            int randNum = rand.nextInt(26);
            roomCode += alphabet[randNum];
        }
        // put room code on screen
        t_roomCode.setText(roomCode);

        // access database to create room and add username to room
        DAORoom dao = new DAORoom();
        b_openRoom.setOnClickListener( v->{
            // get username from player
            userName = et_name.getText().toString();

            // create a new player object and add to database
            while(!dao.addPlayer(roomCode, userName, true)){
                Toast.makeText(getApplicationContext(),"User already exists, please choose new nickname.",Toast.LENGTH_SHORT).show();
            }
            Log.d("TAG", "HostStart: player was created");

            // Open score board
            Intent in = new Intent(HostStartActivity.this, OnlineScoreboardActivity.class);
            in.putExtra("roomCode", roomCode);
            in.putExtra("enteredName", userName);
            in.putExtra("isHost", true);
            startActivity(in);
        });

    }

    // Returns a formatted string of the current system time
    public String getTime(){
        // get the time when the new code was created
        long time = System.currentTimeMillis();
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.CANADA);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return formatter.format(calendar.getTime());
    }

    // when you turn the phone, this function is called to save any data you wish to save
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        // save data
        outState.putString("currentNickname", et_name.getText().toString());
        outState.putString("currentCode", t_roomCode.getText().toString());

        super.onSaveInstanceState(outState);
    }

    // when phone is done turning, this function is called to restore any of that data you saved
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle saved) {
        // get values from saved state
        et_name.setText(saved.getString("currentNickname"));
        t_roomCode.setText(saved.getString("currentCode"));

        super.onRestoreInstanceState(saved);
    }
}