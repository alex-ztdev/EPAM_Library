package com.my.library.utils.validator;

import static org.junit.jupiter.api.Assertions.*;

import com.my.library.controller.command.constant.parameters.UserParameters;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessagesRemoverTest {
    private MessagesRemover messagesRemover ;

    @Mock
    private HttpSession session = mock(HttpSession.class);

    @BeforeEach
    public void setUp() {
        messagesRemover = new MessagesRemover();


    }
    @Test
    public void removeLoginErrors_ShouldRemoveCorrectAttributesFromSession() {
//        when(session.getAttribute(UserParameters.INVALID_LOGIN_PASSWORD)).thenReturn("invalidLoginPassword");
//        when(session.getAttribute(UserParameters.USER_IS_BLOCKED)).thenReturn("isBlocked");

        messagesRemover.removeLoginErrors(session);

        verify(session, times(1)).removeAttribute(UserParameters.INVALID_LOGIN_PASSWORD);
        verify(session,  times(1)).removeAttribute(UserParameters.USER_IS_BLOCKED);
    }
}