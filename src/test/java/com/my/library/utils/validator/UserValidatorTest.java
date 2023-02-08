package com.my.library.utils.validator;

import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


class UserValidatorTest {

    private final UserValidator userValidator = new UserValidator();

    @Test
    void ValidateUserParametersTest() {
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
    void InvalidLoginTest() {
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
    void InvalidPasswordTest() {
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
    void InvalidEmailTest_invalidEmail(String invalidEmail) {
        assertThat(userValidator.isValidEmail(invalidEmail)).isFalse();
    }

    @ParameterizedTest
    @NullSource
    void InvalidEmailTest_nullEmail(String invalidEmail) {
        assertThat(userValidator.isValidEmail(invalidEmail)).isFalse();
    }

    @ParameterizedTest
    @EmptySource
    void InvalidEmailTest_emptyEmail(String invalidEmail) {
        assertThat(userValidator.isValidEmail(invalidEmail)).isFalse();
    }


    @Test
    public void IsValidPasswordTest_validPassword() {
        String password = "validPassword123";
        boolean result = userValidator.isValidPassword(password);
        assertThat(result).isTrue();
    }

    @Test
    public void IsValidPasswordTest_lessThanSixChars() {
        String password = "q123";
        boolean result = userValidator.isValidPassword(password);
        assertThat(result).isFalse();
    }

    @Test
    public void IsValidPasswordTest_noLetters() {
        String password = "12345678";
        boolean result = userValidator.isValidPassword(password);
        assertThat(result).isFalse();
    }

    @Test
    public void IsValidPasswordTest_noNumbers() {
        String password = "password";
        boolean result = userValidator.isValidPassword(password);
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void IsValidPasswordTest_nullOrEmptyPassword(String password) {
        boolean result = userValidator.isValidPassword(password);
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"qwe1231", "Aa1Bb2Cc3Dd4Ee5Ff6Gg7Hh8", "myPassword123", "qwe3qwe321"})
    public void isValidPassword_validPasswords_returnsTrue(String password) {
        boolean result = userValidator.isValidPassword(password);

        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"user123", "login_1", "validLogin123"})
    void isValidLoginTest(String login) {
        boolean result = userValidator.isValidLogin(login);

        assertThat(result).isTrue();
    }
    @ParameterizedTest
    @EmptySource
    @NullSource
    void isValidLoginTest_nullEmptySource(String login) {
        boolean result = userValidator.isValidLogin(login);

        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"11", "qw", "22", "lo"})
    void isValidLoginTest_shortLogin(String login) {
        boolean result = userValidator.isValidLogin(login);

        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"symbols++--", "\\---", "invalid+login", "qwe|/"})
    void isValidLoginTest_invalidSymbolsLogin(String login) {
        boolean result = userValidator.isValidLogin(login);

        assertThat(result).isFalse();
    }



    @ParameterizedTest
    @NullSource
    @EmptySource
    public void isValidNameTest_emptyOrNull(String name) {
        boolean result = userValidator.isValidName(name);

        assertThat(result).isFalse();
    }
    @ParameterizedTest
    @ValueSource(strings = {"Jo213n", "J123", "JohnJohnJohnJohnJohnJohnJohnJohn", "John+John" })
    public void isValidNameTest_invalidName(String name) {
        boolean result = userValidator.isValidName(name);

        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = { "John", "Sophie", "Michael Jackson", "Mary-Jane", "O'Neil" })
    public void isValidNameTest_validName(String name) {
        boolean result = userValidator.isValidName(name);

        assertThat(result).isTrue();
    }@ParameterizedTest
    @NullSource
    @EmptySource
    public void isValidPhoneTest_emptyOrNull(String name) {
        boolean result = userValidator.isValidPhone(name);
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"23909", "390121654", "12312312312312332121" })
    public void isInvalidPhoneTest_wrongLength(String name) {
        boolean result = userValidator.isValidPhone(name);

        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = { "380687777777", "380687777777", "044068777777"})
    public void isValidPhoneTest_validPhone(String name) {
        boolean result = userValidator.isValidPhone(name);

        assertThat(result).isTrue();
    }

}