package com.example.cardscorecalculator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;

public class TimerFragment extends Fragment {
    Button b_start, b_stop, b_reset;
    ImageButton b_addHour, b_addMin, b_addSec, b_subHour, b_subMin, b_subSec;
    TextView t_hour, t_min, t_sec;
    long hour=0, min=0, sec=0;
    boolean timerIsRunning = false;

    public TimerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timer, container, false);

        // connect vars to views
        b_addHour = v.findViewById(R.id.b_addHours);
        b_subHour = v.findViewById(R.id.b_subHours);
        b_addMin = v.findViewById(R.id.b_addMins);
        b_subMin = v.findViewById(R.id.b_subMins);
        b_addSec = v.findViewById(R.id.b_addSec);
        b_subSec = v.findViewById(R.id.b_subSecs);
        b_start = v.findViewById(R.id.b_startTimer);
        b_stop = v.findViewById(R.id.b_stopTimer);
        b_reset = v.findViewById(R.id.b_resetTimer);
        t_hour = v.findViewById(R.id.t_Timerhours);
        t_min = v.findViewById(R.id.t_timerMins);
        t_sec = v.findViewById(R.id.t_timerSecs);

        // sets visibility/clickability of buttons based on if the timer is running
        if(timerIsRunning){
            b_start.setVisibility(View.GONE);
            b_stop.setVisibility(View.VISIBLE);
            b_addHour.setEnabled(false);
            b_subHour.setEnabled(false);
            b_addMin.setEnabled(false);
            b_subMin.setEnabled(false);
            b_addSec.setEnabled(false);
            b_subSec.setEnabled(false);
        } else {
            b_start.setVisibility(View.VISIBLE);
            b_stop.setVisibility(View.GONE);
            b_addHour.setEnabled(true);
            b_subHour.setEnabled(true);
            b_addMin.setEnabled(true);
            b_subMin.setEnabled(true);
            b_addSec.setEnabled(true);
            b_subSec.setEnabled(true);
        }

        // register time receivers (broadcast from TimerService)
        requireActivity().registerReceiver(timerTick, new IntentFilter("timerTick"));
        requireActivity().registerReceiver(timerEnd, new IntentFilter("timerEnd"));

        // adds an hour to the timer
        b_addHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hour+=1;

                if(hour > 99){
                    hour = 0;
                }

                setHr(hour);
            }
        });

        // subtracts an hour from the timer
        b_subHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hour-=1;

                if(hour < 0){
                    hour = 99;
                }

                setHr(hour);
            }
        });

        // adds a minute to the timer
        b_addMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                min+=1;

                if(min > 59){
                    min = 0;
                }

                setMin(min);
            }
        });

        // subtracts a minute from the timer
        b_subMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                min-=1;

                if(min < 0){
                    min = 59;
                }

                setMin(min);
            }
        });

        // adds a second to the timer
        b_addSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sec+=1;

                if(sec > 59){
                    sec = 0;
                }

                setSec(sec);
            }
        });

        // subtracts a second from the timer
        b_subSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sec-=1;

                if(sec < 0){
                    sec = 59;
                }

                setSec(sec);
            }
        });

        // starts the timer
        b_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // starts TimerService (allows timer to run in background)
                Intent i = new Intent(getActivity(), TimerService.class);
                i.putExtra("millis", getTimeInMillis(hour, min, sec));
                getActivity().startService(i);

                // make all add/sub buttons unclickable and switches out start and stop button
                b_addHour.setEnabled(false);
                b_subHour.setEnabled(false);
                b_addMin.setEnabled(false);
                b_subMin.setEnabled(false);
                b_addSec.setEnabled(false);
                b_subSec.setEnabled(false);
                b_start.setVisibility(View.GONE);
                b_stop.setVisibility(View.VISIBLE);

                timerIsRunning = true;

            }
        });

        b_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // stops the timer service
                Intent i = new Intent(getActivity(), TimerService.class);
                getActivity().stopService(i);

                // make all add/sub buttons clickable and switches out start and stop button
                b_start.setVisibility(View.VISIBLE);
                b_stop.setVisibility(View.GONE);
                b_addHour.setEnabled(true);
                b_subHour.setEnabled(true);
                b_addMin.setEnabled(true);
                b_subMin.setEnabled(true);
                b_addSec.setEnabled(true);
                b_subSec.setEnabled(true);

                timerIsRunning = false;
            }
        });

        b_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if the timer is currently running, performs all the functions of the stop button
                if(timerIsRunning){
                    b_stop.performClick();
                }

                // sets the timer to 0
                // have to delay because it takes time for the service to stop
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hour = 0;
                        min = 0;
                        sec = 0;
                        setHr(hour);
                        setMin(min);
                        setSec(sec);
                    }
                }, 200);

            }
        });

        // retrieve data from the instance state if it exists
        if (savedInstanceState != null) {
            t_hour.setText(savedInstanceState.getString("currentHour"));
            t_min.setText(savedInstanceState.getString("currentMin"));
            t_sec.setText(savedInstanceState.getString("currentSec"));
        }

        // Inflate the layout for this fragment
        return v;
    }



    BroadcastReceiver timerTick = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long hrs3 = intent.getLongExtra("hrs", 0);
            long min3 = intent.getLongExtra("min", 0);
            long sec3 = intent.getLongExtra("sec", 0);

            setHr(hrs3);
            setMin(min3);
            setSec(sec3);
        }
    };

    BroadcastReceiver timerEnd = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long timeLeft = intent.getLongExtra("timeLeft", 0);
            boolean finishEarly = intent.getBooleanExtra("finishEarly", false);

            if(finishEarly){
                hour = timeLeft / (60 * 60 * 1000) % 24;
                min = timeLeft / (60 * 1000) % 60;
                sec = timeLeft / 1000 % 60;
                Log.d("TAG", "Timer Ended Early");
            } else {
                hour = 0;
                min = 0;
                sec = 0;
                Log.d("TAG", "Timer Ended");
                Toast.makeText(getActivity(), "Timer finished!", Toast.LENGTH_SHORT).show();
            }

            setHr(hour);
            setMin(min);
            setSec(sec);

            // make all buttons clickable
            b_addHour.setEnabled(true);
            b_subHour.setEnabled(true);
            b_addMin.setEnabled(true);
            b_subMin.setEnabled(true);
            b_addSec.setEnabled(true);
            b_subSec.setEnabled(true);

            b_start.setVisibility(View.VISIBLE);
            b_stop.setVisibility(View.GONE);
            b_reset.setVisibility(View.VISIBLE);

            timerIsRunning = false;

        }
    };

    // converts the provided long to String and puts in the hours field
    public void setHr(long hr2){
        String strHour = String.valueOf(hr2);
        if(strHour.length()<2){
            strHour = "0" + strHour;
        }

        t_hour.setText(strHour);
    }

    // converts the provided long to String and puts in the min field
    public void setMin(long min2){
        String strMin = String.valueOf(min2);
        if(strMin.length()<2){
            strMin = "0" + strMin;
        }

        t_min.setText(strMin);
    }

    // converts the provided long to String and puts in the sec field
    public void setSec(long sec2){
        String strSec = String.valueOf(sec2);
        if(strSec.length()<2){
            strSec = "0" + strSec;
        }

        t_sec.setText(strSec);
    }

    // converts the provided time to milliseconds
    private long getTimeInMillis(long hour, long min, long sec){
        long totalSecs = (hour * 3600) + (min * 60) + sec;
        long millis = totalSecs * 1000;
        return millis;
    }

    // unregisters receivers when fragment is destroyed
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(timerTick);
        getActivity().unregisterReceiver(timerEnd);
    }

    // only required to save data when timer hasn't started yet, otherwise service will update screen automatically
    // when you turn the phone, this function is called to save any data you wish to save
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // save data
        outState.putString("currentHour", t_hour.getText().toString());
        outState.putString("currentMin", t_min.getText().toString());
        outState.putString("currentSec", t_sec.getText().toString());
    }

}
