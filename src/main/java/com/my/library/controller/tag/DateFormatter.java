package com.my.library.controller.tag;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateFormatter {
    public static String formatDateTime(LocalDateTime localDateTime, String pattern, String language) {
        var dateTimeFormatter = DateTimeFormatter.ofPattern(pattern,
                "ua".equals(language) ? new Locale("uk", "UA") : Locale.ENGLISH);
        return localDateTime.format(dateTimeFormatter);
    }
}
