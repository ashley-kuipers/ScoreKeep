package com.example.cardscorecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import java.util.Random;

public class OnlineHostStartActivity extends AppCompatActivity {
    String roomCode = "", userName;
    Button b_openRoom;
    TextView t_roomCode;
    EditText et_name;
    MaterialToolbar topAppBar;
    private DatabaseReference dbr;
    private ValueEventListener vel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_start);

        // connect vars to views
        b_openRoom = findViewById(R.id.b_openRoom);
        t_roomCode = findViewById(R.id.t_roomCode);
        et_name = findViewById(R.id.et_name);
        topAppBar = findViewById(R.id.topAppBarHostStart);

        // sets the app bar back button function
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(OnlineHostStartActivity.this, OnlineSetupActivity.class);
                startActivity(in);
            }
        });

        // create room code
        Random rand = new Random();
        String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        for(int i = 0; i<4; i++){
            int randNum = rand.nextInt(26);
            roomCode += alphabet[randNum];
        }
        // put room code on screen
        t_roomCode.setText(roomCode);

        // access database to create room and add username to room
        // connects to firebase database
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        dbr = db.getReference("Rooms");

        b_openRoom.setOnClickListener( v->{
            // get username from player
            userName = et_name.getText().toString();

            // add player to room in the database
            dbr.child(roomCode).addListenerForSingleValueEvent(vel = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // if room exists
                    if(dataSnapshot.getValue() != null){
                        Toast.makeText(OnlineHostStartActivity.this, "Room already exists! Please go back and reopen page.", Toast.LENGTH_SHORT).show();

                    } else {

                        // if username already exists
                        if(dataSnapshot.child(userName).getValue() != null){
                            // notify user that the username is taken
                            Toast.makeText(OnlineHostStartActivity.this, Html.fromHtml("<small>\"User already exists! Please try a new nickname\"</small>"), Toast.LENGTH_SHORT).show();

                        // else add them into the room
                        } else {
                            // add the player to database
                            addPlayer(roomCode, userName);

                            Intent in = new Intent(OnlineHostStartActivity.this, OnlineScoreboardActivity.class);
                            in.putExtra("roomCode", roomCode);
                            in.putExtra("enteredName", userName);
                            in.putExtra("isHost", true);
                            startActivity(in);

                        }
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

    // Returns a formatted string of the current system time
    public String getTime(){
        // get the time when the new code was created
        long time = System.currentTimeMillis();
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.CANADA);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return formatter.format(calendar.getTime());
    }

    // when you turn the phone, this function is called to save any data you wish to save
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        // save data
        outState.putString("currentNickname", et_name.getText().toString());
        outState.putString("currentCode", t_roomCode.getText().toString());

        super.onSaveInstanceState(outState);
    }

    // when phone is done turning, this function is called to restore any of that data you saved
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle saved) {
        // get values from saved state
        et_name.setText(saved.getString("currentNickname"));
        t_roomCode.setText(saved.getString("currentCode"));

        super.onRestoreInstanceState(saved);
    }

    // creates a player object and adds to database
    private void addPlayer(String roomCode, String username){
        // add new users name to list of users in database
        dbr.child(roomCode).child("user_list").push().setValue(username);

        // create player object and add to database
        Player player = new Player(username, 0, true);
        dbr.child(roomCode).child(player.getName()).setValue(player);

        // set isPlaying to true
        dbr.child(roomCode).child("isPlaying").setValue("true");

        // set timeStamp
        dbr.child(roomCode).child("timeStamp").setValue(getTime());
    }

    // removes event listener when leave activity
    public void removeEL(){
        dbr.removeEventListener(vel);
    }

}