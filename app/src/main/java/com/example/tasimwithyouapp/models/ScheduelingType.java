package com.example.tasimwithyouapp.models;

public enum ScheduelingType {
    PASSPORT("passport"),
    CURRENCY_CONVERSION("currency_conversion"),
    INSURANCE("insurance"),
    DEPARTURE("departure");


    public static final long ONE_MINUTE =  60 * 1000;
    public static final long ONE_HOURS =  60 * ONE_MINUTE;
    public static final long FIVE_HOURS = 5 * ONE_HOURS;
    public static final long TWO_HOURS = 2 * ONE_HOURS;
    public static final long THREE_HOURS = 3 * ONE_HOURS;
    public static final long ONE_DAY = 24 * ONE_HOURS;
    public static final long TWO_DAY = 2 * ONE_DAY;

    public static ScheduelingType fromString(String text) {
        if (text != null) {
            for (ScheduelingType b : ScheduelingType.values()) {
                if (text.equalsIgnoreCase(b.name())) {
                    return b;
                }
            }
        }
        return null;
    }

    public static String toString(ScheduelingType scheduelingType) {
        if (scheduelingType != null) {
            return scheduelingType.name();
        }
        return null;
    }

    private String value;

    public String getValue() {
        return value;
    }

     ScheduelingType(String value) {
        this.value = value;
    }
}