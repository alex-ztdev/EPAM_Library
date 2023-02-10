package com.my.library.utils.builder;

import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserBuilderTest {

    private UserBuilder userBuilder = new UserBuilder();
    @Mock
    private HttpServletRequest request;

    @ParameterizedTest
    @NullSource
    @EmptySource
    void buildNewUser_emptyOrNullLogin_ShouldReturnEmptyOptional(String login) {
        when(request.getParameter(UserParameters.REG_LOGIN)).thenReturn(login);
        when(request.getParameter(UserParameters.REG_EMAIL)).thenReturn("email@email.com");
        when(request.getParameter(UserParameters.REG_PHONE)).thenReturn("380777777777");
        when(request.getParameter(UserParameters.REG_FIRST_NAME)).thenReturn("John");
        when(request.getParameter(UserParameters.REG_SECOND_NAME)).thenReturn("Doe");
        when(request.getParameter(UserParameters.REG_PASSWORD)).thenReturn("password");
        when(request.getParameter(UserParameters.REG_CONF_PASSWORD)).thenReturn("password");

        Optional<User> optionalUser = userBuilder.buildNewUser(request);

        assertThat(optionalUser).isEmpty();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    void buildNewUser_emptyOrNullEmail_ShouldReturnEmptyOptional(String email) {
        when(request.getParameter(UserParameters.REG_LOGIN)).thenReturn("login");
        when(request.getParameter(UserParameters.REG_EMAIL)).thenReturn(email);
        when(request.getParameter(UserParameters.REG_PHONE)).thenReturn("380777777777");
        when(request.getParameter(UserParameters.REG_FIRST_NAME)).thenReturn("John");
        when(request.getParameter(UserParameters.REG_SECOND_NAME)).thenReturn("Doe");
        when(request.getParameter(UserParameters.REG_PASSWORD)).thenReturn("password");
        when(request.getParameter(UserParameters.REG_CONF_PASSWORD)).thenReturn("password");

        Optional<User> optionalUser = userBuilder.buildNewUser(request);

        assertThat(optionalUser).isEmpty();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    void buildNewUser_emptyOrNullPhone_ShouldReturnOptionalOfUser(String phone) {
        when(request.getParameter(UserParameters.REG_LOGIN)).thenReturn("validLogin");
        when(request.getParameter(UserParameters.REG_EMAIL)).thenReturn("validEmail@email.com");
        when(request.getParameter(UserParameters.REG_PHONE)).thenReturn(phone);
        when(request.getParameter(UserParameters.REG_FIRST_NAME)).thenReturn("validFirstName");
        when(request.getParameter(UserParameters.REG_SECOND_NAME)).thenReturn("validSecondName");
        when(request.getParameter(UserParameters.REG_PASSWORD)).thenReturn("password");
        when(request.getParameter(UserParameters.REG_CONF_PASSWORD)).thenReturn("password");

        Optional<User> optionalUser = userBuilder.buildNewUser(request);

        assertThat(optionalUser).isPresent();

        User user = optionalUser.get();

        assertThat(user.getLogin()).isEqualTo("validLogin");
        assertThat(user.getEmail()).isEqualTo("validEmail@email.com");
        assertThat(user.getFirstName()).isEqualTo("validFirstName");
        assertThat(user.getSecondName()).isEqualTo("validSecondName");
        assertThat(user.getPassword()).isEqualTo("password");
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    void buildNewUser_emptyOrNullFirstName_ShouldReturnEmptyOptional(String firstName) {
        when(request.getParameter(UserParameters.REG_LOGIN)).thenReturn("validLogin");
        when(request.getParameter(UserParameters.REG_EMAIL)).thenReturn("validEmail@email.com");
        when(request.getParameter(UserParameters.REG_PHONE)).thenReturn("380777777777");
        when(request.getParameter(UserParameters.REG_FIRST_NAME)).thenReturn(firstName);
        when(request.getParameter(UserParameters.REG_SECOND_NAME)).thenReturn("validSecondName");
        when(request.getParameter(UserParameters.REG_PASSWORD)).thenReturn("password");
        when(request.getParameter(UserParameters.REG_CONF_PASSWORD)).thenReturn("password");

        Optional<User> optionalUser = userBuilder.buildNewUser(request);

        assertThat(optionalUser).isEmpty();
    }
    @ParameterizedTest
    @NullSource
    @EmptySource
    void buildNewUser_emptyOrNullSecondName_ShouldReturnEmptyOptional(String secondName) {
        when(request.getParameter(UserParameters.REG_LOGIN)).thenReturn("validLogin");
        when(request.getParameter(UserParameters.REG_EMAIL)).thenReturn("validEmail@email.com");
        when(request.getParameter(UserParameters.REG_PHONE)).thenReturn("380777777777");
        when(request.getParameter(UserParameters.REG_FIRST_NAME)).thenReturn("validFirstName");
        when(request.getParameter(UserParameters.REG_SECOND_NAME)).thenReturn(secondName);
        when(request.getParameter(UserParameters.REG_PASSWORD)).thenReturn("password");
        when(request.getParameter(UserParameters.REG_CONF_PASSWORD)).thenReturn("password");

        Optional<User> optionalUser = userBuilder.buildNewUser(request);

        assertThat(optionalUser).isEmpty();
    }



}