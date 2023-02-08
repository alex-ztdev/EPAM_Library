package com.my.library.utils;

import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegerParserTest {
    @ParameterizedTest
    @NullSource
    @EmptySource
    public void parseInt_NullInput_ReturnsEmptyOptional(String input) {
        Optional<Integer> result = IntegerParser.parseInt(input);
        assertThat(result).isNotPresent();
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "-123", "100", "123456"})
    void parseInt_ValidInput_ReturnsOptionalOfNumber(String input) {
        Optional<Integer> result = IntegerParser.parseInt(input);

        assertThat(result)
                .isPresent()
                .contains(Integer.valueOf(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "-234324432324234324324", "ghi", "jkl", "2349.230"})
    void parseInt_NotANumberInput_ReturnEmptyOptional(String input) {
        Optional<Integer> result = IntegerParser.parseInt(input);

        assertThat(result).isNotPresent();
    }

}