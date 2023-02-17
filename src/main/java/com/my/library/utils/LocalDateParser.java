package com.my.library.utils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class LocalDateParser {
    public static Optional<LocalDate> parseLocalDate(String localDate) {
        if (localDate == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(LocalDate.parse(localDate));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }
}
