package com.example.cardscorecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.Random;

public class DiceActivity extends AppCompatActivity {
    Button b_rollDice;
    EditText et_numSides;
    TextView t_roll;
    MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Random rand = new Random();

        // get animations
        Animation slideinoutfast;
        slideinoutfast = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideinoutfast);
        Animation slideinoutslow;
        slideinoutslow = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideinoutslow);
        Animation slideinoutmed;
        slideinoutmed = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideinoutmed);
        Animation slideinoutrealslow;
        slideinoutrealslow = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideinoutrealslow);
        Animation slidein;
        slidein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slidein);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);

        // connect vars to views
        b_rollDice = findViewById(R.id.b_rollDice);
        et_numSides = findViewById(R.id.et_test);
        t_roll = findViewById(R.id.t_roll);
        topAppBar = findViewById(R.id.topAppBar);

        // sets the app bar back button function
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // rolls dice
        b_rollDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sides;

                // if the number of sides isn't filled in, default to 6 sides
                if(et_numSides.getText().toString().length() == 0){
                    sides = 6;
                } else {
                    // else get the number of sides from the editText
                    sides = Integer.parseInt(et_numSides.getText().toString());
                }

                int numSides = sides;

                // Create countdown timers that will run at various speeds to give the illusion that the animation is slowing down as it spins
                // timer 1 at 300ms/num
                new CountDownTimer(1200, 300) {
                    public void onTick(long millisUntilFinished) {
                        int newRoll = rand.nextInt(numSides) + 1;
                        t_roll.startAnimation(slideinoutfast);
                        t_roll.setText(newRoll + "");
                        Log.d("TAG", "new number " + newRoll);
                    }
                    public void onFinish() {
                        // timer 2 at 450ms/num
                        new CountDownTimer(1350, 450) {
                            public void onTick(long millisUntilFinished) {
                                int newRoll = rand.nextInt(numSides) + 1;
                                t_roll.startAnimation(slideinoutmed);
                                t_roll.setText(newRoll + "");
                                Log.d("TAG", "new number " + newRoll);
                            }

                            public void onFinish() {
                                // timer 3 at 600ms/num
                                new CountDownTimer(1800, 600) {
                                    public void onTick(long millisUntilFinished) {
                                        int newRoll = rand.nextInt(numSides) + 1;
                                        t_roll.startAnimation(slideinoutslow);
                                        t_roll.setText(newRoll + "");
                                        Log.d("TAG", "new number " + newRoll);
                                    }

                                    public void onFinish() {
                                        // timer 4 at 900ms/num
                                        new CountDownTimer(1800, 900) {
                                            public void onTick(long millisUntilFinished) {
                                                int newRoll = rand.nextInt(numSides) + 1;
                                                t_roll.startAnimation(slideinoutrealslow);
                                                t_roll.setText(newRoll + "");
                                                Log.d("TAG", "new number " + newRoll);
                                            }

                                            public void onFinish() {
                                                // for the last number, has a special slower animation
                                                int newRoll = rand.nextInt(numSides) + 1;
                                                t_roll.startAnimation(slidein);
                                                t_roll.setText(newRoll + "");
                                            }

                                        }.start();
                                    }

                                }.start();
                            }

                        }.start();
                    }

                }.start();


            }
        });

    }

    // when you turn the phone, this function is called to save any data you wish to save
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        // save roll and current et_numSides
        outState.putString("currentRoll", t_roll.getText().toString());
        outState.putString("currentNumSides", et_numSides.getText().toString());

        super.onSaveInstanceState(outState);
    }

    // when phone is done turning, this function is called to restore any of that data you saved
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle saved) {
        // get values from saved state
        t_roll.setText(saved.getString("currentRoll"));
        et_numSides.setText(saved.getString("currentNumSides"));

        super.onRestoreInstanceState(saved);
    }

}