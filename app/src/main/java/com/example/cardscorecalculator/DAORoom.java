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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class DAORoom {
    private DatabaseReference dbr;

    public DAORoom(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        dbr = db.getReference("Rooms");
    }

    public void addPlayer(String roomCode, String username, boolean isHost){
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
                    // get list of users in the room
                    HashMap<String, String> userList = (HashMap<String, String>) task.getResult().child("user_list").getValue();

                    // if the user list doesn't exist or userlist does not contain the user already, add the user to the user list array and create player object
                    if(userList == null || !userList.containsValue(username)){
                        Log.d("TAG", "DAO: Added " + username);

                        // add new users name to list of users
                        dbr.child(roomCode).child("user_list").push().setValue(username);

                        // create player object and add to database
                        Player player = new Player(username, 0, isHost);
                        dbr.child(roomCode).child(player.getName()).setValue(player);

                        // set isPlaying to true
                        dbr.child(roomCode).child("isPlaying").setValue("true");

                        // set timeStamp
                        dbr.child(roomCode).child("timeStamp").setValue(getTime());

                        Log.d("TAG", "DAO: isPlaying set to true");
                    // else user already exists, so don't have to do anything but open new activity
                    } else {
                        Log.d("TAG", "DAO: User already exists");
                        // TODO: notify player that user already exists?

                        // set isPlaying to true TODO: not sure if this is necessary?
//                        dbr.child(roomCode).child("isPlaying").setValue("true");
                    }

                }
            }
        });

    }

    public DatabaseReference getScores(String roomCode){
        return dbr.child(roomCode);
    }

    // Returns a formatted string of the current system time
    public String getTime(){
        // get the time when the new code was created
        long time = System.currentTimeMillis();
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy/HH:mm:ss", Locale.CANADA);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return formatter.format(calendar.getTime());
    }

}
