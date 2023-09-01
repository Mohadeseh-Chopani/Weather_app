package com.example.weather.Notification;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

public class Notification extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel=new NotificationChannel("temperature_notification","notification about informing from weather",
                NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(channel);

    }
}
