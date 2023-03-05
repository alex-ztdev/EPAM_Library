package com.my.library.controller.filter;

import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.commands.AdminCommands;
import com.my.library.controller.command.constant.commands.GeneralCommands;
import com.my.library.controller.command.constant.commands.LibrarianCommands;
import com.my.library.controller.command.constant.commands.UserCommands;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.controller.context.AppContext;
import com.my.library.dao.constants.UserRole;
import com.my.library.dto.UserDTO;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.ServiceFactory;
import com.my.library.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationFilterTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private FilterChain chain;

    private AuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new AuthenticationFilter();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"randomInput", "invalidCommand"})
    void doFilter_UnknownCommand_ShouldRedirectToUnsupportedCommandPage(String command) throws ServletException, IOException {
        doReturn(session).when(request).getSession();
        doReturn(command).when(request).getParameter(GeneralCommands.COMMAND_PARAMETER);

        filter.doFilter(request, response, chain);

        verify(session).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.UNSUPPORTED_OPERATION);
        verify(response).sendRedirect(request.getContextPath() + RedirectToPage.UNSUPPORTED_OPERATION);
    }


    @ParameterizedTest
    @ValueSource(strings = {LibrarianCommands.DISPLAY_READERS, AdminCommands.DISPLAY_USERS, UserCommands.MY_PROFILE})
    void doFilter_RegisteredUserCommandNoUserInSession_ShouldRedirectToUnsupportedCommandPage(String command) throws ServletException, IOException {
        doReturn(session).when(request).getSession();
        doReturn(command).when(request).getParameter(GeneralCommands.COMMAND_PARAMETER);
        doReturn(null).when(session).getAttribute(UserParameters.USER_IN_SESSION);

        filter.doFilter(request, response, chain);

        verify(session).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.NOT_AUTHORIZED);
        verify(response).sendRedirect(request.getContextPath() + RedirectToPage.NOT_AUTHORIZED);
    }


    @ParameterizedTest
    @ValueSource(strings = {GeneralCommands.BOOKS_LIST, GeneralCommands.ERROR_PAGE, GeneralCommands.LOGIN_PAGE,
            GeneralCommands.CHANGE_LANGUAGE, GeneralCommands.HOME, GeneralCommands.NOT_AUTHORIZED})
    void doFilter_GeneralCommand_ShouldInvokeFilterChain(String command) throws ServletException, IOException {
        doReturn(session).when(request).getSession();
        doReturn(command).when(request).getParameter(GeneralCommands.COMMAND_PARAMETER);

        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @ParameterizedTest
    @ValueSource(strings = {LibrarianCommands.DISPLAY_READERS, AdminCommands.DISPLAY_USERS, UserCommands.MY_PROFILE})
    void doFilter_UserServiceThrowsException_ShouldRedirectToUnsupportedCommandPage(String command) throws ServiceException {
        UserDTO userDTO = mock(UserDTO.class);

        AppContext appContextMock = mock(AppContext.class);
        ServiceFactory serviceFactoryMock = mock(ServiceFactory.class);
        UserService userServiceMock = mock(UserService.class);

        try (var appContextStaticMock = mockStatic(AppContext.class)) {
            appContextStaticMock.when(AppContext::getInstance)
                    .thenReturn(appContextMock);

            when(appContextMock.getServiceFactory()).thenReturn(serviceFactoryMock);

            when(serviceFactoryMock.getUserService()).thenReturn(userServiceMock);
            doThrow(new ServiceException()).when(userServiceMock).isBanned(anyLong());


            doReturn(session).when(request).getSession();
            doReturn(command).when(request).getParameter(GeneralCommands.COMMAND_PARAMETER);
            doReturn(userDTO).when(session).getAttribute(UserParameters.USER_IN_SESSION);

            assertThatThrownBy(() -> filter.doFilter(request, response, chain))
                    .isExactlyInstanceOf(RuntimeException.class);
        }
    }


    @Nested
    @DisplayName("User")
    class User {
        @ParameterizedTest
        @ValueSource(strings = {UserCommands.MY_PROFILE, UserCommands.CANCEL_ORDER, UserCommands.DISPLAY_MY_ORDERS,
                UserCommands.DISPLAY_MY_REQUESTS, UserCommands.ORDER_BOOK, UserCommands.ORDER_BOOK_REDIRECT})
        @DisplayName("not banned user can execute user commands")
        void doFilter_UserCommandAndUserRoleIsUserAndIsNotBanned_ShouldInvokeFilterChain(String command) throws ServletException, IOException {
            UserDTO userDTO = mock(UserDTO.class);

            AppContext appContextMock = mock(AppContext.class);
            ServiceFactory serviceFactoryMock = mock(ServiceFactory.class);
            UserService userServiceMock = mock(UserService.class);

            try (var appContextStaticMock = mockStatic(AppContext.class)) {
                appContextStaticMock.when(AppContext::getInstance)
                        .thenReturn(appContextMock);

                when(appContextMock.getServiceFactory()).thenReturn(serviceFactoryMock);

                when(serviceFactoryMock.getUserService()).thenReturn(userServiceMock);

                doReturn(UserRole.USER).when(userDTO).getRole();

                doReturn(session).when(request).getSession();
                doReturn(command).when(request).getParameter(GeneralCommands.COMMAND_PARAMETER);
                doReturn(userDTO).when(session).getAttribute(UserParameters.USER_IN_SESSION);

                filter.doFilter(request, response, chain);

                verify(chain).doFilter(request, response);
                verify(session, never()).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.UNSUPPORTED_OPERATION);
                verify(response, never()).sendRedirect(request.getContextPath() + RedirectToPage.UNSUPPORTED_OPERATION);
            }
        }

        @ParameterizedTest
        @ValueSource(strings = {LibrarianCommands.DISPLAY_READERS, LibrarianCommands.DISPLAY_USERS_ORDERS,
                LibrarianCommands.RETURN_ORDER, AdminCommands.DISPLAY_USERS, AdminCommands.BLOCK_USER,
                AdminCommands.UNBLOCK_USER, AdminCommands.CHANGE_ROLE, AdminCommands.ADD_BOOK_REDIRECT,
                AdminCommands.DISPLAY_USERS, AdminCommands.CHANGE_ROLE, AdminCommands.ADD_BOOK, AdminCommands.REMOVE_BOOK,
                AdminCommands.RESTORE_BOOK, AdminCommands.UPDATE_BOOK, AdminCommands.UPDATE_BOOK_REDIRECT})
        @DisplayName("user can't execute admin or librarian commands")
        void doFilter_UserRoleIsUserAndIsNotBanned_ShouldRedirectToNotAuthorizedPage(String command) throws ServletException, IOException {
            UserDTO userDTO = mock(UserDTO.class);

            AppContext appContextMock = mock(AppContext.class);
            ServiceFactory serviceFactoryMock = mock(ServiceFactory.class);
            UserService userServiceMock = mock(UserService.class);

            try (var appContextStaticMock = mockStatic(AppContext.class)) {
                appContextStaticMock.when(AppContext::getInstance)
                        .thenReturn(appContextMock);

                when(appContextMock.getServiceFactory()).thenReturn(serviceFactoryMock);

                when(serviceFactoryMock.getUserService()).thenReturn(userServiceMock);

                doReturn(UserRole.USER).when(userDTO).getRole();

                doReturn(session).when(request).getSession();
                doReturn(command).when(request).getParameter(GeneralCommands.COMMAND_PARAMETER);
                doReturn(userDTO).when(session).getAttribute(UserParameters.USER_IN_SESSION);

                filter.doFilter(request, response, chain);

                verify(session).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.NOT_AUTHORIZED);
                verify(response).sendRedirect(request.getContextPath() + RedirectToPage.NOT_AUTHORIZED);
                verify(chain).doFilter(request, response);
            }
        }

        @ParameterizedTest
        @ValueSource(strings = {UserCommands.MY_PROFILE, UserCommands.CANCEL_ORDER, UserCommands.DISPLAY_MY_ORDERS,
                UserCommands.DISPLAY_MY_REQUESTS, UserCommands.ORDER_BOOK, UserCommands.ORDER_BOOK_REDIRECT})
        @DisplayName("user is banned should redirect to login page")
        void doFilter_AdminCommandAndUserRoleIsLibrarianAndIsBanned_ShouldSendRedirectToLoginPage(String command) throws ServletException, IOException, ServiceException {
            UserDTO userDTO = mock(UserDTO.class);

            AppContext appContextMock = mock(AppContext.class);
            ServiceFactory serviceFactoryMock = mock(ServiceFactory.class);
            UserService userServiceMock = mock(UserService.class);

            try (var appContextStaticMock = mockStatic(AppContext.class)) {
                appContextStaticMock.when(AppContext::getInstance)
                        .thenReturn(appContextMock);

                when(appContextMock.getServiceFactory()).thenReturn(serviceFactoryMock);

                when(serviceFactoryMock.getUserService()).thenReturn(userServiceMock);
                when(userServiceMock.isBanned(anyLong())).thenReturn(true);

                doReturn(session).when(request).getSession();
                doReturn(command).when(request).getParameter(GeneralCommands.COMMAND_PARAMETER);
                doReturn(userDTO).when(session).getAttribute(UserParameters.USER_IN_SESSION);

                filter.doFilter(request, response, chain);

                verify(session).invalidate();
                verify(response).sendRedirect(request.getContextPath() + RedirectToPage.LOGIN_PAGE);
                verify(chain).doFilter(request, response);
            }
        }
    }


    @Nested
    @DisplayName("Librarian")
    class Librarian {
        @ParameterizedTest
        @ValueSource(strings = {LibrarianCommands.ACCEPT_ORDER, LibrarianCommands.DECLINE_ORDER, LibrarianCommands.RETURN_ORDER,
                LibrarianCommands.DISPLAY_USERS_ORDERS, LibrarianCommands.DISPLAY_READERS})
        @DisplayName("not banned librarian can execute librarian commands")
        void doFilter_LibrarianCommandAndUserRoleIsLibrarianAndIsNotBanned_ShouldInvokeFilterChain(String command) throws ServletException, IOException {
            UserDTO userDTO = mock(UserDTO.class);

            AppContext appContextMock = mock(AppContext.class);
            ServiceFactory serviceFactoryMock = mock(ServiceFactory.class);
            UserService userServiceMock = mock(UserService.class);

            try (var appContextStaticMock = mockStatic(AppContext.class)) {
                appContextStaticMock.when(AppContext::getInstance)
                        .thenReturn(appContextMock);

                when(appContextMock.getServiceFactory()).thenReturn(serviceFactoryMock);

                when(serviceFactoryMock.getUserService()).thenReturn(userServiceMock);

                doReturn(UserRole.LIBRARIAN).when(userDTO).getRole();

                doReturn(session).when(request).getSession();
                doReturn(command).when(request).getParameter(GeneralCommands.COMMAND_PARAMETER);
                doReturn(userDTO).when(session).getAttribute(UserParameters.USER_IN_SESSION);

                filter.doFilter(request, response, chain);

                verify(chain).doFilter(request, response);
                verify(session, never()).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.UNSUPPORTED_OPERATION);
                verify(response, never()).sendRedirect(request.getContextPath() + RedirectToPage.UNSUPPORTED_OPERATION);
            }
        }

        @ParameterizedTest
        @ValueSource(strings = {AdminCommands.DISPLAY_USERS, AdminCommands.BLOCK_USER, AdminCommands.UNBLOCK_USER, AdminCommands.CHANGE_ROLE,
                AdminCommands.ADD_BOOK_REDIRECT, AdminCommands.DISPLAY_USERS, AdminCommands.CHANGE_ROLE, AdminCommands.ADD_BOOK, AdminCommands.REMOVE_BOOK,
                AdminCommands.RESTORE_BOOK, AdminCommands.UPDATE_BOOK, AdminCommands.UPDATE_BOOK_REDIRECT})
        @DisplayName("not banned librarian can't execute admin commands")
        void doFilter_AdminCommandAndUserRoleIsLibrarianAndIsNotBanned_ShouldInvokeFilterChain(String command) throws ServletException, IOException {
            UserDTO userDTO = mock(UserDTO.class);

            AppContext appContextMock = mock(AppContext.class);
            ServiceFactory serviceFactoryMock = mock(ServiceFactory.class);
            UserService userServiceMock = mock(UserService.class);

            try (var appContextStaticMock = mockStatic(AppContext.class)) {
                appContextStaticMock.when(AppContext::getInstance)
                        .thenReturn(appContextMock);

                when(appContextMock.getServiceFactory()).thenReturn(serviceFactoryMock);

                when(serviceFactoryMock.getUserService()).thenReturn(userServiceMock);

                doReturn(UserRole.LIBRARIAN).when(userDTO).getRole();

                doReturn(session).when(request).getSession();
                doReturn(command).when(request).getParameter(GeneralCommands.COMMAND_PARAMETER);
                doReturn(userDTO).when(session).getAttribute(UserParameters.USER_IN_SESSION);

                filter.doFilter(request, response, chain);

                verify(session).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.NOT_AUTHORIZED);
                verify(response).sendRedirect(request.getContextPath() + RedirectToPage.NOT_AUTHORIZED);
                verify(chain).doFilter(request, response);
            }
        }

        @ParameterizedTest
        @ValueSource(strings = {AdminCommands.DISPLAY_USERS, AdminCommands.BLOCK_USER, AdminCommands.UNBLOCK_USER, AdminCommands.CHANGE_ROLE,
                AdminCommands.ADD_BOOK_REDIRECT, AdminCommands.DISPLAY_USERS, AdminCommands.CHANGE_ROLE, AdminCommands.ADD_BOOK, AdminCommands.REMOVE_BOOK,
                AdminCommands.RESTORE_BOOK, AdminCommands.UPDATE_BOOK, AdminCommands.UPDATE_BOOK_REDIRECT})
        @DisplayName("librarian is banned should redirect to login page")
        void doFilter_AdminCommandAndUserRoleIsLibrarianAndIsBanned_ShouldSendRedirectToLoginPage(String command) throws ServletException, IOException, ServiceException {
            UserDTO userDTO = mock(UserDTO.class);

            AppContext appContextMock = mock(AppContext.class);
            ServiceFactory serviceFactoryMock = mock(ServiceFactory.class);
            UserService userServiceMock = mock(UserService.class);

            try (var appContextStaticMock = mockStatic(AppContext.class)) {
                appContextStaticMock.when(AppContext::getInstance)
                        .thenReturn(appContextMock);

                when(appContextMock.getServiceFactory()).thenReturn(serviceFactoryMock);

                when(serviceFactoryMock.getUserService()).thenReturn(userServiceMock);
                when(userServiceMock.isBanned(anyLong())).thenReturn(true);

                doReturn(session).when(request).getSession();
                doReturn(command).when(request).getParameter(GeneralCommands.COMMAND_PARAMETER);
                doReturn(userDTO).when(session).getAttribute(UserParameters.USER_IN_SESSION);

                filter.doFilter(request, response, chain);

                verify(session).invalidate();
                verify(response).sendRedirect(request.getContextPath() + RedirectToPage.LOGIN_PAGE);
                verify(chain).doFilter(request, response);
            }
        }
    }

    @Nested
    @DisplayName("Admin")
    class Admin {
        @ParameterizedTest
        @ValueSource(strings = {AdminCommands.DISPLAY_USERS, AdminCommands.BLOCK_USER, AdminCommands.UNBLOCK_USER, AdminCommands.CHANGE_ROLE,
                AdminCommands.ADD_BOOK_REDIRECT, AdminCommands.DISPLAY_USERS, AdminCommands.CHANGE_ROLE, AdminCommands.ADD_BOOK, AdminCommands.REMOVE_BOOK,
                AdminCommands.RESTORE_BOOK, AdminCommands.UPDATE_BOOK, AdminCommands.UPDATE_BOOK_REDIRECT})
        @DisplayName("not banned admin can execute admin commands")
        void doFilter_AdminCommandAndUserRoleIsAdminAndIsNotBanned_ShouldInvokeFilterChain(String command) throws ServletException, IOException {
            UserDTO userDTO = mock(UserDTO.class);

            AppContext appContextMock = mock(AppContext.class);
            ServiceFactory serviceFactoryMock = mock(ServiceFactory.class);
            UserService userServiceMock = mock(UserService.class);

            try (var appContextStaticMock = mockStatic(AppContext.class)) {
                appContextStaticMock.when(AppContext::getInstance)
                        .thenReturn(appContextMock);

                when(appContextMock.getServiceFactory()).thenReturn(serviceFactoryMock);

                when(serviceFactoryMock.getUserService()).thenReturn(userServiceMock);

                doReturn(UserRole.ADMIN).when(userDTO).getRole();

                doReturn(session).when(request).getSession();
                doReturn(command).when(request).getParameter(GeneralCommands.COMMAND_PARAMETER);
                doReturn(userDTO).when(session).getAttribute(UserParameters.USER_IN_SESSION);

                filter.doFilter(request, response, chain);

                verify(chain).doFilter(request, response);
                verify(session, never()).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.UNSUPPORTED_OPERATION);
                verify(response, never()).sendRedirect(request.getContextPath() + RedirectToPage.UNSUPPORTED_OPERATION);
            }
        }

        @ParameterizedTest
        @ValueSource(strings = {AdminCommands.DISPLAY_USERS, AdminCommands.BLOCK_USER, AdminCommands.UNBLOCK_USER,
                AdminCommands.CHANGE_ROLE, AdminCommands.ADD_BOOK_REDIRECT, AdminCommands.DISPLAY_USERS,
                AdminCommands.CHANGE_ROLE, AdminCommands.ADD_BOOK, AdminCommands.REMOVE_BOOK,
                AdminCommands.RESTORE_BOOK, AdminCommands.UPDATE_BOOK, AdminCommands.UPDATE_BOOK_REDIRECT})
        @DisplayName("admin is banned should redirect to login page")
        void doFilter_AdminCommandAndUserRoleIsAdminAndIsBanned_ShouldSendRedirectToLoginPage(String command) throws ServletException, IOException, ServiceException {
            UserDTO userDTO = mock(UserDTO.class);

            AppContext appContextMock = mock(AppContext.class);
            ServiceFactory serviceFactoryMock = mock(ServiceFactory.class);
            UserService userServiceMock = mock(UserService.class);

            try (var appContextStaticMock = mockStatic(AppContext.class)) {
                appContextStaticMock.when(AppContext::getInstance)
                        .thenReturn(appContextMock);

                when(appContextMock.getServiceFactory()).thenReturn(serviceFactoryMock);

                when(serviceFactoryMock.getUserService()).thenReturn(userServiceMock);
                when(userServiceMock.isBanned(anyLong())).thenReturn(true);


                doReturn(session).when(request).getSession();
                doReturn(command).when(request).getParameter(GeneralCommands.COMMAND_PARAMETER);
                doReturn(userDTO).when(session).getAttribute(UserParameters.USER_IN_SESSION);

                filter.doFilter(request, response, chain);

                verify(session).invalidate();
                verify(response).sendRedirect(request.getContextPath() + RedirectToPage.LOGIN_PAGE);
                verify(chain).doFilter(request, response);
            }
        }
    }


}