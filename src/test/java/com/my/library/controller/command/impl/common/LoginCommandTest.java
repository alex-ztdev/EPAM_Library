package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dao.constants.UserStatus;
import com.my.library.entities.User;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginCommandTest {

    @Mock
    private HttpSession session;

    @Mock
    private HttpServletRequest request;

    @Mock
    private UserService userService;

    private LoginCommand loginCommand;

    @BeforeEach
    void setUp() {
        loginCommand = new LoginCommand(userService);
    }

    @ParameterizedTest
    @EmptySource
    @NullSource
    @ValueSource(strings = {"invalidPassword", "123\\", "qw,", "--++--"})
    void execute_NonValidPassword_ShouldReturnToLoginPage(String password) throws CommandException {
        String validLogin = "login";
        doReturn(session).when(request).getSession();

        doReturn(validLogin).when(request).getParameter(UserParameters.LOGIN);
        doReturn(password).when(request).getParameter(UserParameters.PASSWORD);

        CommandResult result = loginCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.LOGIN_PAGE);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(session).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.LOGIN_PAGE);
    }

    @ParameterizedTest
    @EmptySource
    @NullSource
    @ValueSource(strings = {"===invalidLogin===", "///", "123", "--++--"})
    void execute_NonValidLogin_ShouldReturnToLoginPage(String login) throws CommandException {
        String validPassword = "pass123";
        doReturn(session).when(request).getSession();


        doReturn(login).when(request).getParameter(UserParameters.LOGIN);
        doReturn(validPassword).when(request).getParameter(UserParameters.PASSWORD);

        CommandResult result = loginCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.LOGIN_PAGE);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(session).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.LOGIN_PAGE);
    }

    @Test
    void execute_AuthenticationFailed_ShouldSetInvalidLoginPasswordMsgToSessionAndReturnToLoginPage() throws CommandException, ServiceException {
        String validLogin = "validLogin";
        String validPassword = "pass123";

        doReturn(session).when(request).getSession();

        doReturn(validLogin).when(request).getParameter(UserParameters.LOGIN);
        doReturn(validPassword).when(request).getParameter(UserParameters.PASSWORD);

        doReturn(Optional.empty()).when(userService).authenticate(validLogin, validPassword);

        CommandResult result = loginCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.LOGIN_PAGE);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(session).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.LOGIN_PAGE);

        verify(session).setAttribute(UserParameters.INVALID_LOGIN_PASSWORD, UserParameters.INVALID_LOGIN_PASSWORD);
    }

    @Test
    void execute_UserIsBlocked_ShouldSetUserIsBlockedMsgToSessionAndReturnToLoginPage() throws CommandException, ServiceException {
        String validLogin = "validLogin";
        String validPassword = "pass123";

        User user = mock(User.class);

        doReturn(UserStatus.BLOCKED).when(user).getStatus();

        doReturn(session).when(request).getSession();

        doReturn(validLogin).when(request).getParameter(UserParameters.LOGIN);
        doReturn(validPassword).when(request).getParameter(UserParameters.PASSWORD);

        doReturn(Optional.of(user)).when(userService).authenticate(validLogin, validPassword);

        CommandResult result = loginCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.LOGIN_PAGE);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(session).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.LOGIN_PAGE);

        verify(session).setAttribute(UserParameters.USER_IS_BLOCKED, UserParameters.USER_IS_BLOCKED);
    }

    @Test
    void execute_SuccessfulAuthentication_ShouldSetUserInSessionAndReturnToLoginPage() throws CommandException, ServiceException {
        String validLogin = "validLogin";
        String validPassword = "pass123";

        User user = mock(User.class);

        doReturn(UserStatus.NORMAL).when(user).getStatus();

        doReturn(session).when(request).getSession();

        doReturn(validLogin).when(request).getParameter(UserParameters.LOGIN);
        doReturn(validPassword).when(request).getParameter(UserParameters.PASSWORD);

        doReturn(Optional.of(user)).when(userService).authenticate(validLogin, validPassword);

        CommandResult result = loginCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.HOME);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(session).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.LOGIN_PAGE);
        verify(session).setAttribute(eq(UserParameters.USER_IN_SESSION), any());
    }

    @Test
    void execute_UserServiceThrowsException_ShouldThrowCommandException() throws ServiceException {
        String validLogin = "validLogin";
        String validPassword = "pass123";

        doReturn(session).when(request).getSession();

        doReturn(validLogin).when(request).getParameter(UserParameters.LOGIN);
        doReturn(validPassword).when(request).getParameter(UserParameters.PASSWORD);

        doThrow(ServiceException.class).when(userService).authenticate(validLogin, validPassword);

        assertThatThrownBy(() -> loginCommand.execute(request))
                .isExactlyInstanceOf(CommandException.class)
                .hasCauseExactlyInstanceOf(ServiceException.class);

        verify(session).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.LOGIN_PAGE);
    }
}