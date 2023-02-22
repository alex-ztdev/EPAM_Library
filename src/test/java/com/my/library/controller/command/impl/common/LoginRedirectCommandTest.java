package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.entities.User;
import com.my.library.exceptions.CommandException;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginRedirectCommandTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @InjectMocks
    private LoginRedirectCommand loginRedirectCommand;

    @Test
    public void execute_WhenUserAlreadyLogged_ShouldReturnCommandResultToHomePage() throws CommandException {
        doReturn(session).when(request).getSession();
        doReturn(Parameters.LOGIN_INVOCATION).when(request).getParameter(Parameters.LOGIN_INVOCATION);

        when(session.getAttribute(UserParameters.USER_IN_SESSION)).thenReturn(mock(User.class));

        CommandResult result = loginRedirectCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.HOME);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);
    }

    @Test
    public void execute_WhenInvokedWithRegistration_ReturnsLoginPageWithNoErrors() throws CommandException {
        doReturn(session).when(request).getSession();
        doReturn(null).when(request).getParameter(Parameters.LOGIN_INVOCATION);
        doReturn(Parameters.REG_INVOCATION).when(request).getParameter(Parameters.REG_INVOCATION);

        when(session.getAttribute(UserParameters.USER_IN_SESSION)).thenReturn(null);

        CommandResult result = loginRedirectCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(Pages.LOGIN_PAGE);
        assertThat(result.getAction()).isEqualTo(CommandDirection.FORWARD);

        verify(session, times(1)).removeAttribute(UserParameters.USER_EMAIL_ALREADY_EXISTS);
        verify(session, times(1)).removeAttribute(UserParameters.USER_LOGIN_ALREADY_EXISTS);
        verify(session, times(1)).removeAttribute(UserParameters.USER_PHONE_ALREADY_EXISTS);
        verify(session, times(1)).removeAttribute(UserParameters.REG_LOGIN_VAL);
        verify(session, times(1)).removeAttribute(UserParameters.REG_EMAIL_VAL);
        verify(session, times(1)).removeAttribute(UserParameters.REG_PHONE_VAL);
        verify(session, times(1)).removeAttribute(UserParameters.REG_FIRST_NAME_VAL);
        verify(session, times(1)).removeAttribute(UserParameters.REG_SECOND_NAME_VAL);
    }

    @Test
    public void execute_WhenInvokedRegForm_ReturnsLoginPageWithNoErrors() throws CommandException {
        doReturn(session).when(request).getSession();
        doReturn(null).when(request).getParameter(Parameters.LOGIN_INVOCATION);
        doReturn(Parameters.REG_INVOCATION).when(request).getParameter(Parameters.REG_INVOCATION);
        doReturn(UserParameters.REG_FORM).when(request).getParameter(UserParameters.REG_FORM);

        when(session.getAttribute(UserParameters.USER_IN_SESSION)).thenReturn(null);

        CommandResult result = loginRedirectCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(Pages.LOGIN_PAGE);
        assertThat(result.getAction()).isEqualTo(CommandDirection.FORWARD);

        verify(session, times(1)).removeAttribute(UserParameters.USER_EMAIL_ALREADY_EXISTS);
        verify(session, times(1)).removeAttribute(UserParameters.USER_LOGIN_ALREADY_EXISTS);
        verify(session, times(1)).removeAttribute(UserParameters.USER_PHONE_ALREADY_EXISTS);
        verify(session, times(1)).removeAttribute(UserParameters.REG_LOGIN_VAL);
        verify(session, times(1)).removeAttribute(UserParameters.REG_EMAIL_VAL);
        verify(session, times(1)).removeAttribute(UserParameters.REG_PHONE_VAL);
        verify(session, times(1)).removeAttribute(UserParameters.REG_FIRST_NAME_VAL);
        verify(session, times(1)).removeAttribute(UserParameters.REG_SECOND_NAME_VAL);
    }


    @Test
    public void execute_WhenRegistrationSuccessMsg_ReturnsLoginPageWithNoErrors() throws CommandException {
        doReturn(session).when(request).getSession();
        doReturn(null).when(request).getParameter(Parameters.LOGIN_INVOCATION);
        doReturn(Parameters.REG_INVOCATION).when(request).getParameter(Parameters.REG_INVOCATION);
        doReturn(UserParameters.REG_FORM).when(request).getParameter(UserParameters.REG_FORM);
        doReturn(Parameters.REG_SUCCESS_MSG).when(request).getParameter(Parameters.REG_SUCCESS_MSG);

        when(session.getAttribute(UserParameters.USER_IN_SESSION)).thenReturn(null);

        CommandResult result = loginRedirectCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(Pages.LOGIN_PAGE);
        assertThat(result.getAction()).isEqualTo(CommandDirection.FORWARD);
    }
}
