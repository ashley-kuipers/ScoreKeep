package com.example.cardscorecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class OnlineScoreboardActivity extends AppCompatActivity {
    String roomCode, userName;
    ListView scoreboard;
    ArrayList<String> leaderboard = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_scoreboard);

        scoreboard = findViewById(R.id.lv_leaderboard);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.custom_listview, android.R.id.text1, leaderboard);
        scoreboard.setAdapter(adapter);

        Bundle bun = getIntent().getExtras();
        roomCode = bun.getString("roomCode");
        userName = bun.getString("enteredName");
        Log.d("TAG", "Room Code  " + roomCode + ", with " + userName);

        DAORoom dao = new DAORoom();
        DatabaseReference room = dao.getScores(roomCode);



//        room.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.e("firebase", "Error getting data", task.getException());
//                }
//                else {
//                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                    HashMap<String, String> userList = (HashMap<String, String>) task.getResult().child("user_list").getValue();
//
//                    if (userList != null){
//                        for ( String value : userList.values() ) {
//                            HashMap<String, Integer> scores = (HashMap<String, Integer>) task.getResult().child(value).getValue();
//                            leaderboard.add(value + " " + scores.get("score"));
//                            adapter.notifyDataSetChanged();
//
//                        }
//                    }
//
//                }
//            }
//        });

        room.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                leaderboard.clear();
                HashMap<String, String> changedScores = (HashMap<String, String>) snapshot.child("user_list").getValue();
                if (changedScores != null){
                    for ( String value : changedScores.values() ) {
                        HashMap<String, Integer> scores = (HashMap<String, Integer>) snapshot.child(value).getValue();
                        leaderboard.add(value + " " + scores.get("score"));
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}