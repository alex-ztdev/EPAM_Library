package com.my.library.controller.command.impl.admin;

import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnblockUserCommandTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private UserService userService;

    @InjectMocks
    private UnblockUserCommand unblockUserCommand;

    @ParameterizedTest
    @ValueSource(strings = {"1", "100", "200", "300", "1000"})
    void execute_validIdAndUserExists_ShouldBlockUser(String validUserId) throws CommandException, ServiceException {
        String previousPage = "previous_page";
        doReturn(previousPage).when(session).getAttribute(Parameters.PREVIOUS_PAGE);

        doReturn(session).when(request).getSession();

        doReturn(true).when(userService).unblockUser(Integer.parseInt(validUserId)) ;

        doReturn(validUserId).when(request).getParameter(Parameters.USER_ID);

        CommandResult result = unblockUserCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(previousPage);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(userService).unblockUser(Integer.parseInt(validUserId)) ;
        verify(request).getSession();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"NaN", "invalidId", "qwe", "randomInput"})
    void execute_invalidUserId_ShouldReturnToUnsupportedOperationPage(String invalidUserId) throws CommandException, ServiceException {
        doReturn(session).when(request).getSession();

        doReturn(invalidUserId).when(request).getParameter(Parameters.USER_ID);

        CommandResult result = unblockUserCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.UNSUPPORTED_OPERATION);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(userService, times(0)).unblockUser(anyLong()) ;
        verify(request).getSession();
    }

    @Test
    void execute_blockingUserFailed_ShouldReturnToUnsupportedOperationPage() throws CommandException, ServiceException {
        String userId = "123";

        doReturn(session).when(request).getSession();

        doReturn(userId).when(request).getParameter(Parameters.USER_ID);

        doReturn(false).when(userService).unblockUser(Long.parseLong(userId)) ;

        CommandResult result = unblockUserCommand.execute(request);


        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.UNSUPPORTED_OPERATION);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(userService, times(1)).unblockUser(anyLong()) ;
        verify(request).getSession();
    }

    @Test
    void execute_UserServiceThrowsException_ShouldThrowCommandException() throws ServiceException {
        String userId = "123";

        doReturn(session).when(request).getSession();

        doReturn(userId).when(request).getParameter(Parameters.USER_ID);

        doThrow(ServiceException.class).when(userService).unblockUser(anyLong()) ;

        assertThatThrownBy(() -> unblockUserCommand.execute(request))
                .isExactlyInstanceOf(CommandException.class)
                .hasCauseExactlyInstanceOf(ServiceException.class);

        verify(userService, times(1)).unblockUser(anyLong()) ;
        verify(request).getSession();
    }
}