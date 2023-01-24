package com.my.library.controller.filter;

import com.my.library.controller.command.constant.CommandTypes;
import com.my.library.controller.command.constant.UserConstants;
import com.my.library.entities.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest response = (HttpServletRequest) servletResponse;
        HttpServletResponse request = (HttpServletResponse) servletRequest;
        HttpSession session = response.getSession();
        User user = (User) session.getAttribute(UserConstants.USER_IN_SESSION);
        String command = request.getParameter();

    }

}
