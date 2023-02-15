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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChangeLanguageCommandTest {
    private final static String LANGUAGE_PARAMETER = "language";

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;

    @InjectMocks
    private ChangeLanguageCommand changeLanguageCommand;

    @Test
    void execute_PreviousPagePresent_ShouldRedirectToPreviousPage() throws CommandException {
        doReturn(session).when(request).getSession();
        String language = "ua";

        doReturn(language).when(request).getParameter(LANGUAGE_PARAMETER);
        doReturn(Pages.ORDER_PAGE).when(session).getAttribute(Parameters.PREVIOUS_PAGE);


        CommandResult result = changeLanguageCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(Pages.ORDER_PAGE);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);


        verify(request).getSession();
        verify(session).setAttribute(LANGUAGE_PARAMETER, language);
    }

    @Test
    void execute_NoPreviousPage_ShouldRedirectToHome() throws CommandException {
        doReturn(session).when(request).getSession();
        String language = "ua";

        doReturn(language).when(request).getParameter(LANGUAGE_PARAMETER);
        doReturn(null).when(session).getAttribute(Parameters.PREVIOUS_PAGE);


        CommandResult result = changeLanguageCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.HOME);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);


        verify(request).getSession();
        verify(session).setAttribute(LANGUAGE_PARAMETER, language);
    }
}