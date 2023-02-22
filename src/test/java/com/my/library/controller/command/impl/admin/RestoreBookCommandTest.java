package com.my.library.controller.command.impl.admin;

import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestoreBookCommandTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private BookService bookService;

    @InjectMocks
    private RestoreBookCommand restoreBookCommand;

    @ParameterizedTest
    @ValueSource(strings = {"1", "100", "200", "300", "1000"})
    void execute_validIdAndBookExists_ShouldRemoveBook(String validBookId) throws CommandException, ServiceException {
        String previousPage = "previous_page";
        doReturn(previousPage).when(session).getAttribute(Parameters.PREVIOUS_PAGE);

        doReturn(session).when(request).getSession();

        doReturn(validBookId).when(request).getParameter(Parameters.BOOK_ID);

        CommandResult result = restoreBookCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(previousPage);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(bookService).restore(Integer.parseInt(validBookId));
        verify(request).getSession();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"NaN", "invalidId", "qwe", "randomInput"})
    void execute_invalidBookId_ShouldReturnToUnsupportedOperationPage(String invalidBookId) throws CommandException, ServiceException {
        doReturn(invalidBookId).when(request).getParameter(Parameters.BOOK_ID);

        CommandResult result = restoreBookCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.UNSUPPORTED_OPERATION);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(bookService, times(0)).restore(anyLong());
    }

    @Test
    void execute_BookServiceThrowsException_ShouldThrowCommandException() throws ServiceException {
        String bookId = "123";

        doReturn(bookId).when(request).getParameter(Parameters.BOOK_ID);

        doThrow(ServiceException.class).when(bookService).restore(anyLong());

        assertThatThrownBy(() -> restoreBookCommand.execute(request))
                .isExactlyInstanceOf(CommandException.class)
                .hasCauseExactlyInstanceOf(ServiceException.class);

        verify(bookService, times(1)).restore(anyLong());
    }
}