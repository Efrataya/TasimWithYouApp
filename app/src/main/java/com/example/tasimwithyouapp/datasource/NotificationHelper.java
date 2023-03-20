package com.example.tasimwithyouapp.datasource;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.tasimwithyouapp.R;
import com.example.tasimwithyouapp.models.ScheduledNotificationHandle;
import com.example.tasimwithyouapp.models.User;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NotificationHelper {

    public static final String CHANNEL_ID = "channel1";

    public static boolean checkNotificationsPermission(Activity context) {
        return NotificationManagerCompat.from(context)
                .areNotificationsEnabled();
    }

    public static List<ScheduledNotificationHandle> scheduleNotifications(
            Activity context,
            User user,
            List<ScheduledNotificationHandle> scheduledNotificationHandles
    ) {

        if (!checkNotificationsPermission(context)) {
            System.out.println("[Error] No notification permissions");
            return scheduledNotificationHandles;
        }
        WorkManager workManager = WorkManager.getInstance(context);
        workManager.cancelAllWork();
        List<OneTimeWorkRequest> workRequests = new ArrayList<>();
        long date = LocalDateTime
                .parse(user.currentFlight.getFlightDate())
                .toInstant(ZoneId.systemDefault().getRules().getOffset(LocalDateTime.now()))
                .toEpochMilli();
        for (ScheduledNotificationHandle handle : scheduledNotificationHandles) {
            if (date - handle.getDate() <= 0) // if the notification date is in the past,
                continue;
            if(handle.getDate() - System.currentTimeMillis() < 0) {
                System.out.println("Notification date is in the past, skipping");
                continue;
            }
            String scheduledNotificationHandleJson = new Gson().toJson(handle);
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                    .setInputData(
                            new Data.Builder()
                                    .putString("scheduledNotificationHandle",
                                            scheduledNotificationHandleJson).build())
                    .setInitialDelay(
                            handle.getDate() - System.currentTimeMillis(),
                            TimeUnit.MILLISECONDS)
                    .build();

            handle.setId(workRequest.getId());
            workRequests.add(workRequest);
            System.out.println("[Info] Scheduled notification: " + handle.getMessage() + " at " + handle.getDate() + " with id " + handle.getId());
        }
        if (!workRequests.isEmpty())
            workManager.enqueue(workRequests);
        System.out.println("[Info] " + workRequests.size() + " notifications scheduled");
        return scheduledNotificationHandles;
    }

    public static Notification createNotification(Context context,
                                                  ScheduledNotificationHandle scheduledNotificationHandle) {
        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("התראה!")
                .setContentText(scheduledNotificationHandle.getMessage())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
    }

}
