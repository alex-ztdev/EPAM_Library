package com.my.library.utils;

import java.util.Optional;

public class LongParser {
    public static Optional<Long> parseLong(String number) {
        try {
            return Optional.of(Long.parseLong(number));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
