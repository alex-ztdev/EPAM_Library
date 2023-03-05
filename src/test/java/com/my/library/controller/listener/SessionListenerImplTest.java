package com.my.library.controller.listener;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionListenerImplTest {
    @Mock
    private HttpSessionEvent sessionEvent;
    @Mock
    private HttpSession session;
    @InjectMocks
    private SessionListenerImpl sessionListener;

    @Test
    public void sessionCreated_ShouldGetSessionId() {
        when(sessionEvent.getSession()).thenReturn(session);

        sessionListener.sessionCreated(sessionEvent);

        verify(sessionEvent).getSession();
        verify(session).getId();
    }

    @Test
    public void sessionDestroyed_ShouldGetSessionId() {
        when(sessionEvent.getSession()).thenReturn(session);

        sessionListener.sessionDestroyed(sessionEvent);

        verify(sessionEvent).getSession();
        verify(session).getId();
    }
}