package com.example.cardscorecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OnlinePlayerJoinActivity extends AppCompatActivity {
    Button b_join;
    EditText et_room, et_name;
    String userName, roomCode;
    MaterialToolbar topAppBar;
    private DatabaseReference dbr;
    private ValueEventListener vel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_join);

        // connect vars to views
        b_join = findViewById(R.id.b_joinRoom);
        et_room = findViewById(R.id.et_room_code_player_join);
        et_name = findViewById(R.id.et_player_join);
        topAppBar = findViewById(R.id.topAppBarPlayerJoin);

        // sets the app bar back button function
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // connects to firebase database
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        dbr = db.getReference("Rooms");

        // player
        b_join.setOnClickListener( v->{
            roomCode = et_room.getText().toString();
            userName = et_name.getText().toString();

            // add player to room in the database
            dbr.child(roomCode).addListenerForSingleValueEvent(vel = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // if room exists
                    if(dataSnapshot.getValue() != null){
                        // if username already exists
                        if(dataSnapshot.child(userName).getValue() != null){
                            Log.d("TAG", "Username exists already!");

                            Toast.makeText(OnlinePlayerJoinActivity.this, Html.fromHtml("<small>\"User already exists! Please try a new nickname\"</small>"), Toast.LENGTH_SHORT).show();

                        // else user doesn't exist, so add them into the room
                        } else {
                            Log.d("TAG", "User doesn't exist! Adding them");

                            // add the player to database
                            addPlayer(roomCode, userName);

                            Intent in = new Intent(OnlinePlayerJoinActivity.this, OnlineScoreboardActivity.class);
                            in.putExtra("roomCode", roomCode);
                            in.putExtra("enteredName", userName);
                            in.putExtra("isHost", false);
                            startActivity(in);

                        }

                    } else {
                        // notify user that room doesn't exist
                        Toast.makeText(OnlinePlayerJoinActivity.this, "Room does not exist! Please try a new code.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    System.out.println("Connection failed.");
                }

            });

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    removeEL();
                }
            }, 400);

        });

    }

    // when you turn the phone, this function is called to save any data you wish to save
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // save data
        outState.putString("currentRoomCode", et_room.getText().toString());
        outState.putString("currentName", et_name.getText().toString());
    }

    // when phone is done turning, this function is called to restore any of that data you saved
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle saved) {
        super.onRestoreInstanceState(saved);
        et_room.setText(saved.getString("currentRoomCode"));
        et_name.setText(saved.getString("currentName"));
    }

    // creates a player object and adds to database
    private void addPlayer(String roomCode, String username){
        // add new users name to list of users in database
        dbr.child(roomCode).child("user_list").push().setValue(username);

        // create player object and add to database
        Player player = new Player(username, 0, false);
        dbr.child(roomCode).child(player.getName()).setValue(player);

        // set isPlaying to true
        dbr.child(roomCode).child("isPlaying").setValue("true");

        // set timeStamp
        dbr.child(roomCode).child("timeStamp").setValue(getTime());
    }

    // removes the event listener when leave activity
    public void removeEL(){
        dbr.removeEventListener(vel);
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