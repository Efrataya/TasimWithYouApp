package com.example.tasimwithyouapp;

public enum ScheduelingType {
    PASSPORT("passport"),
    CURRENCY_CONVERSION("currency_conversion"),
    INSURANCE("insurance"),
    DEPARTURE("departure");

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