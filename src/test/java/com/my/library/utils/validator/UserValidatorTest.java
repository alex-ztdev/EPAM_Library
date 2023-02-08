package com.my.library.utils.validator;

import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.in;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

//TODO: implement tests
class UserValidatorTest {

    private final UserValidator userValidator = new UserValidator();

    @Test
    void testValidateUserParameters() {
        User user = new User();
        user.setLogin("validLogin");
        user.setEmail("validEmail@example.com");
        user.setPhoneNumber("380950000000");
        user.setFirstName("John");
        user.setSecondName("Doe");
        user.setPassword("validPassword123");

        List<String> validationList = userValidator.validateUserParameters(user);

        assertThat(validationList).isEmpty();
    }

    @Test
    void testInvalidLogin() {
        User user = new User();
        user.setLogin("invalid Login");
        user.setEmail("validEmail@example.com");
        user.setPhoneNumber("380950000000");
        user.setFirstName("John");
        user.setSecondName("Doe");
        user.setPassword("validPassword123");

        List<String> validationList = userValidator.validateUserParameters(user);
        assertEquals(1, validationList.size());
        assertEquals(UserParameters.REG_INVALID_LOGIN, validationList.get(0));
    }

    @Test
    void testInvalidPassword() {
        User user = new User();
        user.setLogin("validLogin");
        user.setEmail("validEmail@example.com");
        user.setPhoneNumber("380950000000");
        user.setFirstName("John");
        user.setSecondName("Doe");
        user.setPassword("invalid Password");

        List<String> validationList = userValidator.validateUserParameters(user);
        assertThat(validationList).hasSize(1);
        assertThat(validationList).containsOnly(UserParameters.REG_INVALID_PASSWORD);
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalidmail", "invalid@@mail.com", "invalid@my..com", "invalid@qwe.com1"})
    void testInvalidEmail_invalidEmail(String invalidEmail) {
        assertThat(userValidator.isValidEmail(invalidEmail)).isFalse();
    }
    @ParameterizedTest
    @NullSource
    void testInvalidEmail_nullEmail(String invalidEmail) {
        assertThat(userValidator.isValidEmail(invalidEmail)).isFalse();
    }
    @ParameterizedTest
    @EmptySource
    void testInvalidEmail_emptyEmail(String invalidEmail) {
        assertThat(userValidator.isValidEmail(invalidEmail)).isFalse();
    }


    @Test
    public void testIsValidPassword_validPassword() {
        String password = "validPassword123";
        boolean result = userValidator.isValidPassword(password);
        assertThat(result).isTrue();
    }

    @Test
    public void testIsValidPassword_invalidPassword_lessThanSixChars() {
        String password = "q123";
        boolean result = userValidator.isValidPassword(password);
        assertThat(result).isFalse();
    }

    @Test
    public void testIsValidPassword_invalidPassword_noLetters() {
        String password = "12345678";
        boolean result = userValidator.isValidPassword(password);
        assertThat(result).isFalse();
    }

    @Test
    public void testIsValidPassword_invalidPassword_noNumbers() {
        String password = "password";
        boolean result = userValidator.isValidPassword(password);
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void testIsValidPassword_nullOrEmptyPassword(String password) {
        boolean result = userValidator.isValidPassword(password);
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"qwe1231", "Aa1Bb2Cc3Dd4Ee5Ff6Gg7Hh8", "myPassword123", "qwe3qwe321"})
    public void isValidPassword_validPasswords_returnsTrue(String password) {
        boolean result = userValidator.isValidPassword(password);

        assertThat(result).isTrue();
    }



}