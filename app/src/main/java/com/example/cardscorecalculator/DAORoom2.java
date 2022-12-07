package com.example.cardscorecalculator;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.TimeUnit;

public class DAORoom2 {
    private DatabaseReference dbr;
    private ValueEventListener vel;

    public DAORoom2(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        dbr = db.getReference("Rooms");
    }

    public void newPlayer(String roomCode, String username, boolean isHost){
        dbr.child(roomCode).addListenerForSingleValueEvent(vel = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Random rand = new Random();
                // if room exists
                if(dataSnapshot.getValue() != null){
                    Log.d("TAG", "room: Room Exists! " + dataSnapshot.getValue());

                    // if username already exists
                    if(dataSnapshot.child(username).getValue() != null){
                        Log.d("TAG", "username exists! " + dataSnapshot.child(username).getValue());

                        // add them in with username + randomnumber
                        int num = rand.nextInt(88) + 10;
                        addPlayer(roomCode, username + num, false);

                    // else user doesn't exist, so add them into the room
                    } else {
                        Log.d("TAG", "user doesn't exist! Adding them");

                        // TODO: should be isHost == false??
                        addPlayer(roomCode, username, false);
                    }

                // else the room doesn't exist
                } else {
                    // if the user is the host, then create a new room, else do nothing
                    if(isHost){
                        Log.d("TAG", "Room doesn't exist! Creating one ...");
                        addPlayer(roomCode, username, true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("failed");
            }

        });

    }

    private void addPlayer(String roomCode, String username, boolean isHost){
        // add new users name to list of users in database
        dbr.child(roomCode).child("user_list").push().setValue(username);

        // create player object and add to database
        Player player = new Player(username, 0, isHost);
        dbr.child(roomCode).child(player.getName()).setValue(player);

        // set isPlaying to true
        dbr.child(roomCode).child("isPlaying").setValue("true");

        // set timeStamp
        dbr.child(roomCode).child("timeStamp").setValue(getTime());
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

    public void removeEL(){
        dbr.removeEventListener(vel);
        Log.d("TAG", "removed event listener");
    }


}
