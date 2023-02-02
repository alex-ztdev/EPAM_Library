package com.my.library.controller.filter;

import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.commands.AdminCommands;
import com.my.library.controller.command.constant.commands.GeneralCommands;
import com.my.library.controller.command.constant.commands.LibrarianCommands;
import com.my.library.controller.command.constant.commands.UserCommands;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dao.constants.UserRole;
import com.my.library.entities.User;
import com.my.library.utils.Pages;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

@WebFilter("/controller")
public class AuthenticationFilter implements Filter {
    private static final List<String> GENERAL_COMMANDS = List.of(
            GeneralCommands.CHANGE_LANGUAGE,
            GeneralCommands.LOGIN,
            GeneralCommands.LOGIN_PAGE,
            GeneralCommands.LOGOUT,
            GeneralCommands.REGISTRATION,
            GeneralCommands.ALL_BOOKS_LIST,
            GeneralCommands.BOOKS_LIST,
            GeneralCommands.SEARCH_BOOK,
            GeneralCommands.SORT_BOOKS,
            GeneralCommands.HOME,
            GeneralCommands.NOT_AUTHORIZED

    );

    private static final List<String> LIBRARIAN_COMMANDS = List.of(
            //TODO: Add Librarian commands
            LibrarianCommands.DISPLAY_USERS_ORDERS,
            LibrarianCommands.RETURN_ORDER
    );

    private static final List<String> USER_COMMANDS = List.of(
            //TODO: Add User commands
            UserCommands.ORDER_BOOK_REDIRECT,
            UserCommands.ORDER_BOOK,
            UserCommands.DISPLAY_MY_ORDERS
    );


    private static final List<String> ADMIN_COMMANDS = List.of(
            AdminCommands.REMOVE_BOOK,
            AdminCommands.RESTORE_BOOK,
            AdminCommands.ADD_BOOK_REDIRECT,
            AdminCommands.ADD_BOOK,
            AdminCommands.UPDATE_BOOK_REDIRECT,
            AdminCommands.UPDATE_BOOK,
            AdminCommands.DISPLAY_USERS,

            //TODO: remove LibrarianCommands from ADMIN?
            LibrarianCommands.DISPLAY_USERS_ORDERS,
            LibrarianCommands.RETURN_ORDER
    );

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(UserParameters.USER_IN_SESSION);

        String command = request.getParameter(GeneralCommands.COMMAND_PARAMETER);

        if (command == null || !ADMIN_COMMANDS.contains(command) && !USER_COMMANDS.contains(command) && !GENERAL_COMMANDS.contains(command) && !LIBRARIAN_COMMANDS.contains(command)) {
            logger.log(Level.DEBUG, "Unknown command: " +command + " was received");
            session.setAttribute(Parameters.PREVIOUS_PAGE, Pages.UNSUPPORTED_COMMAND);
            response.sendError(400);
        }else if (GENERAL_COMMANDS.contains(command)) {
            chain.doFilter(servletRequest, servletResponse);
        } else {
            if (user == null) {
                session.setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.NOT_AUTHORIZED);
                response.sendRedirect(request.getContextPath() + RedirectToPage.NOT_AUTHORIZED);
                logger.log(Level.DEBUG, "No user in session! Tried to execute: " + command);
            } else {
                //TODO: implement admin, librarian, user, (unknown?)

                if (user.getRole() == UserRole.LIBRARIAN && LIBRARIAN_COMMANDS.contains(command)) {
                    logger.log(Level.DEBUG, "Librarian: " + user.getUserId() +" executed: " + command);
                    chain.doFilter(servletRequest, servletResponse);
                } else if (user.getRole() == UserRole.USER && USER_COMMANDS.contains(command)) {
                    logger.log(Level.DEBUG, "User: " + user.getUserId() +" executed: " + command);
                    chain.doFilter(servletRequest, servletResponse);
                } else if (user.getRole() == UserRole.ADMIN && ADMIN_COMMANDS.contains(command)) {
                    logger.log(Level.DEBUG, "Admin: " + user.getUserId() +" executed: " + command);
                    chain.doFilter(servletRequest, servletResponse);
                } else {
                    session.setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.NOT_AUTHORIZED);
                    response.sendRedirect(request.getContextPath() + RedirectToPage.NOT_AUTHORIZED);
                }
            }
        }
    }
    @Override
    public void destroy() {
    }
}
