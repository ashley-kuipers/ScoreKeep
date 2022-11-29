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
    TextView t_roomCodeTitle;
    EditText et_roomCode;
    String userName, roomCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_join);

        b_join = findViewById(R.id.b_joinRoom);
        et_roomCode = findViewById(R.id.et_roomCode);
        t_roomCodeTitle = findViewById(R.id.t_enterRoomCode);

        // get data from intent
        Bundle bun = getIntent().getExtras();
        userName = bun.getString("enteredName");

        String out = "Hello " + userName.toUpperCase() + ", enter the Room Code provided by your host:";
        t_roomCodeTitle.setText(out);

        DAORoom dao = new DAORoom();
        b_join.setOnClickListener( v->{
            // TODO: some input validation here
            roomCode = et_roomCode.getText().toString();

            Player player = new Player(userName, 0);
            dao.addPlayer(roomCode, player);

            // need to add delay so database has time to update
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent in = new Intent(PlayerJoinActivity.this, OnlineScoreboardActivity.class);
                    in.putExtra("roomCode", roomCode);
                    in.putExtra("enteredName", userName);
                    startActivity(in);
                }
            }, 400);

        });

    }
}