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
import android.widget.TextView;


public class StopwatchFragment extends Fragment {
    private long secs,mins,hrs, ms;
    private Button b_start, b_stop, b_reset;
    private TextView t_hour, t_min, t_sec;
    private boolean isRunning, reset=true;

    public StopwatchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        // connect vars to vals
        b_start = v.findViewById(R.id.b_startStopwatch);
        b_stop = v.findViewById(R.id.b_stopStopwatch);
        b_reset = v.findViewById(R.id.b_resetStopwatch);
        t_hour = v.findViewById(R.id.t_StopWatchhours);
        t_min = v.findViewById(R.id.t_stopWatchMins);
        t_sec = v.findViewById(R.id.t_stopWatchSecs);

        // register receivers (listening to broadcasts from StopWatchService)
        requireActivity().registerReceiver(stopWatchTick, new IntentFilter("stopWatchTick"));
        requireActivity().registerReceiver(stopWatchEnd, new IntentFilter("stopWatchEnd"));

        // starts the stopwatch
        b_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // starts the stopwatch service
                Intent i = new Intent(getActivity(), StopWatchService.class);
                i.putExtra("reset", reset);
                getActivity().startService(i);

                // changes start button to stop
                b_start.setVisibility(View.GONE);
                b_stop.setVisibility(View.VISIBLE);

                isRunning = true;

                reset = false;
            }
        });

        // stops the stopwatch
        b_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // stops the service
                Intent i = new Intent(getActivity(), StopWatchService.class);
                getActivity().stopService(i);

                // resets visibility of buttons
                b_start.setVisibility(View.VISIBLE);
                b_stop.setVisibility(View.GONE);

                isRunning = false;

            }
        });

        // resets the timer values
        b_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if the timer was currently running, need to perform stop button functions first
                if(isRunning){
                    b_stop.performClick();
                }

                // updates the timer screen to 0
                // have to delay so gives time for the service to stop
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateStopwatch(0.0F);
                    }
                }, 200);

                reset = true;
            }
        });

        // retrieves data from instance state if it exists
        if (savedInstanceState != null) {
            t_hour.setText(savedInstanceState.getString("currentHour"));
            t_min.setText(savedInstanceState.getString("currentMin"));
            t_sec.setText(savedInstanceState.getString("currentSec"));
        }

        return v;
    }

    // updates the stopwatch screen
    public void updateStopwatch(float time){
        secs = (long)(time/1000);
        mins = (long)((time/1000)/60);
        hrs = (long)(((time/1000)/60)/60);

        // updates seconds and milliseconds
        secs = secs % 60;
        ms=(long)time;
        setSec(secs, ms);

        // updates minutes
        mins = mins % 60;
        setMin(mins);

        // update hours
        setHr(hrs);

    }

    // formats the provided long and puts it in the hour field
    public void setHr(long hr2){
        String hours=String.valueOf(hr2);
        if(hr2 == 0){
            hours = "00";
        }
        if(hr2 <10 && hr2 > 0){
            hours = "0"+hours;
        }

        t_hour.setText(hours);
    }

    // formats the provided long and puts it in the min field
    public void setMin(long min2){
        String minutes=String.valueOf(min2);
        if(min2 == 0){
            minutes = "00";
        }
        if(min2 <10 && min2 > 0){
            minutes = "0"+minutes;
        }

        t_min.setText(minutes);
    }

    // formats the provided long and puts it in the sec field (accounts for 10th of second)
    public void setSec(long sec2, long ms2){
        String milliseconds = String.valueOf((long)ms2);

        if (milliseconds.length()<3){
            milliseconds = "0";
        } else {
            milliseconds = milliseconds.substring(milliseconds.length()-3, milliseconds.length()-2);
        }

        String seconds=String.valueOf(sec2);
        if(sec2 == 0){
            seconds = "00";
        }
        if(sec2 <10 && sec2 > 0){
            seconds = "0"+seconds;
        }

        String output = seconds + "." + milliseconds;

        t_sec.setText(output);
    }

    // Receiver that listens for each stopwatch tick
    BroadcastReceiver stopWatchTick = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            float elapsedTime = intent.getFloatExtra("elapsedTime", 0);
            updateStopwatch(elapsedTime);
        }
    };

    // Receiver that listens to when the stopwatch ends
    BroadcastReceiver stopWatchEnd = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long elapsedTime = intent.getLongExtra("elapsedTime", 0);
            updateStopwatch((float) elapsedTime);
        }
    };

    // unregisters receivers on destroy
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(stopWatchTick);
        getActivity().unregisterReceiver(stopWatchEnd);
    }

    // only required to save data when stopwatch hasn't started yet, otherwise service will update screen automatically
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

