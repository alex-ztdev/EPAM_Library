package com.my.library.controller.filter;

import com.my.library.controller.command.constant.GeneralCommands;
import com.my.library.controller.command.constant.UserParameters;
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
    private final static List<String> GENERAL_COMMANDS = List.of(
            GeneralCommands.CHANGE_LANGUAGE,
            GeneralCommands.LOGIN,
            GeneralCommands.LOGOUT,
            GeneralCommands.REGISTRATION,
            GeneralCommands.ALL_BOOKS_LIST,
            GeneralCommands.BOOK_PAGE,
            GeneralCommands.SEARCH_BOOK,
            GeneralCommands.SORT_BOOKS
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

        if (GENERAL_COMMANDS.contains(command)) {
            chain.doFilter(servletRequest, servletResponse);
        } else {
            if (user == null) {
                response.sendRedirect(Pages.NOT_AUTHORIZED);
                logger.log(Level.DEBUG, "No user in session! Tried to execute: " + command);
            }
            else{
                //TODO: implement admin, librarian, user, (unknown?)
            }

        }
    }

    @Override
    public void destroy() {
    }
}
