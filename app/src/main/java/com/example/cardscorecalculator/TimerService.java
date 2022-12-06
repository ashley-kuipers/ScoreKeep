package com.example.cardscorecalculator;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

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
        long millis = intent.getLongExtra("millis", 0);

        if(millis != 0){
            // creates a countdown timer based on the time provided by the user (add 500 ms to account for time to start service)
            ct = new CountDownTimer(millis+500, 1000) {
                @Override
                public void onTick(long l) {
                    // on every tick, get the hours, min, and second
                    long hrs = l / (60 * 60 * 1000) % 24;
                    long min = l / (60 * 1000) % 60;
                    long sec = l / 1000 % 60;

                    // send a timer tick broadcast to the timer fragment
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
                    // tells the fragment the timer is finished (and lets it know it finished completely)
                    Intent in = new Intent("timerEnd");
                    in.putExtra("finishEarly", false);
                    sendBroadcast(in);

                    // tracks if the timer finished completely or early
                    finished = true;

                }
            };
            ct.start();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!finished){
            // cancels the timer if it is still going
            ct.cancel();

            // lets the fragment know the timer has ended (and that it ended early)
            Intent in = new Intent("timerEnd");
            in.putExtra("finishEarly", true);
            in.putExtra("timeLeft", timeLeft);
            sendBroadcast(in);

        }
    }
}
