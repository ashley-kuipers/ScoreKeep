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
    HashMap<Integer, String> sendAway= new HashMap<Integer, String>();
    boolean isPlaying = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_scoreboard);

        b_add = findViewById(R.id.b_add);
        b_endGame = findViewById(R.id.b_onlineEndGame);
        scoreboard_buttons = findViewById(R.id.scoreboard_buttons);

        scoreboard = findViewById(R.id.lv_leaderboard);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.custom_listview, android.R.id.text1, leaderboard);
        scoreboard.setAdapter(adapter);

        Bundle bun = getIntent().getExtras();
        roomCode = bun.getString("roomCode");
        userName = bun.getString("enteredName");

        Log.d("TAG", "Room Code " + roomCode + ", with " + userName);

        // have to delay this part to allow player to be added to the database from previous activity
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DAORoom dao = new DAORoom();
                DatabaseReference room = dao.getScores(roomCode);

                // everytime the database changes, it updates
                room.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // clear leaderboard because it reloads on every change
                        leaderboard.clear();
                        sendAway.clear();

                        // get the list of players
                        HashMap<String, String> userList = (HashMap<String, String>) snapshot.child("user_list").getValue();
                        if (userList != null){
                            for ( String value : userList.values() ) {
                                // get scores for each player and add to listview
                                HashMap<String, Integer> scores = (HashMap<String, Integer>) snapshot.child(value).getValue();
                                if (scores != null){
                                    currentScore = Integer.parseInt(String.valueOf(scores.get("score")));
                                }
                                leaderboard.add(value.toUpperCase() + ": " + currentScore);
                                sendAway.put(currentScore, value.toUpperCase());
                                adapter.notifyDataSetChanged();

                                // if the user is the host, add the "end game" button to their layout
                                HashMap<String, Boolean> host = (HashMap<String, Boolean>) snapshot.child(value).getValue();
                                if(host != null){
                                    boolean isHost = host.get("host");
                                    if (isHost){
                                        b_endGame.setVisibility(VISIBLE);
                                    }
                                }
                                // if the host ends the game, set isPlaying to false and open new activity
                                b_endGame.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        room.child("isPlaying").setValue("false");
                                        isPlaying = false;
                                        // open end game activity
                                        Intent in = new Intent(OnlineScoreboardActivity.this, EndGame.class);
                                        in.putExtra("scores", sendAway);
                                        startActivity(in);
                                    }
                                });

                                // get the value of the isPlaying variable
                                HashMap<String, Boolean> playing = (HashMap<String, Boolean>) snapshot.getValue();
                                if(playing != null){
                                    isPlaying = Boolean.valueOf(String.valueOf(playing.get("isPlaying")));
                                    if (!isPlaying){
                                        Intent in = new Intent(OnlineScoreboardActivity.this, EndGame.class);
                                        in.putExtra("scores", sendAway);
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

                b_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        LayoutInflater layoutInflater = getLayoutInflater();
                        View alertLayout = layoutInflater.inflate(R.layout.alert_layout, null);
                        Button b_cancel = alertLayout.findViewById(R.id.b_cancel);
                        Button b_addScore = alertLayout.findViewById(R.id.b_addScore);
                        EditText et_addScore = alertLayout.findViewById(R.id.et_addScore);


                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OnlineScoreboardActivity.this);

                        alertDialog.setCancelable(false);
                        alertDialog.setView(alertLayout);
                        AlertDialog dialog = alertDialog.create();
                        b_cancel.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        b_addScore.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                int addScore = Integer.parseInt(et_addScore.getText().toString());
                                currentScore += addScore;

                                // change score in database
                                room.child(userName).child("score").setValue(currentScore);

                                Log.d("TAG", "Added " + addScore + " to " + userName + "'s score, current score " + currentScore);
                                dialog.dismiss();

                            }
                        });
                        dialog.show();

                    }
                });
            }
        }, 500);



    }


}
