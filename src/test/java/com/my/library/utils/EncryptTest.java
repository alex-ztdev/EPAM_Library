package com.my.library.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class EncryptTest {

    @ParameterizedTest
    @NullSource
    void encryptWithSha512Hex_NullInput_ShouldThrowNPE(String password) {

        assertThatThrownBy(() -> Encrypt.encryptWithSha512Hex(password))
                .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/password_encryption.csv")
    void encryptWithSha512Hex_validInput(String password, String expected) {
        assertThat(Encrypt.encryptWithSha512Hex(password)).isEqualTo(expected);
    }
}