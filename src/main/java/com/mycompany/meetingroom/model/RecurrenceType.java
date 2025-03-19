package com.mycompany.meetingroom.model;

public enum RecurrenceType {
    DAILY,
    WEEKLY,
    BIWEEKLY,
    MONTHLY,
    YEARLY;

    public static RecurrenceType fromString(String value) {
        for (RecurrenceType type : RecurrenceType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant " + RecurrenceType.class.getCanonicalName() + "." + value);
    }
}
