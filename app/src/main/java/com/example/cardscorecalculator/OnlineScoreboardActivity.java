package com.example.cardscorecalculator;

import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
//TODO: Figure out timing?? seems to be messing with the scores on the database
public class OnlineScoreboardActivity extends AppCompatActivity {
    String roomCode, userName;
    ListView scoreboard;
    ArrayList<String> leaderboard = new ArrayList<>();
    Button b_add, b_endGame;
    int currentScore;
    LinearLayout scoreboard_buttons;
    HashMap<Integer, String> scoresMap = new HashMap<Integer, String>();
    boolean isPlaying = true, isHost;
    TextView t_onlineScoreboardTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_scoreboard);

        b_add = findViewById(R.id.b_add);
        b_endGame = findViewById(R.id.b_onlineEndGame);
        scoreboard_buttons = findViewById(R.id.scoreboard_buttons);
        t_onlineScoreboardTitle = findViewById(R.id.t_olscoreboardTitle);

        scoreboard = findViewById(R.id.lv_leaderboard);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.custom_listview, android.R.id.text1, leaderboard);
        scoreboard.setAdapter(adapter);

        Bundle bun = getIntent().getExtras();
        roomCode = bun.getString("roomCode");
        userName = bun.getString("enteredName");
        isHost = bun.getBoolean("isHost");

        // if the user is the host, add the "end game" button to their view
        if(isHost) {
            b_endGame.setVisibility(VISIBLE);
        }

        t_onlineScoreboardTitle.setText("Room " + roomCode + " scores");

        Log.d("TAG", "Room Code " + roomCode + ", with " + userName + ", who is host? " + isHost);
        
        // get database reference for this room
        DAORoom dao = new DAORoom();
        DatabaseReference room = dao.getScores(roomCode);

        // everytime the database changes, it updates (add handler so database has time to add original player in)
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("TAG", "beginning of handler");
                room.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("TAG", "on Data Change");
                        // clear leaderboard because it reloads on every change
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
                                Log.d("TAG", "at listview");
                                // add text to the leaderboard listview
                                leaderboard.add(value.toUpperCase() + ": " + currentScore);
                                adapter.notifyDataSetChanged();

                                // add score to scores list (to send to endGame activity)
                                scoresMap.put(currentScore, value.toUpperCase());

                                // get the value of the isPlaying variable
                                HashMap<String, Boolean> allInfo = (HashMap<String, Boolean>) snapshot.getValue();
                                if(allInfo != null){
                                    // check isPlaying value
                                    isPlaying = Boolean.parseBoolean(String.valueOf(allInfo.get("isPlaying")));

                                    Log.d("TAG", "setting isPlaying to" + isPlaying);

                                    // if isPlaying is false (game has ended), open EndGame activity
                                    if (!isPlaying){
                                        Intent in = new Intent(OnlineScoreboardActivity.this, EndGame.class);
                                        in.putExtra("scores", scoresMap);
                                        startActivity(in);
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
                // set the isPlaying value in the database to false
                room.child("isPlaying").setValue("false");

                // open end game activity
                Intent in = new Intent(OnlineScoreboardActivity.this, EndGame.class);
                in.putExtra("scores", scoresMap);
                startActivity(in);
            }
        });

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // if the host leaves the game, the game ends
        if(isHost){
            DAORoom dao = new DAORoom();
            DatabaseReference room = dao.getScores(roomCode);
            room.child("isPlaying").setValue("false");
        }
    }
}



