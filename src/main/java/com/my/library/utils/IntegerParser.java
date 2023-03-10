package com.my.library.utils;

import java.util.Optional;

public class IntegerParser {

    public static Optional<Integer> parseInt(String number) {
        if (number == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Integer.parseInt(number));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
