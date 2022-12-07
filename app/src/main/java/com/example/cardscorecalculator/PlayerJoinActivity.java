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
    EditText et_room, et_name;
    String userName, roomCode;
    MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_join);

        // connect vars to views
        b_join = findViewById(R.id.b_joinRoom);
        et_room = findViewById(R.id.et_room_code_player_join);
        et_name = findViewById(R.id.et_player_join);
        topAppBar = findViewById(R.id.topAppBarPlayerJoin);

        // sets the app bar back button function
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // connects to firebase database
        DAORoom2 dao = new DAORoom2();

        // player
        b_join.setOnClickListener( v->{
            roomCode = et_room.getText().toString();
            userName = et_name.getText().toString();

            // add player to room in the database
            dao.newPlayer(roomCode, userName, false);

            // need to add delay so database has time to update
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // check if they exist in the database?


                    Intent in = new Intent(PlayerJoinActivity.this, OnlineScoreboardActivity.class);
                    in.putExtra("roomCode", roomCode);
                    in.putExtra("enteredName", userName);
                    in.putExtra("isHost", false);
                    startActivity(in);


                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dao.removeEL();
                        }
                    }, 400);
                }
            }, 400);

        });

    }





    // when you turn the phone, this function is called to save any data you wish to save
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // save data
        Log.d("TAG", "saved room code " + et_room.getText().toString());
        outState.putString("currentRoomCode", et_room.getText().toString());
        outState.putString("currentName", et_name.getText().toString());
    }

    // when phone is done turning, this function is called to restore any of that data you saved
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle saved) {
        super.onRestoreInstanceState(saved);

        Log.d("TAG", "retrieved " + saved.getString("currentRoomCode"));
        et_room.setText(saved.getString("currentRoomCode"));
        et_name.setText(saved.getString("currentName"));
        Log.d("TAG", "retrieved");

    }

}