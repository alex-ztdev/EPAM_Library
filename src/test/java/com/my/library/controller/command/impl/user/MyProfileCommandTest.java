package com.my.library.controller.command.impl.user;

import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dto.UserDTO;
import com.my.library.entities.User;
import com.my.library.exceptions.CommandException;
import com.my.library.services.BookService;
import com.my.library.services.OrderService;
import com.my.library.services.UserService;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MyProfileCommandTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;

    @Mock
    private UserService userService;

    @InjectMocks
    private MyProfileCommand myProfileCommand;


    @Test
    void execute_ShouldReturnCommandResultWithForwardToMyProfilePage() throws CommandException {
        UserDTO userDTO = mock(UserDTO.class);
        doReturn(session).when(request).getSession();
        doReturn(userDTO).when(session).getAttribute(UserParameters.USER_IN_SESSION);

        var result = myProfileCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getAction()).isEqualTo(CommandDirection.FORWARD);
        assertThat(result.getPage()).isEqualTo(Pages.MY_PROFILE);

        verify(request).getSession();
        verify(request).setAttribute(Parameters.USER_DTO, userDTO);
        verify(session).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.MY_PROFILE);
    }

}