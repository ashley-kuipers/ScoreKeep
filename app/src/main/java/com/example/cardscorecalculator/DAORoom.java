package com.example.cardscorecalculator;

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
        dbr.child(roomCode).child(player.getName()).setValue(player);

        // avoids duplicate usernames TODO: notify user that username already exists
        dbr.child(roomCode).child("user_list").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    HashMap<String, String> userList = (HashMap<String, String>) task.getResult().getValue();

                    if(userList == null || !userList.containsValue(player.getName())){
                        dbr.child(roomCode).child("user_list").push().setValue(player.getName());
                        Log.d("TAG", "DAO: Added " + player.getName());
                    } else {
                        Log.d("TAG", "DAO: User already exists");
                    }

                }
            }
        });


    }

    public DatabaseReference getScores(String roomCode){
        return dbr.child(roomCode);
    }

}
