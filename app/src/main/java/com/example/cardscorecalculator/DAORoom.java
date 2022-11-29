package com.example.cardscorecalculator;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DAORoom {
    private DatabaseReference dbr;

    public DAORoom(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        dbr = db.getReference("Rooms");
    }

    public void addPlayer(String roomCode, Player player){
        dbr.child(roomCode).child(player.getName()).setValue(player);

        // TODO: have to check if user name is already there, keeps readding names
        dbr.child(roomCode).child("user_list").push().setValue(player.getName());
    }

    public DatabaseReference getScores(String roomCode){
        return dbr.child(roomCode);
    }

}
