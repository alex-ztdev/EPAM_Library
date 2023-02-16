package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.entities.User;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterCommandTest {
    @Mock
    private HttpSession session;

    @Mock
    private HttpServletRequest request;

    @Mock
    private UserService userService;
    private RegisterCommand registerCommand;

    @BeforeEach
    void setUp() {
        registerCommand = new RegisterCommand(userService);
    }

    @Test
    void execute_validData_ShouldSetUserDataToSessionAndReturnToRegistrationPage() throws CommandException, ServiceException {
        User user = mock(User.class);
        doReturn(1L).when(user).getUserId();
        doReturn(user).when(userService).save(any());

        when(request.getSession()).thenReturn(session);

        when(request.getParameter(UserParameters.REG_LOGIN)).thenReturn("login");
        when(request.getParameter(UserParameters.REG_EMAIL)).thenReturn("email@email.com");
        when(request.getParameter(UserParameters.REG_PHONE)).thenReturn("380777777777");
        when(request.getParameter(UserParameters.REG_FIRST_NAME)).thenReturn("John");
        when(request.getParameter(UserParameters.REG_SECOND_NAME)).thenReturn("Doe");
        when(request.getParameter(UserParameters.REG_PASSWORD)).thenReturn("password123");
        when(request.getParameter(UserParameters.REG_CONF_PASSWORD)).thenReturn("password123");

        CommandResult result = registerCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.LOGIN_PAGE_WITH_SUCCESS);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(session).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.REGISTRATION_PAGE);

        verify(session).setAttribute(UserParameters.REG_LOGIN_VAL, request.getParameter(UserParameters.REG_LOGIN));
        verify(session).setAttribute(UserParameters.REG_EMAIL_VAL, request.getParameter(UserParameters.REG_EMAIL));
        verify(session).setAttribute(UserParameters.REG_PHONE_VAL, request.getParameter(UserParameters.REG_PHONE));
        verify(session).setAttribute(UserParameters.REG_FIRST_NAME_VAL, request.getParameter(UserParameters.REG_FIRST_NAME));
        verify(session).setAttribute(UserParameters.REG_SECOND_NAME_VAL, request.getParameter(UserParameters.REG_SECOND_NAME));
    }


    @Test
    void execute_validationsErrors_ShouldSetUserDataValidationErrorsToSessionAndReturnToRegistrationPage() throws CommandException, ServiceException {
        when(request.getSession()).thenReturn(session);

        when(request.getParameter(UserParameters.REG_LOGIN)).thenReturn("login");
        when(request.getParameter(UserParameters.REG_EMAIL)).thenReturn("email@email.com");
        when(request.getParameter(UserParameters.REG_PHONE)).thenReturn("380777777777");
        when(request.getParameter(UserParameters.REG_FIRST_NAME)).thenReturn("John");
        when(request.getParameter(UserParameters.REG_SECOND_NAME)).thenReturn("Doe");
        when(request.getParameter(UserParameters.REG_PASSWORD)).thenReturn("password123");
        when(request.getParameter(UserParameters.REG_CONF_PASSWORD)).thenReturn("password123");

        List<String> validationList = List.of(
                UserParameters.USER_EMAIL_ALREADY_EXISTS,
                UserParameters.USER_LOGIN_ALREADY_EXISTS,
                UserParameters.USER_PHONE_ALREADY_EXISTS
        );
        doReturn(validationList).when(userService).canBeRegistered(any());


        CommandResult result = registerCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.REGISTRATION_PAGE);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(session).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.REGISTRATION_PAGE);

        verify(session).setAttribute(UserParameters.REG_LOGIN_VAL, request.getParameter(UserParameters.REG_LOGIN));
        verify(session).setAttribute(UserParameters.REG_EMAIL_VAL, request.getParameter(UserParameters.REG_EMAIL));
        verify(session).setAttribute(UserParameters.REG_PHONE_VAL, request.getParameter(UserParameters.REG_PHONE));
        verify(session).setAttribute(UserParameters.REG_FIRST_NAME_VAL, request.getParameter(UserParameters.REG_FIRST_NAME));
        verify(session).setAttribute(UserParameters.REG_SECOND_NAME_VAL, request.getParameter(UserParameters.REG_SECOND_NAME));

        verify(session).setAttribute(UserParameters.USER_EMAIL_ALREADY_EXISTS, UserParameters.USER_EMAIL_ALREADY_EXISTS);
        verify(session).setAttribute(UserParameters.USER_LOGIN_ALREADY_EXISTS, UserParameters.USER_LOGIN_ALREADY_EXISTS);
        verify(session).setAttribute(UserParameters.USER_PHONE_ALREADY_EXISTS, UserParameters.USER_PHONE_ALREADY_EXISTS);
    }

    @Test
    void execute_NullUserParameter_ShouldReturnToRegistrationPage() throws CommandException {
        when(request.getSession()).thenReturn(session);

        when(request.getParameter(UserParameters.REG_LOGIN)).thenReturn(null);
        when(request.getParameter(UserParameters.REG_EMAIL)).thenReturn("email@email.com");
        when(request.getParameter(UserParameters.REG_PHONE)).thenReturn("380777777777");
        when(request.getParameter(UserParameters.REG_FIRST_NAME)).thenReturn("John");
        when(request.getParameter(UserParameters.REG_SECOND_NAME)).thenReturn("Doe");
        when(request.getParameter(UserParameters.REG_PASSWORD)).thenReturn("password123");
        when(request.getParameter(UserParameters.REG_CONF_PASSWORD)).thenReturn("password123");

        CommandResult result = registerCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.REGISTRATION_PAGE);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(session).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.REGISTRATION_PAGE);

        verify(session, times(0)).setAttribute(UserParameters.REG_LOGIN_VAL, request.getParameter(UserParameters.REG_LOGIN));
        verify(session, times(0)).setAttribute(UserParameters.REG_EMAIL_VAL, request.getParameter(UserParameters.REG_EMAIL));
        verify(session, times(0)).setAttribute(UserParameters.REG_PHONE_VAL, request.getParameter(UserParameters.REG_PHONE));
        verify(session, times(0)).setAttribute(UserParameters.REG_FIRST_NAME_VAL, request.getParameter(UserParameters.REG_FIRST_NAME));
        verify(session, times(0)).setAttribute(UserParameters.REG_SECOND_NAME_VAL, request.getParameter(UserParameters.REG_SECOND_NAME));
    }

    @Test
    void execute_UserServiceThrowsException_ShouldThrowCommandException() throws ServiceException {
        doThrow(ServiceException.class).when(userService).save(any());

        when(request.getSession()).thenReturn(session);

        when(request.getParameter(UserParameters.REG_LOGIN)).thenReturn("login");
        when(request.getParameter(UserParameters.REG_EMAIL)).thenReturn("email@email.com");
        when(request.getParameter(UserParameters.REG_PHONE)).thenReturn("380777777777");
        when(request.getParameter(UserParameters.REG_FIRST_NAME)).thenReturn("John");
        when(request.getParameter(UserParameters.REG_SECOND_NAME)).thenReturn("Doe");
        when(request.getParameter(UserParameters.REG_PASSWORD)).thenReturn("password123");
        when(request.getParameter(UserParameters.REG_CONF_PASSWORD)).thenReturn("password123");

        assertThatThrownBy(()->registerCommand.execute(request))
                .isExactlyInstanceOf(CommandException.class)
                .hasCauseExactlyInstanceOf(ServiceException.class);

        verify(session).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.REGISTRATION_PAGE);

        verify(session).setAttribute(UserParameters.REG_LOGIN_VAL, request.getParameter(UserParameters.REG_LOGIN));
        verify(session).setAttribute(UserParameters.REG_EMAIL_VAL, request.getParameter(UserParameters.REG_EMAIL));
        verify(session).setAttribute(UserParameters.REG_PHONE_VAL, request.getParameter(UserParameters.REG_PHONE));
        verify(session).setAttribute(UserParameters.REG_FIRST_NAME_VAL, request.getParameter(UserParameters.REG_FIRST_NAME));
        verify(session).setAttribute(UserParameters.REG_SECOND_NAME_VAL, request.getParameter(UserParameters.REG_SECOND_NAME));
    }


//    when(request.getSession()).thenReturn(session);
//
//        when(request.getParameter(UserParameters.REG_LOGIN)).thenReturn(null);
//        when(request.getParameter(UserParameters.REG_EMAIL)).thenReturn("email@email.com");
//        when(request.getParameter(UserParameters.REG_PHONE)).thenReturn("380777777777");
//        when(request.getParameter(UserParameters.REG_FIRST_NAME)).thenReturn("John");
//        when(request.getParameter(UserParameters.REG_SECOND_NAME)).thenReturn("Doe");
//        when(request.getParameter(UserParameters.REG_PASSWORD)).thenReturn("password123");
//        when(request.getParameter(UserParameters.REG_CONF_PASSWORD)).thenReturn("password123");
//
//
//        CommandResult result = registerCommand.execute(request);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getPage()).isEqualTo(RedirectToPage.REGISTRATION_PAGE);
//        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);
//
//        verify(session).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.REGISTRATION_PAGE);
//
//        verify(session).setAttribute(UserParameters.REG_LOGIN_VAL, request.getParameter(UserParameters.REG_LOGIN));
//        verify(session).setAttribute(UserParameters.REG_EMAIL_VAL, request.getParameter(UserParameters.REG_EMAIL));
//        verify(session).setAttribute(UserParameters.REG_PHONE_VAL, request.getParameter(UserParameters.REG_PHONE));
//        verify(session).setAttribute(UserParameters.REG_FIRST_NAME_VAL, request.getParameter(UserParameters.REG_FIRST_NAME));
//        verify(session).setAttribute(UserParameters.REG_SECOND_NAME_VAL, request.getParameter(UserParameters.REG_SECOND_NAME));
}