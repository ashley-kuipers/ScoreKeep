package com.example.cardscorecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class PlayerJoinActivity extends AppCompatActivity {
    Button b_join;
    EditText et_roomCode, et_name;
    String userName, roomCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_join);

        b_join = findViewById(R.id.b_joinRoom);
        et_roomCode = findViewById(R.id.et_roomCode);
        et_name = findViewById(R.id.et_name2);

        DAORoom dao = new DAORoom();

        b_join.setOnClickListener( v->{
            // TODO: some input validation here
            roomCode = et_roomCode.getText().toString();
            userName = et_name.getText().toString();

            // add player to room in the database
            dao.addPlayer(roomCode, userName, false);

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
}