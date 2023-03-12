package com.example.tasimwithyouapp.datasource;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.tasimwithyouapp.models.ScheduledNotificationHandle;
import com.google.gson.Gson;

public class NotificationWorker extends Worker {
    public static int NOTIFICATION_ID = 0;
    private static final Object lock = new Object();

    public static void incrementNotificationId() {
        synchronized (lock) {
            NOTIFICATION_ID++;
        }
    }

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        ScheduledNotificationHandle scheduledNotificationHandle;
        Gson gson = new Gson();
        scheduledNotificationHandle = gson.fromJson(getInputData()
                .getString("scheduledNotificationHandle"), ScheduledNotificationHandle.class);
        incrementNotificationId();
        NotificationManagerCompat.from(getApplicationContext())
                .notify(NOTIFICATION_ID,
                        NotificationHelper.createNotification(getApplicationContext(),
                                scheduledNotificationHandle));
        return Result.success();
    }
}
