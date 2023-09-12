package ru.practicum.baseService.enums;

public enum Status {
    CONFIRMED, REJECTED, PENDING, CANCELED;

    public static Status from(String status) {
        for (Status value : Status.values()) {
            if (value.name().equalsIgnoreCase(status)) {
                return value;
            }
        }
        return null;
    }
}