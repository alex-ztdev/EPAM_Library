package com.my.library.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class LongParserTest {

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void parseLong_NullInput_ReturnsEmptyOptional(String input) {
        Optional<Long> result = LongParser.parseLong(input);
        assertThat(result).isNotPresent();
    }

    @ParameterizedTest
    @ValueSource(strings = {"123213123213321", "-1233211323213213", "1000000", "12345600000000000"})
    void parseLong_ValidInput_ReturnsOptionalOfNumber(String input) {
        Optional<Long> result = LongParser.parseLong(input);

        assertThat(result)
                .isPresent()
                .contains(Long.valueOf(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "-234324432324234324324", "ghi", "jkl", "2349.230"})
    void parseLong_NotANumberInput_ReturnEmptyOptional(String input) {
        Optional<Long> result = LongParser.parseLong(input);

        assertThat(result).isNotPresent();
    }
}