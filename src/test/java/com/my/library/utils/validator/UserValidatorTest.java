package com.my.library.utils.validator;

import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.entities.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @Nested
    @DisplayName("validateUserParameters")
    class ValidateUserParameters {
        @Test
        void validateUserParameters_ValidInput_ReturnsEmptyList() {
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
    }


    @Nested
    @DisplayName("isValidEmail")
    class IsValidEmail {
        @ParameterizedTest
        @ValueSource(strings = {"invalidmail", "invalid@@mail.com", "invalid@my..com", "invalid@qwe.com1"})
        void isValidEmail_invalidEmailInput_ReturnsFalse(String invalidEmail) {
            assertThat(userValidator.isValidEmail(invalidEmail)).isFalse();
        }

        @ParameterizedTest
        @NullSource
        void isValidEmail_nullInput_ReturnsFalse(String invalidEmail) {
            assertThat(userValidator.isValidEmail(invalidEmail)).isFalse();
        }

        @ParameterizedTest
        @EmptySource
        void isValidEmail_emptyInput_ReturnsFalse(String invalidEmail) {
            assertThat(userValidator.isValidEmail(invalidEmail)).isFalse();
        }
    }


    @Nested
    @DisplayName("isValidPassword")
    class IsValidPassword {
        @Test
        public void isValidPassword_LessThanSixChars() {
            String password = "q123";
            boolean result = userValidator.isValidPassword(password);
            assertThat(result).isFalse();
        }

        @Test
        public void isValidPassword_NoLetters() {
            String password = "12345678";
            boolean result = userValidator.isValidPassword(password);
            assertThat(result).isFalse();
        }

        @Test
        public void isValidPassword_NoNumbers() {
            String password = "password";
            boolean result = userValidator.isValidPassword(password);
            assertThat(result).isFalse();
        }

        @ParameterizedTest
        @NullSource
        @EmptySource
        public void isValidPassword_NullOrEmptyPassword_ReturnsFalse(String password) {
            boolean result = userValidator.isValidPassword(password);
            assertThat(result).isFalse();
        }

        @ParameterizedTest
        @ValueSource(strings = {"qwe1231", "Aa1Bb2Cc3Dd4Ee5Ff6Gg7Hh8", "myPassword123", "qwe3qwe321"})
        public void isValidPassword_ValidPasswords_ReturnsTrue(String password) {
            boolean result = userValidator.isValidPassword(password);

            assertThat(result).isTrue();
        }

    }

    @Nested
    @DisplayName("isValidLogin")
    class IsValidLogin {
        @ParameterizedTest
        @ValueSource(strings = {"user123", "login_1", "validLogin123"})
        void isValidLogin_ValidLogin_ReturnsTrue(String login) {
            boolean result = userValidator.isValidLogin(login);

            assertThat(result).isTrue();
        }

        @ParameterizedTest
        @EmptySource
        @NullSource
        void isValidLogin_NullAndEmptySource_ReturnsFalse(String login) {
            boolean result = userValidator.isValidLogin(login);

            assertThat(result).isFalse();
        }

        @ParameterizedTest
        @ValueSource(strings = {"11", "qw", "22", "lo"})
        void isValidLogin_shortLogin_ReturnsFalse(String login) {
            boolean result = userValidator.isValidLogin(login);

            assertThat(result).isFalse();
        }

        @ParameterizedTest
        @ValueSource(strings = {"symbols++--", "\\---", "invalid+login", "qwe|/"})
        void isValidLogin_InvalidSymbolsLogin_ReturnsFalse(String login) {
            boolean result = userValidator.isValidLogin(login);

            assertThat(result).isFalse();
        }

    }


    @Nested
    @DisplayName("isValidName")
    class IsValidName {
        @ParameterizedTest
        @NullSource
        @EmptySource
        public void isValidName_EmptyOrNull_ReturnsFalse(String name) {
            boolean result = userValidator.isValidName(name);

            assertThat(result).isFalse();
        }

        @ParameterizedTest
        @ValueSource(strings = {"Jo213n", "J123", "JohnJohnJohnJohnJohnJohnJohnJohn", "John+John"})
        public void isValidName_InvalidName_ReturnsFalse(String name) {
            boolean result = userValidator.isValidName(name);

            assertThat(result).isFalse();
        }

        @ParameterizedTest
        @ValueSource(strings = {"John", "Sophie", "Michael Jackson", "Mary-Jane", "O'Neil"})
        public void isValidName_ValidName_ReturnsTrue(String name) {
            boolean result = userValidator.isValidName(name);

            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("isValidPhone")
    class IsValidPhone {
        @ParameterizedTest
        @NullSource
        @EmptySource
        public void isValidPhone_emptyOrNull_ReturnsFalse(String name) {
            boolean result = userValidator.isValidPhone(name);
            assertThat(result).isFalse();
        }

        @ParameterizedTest
        @ValueSource(strings = {"23909", "390121654", "12312312312312332121"})
        public void isValidPhone_WrongLength_ReturnsFalse(String name) {
            boolean result = userValidator.isValidPhone(name);

            assertThat(result).isFalse();
        }

        @ParameterizedTest
        @ValueSource(strings = {"380687777777", "380687777777", "044068777777"})
        public void isValidPhone_ValidPhone_ReturnsTrue(String name) {
            boolean result = userValidator.isValidPhone(name);

            assertThat(result).isTrue();
        }
    }


}