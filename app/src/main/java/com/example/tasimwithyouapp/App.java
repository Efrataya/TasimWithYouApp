package com.example.tasimwithyouapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import androidx.core.app.NotificationManagerCompat;

import com.example.tasimwithyouapp.datasource.NotificationHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class App extends Application {
    public static final boolean isBeta = true;
    @Override
    public void onCreate() {
        super.onCreate();
        NotificationChannel channel = new NotificationChannel(
                NotificationHelper.CHANNEL_ID,
                NotificationHelper.CHANNEL_ID,
                NotificationManager.IMPORTANCE_HIGH
        );
        NotificationManagerCompat.from(this)
                .createNotificationChannel(channel);
    }


}

