package com.my.library.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.http.HttpResponse;

@WebFilter("/pages/*")
public class PagesFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.log(Level.DEBUG, "PagesFilter invoked");
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.sendError(404);
        chain.doFilter(request, response);
    }
}
