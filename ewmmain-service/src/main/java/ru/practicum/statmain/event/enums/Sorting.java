package ru.practicum.statmain.event.enums;

import java.util.Optional;

public enum Sorting {
    EVENT_DATE, VIEWS;

    public static Optional<Sorting> from(String stringSort) {
        for (Sorting sorting : values()) {
            if (sorting.name().equalsIgnoreCase(stringSort)) {
                return Optional.of(sorting);
            }
        }
        return Optional.empty();
    }
}
