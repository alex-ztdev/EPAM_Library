package com.my.library.controller.filter;

import com.my.library.controller.command.constant.Parameters;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

//TODO: implement or remove filter

//@WebFilter
//public class LocaleFilter implements Filter {
//
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//        String locale = httpRequest.getParameter(Parameters.LOCALE);
//
//        if (locale.isBlank()) {
//            String sessionLocale = (String) httpRequest.getSession().getAttribute(LOCALE);
//            if (sessionLocale.isBlank()) {
//                httpRequest.getSession().setAttribute(Parameters.LOCALE, );
//            }
//            chain.doFilter(request, response);
//        } else {
//            httpRequest.getSession().setAttribute(Parameters.LOCALE, locale);
//            httpResponse.sendRedirect(httpRequest.getHeader(REFERER));
//        }
//    }
//
//}
