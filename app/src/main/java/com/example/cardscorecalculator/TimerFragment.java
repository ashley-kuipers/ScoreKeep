package com.example.cardscorecalculator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

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

public class TimerFragment extends Fragment {
    Button b_start, b_stop, b_reset, b_back;
    ImageButton b_addHour, b_addMin, b_addSec, b_subHour, b_subMin, b_subSec;
    TextView t_hour, t_min, t_sec;
    long hour=0, min=0, sec=0;
    boolean timerIsRunning = false;

    public TimerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timer, container, false);

        b_addHour = v.findViewById(R.id.b_addHours);
        b_subHour = v.findViewById(R.id.b_subHours);
        b_addMin = v.findViewById(R.id.b_addMins);
        b_subMin = v.findViewById(R.id.b_subMins);
        b_addSec = v.findViewById(R.id.b_addSec);
        b_subSec = v.findViewById(R.id.b_subSecs);

        b_start = v.findViewById(R.id.b_startTimer);
        b_stop = v.findViewById(R.id.b_stopTimer);
        b_reset = v.findViewById(R.id.b_resetTimer);
        b_back = v.findViewById(R.id.b_timerBack);

        if(timerIsRunning){
            b_start.setVisibility(View.GONE);
            b_stop.setVisibility(View.VISIBLE);
        } else {
            b_start.setVisibility(View.VISIBLE);
            b_stop.setVisibility(View.GONE);
        }

        t_hour = v.findViewById(R.id.t_Timerhours);
        t_min = v.findViewById(R.id.t_timerMins);
        t_sec = v.findViewById(R.id.t_timerSecs);

        // starts a new receiver to wait for broadcast from the service
        requireActivity().registerReceiver(timerTick, new IntentFilter("timerTick"));
        requireActivity().registerReceiver(timerEnd, new IntentFilter("timerEnd"));

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

        b_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), TimerService.class);
                i.putExtra("millis", getTimeInMillis(hour, min, sec));
                getActivity().startService(i);

                // make all buttons invisible
                b_addHour.setEnabled(false);
                b_subHour.setEnabled(false);
                b_addMin.setEnabled(false);
                b_subMin.setEnabled(false);
                b_addSec.setEnabled(false);
                b_subSec.setEnabled(false);

                Log.d("TAG", "start pressed");

                b_start.setVisibility(View.GONE);
                b_stop.setVisibility(View.VISIBLE);

                timerIsRunning = true;

            }
        });

        b_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b_start.setVisibility(View.VISIBLE);
                b_stop.setVisibility(View.GONE);

                timerIsRunning = false;

                Intent i = new Intent(getActivity(), TimerService.class);
                getActivity().stopService(i);

            }
        });

        b_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timerIsRunning){
                    b_stop.performClick();
                }
                // have to delay so gives time for the service to stop
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

        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), MainActivity.class);
                startActivity(in);
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    public long getTimeInMillis(long hour, long min, long sec){
        long totalSecs = (hour * 3600) + (min * 60) + sec;
        long millis = totalSecs * 1000;

        return millis;
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

    public void setHr(long hr2){
        String strHour = String.valueOf(hr2);
        if(strHour.length()<2){
            strHour = "0" + strHour;
        }

        t_hour.setText(strHour);
    }

    public void setMin(long min2){
        String strMin = String.valueOf(min2);
        if(strMin.length()<2){
            strMin = "0" + strMin;
        }

        t_min.setText(strMin);
    }

    public void setSec(long sec2){
        String strSec = String.valueOf(sec2);
        if(strSec.length()<2){
            strSec = "0" + strSec;
        }

        t_sec.setText(strSec);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(timerTick);
        getActivity().unregisterReceiver(timerEnd);
    }
}
