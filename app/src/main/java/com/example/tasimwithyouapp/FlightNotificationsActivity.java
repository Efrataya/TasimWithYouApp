package com.example.tasimwithyouapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;

public class FlightNotificationsActivity extends AppCompatActivity {


    CheckBox notification24h, notification5h, notification3h, notification1m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_flight);
        // notification24h = findViewById(R.id.notification_24h);
    }
}