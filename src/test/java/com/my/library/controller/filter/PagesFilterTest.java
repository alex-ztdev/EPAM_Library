package com.my.library.controller.filter;

import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagesFilterTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private HttpSession session;

    @InjectMocks
    private PagesFilter pagesFilter;


    @Test
    public void doFilter_ShouldSetPreviousPageAttribute() throws IOException, ServletException {
        when(request.getSession()).thenReturn(session);

        pagesFilter.doFilter(request, response, filterChain);

        verify(session).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.ERROR_PAGE);
    }

    @Test
    public void doFilter_ShouldRedirectToErrorPage() throws IOException, ServletException {
        when(request.getSession()).thenReturn(session);

        pagesFilter.doFilter(request, response, filterChain);

        verify(response).sendRedirect(eq(request.getContextPath() + RedirectToPage.ERROR_PAGE));
    }

    @Test
    public void doFilter_ShouldCallFilterChain() throws IOException, ServletException {
        when(request.getSession()).thenReturn(session);

        pagesFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(eq(request), eq(response));
    }
}