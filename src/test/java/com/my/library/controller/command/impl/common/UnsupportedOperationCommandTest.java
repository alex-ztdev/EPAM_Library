package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UnsupportedOperationCommandTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @InjectMocks
    private UnsupportedOperationCommand unsupportedOperationCommand;

    @Test
    void execute_ShouldReturnCommandResultWithForwardToUnsupportedOperationPage() throws CommandException {
        doReturn(session).when(request).getSession();

        CommandResult result = unsupportedOperationCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(Pages.UNSUPPORTED_COMMAND);
        assertThat(result.getAction()).isEqualTo(CommandDirection.FORWARD);

        verify(request).getSession();
        verify(session).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.UNSUPPORTED_OPERATION);
    }
}