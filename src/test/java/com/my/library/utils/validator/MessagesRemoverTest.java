package com.my.library.utils.validator;

import com.my.library.controller.command.constant.parameters.BookParameters;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        messagesRemover.removeLoginErrors(session);

        verify(session, times(1)).removeAttribute(UserParameters.INVALID_LOGIN_PASSWORD);
        verify(session,  times(1)).removeAttribute(UserParameters.USER_IS_BLOCKED);
    }

    @Test
    public void removeBookErrors_ShouldRemoveCorrectAttributesFromSession() {
        messagesRemover.removeBookErrors(session);

        verify(session, times(1)).removeAttribute(BookParameters.BOOK_INVALID_DATA);
        verify(session, times(1)).removeAttribute(BookParameters.BOOK_ALREADY_EXISTS);
        verify(session, times(1)).removeAttribute(BookParameters.SUCCESSFULLY_UPDATED);
        verify(session, times(1)).removeAttribute(BookParameters.SUCCESSFULLY_ADDED);
        verify(session, times(1)).removeAttribute(Parameters.UPDATE_BOOK);
        verify(session, times(1)).removeAttribute(Parameters.ADD_BOOK);
    }
    @Test
    public void removeRegistrationErrors_ShouldRemoveCorrectAttributesFromSession() {
        messagesRemover.removeRegistrationErrors(session);

        verify(session, times(1)).removeAttribute(UserParameters.USER_EMAIL_ALREADY_EXISTS);
        verify(session, times(1)).removeAttribute(UserParameters.USER_LOGIN_ALREADY_EXISTS);
        verify(session, times(1)).removeAttribute(UserParameters.USER_PHONE_ALREADY_EXISTS);
        verify(session, times(1)).removeAttribute(UserParameters.REG_LOGIN_VAL);
        verify(session, times(1)).removeAttribute(UserParameters.REG_EMAIL_VAL);
        verify(session, times(1)).removeAttribute(UserParameters.REG_PHONE_VAL);
        verify(session, times(1)).removeAttribute(UserParameters.REG_FIRST_NAME_VAL);
        verify(session, times(1)).removeAttribute(UserParameters.REG_SECOND_NAME_VAL);
    }

}