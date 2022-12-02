package com.example.cardscorecalculator;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

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
    private boolean success = true;

    public DAORoom(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        dbr = db.getReference("Rooms");
    }

    public boolean addPlayer(String roomCode, String username, boolean isHost){

        // checks that username does not already exist
        dbr.child(roomCode).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("TAG", "Error getting data", task.getException());
                } else {
                    // get list of users in the room
                    HashMap<String, String> userList = (HashMap<String, String>) task.getResult().child("user_list").getValue();
                    Log.d("TAG", "DAO userlist " + userList);

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

                        setSuccess(true);

                        // else user already exists, so don't have to do anything but open new activity
                    } else {
                        Log.d("TAG", "DAO: User already exists");
                        setSuccess(false);
                    }
                    Log.d("TAG", "DAO: Returned1 " + success);
                }
            }
        });

        // TODO: this doesn't work, always returns true because upper stuff is in its own thread

        Log.d("TAG", "DAO: Returned2 " + success);
        return success;
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

    public void setSuccess(boolean bool){
        this.success = bool;
    }

    public boolean isSuccess() {
        return success;
    }

}
