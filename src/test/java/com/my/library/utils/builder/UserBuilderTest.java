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
        when(request.getParameter(UserParameters.REG_PHONE)).thenReturn("");
        when(request.getParameter(UserParameters.REG_FIRST_NAME)).thenReturn("John");
        when(request.getParameter(UserParameters.REG_SECOND_NAME)).thenReturn("Doe");
        when(request.getParameter(UserParameters.REG_PASSWORD)).thenReturn("password");
        when(request.getParameter(UserParameters.REG_CONF_PASSWORD)).thenReturn("password");

        Optional<User> optionalUser = userBuilder.buildNewUser(request);

        assertThat(optionalUser).isEmpty();
    }
}