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
import android.widget.TextView;


public class StopwatchFragment extends Fragment {
//    private String hours,minutes,seconds,milliseconds;
    private long secs,mins,hrs, ms;
    private Button b_start, b_stop, b_reset, b_back;
    private TextView t_hour, t_min, t_sec;
    private boolean isRunning, reset=true;

    public StopwatchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        b_start = v.findViewById(R.id.b_startStopwatch);
        b_stop = v.findViewById(R.id.b_stopStopwatch);
        b_reset = v.findViewById(R.id.b_resetStopwatch);
        b_back = v.findViewById(R.id.b_stopWatchBack);

        t_hour = v.findViewById(R.id.t_StopWatchhours);
        t_min = v.findViewById(R.id.t_stopWatchMins);
        t_sec = v.findViewById(R.id.t_stopWatchSecs);

        requireActivity().registerReceiver(stopWatchTick, new IntentFilter("stopWatchTick"));
        requireActivity().registerReceiver(stopWatchEnd, new IntentFilter("stopWatchEnd"));

        b_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), StopWatchService.class);
                i.putExtra("reset", reset);
                getActivity().startService(i);

                b_start.setVisibility(View.GONE);
                b_stop.setVisibility(View.VISIBLE);

                isRunning = true;

                reset = false;
            }
        });

        b_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), StopWatchService.class);
                getActivity().stopService(i);

                b_start.setVisibility(View.VISIBLE);
                b_stop.setVisibility(View.GONE);

                Log.d("TAG", "Timer stopped");

                isRunning = false;

            }
        });

        b_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRunning){
                    b_stop.performClick();
                }
                // have to delay so gives time for the service to stop
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateTimer(0.0F);
                    }
                }, 200);

                Log.d("TAG", "Timer reset");
                reset = true;
            }
        });

        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), MainActivity.class);
                startActivity(in);
            }
        });
        return v;
    }

    public void updateTimer(float time){
        secs = (long)(time/1000);
        mins = (long)((time/1000)/60);
        hrs = (long)(((time/1000)/60)/60);

        // seconds and milliseconds
        secs = secs % 60;
        ms=(long)time;
        setSec(secs, ms);

        // updates minutes
        mins = mins % 60;
        setMin(mins);

        // update hours
        setHr(hrs);

    }

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

    BroadcastReceiver stopWatchTick = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            float elapsedTime = intent.getFloatExtra("elapsedTime", 0);
            updateTimer(elapsedTime);
        }
    };

    BroadcastReceiver stopWatchEnd = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO: this intent does not exist yet
            long elapsedTime = intent.getLongExtra("elapsedTime", 0);
            updateTimer((float) elapsedTime);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(stopWatchTick);
        getActivity().unregisterReceiver(stopWatchEnd);
    }
}

