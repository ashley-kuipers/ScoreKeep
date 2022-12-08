package com.example.cardscorecalculator;

import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
public class OnlineScoreboardActivity extends AppCompatActivity {
    String roomCode, userName;
    ListView scoreboard;
    ArrayList<String> leaderboard = new ArrayList<>();
    Button b_add, b_endGame, b_leaveGame;
    int currentScore;
    LinearLayout scoreboard_buttons;
    HashMap<Integer, String> scoresMap = new HashMap<Integer, String>();
    boolean isPlaying = true, isHost, justRotated;
    MaterialToolbar topAppBar;
    private DatabaseReference room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_scoreboard);

        // connect views to vars
        b_add = findViewById(R.id.b_add2);
        b_endGame = findViewById(R.id.b_onlineEndGame);
        b_leaveGame = findViewById(R.id.b_leaveGame);
        scoreboard_buttons = findViewById(R.id.scoreboard_buttons);
        topAppBar = findViewById(R.id.topAppBarOnlineScoreboard);
        scoreboard = findViewById(R.id.lv_leaderboard);

        // create listview adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.custom_listview, android.R.id.text1, leaderboard);
        scoreboard.setAdapter(adapter);

        // get vars from intent
        Bundle bun = getIntent().getExtras();
        roomCode = bun.getString("roomCode");
        userName = bun.getString("enteredName");
        isHost = bun.getBoolean("isHost");

        // if the user is the host, add the "end game" button to their view
        if(isHost) {
            b_endGame.setVisibility(VISIBLE);
        } else {
            b_leaveGame.setVisibility(VISIBLE);
        }

        // add room code to top bar
        topAppBar.setTitle("Room " + roomCode + " Scoreboard");

        Log.d("TAG", "Room Code " + roomCode + ", with " + userName + ", who is host? " + isHost);
        
        // get database reference for this room
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbr = db.getReference("Rooms");
        room = dbr.child(roomCode);

        // everytime the database changes, it updates the screen content (add handler delay so database has time to add original player in)
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                room.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // clear leaderboard (entries get re-added on every update)
                        leaderboard.clear();
                        scoresMap.clear();

                        // get the list of players
                        HashMap<String, String> userList = (HashMap<String, String>) snapshot.child("user_list").getValue();

                        // if the list is not null, get the scores for each player and add it to the list adapter
                        if (userList != null){
                            for ( String value : userList.values() ) {
                                // get player info
                                HashMap<String, Integer> playerInfo = (HashMap<String, Integer>) snapshot.child(value).getValue();
                                if (playerInfo != null){
                                    // get the score from the playerInfo
                                    currentScore = Integer.parseInt(String.valueOf(playerInfo.get("score")));
                                }

                                // add text to the leaderboard listview
                                leaderboard.add(value.toUpperCase() + ": " + currentScore);
                                adapter.notifyDataSetChanged();

                                // add score to scores list (to send to endGame activity)
                                scoresMap.put(currentScore, value.toUpperCase());

                            }

                            // get the value of the isPlaying variable
                            HashMap<String, Boolean> allInfo = (HashMap<String, Boolean>) snapshot.getValue();
                            if(allInfo != null){
                                // check isPlaying value
                                isPlaying = Boolean.parseBoolean(String.valueOf(allInfo.get("isPlaying")));

                                if(justRotated){
                                    // if it was just rotated, set isPlaying back to true
                                    justRotated = false;
                                } else {
                                    if(!isPlaying) {
                                        Intent in = new Intent(OnlineScoreboardActivity.this, EndGame.class);
                                        in.putExtra("scores", scoresMap);
                                        startActivity(in);

                                        // delete room data from database
                                        room.removeValue();
                                    }
                                }

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }, 200);


        // if the host ends the game, set isPlaying to false and open new activity
        b_endGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set the isPlaying value in the database to false (triggers a database change and opens endGame activity through there)
                room.child("isPlaying").setValue("false");
            }
        });

        // if the player leaves the game, game doesn't end for everyone else but they go to end screen with current scores
        b_leaveGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(OnlineScoreboardActivity.this, EndGame.class);
                in.putExtra("scores", scoresMap);
                startActivity(in);
            }
        });

        // sets the function of the add button in the top bar
        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.b_add) {
                    Toast.makeText(getApplicationContext(), "Add", Toast.LENGTH_LONG).show();
                    b_add.performClick();
                }
                return false;
            }
        });

        // brings up an alert dialog where player can enter their score and updates score in database
        b_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get initial variables
                LayoutInflater layoutInflater = getLayoutInflater();
                View alertLayout = layoutInflater.inflate(R.layout.alert_layout, null);
                Button b_cancel = alertLayout.findViewById(R.id.b_cancel);
                Button b_addScore = alertLayout.findViewById(R.id.b_addScore);
                EditText et_addScore = alertLayout.findViewById(R.id.et_addScore);

                // build dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OnlineScoreboardActivity.this);
                alertDialog.setCancelable(false);
                alertDialog.setView(alertLayout);
                AlertDialog dialog = alertDialog.create();

                // exits the dialog when the user hits "cancel"
                b_cancel.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                // Adds the entered score to the database
                b_addScore.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        room.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    Log.e("TAG", "Error getting data", task.getException());
                                }
                                else {
                                    // get player info
                                    HashMap<String, String> playerInfo = (HashMap<String, String>) task.getResult().child(userName).getValue();

                                    if (playerInfo != null){
                                        // get the score from the playerInfo
                                        currentScore = Integer.parseInt(String.valueOf(playerInfo.get("score")));
                                    }

                                    int addScore = Integer.parseInt(et_addScore.getText().toString());
                                    currentScore += addScore;

                                    // change score in database
                                    room.child(userName).child("score").setValue(currentScore);

                                    Log.d("TAG", "Added " + addScore + " to " + userName + "'s score, current score " + currentScore);
                                    dialog.dismiss();
                                }

                            }
                        });

                    }
                });

                dialog.show();

            }
        });
    }

    // if the app isn't in the process of being rotated, and the host leaves the game, isPlaying set to false
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isHost && !justRotated){
            room.child("isPlaying").setValue("false");
        }
    }

    // when you turn the phone, this function is called to save any data you wish to save
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // save data
        justRotated = true;
    }

    // when phone is done turning, this function is called to restore any of that data you saved
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle saved) {
        super.onRestoreInstanceState(saved);
        justRotated = true;
        room.child("isPlaying").setValue("true");
    }
}



