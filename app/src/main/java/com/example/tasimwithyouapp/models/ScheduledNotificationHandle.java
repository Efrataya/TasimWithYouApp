package com.example.tasimwithyouapp.models;

import java.util.UUID;

public class ScheduledNotificationHandle {
    private final long date;
    private final String message;
    private final ScheduelingType scheduelingType;
    private UUID id;

    public ScheduledNotificationHandle(long date, String message, ScheduelingType scheduelingType, UUID id) {
        this.date = date;
        this.message = message;
        this.scheduelingType = scheduelingType;
        this.id = id;
    }
    public ScheduledNotificationHandle(long date, String message, ScheduelingType scheduelingType) {
        this.date = date;
        this.message = message;
        this.scheduelingType = scheduelingType;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public long getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public ScheduelingType getScheduelingType() {
        return scheduelingType;
    }

    public UUID getId() {
        return id;
    }
}
