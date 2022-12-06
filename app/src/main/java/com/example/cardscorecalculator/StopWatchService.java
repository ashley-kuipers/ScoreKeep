package com.example.cardscorecalculator;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class StopWatchService extends Service {
    private long startTime;
    private static long elapsedTime;
    private Handler hand = new Handler();
    private boolean stopped = false, reset;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // tracks if the user has pressed reset
        reset = intent.getBooleanExtra("reset", false);

        // if the user has reset the stopwatch, the start time is the current time
        if (reset) {
            startTime = System.currentTimeMillis();
            elapsedTime = 0;
        // else it starts from where it left off when it was stopped
        } else {
            startTime = System.currentTimeMillis() - elapsedTime;
        }

        // starts the stopwatch
        hand.removeCallbacks(startTimer);
        hand.postDelayed(startTimer, 0);

        return super.onStartCommand(intent, flags, startId);
    }

    // Stops the stopwatch and signals the fragment when service is stopped
    @Override
    public void onDestroy() {
        super.onDestroy();
        hand.removeCallbacks(startTimer);
        stopped = true;

        // send broadcast to the fragment that stopwatch has ended along with total time it was running
        Intent in = new Intent("stopWatchEnd");
        in.putExtra("elapsedTime", elapsedTime);
        sendBroadcast(in);
    }

    // sends the broadcast to the fragment with the provided time
    public void updateFragment(float time){
        // send signal to fragment
        Intent in = new Intent("stopWatchTick");
        in.putExtra("elapsedTime", time);
        sendBroadcast(in);
    }

    // updates fragment every 100 ms
    private Runnable startTimer = new Runnable() {
        @Override
        public void run() {
            // calculates elapsed time and sends broadcast to fragment
            elapsedTime = System.currentTimeMillis() - startTime;
            updateFragment(elapsedTime);
            hand.postDelayed(this, 100);
        }
    };
}
