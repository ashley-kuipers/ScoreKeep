package com.example.cardscorecalculator;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class TimerService extends Service {
    boolean finished = false, sound, notification;
    long timeLeft=0;
    CountDownTimer ct;
    private static final String CHANNEL_ID = "0";
    NotificationManagerCompat notificationManager;
    NotificationCompat.Builder builder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // retrieve settings
        getSharedPreferences();

        createNotificationChannel();

        // Create an explicit intent for an Activity in your app
        Intent i = new Intent(this, TimerActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_IMMUTABLE);

        // Building push notification
        builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("ScoreKeep")
                .setContentText("Timer is finished!")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true);

        notificationManager = NotificationManagerCompat.from(this);

        Log.d("TAG", "notification before timer " + notification);
        Log.d("TAG", "are notifications enabled?? before timer " + NotificationManagerCompat.from(this).areNotificationsEnabled());

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
                    // send push notification
                    Log.d("TAG", "notification end timer " + notification);
                    if(notification){
                        notificationManager.notify(111, builder.build());
                        Log.d("TAG", "Sent push notification");
                    }

                    if(sound){
                        MediaPlayer ring = MediaPlayer.create(getBaseContext(), R.raw.alarm);
                        ring.start();
                    }

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

    // Retrieve shared preferences file
    public void getSharedPreferences(){
        // get values from shared preferences
        SharedPreferences sh = this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        // retrieve variables from file
        sound = sh.getBoolean("soundSetting", true);
        notification = sh.getBoolean("notificationSetting", NotificationManagerCompat.from(this).areNotificationsEnabled());
        Log.d("TAG", "are notifications enabled?? timerservice " + NotificationManagerCompat.from(this).areNotificationsEnabled());
        Log.d("TAG", "notification timerservice " + notification);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = this.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
