package com.example.cardscorecalculator;

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

import java.util.Random;

public class DiceActivity extends AppCompatActivity {
    Button b_rollDice, b_diceBack;
    EditText et_numSides;
    TextView t_roll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Random rand = new Random();
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

        b_rollDice = findViewById(R.id.b_rollDice);
        b_diceBack = findViewById(R.id.b_diceBack);
        et_numSides = findViewById(R.id.et_test);
        t_roll = findViewById(R.id.t_roll);


        b_rollDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sides;
                if(et_numSides.getText().toString().length() == 0){
                    sides = 6;
                } else {
                    sides = Integer.parseInt(et_numSides.getText().toString());
                }
                int numSides = sides;

                new CountDownTimer(1200, 300) {
                    public void onTick(long millisUntilFinished) {
                        int newRoll = rand.nextInt(numSides) + 1;
                        t_roll.startAnimation(slideinoutfast);
                        t_roll.setText(newRoll + "");
                        Log.d("TAG", "new number " + newRoll);
                    }

                    public void onFinish() {
                        new CountDownTimer(1350, 450) {
                            public void onTick(long millisUntilFinished) {
                                int newRoll = rand.nextInt(numSides) + 1;
                                t_roll.startAnimation(slideinoutmed);
                                t_roll.setText(newRoll + "");
                                Log.d("TAG", "new number " + newRoll);
                            }

                            public void onFinish() {
                                new CountDownTimer(1800, 600) {
                                    public void onTick(long millisUntilFinished) {
                                        int newRoll = rand.nextInt(numSides) + 1;
                                        t_roll.startAnimation(slideinoutslow);
                                        t_roll.setText(newRoll + "");
                                        Log.d("TAG", "new number " + newRoll);
                                    }

                                    public void onFinish() {
                                        new CountDownTimer(1800, 900) {
                                            public void onTick(long millisUntilFinished) {
                                                int newRoll = rand.nextInt(numSides) + 1;
                                                t_roll.startAnimation(slideinoutrealslow);
                                                t_roll.setText(newRoll + "");
                                                Log.d("TAG", "new number " + newRoll);
                                            }

                                            public void onFinish() {
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

        b_diceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}