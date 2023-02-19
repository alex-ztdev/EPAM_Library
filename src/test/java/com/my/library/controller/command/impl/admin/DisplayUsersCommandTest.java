package com.my.library.controller.command.impl.admin;

import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.entities.User;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.UserService;
import com.my.library.utils.Pages;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DisplayUsersCommandTest {
    private static final int RECORDS_PER_PAGE = 5;
    @Mock
    private HttpSession session;
    @Mock
    private HttpServletRequest request;
    @Mock
    private UserService userService;
    private DisplayUsersCommand displayUsersCommand;

    @BeforeEach
    void setUp() {
        displayUsersCommand = new DisplayUsersCommand(userService);
    }

    @Test
    void execute_ValidData_ShouldSetUsersInRequest() throws CommandException, ServiceException {
        int currPage = 1;
        int totalPages = 1;

        List<User> usersList = List.of(new User(), new User(), new User());
        int readersCount = usersList.size();

        doReturn(session).when(request).getSession();
        doReturn(String.valueOf(currPage)).when(request).getParameter(Parameters.GENERAL_CURR_PAGE);
        doReturn(readersCount).when(userService).countTotalUsers();

        doReturn(usersList).when(userService).findAll(0, RECORDS_PER_PAGE);


        CommandResult result = displayUsersCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(Pages.DISPLAY_USERS_PAGE);
        assertThat(result.getAction()).isEqualTo(CommandDirection.FORWARD);


        verify(request).setAttribute(Parameters.GENERAL_CURR_PAGE, currPage);
        verify(request).setAttribute(Parameters.GENERAL_TOTAL_PAGES, totalPages);
        verify(request).setAttribute(eq(Parameters.USERS_LIST), anyList());
        verify(request).setAttribute(Parameters.USERS_PER_PAGE, RECORDS_PER_PAGE);
        verify(session).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.DISPLAY_USERS_WITH_PARAMETERS.formatted(currPage));
    }


    @Test
    void execute_InvalidPageNumber_ShouldReturnCommandWithRedirectionToUnsupportedCommandPage() throws CommandException {
        doReturn(session).when(request).getSession();
        doReturn("NaN").when(request).getParameter(Parameters.GENERAL_CURR_PAGE);

        CommandResult result = displayUsersCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.UNSUPPORTED_OPERATION);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);
    }

    @Test
    void execute_userServiceThrowsException_ShouldThrowCommandException() throws ServiceException {
        int currPage = 1;

        doReturn(session).when(request).getSession();
        doReturn(String.valueOf(currPage)).when(request).getParameter(Parameters.GENERAL_CURR_PAGE);

        doThrow(ServiceException.class).when(userService).findAll(0, RECORDS_PER_PAGE);


        assertThatThrownBy(() -> displayUsersCommand.execute(request))
                .isExactlyInstanceOf(CommandException.class)
                .hasCauseExactlyInstanceOf(ServiceException.class);


        verify(session).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.DISPLAY_USERS_WITH_PARAMETERS.formatted(currPage));
    }
}