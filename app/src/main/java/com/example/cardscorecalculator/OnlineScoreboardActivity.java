package com.example.cardscorecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class OnlineScoreboardActivity extends AppCompatActivity {
    String roomCode, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_scoreboard);

        Bundle bun = getIntent().getExtras();
        roomCode = bun.getString("roomCode");
        userName = bun.getString("enteredName");
        Log.d("TAG", "Room Code  " + roomCode + ", with " + userName);

        DAORoom dao = new DAORoom();
        DatabaseReference room = dao.getScores(roomCode);

        room.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    HashMap<String, String> userList = (HashMap<String, String>) task.getResult().child("user_list").getValue();

                    for ( String value : userList.values() ) {
                        HashMap<String, Integer> scores = (HashMap<String, Integer>) task.getResult().child(value).getValue();

                        // add name and score to listview
                        Log.d("TAG", "name " + value + ", score " + scores.get("score"));

                    }

                }
            }
        });



    }
}