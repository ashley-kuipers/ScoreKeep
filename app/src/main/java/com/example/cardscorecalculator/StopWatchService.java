package com.example.cardscorecalculator;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

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
        Log.d("TAG", "Starting stopwatch service");
        reset = intent.getBooleanExtra("reset", false);

        if (reset) {
            startTime = System.currentTimeMillis();
            elapsedTime = 0;
        } else {
            startTime = System.currentTimeMillis() - elapsedTime;
        }

        hand.removeCallbacks(startTimer);
        hand.postDelayed(startTimer, 0);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hand.removeCallbacks(startTimer);
        stopped = true;

        Intent in = new Intent("stopWatchEnd");
        in.putExtra("elapsedTime", elapsedTime);
        sendBroadcast(in);

        Log.d("TAG", "Stopwatch service ended with time " + elapsedTime);
        // end stopwatch
    }

    public void updateFragment(float time){
        // send signal to fragment
        Intent in = new Intent("stopWatchTick");
        in.putExtra("elapsedTime", time);
        sendBroadcast(in);
    }

    private Runnable startTimer = new Runnable() {
        @Override
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            updateFragment(elapsedTime);
            hand.postDelayed(this, 100);
        }
    };
}
