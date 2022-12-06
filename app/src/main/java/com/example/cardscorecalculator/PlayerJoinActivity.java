package com.example.cardscorecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;

public class PlayerJoinActivity extends AppCompatActivity {
    Button b_join;
    EditText et_roomCode, et_name;
    String userName, roomCode;
    MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_join);

        b_join = findViewById(R.id.b_joinRoom);
        et_roomCode = findViewById(R.id.et_roomCodePlayerJoin);
        et_name = findViewById(R.id.et_namePlayerJoin);
        topAppBar = findViewById(R.id.topAppBarPlayerJoin);

        // sets the app bar back button function
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // connects to firebase database
        DAORoom dao = new DAORoom();

        // player
        b_join.setOnClickListener( v->{
            // TODO: some input validation here
            roomCode = et_roomCode.getText().toString();
            userName = et_name.getText().toString();

            // add player to room in the database
            while(!dao.addPlayer(roomCode, userName, false)){
                Toast.makeText(getApplicationContext(),"User already exists, please choose new nickname.",Toast.LENGTH_SHORT).show();
            }

            // need to add delay so database has time to update
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent in = new Intent(PlayerJoinActivity.this, OnlineScoreboardActivity.class);
                    in.putExtra("roomCode", roomCode);
                    in.putExtra("enteredName", userName);
                    in.putExtra("isHost", false);
                    startActivity(in);
                }
            }, 400);

        });

    }

    // when you turn the phone, this function is called to save any data you wish to save
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        // save data
        outState.putString("currentRoomCode", et_roomCode.getText().toString());
        outState.putString("currentName", et_name.getText().toString());

        Log.d("TAG", "room code enter " + et_roomCode.getText().toString());
        super.onSaveInstanceState(outState);
    }

    // when phone is done turning, this function is called to restore any of that data you saved
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle saved) {
        // get values from saved state
        Log.d("TAG", saved.getString("currentRoomCode"));
        String code = saved.getString("currentRoomCode");
        String name = saved.getString("currentName");

        et_roomCode.setText(code);
        et_name.setText(name);

        super.onRestoreInstanceState(saved);
    }
}