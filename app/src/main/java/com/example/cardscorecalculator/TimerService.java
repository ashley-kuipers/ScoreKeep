package com.example.cardscorecalculator;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class TimerService extends Service {
    boolean finished = false;
    long timeLeft=0;
    CountDownTimer ct;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean timer = intent.getBooleanExtra("timer", true);
        long millis = intent.getLongExtra("millis", 0);

        Log.d("TAG", "service started with " + millis);

        if(timer){
            // timer

            if(millis == 0){
                // make toast that says they need to enter an amount
            } else {
                // run the timer
                Log.d("TAG", "timer started");
                ct = new CountDownTimer(millis+500, 1000) {
                    @Override
                    public void onTick(long l) {
                        long hrs = l / (60 * 60 * 1000) % 24;
                        long min = l / (60 * 1000) % 60;
                        long sec = l / 1000 % 60;

                        Log.d("TAG", "time tick hr: " + hrs + " min: " + min + " sec: " + sec);

                        Intent in = new Intent("timerTick");
                        in.putExtra("hrs", hrs);
                        in.putExtra("min", min);
                        in.putExtra("sec", sec);
                        sendBroadcast(in);

                        timeLeft = l;
                    }

                    @Override
                    public void onFinish() {
                        // TODO: send push notification
                        Intent in = new Intent("timerEnd");
                        in.putExtra("finishEarly", false);
                        sendBroadcast(in);
                        finished = true;

                    }
                };
                ct.start();
            }
        } else {
            // stopwatch
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!finished){
            // service stopped early
            ct.cancel();
            Log.d("TAG", "Service Stopped Early");
            Intent in = new Intent("timerEnd");
            in.putExtra("finishEarly", true);
            in.putExtra("timeLeft", timeLeft);
            sendBroadcast(in);

        } else {
            Log.d("TAG", "Service Stopped");
        }
    }
}
