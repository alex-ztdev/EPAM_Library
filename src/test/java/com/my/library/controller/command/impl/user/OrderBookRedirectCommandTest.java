package com.my.library.controller.command.impl.user;

import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.entities.Author;
import com.my.library.entities.Book;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderBookRedirectCommandTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private BookService bookService;
    @InjectMocks
    private OrderBookRedirectCommand orderBookRedirectCommand;

    OrderBookRedirectCommandTest() {
    }

    @Test
    void execute_validData_ShouldReturnCommandResultWithForwardToOrderPage() throws CommandException, ServiceException {
        Book book = mock(Book.class);
        Author author = new Author("FirstName", "SecondName");

        doReturn(author).when(book).getAuthor();
        when(request.getSession()).thenReturn(session);

        when(request.getParameter(Parameters.BOOK_ID)).thenReturn("1");
        when(bookService.getQuantity(1L)).thenReturn(10);
        when(bookService.isRemoved(1L)).thenReturn(false);
        when(bookService.find(1L)).thenReturn(Optional.of(book));

        var result = orderBookRedirectCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(Pages.ORDER_PAGE);
        assertThat(result.getAction()).isEqualTo(CommandDirection.FORWARD);

        verify(request).getParameter(Parameters.BOOK_ID);
        verify(bookService).getQuantity(1L);
        verify(bookService).isRemoved(1L);
        verify(bookService).find(1L);
    }


    @Test
    void execute_InvalidBookId_ShouldReturnCommandResultToUnsupportedCommandPage() throws CommandException {
        when(request.getSession()).thenReturn(session);

        when(request.getParameter(Parameters.BOOK_ID)).thenReturn("NaN");

        var result = orderBookRedirectCommand.execute(request);

        assertThat(result).isNotNull();

        assertThat(result.getPage()).isEqualTo(RedirectToPage.UNSUPPORTED_OPERATION);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(request).getParameter(Parameters.BOOK_ID);
    }

    @Test
    void execute_NoBookFound_ShouldReturnCommandResultToUnsupportedCommandPage() throws CommandException, ServiceException {
        when(request.getSession()).thenReturn(session);

        when(request.getParameter(Parameters.BOOK_ID)).thenReturn("1");

        when(bookService.find(1L)).thenReturn(Optional.empty());

        var result = orderBookRedirectCommand.execute(request);

        assertThat(result).isNotNull();

        assertThat(result.getPage()).isEqualTo(RedirectToPage.UNSUPPORTED_OPERATION);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(request).getParameter(Parameters.BOOK_ID);
    }


    @Test
    void execute_BookServiceThrowsException_ShouldThrowCommandException() throws ServiceException {
        when(request.getSession()).thenReturn(session);

        when(request.getParameter(Parameters.BOOK_ID)).thenReturn("1");

        doThrow(ServiceException.class).when(bookService).find(anyLong());


        assertThatThrownBy(() -> orderBookRedirectCommand.execute(request))
                .isExactlyInstanceOf(CommandException.class)
                .hasCauseExactlyInstanceOf(ServiceException.class);
    }


}