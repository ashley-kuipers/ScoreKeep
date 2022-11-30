package com.example.cardscorecalculator;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DAORoom {
    private DatabaseReference dbr;

    public DAORoom(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        dbr = db.getReference("Rooms");
    }

    public void addPlayer(String roomCode, Player player){
        final int[] currentScore = {0};
        // TODO: notify user that username already exists
        // TODO: when host rejoins, it takes them off host

        // avoids duplicate usernames
        dbr.child(roomCode).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("TAG", "Error getting data", task.getException());
                }
                else {
                    HashMap<String, String> userList = (HashMap<String, String>) task.getResult().child("user_list").getValue();

                    // if the user list doesn't exist or user doesn't already exist
                    if(userList == null || !userList.containsValue(player.getName())){
                        // add new user
                        dbr.child(roomCode).child("user_list").push().setValue(player.getName());
                        Log.d("TAG", "DAO: Added " + player.getName());

                    // else user already exists
                    } else {
                        Log.d("TAG", "DAO: User already exists");

                        // gets current score of player (account for if they got disconnected and rejoin)
                        currentScore[0] = Integer.parseInt(String.valueOf(task.getResult().child(player.getName()).child("score").getValue()));

                    }

                }
            }
        });

        // need to add delay so has time to access database
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                player.setScore(currentScore[0]);
                dbr.child(roomCode).child(player.getName()).setValue(player);
                dbr.child(roomCode).child("isPlaying").setValue("true");
            }
        }, 500);

    }

    public DatabaseReference getScores(String roomCode){
        return dbr.child(roomCode);
    }


}
