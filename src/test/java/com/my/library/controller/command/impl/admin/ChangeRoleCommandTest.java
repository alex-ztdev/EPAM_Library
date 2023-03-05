package com.my.library.controller.command.impl.admin;

import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dao.constants.UserRole;
import com.my.library.dao.constants.UserStatus;
import com.my.library.entities.User;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangeRoleCommandTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private UserService userService;

    @InjectMocks
    private ChangeRoleCommand changeRoleCommand;

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"NaN", "invalidInput", "randomInput", "-000+"})
    void execute_InvalidUserId_ShouldReturnToUnsupportedCommandPage(String userId) throws CommandException {
        doReturn(userId).when(request).getParameter(Parameters.USER_ID);
        doReturn("user").when(request).getParameter(UserParameters.ROLE);

        CommandResult result = changeRoleCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.UNSUPPORTED_OPERATION);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"qwerty", "invalidInput", "randomInput", "000"})
    void execute_InvalidUserRole_ShouldReturnToUnsupportedCommandPage(String userRole) throws CommandException {
        doReturn("1").when(request).getParameter(Parameters.USER_ID);
        doReturn(userRole).when(request).getParameter(UserParameters.ROLE);

        CommandResult result = changeRoleCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.UNSUPPORTED_OPERATION);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);
    }

    @Test
    void execute_WhenUserRoleIsAdmin_ShouldReturnToUnsupportedCommandPage() throws CommandException, ServiceException {
        User user = mock(User.class);
        doReturn(UserRole.ADMIN).when(user).getRole();
        doReturn(Optional.of(user)).when(userService).find(anyLong());

        doReturn("1").when(request).getParameter(Parameters.USER_ID);
        doReturn("user").when(request).getParameter(UserParameters.ROLE);

        CommandResult result = changeRoleCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.UNSUPPORTED_OPERATION);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);
    }

    @Test
    void execute_WhenUserIsNotFound_ShouldReturnToUnsupportedCommandPage() throws CommandException {
        doReturn("1").when(request).getParameter(Parameters.USER_ID);
        doReturn("user").when(request).getParameter(UserParameters.ROLE);

        CommandResult result = changeRoleCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.UNSUPPORTED_OPERATION);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);
    }

    @Test
    void execute_WhenUserIsBlocked_ShouldReturnToUnsupportedCommandPage() throws CommandException, ServiceException {
        User user = mock(User.class);
        doReturn(UserStatus.BLOCKED).when(user).getStatus();

        doReturn("1").when(request).getParameter(Parameters.USER_ID);
        doReturn("user").when(request).getParameter(UserParameters.ROLE);

        doReturn(Optional.of(user)).when(userService).find(anyLong());

        CommandResult result = changeRoleCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.UNSUPPORTED_OPERATION);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);
    }


    @ParameterizedTest
    @CsvSource({"1, USER", "2, LIBRARIAN",  "3, LIBRARIAN",  "2, USER"})
    void execute_ValidData_ShouldSetNewUserRoleAndReturnToPreviousPage(String userId, String newRole) throws ServiceException, CommandException {
        String previousPage = "previous_page";
        User user = mock(User.class);
        doReturn(UserRole.valueOf(newRole)).when(user).getRole();
        doReturn(Optional.of(user)).when(userService).find(anyLong());

        doReturn(userId).when(request).getParameter(Parameters.USER_ID);
        doReturn(newRole).when(request).getParameter(UserParameters.ROLE);

        doReturn(session).when(request).getSession();

        doReturn(previousPage).when(session).getAttribute(Parameters.PREVIOUS_PAGE);

        CommandResult result = changeRoleCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(previousPage);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);
    }

    @Test
    void execute_UserServiceThrowsException_ShouldThrowCommandException() throws ServiceException {

        doReturn("1").when(request).getParameter(Parameters.USER_ID);
        doReturn("USER").when(request).getParameter(UserParameters.ROLE);

        doThrow(ServiceException.class).when(userService).find(anyLong());

        doReturn(session).when(request).getSession();

        assertThatThrownBy(() -> changeRoleCommand.execute(request))
                .isExactlyInstanceOf(CommandException.class)
                .hasCauseExactlyInstanceOf(ServiceException.class);
    }

}