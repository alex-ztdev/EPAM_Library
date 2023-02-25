package com.my.library.controller.filter;

import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebFilter("/pages/*")
public class PagesFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.log(Level.DEBUG, "PagesFilter invoked");
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        HttpSession session = httpRequest.getSession();
        session.setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.ERROR_PAGE);

        httpResponse.sendRedirect(httpRequest.getContextPath() + RedirectToPage.ERROR_PAGE);
        chain.doFilter(request, response);
    }
}
