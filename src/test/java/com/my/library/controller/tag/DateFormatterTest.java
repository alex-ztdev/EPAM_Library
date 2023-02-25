package com.my.library.controller.tag;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


class DateFormatterTest {
    @Test
    void formatLocalDateTime_shouldFormatDateTimeCorrectly() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 2, 21, 13, 30);
        String pattern = "dd/MM/yyyy HH:mm:ss";
        String language = "en";

        String result = DateFormatter.formatLocalDateTime(dateTime, pattern, language);

        assertThat(result).isEqualTo("21/02/2023 13:30:00");
    }


    @Test
    void formatDateTime_shouldFormatDateCorrectly() {
        LocalDate date = LocalDate.of(2023, 2, 21);
        String pattern = "dd/MM/yyyy";
        String language = "ua";

        String result = DateFormatter.formatDateTime(date, pattern, language);

        assertThat(result).isEqualTo("21/02/2023");
    }
}