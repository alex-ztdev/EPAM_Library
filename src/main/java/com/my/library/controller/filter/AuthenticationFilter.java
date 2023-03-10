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
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebFilter("/controller")
public class AuthenticationFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();
    private static final Set<String> GENERAL_COMMANDS = new HashSet<>(List.of(
            GeneralCommands.CHANGE_LANGUAGE,
            GeneralCommands.LOGIN,
            GeneralCommands.LOGIN_PAGE,
            GeneralCommands.LOGOUT,
            GeneralCommands.REGISTRATION,
            GeneralCommands.BOOKS_LIST,
            GeneralCommands.SEARCH_BOOK,
            GeneralCommands.HOME,
            GeneralCommands.NOT_AUTHORIZED,
            GeneralCommands.UNSUPPORTED_OPERATION,
            GeneralCommands.ERROR_PAGE
    ));

    private static final Set<String> LIBRARIAN_COMMANDS = new HashSet<>(List.of(
            LibrarianCommands.DISPLAY_USERS_ORDERS,
            LibrarianCommands.RETURN_ORDER,
            LibrarianCommands.DISPLAY_READERS,
            LibrarianCommands.DISPLAY_REQUESTED_ORDERS,
            LibrarianCommands.ACCEPT_ORDER,
            LibrarianCommands.DECLINE_ORDER,

            UserCommands.MY_PROFILE
    ));

    private static final Set<String> USER_COMMANDS = new HashSet<>(List.of(
            UserCommands.ORDER_BOOK_REDIRECT,
            UserCommands.ORDER_BOOK,
            UserCommands.DISPLAY_MY_ORDERS,
            UserCommands.MY_PROFILE,
            UserCommands.DISPLAY_MY_REQUESTS,
            UserCommands.CANCEL_ORDER
    ));

    private static final Set<String> ADMIN_COMMANDS = new HashSet<>(List.of(
            AdminCommands.REMOVE_BOOK,
            AdminCommands.RESTORE_BOOK,
            AdminCommands.ADD_BOOK_REDIRECT,
            AdminCommands.ADD_BOOK,
            AdminCommands.UPDATE_BOOK_REDIRECT,
            AdminCommands.UPDATE_BOOK,
            AdminCommands.DISPLAY_USERS,
            AdminCommands.BLOCK_USER,
            AdminCommands.UNBLOCK_USER,
            AdminCommands.CHANGE_ROLE,

            UserCommands.MY_PROFILE,

            //TODO: remove LibrarianCommands from ADMIN?
            LibrarianCommands.DISPLAY_USERS_ORDERS,
            LibrarianCommands.DISPLAY_REQUESTED_ORDERS,
            LibrarianCommands.RETURN_ORDER,
            LibrarianCommands.ACCEPT_ORDER,
            LibrarianCommands.DECLINE_ORDER
    ));

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        String command = request.getParameter(GeneralCommands.COMMAND_PARAMETER);

        if (command == null || !ADMIN_COMMANDS.contains(command)
                && !USER_COMMANDS.contains(command)
                && !GENERAL_COMMANDS.contains(command)
                && !LIBRARIAN_COMMANDS.contains(command)
        ) {
            logger.log(Level.DEBUG, "Unknown command: " + command + " was received");
            session.setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.UNSUPPORTED_OPERATION);
            response.sendRedirect(request.getContextPath() + RedirectToPage.UNSUPPORTED_OPERATION);
        } else if (!GENERAL_COMMANDS.contains(command)) {
            UserDTO user = (UserDTO) session.getAttribute(UserParameters.USER_IN_SESSION);
            if (user == null) {
                session.setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.NOT_AUTHORIZED);
                response.sendRedirect(request.getContextPath() + RedirectToPage.NOT_AUTHORIZED);
                chain.doFilter(request, response);
                logger.log(Level.DEBUG, "No user in session! Tried to execute: " + command);
            } else {
                if (isUserBanned(user)) {
                    logger.log(Level.DEBUG, "User with id: " + user.getUserId() + " is banned! Invoked logout command");
                    session.invalidate();
                    response.sendRedirect(request.getContextPath() + RedirectToPage.LOGIN_PAGE);
                    chain.doFilter(request, response);
                } else if (user.getRole() == UserRole.LIBRARIAN && LIBRARIAN_COMMANDS.contains(command)) {
                    logger.log(Level.DEBUG, "Librarian: " + user.getUserId() + " executed: " + command);
                    chain.doFilter(request, response);
                } else if (user.getRole() == UserRole.USER && USER_COMMANDS.contains(command)) {
                    logger.log(Level.DEBUG, "User: " + user.getUserId() + " executed: " + command);
                    chain.doFilter(request, response);
                } else if (user.getRole() == UserRole.ADMIN && ADMIN_COMMANDS.contains(command)) {
                    logger.log(Level.DEBUG, "Admin: " + user.getUserId() + " executed: " + command);
                    chain.doFilter(request, response);
                } else {
                    session.setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.NOT_AUTHORIZED);
                    response.sendRedirect(request.getContextPath() + RedirectToPage.NOT_AUTHORIZED);
                    chain.doFilter(request, response);
                }
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean isUserBanned(UserDTO user) {
        try (var serviceFactory = AppContext.getInstance().getServiceFactory()) {
            var userService = serviceFactory.getUserService();
            return userService.isBanned(user.getUserId());
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "AuthenticationFilter/ isUserBanned method userService throws exception");
            throw new RuntimeException(e);
        }

    }
}
