package com.my.library.controller;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandFactory;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.commands.GeneralCommands;
import com.my.library.controller.context.AppContext;
import com.my.library.exceptions.CommandException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.lang.reflect.Constructor;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryControllerTest {

    @Mock
    private CommandFactory commandFactory;

    @Mock
    private AppContext appContext;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private ServletContext servletContext;

    @Spy
    @InjectMocks
    private LibraryController controller;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        Constructor<AppContext> constructor = AppContext.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        appContext = mock(AppContext.class);
    }

    @AfterEach
    void end() throws NoSuchMethodException {
        var constructor = AppContext.class.getDeclaredConstructor();

        constructor.setAccessible(false);
    }

    @Test
    void doPost_RedirectionCommand_ShouldRedirectToCommandsPage() throws ServletException, IOException, CommandException {
        appContext = mock(AppContext.class);

        try (var AppContextStaticMock = mockStatic(AppContext.class)) {
            String commandName = "redirect-command";
            AppContextStaticMock.when(AppContext::getInstance).thenReturn(appContext);

            when(appContext.getCommandFactory()).thenReturn(commandFactory);

            when(request.getParameter(GeneralCommands.COMMAND_PARAMETER)).thenReturn(commandName);
            when(request.getContextPath()).thenReturn("context");

            Command command = mock(Command.class);

            when(commandFactory.createCommand(commandName)).thenReturn(command);

            when(command.execute(request)).thenReturn(new CommandResult("page.jsp", CommandDirection.REDIRECT));


            controller.doPost(request, response);

            verify(response).sendRedirect(request.getContextPath() + "page.jsp");
        }
    }

    @Test
    void doPost_ForwardCommand_ShouldRedirectToCommandsPage() throws ServletException, IOException, CommandException {
        appContext = mock(AppContext.class);

        try (var AppContextStaticMock = mockStatic(AppContext.class)) {
            String commandName = "forward-command";

            var commandResult = new CommandResult("page.jsp", CommandDirection.FORWARD);

            AppContextStaticMock.when(AppContext::getInstance).thenReturn(appContext);

            when(appContext.getCommandFactory()).thenReturn(commandFactory);

            when(request.getParameter(GeneralCommands.COMMAND_PARAMETER)).thenReturn(commandName);
            when(request.getSession()).thenReturn(session);
            when(session.getServletContext()).thenReturn(servletContext);
            when(servletContext.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);


            Command command = mock(Command.class);

            when(commandFactory.createCommand(commandName)).thenReturn(command);

            when(command.execute(request)).thenReturn(commandResult);

            controller.doPost(request, response);

            verify(request).getSession();
            verify(session).getServletContext();
            verify(servletContext).getRequestDispatcher(commandResult.getPage());
            verify(requestDispatcher).forward(request, response);
        }
    }

    @Test
    void doPost_CommandExceptionIsThrown_ShouldRedirectToErrorPage() throws ServletException, IOException, CommandException {
        appContext = mock(AppContext.class);

        try (var AppContextStaticMock = mockStatic(AppContext.class)) {
            String commandName = "forward-command";

            AppContextStaticMock.when(AppContext::getInstance).thenReturn(appContext);

            when(appContext.getCommandFactory()).thenReturn(commandFactory);

            when(request.getParameter(GeneralCommands.COMMAND_PARAMETER)).thenReturn(commandName);

            Command command = mock(Command.class);

            when(commandFactory.createCommand(commandName)).thenReturn(command);

            when(command.execute(request)).thenThrow(CommandException.class);

            controller.doPost(request, response);

            verify(response).sendRedirect(request.getContextPath() + RedirectToPage.ERROR_PAGE);
        }
    }

    @Test
    void doGet_RedirectionCommand_ShouldRedirectToCommandsPage() throws ServletException, IOException, CommandException {
        appContext = mock(AppContext.class);

        try (var AppContextStaticMock = mockStatic(AppContext.class)) {
            String commandName = "redirect-command";
            AppContextStaticMock.when(AppContext::getInstance).thenReturn(appContext);

            when(appContext.getCommandFactory()).thenReturn(commandFactory);

            when(request.getParameter(GeneralCommands.COMMAND_PARAMETER)).thenReturn(commandName);
            when(request.getContextPath()).thenReturn("context");

            Command command = mock(Command.class);

            when(commandFactory.createCommand(commandName)).thenReturn(command);

            when(command.execute(request)).thenReturn(new CommandResult("page.jsp", CommandDirection.REDIRECT));


            controller.doGet(request, response);

            verify(response).sendRedirect(request.getContextPath() + "page.jsp");
        }
    }

    @Test
    void doGet_ForwardCommand_ShouldRedirectToCommandsPage() throws ServletException, IOException, CommandException {
        appContext = mock(AppContext.class);

        try (var AppContextStaticMock = mockStatic(AppContext.class)) {
            String commandName = "forward-command";

            var commandResult = new CommandResult("page.jsp", CommandDirection.FORWARD);

            AppContextStaticMock.when(AppContext::getInstance).thenReturn(appContext);

            when(appContext.getCommandFactory()).thenReturn(commandFactory);

            when(request.getParameter(GeneralCommands.COMMAND_PARAMETER)).thenReturn(commandName);
            when(request.getSession()).thenReturn(session);
            when(session.getServletContext()).thenReturn(servletContext);
            when(servletContext.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);


            Command command = mock(Command.class);

            when(commandFactory.createCommand(commandName)).thenReturn(command);

            when(command.execute(request)).thenReturn(commandResult);

            controller.doGet(request, response);

            verify(request).getSession();
            verify(session).getServletContext();
            verify(servletContext).getRequestDispatcher(commandResult.getPage());
            verify(requestDispatcher).forward(request, response);
        }
    }

    @Test
    void doGet_CommandExceptionIsThrown_ShouldRedirectToErrorPage() throws ServletException, IOException, CommandException {
        appContext = mock(AppContext.class);

        try (var AppContextStaticMock = mockStatic(AppContext.class)) {
            String commandName = "forward-command";

            AppContextStaticMock.when(AppContext::getInstance).thenReturn(appContext);

            when(appContext.getCommandFactory()).thenReturn(commandFactory);

            when(request.getParameter(GeneralCommands.COMMAND_PARAMETER)).thenReturn(commandName);

            Command command = mock(Command.class);

            when(commandFactory.createCommand(commandName)).thenReturn(command);

            when(command.execute(request)).thenThrow(CommandException.class);

            controller.doGet(request, response);

            verify(response).sendRedirect(request.getContextPath() + RedirectToPage.ERROR_PAGE);
        }
    }
}