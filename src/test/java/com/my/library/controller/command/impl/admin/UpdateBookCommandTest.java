package com.my.library.controller.command.impl.admin;

import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.BookParameters;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.dao.TransactionManager;
import com.my.library.entities.Book;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.AuthorService;
import com.my.library.services.BookService;
import com.my.library.utils.builder.RequestBookBuilder;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateBookCommandTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private BookService bookService;
    @Mock
    private AuthorService authorService;
    @Mock
    private TransactionManager transactionManager;

    @InjectMocks
    private UpdateBookCommand updateBookCommand;

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"-100", "-20", "-10", "NaN", "invalidId", "qwe", "randomInput"})
    void execute_invalidBookId_ShouldReturnToUnsupportedOperationPage(String invalidBookId) throws CommandException {
        when(request.getParameter(Parameters.BOOK_ID)).thenReturn(invalidBookId);

        doReturn(session).when(request).getSession();

        CommandResult result = updateBookCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.UNSUPPORTED_OPERATION);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(request).getSession();
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3", "4", "500"})
    void execute_BookDoesntExists_ShouldReturnToUnsupportedOperationPage(String bookId) throws CommandException, ServiceException {
        when(request.getParameter(Parameters.BOOK_ID)).thenReturn(bookId);
        when(request.getParameter(BookParameters.COPIES)).thenReturn("10");
        when(request.getParameter(BookParameters.TITLE)).thenReturn("The Great Gatsby");
        when(request.getParameter(BookParameters.AUTHOR_FIRST_NAME)).thenReturn("Scott");
        when(request.getParameter(BookParameters.AUTHOR_SECOND_NAME)).thenReturn("Fitzgerald");
        when(request.getParameter(BookParameters.GENRE)).thenReturn("Novel");
        when(request.getParameter(BookParameters.PUBLISHER)).thenReturn("Scribner");
        when(request.getParameter(BookParameters.PAGES)).thenReturn("180");
        when(request.getParameter(BookParameters.PUBLICATION_DATE)).thenReturn("1925-04-10");

        doReturn(session).when(request).getSession();

        doReturn(Optional.empty()).when(bookService).find(Long.parseLong(bookId));

        CommandResult result = updateBookCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.UNSUPPORTED_OPERATION);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(request).getSession();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"-10", "-5", "-3", "NaN", "RandomInput"})
    void execute_InvalidCopies_ShouldReturnToUnsupportedOperationPage(String copies) throws CommandException, ServiceException {
        when(request.getParameter(Parameters.BOOK_ID)).thenReturn("1");
        when(request.getParameter(BookParameters.COPIES)).thenReturn(copies);

        doReturn(session).when(request).getSession();

        doReturn(Optional.of(mock(Book.class))).when(bookService).find(1L);

        CommandResult result = updateBookCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(String.format(RedirectToPage.BOOKS_UPDATE_PAGE_WITH_PARAMETER, "1"));
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(request).getSession();
        verify(session).setAttribute(BookParameters.BOOK_INVALID_DATA, BookParameters.BOOK_INVALID_DATA);
    }

    @Test
    void execute_bookWithSuchDataAlreadyExists_ShouldReturnToUpdateBookPage() throws CommandException, ServiceException {
        when(request.getParameter(Parameters.BOOK_ID)).thenReturn("1");
        when(request.getParameter(BookParameters.COPIES)).thenReturn("10");
        when(request.getParameter(BookParameters.TITLE)).thenReturn("The Great Gatsby");
        when(request.getParameter(BookParameters.AUTHOR_FIRST_NAME)).thenReturn("Scott");
        when(request.getParameter(BookParameters.AUTHOR_SECOND_NAME)).thenReturn("Fitzgerald");
        when(request.getParameter(BookParameters.GENRE)).thenReturn("Novel");
        when(request.getParameter(BookParameters.PUBLISHER)).thenReturn("Scribner");
        when(request.getParameter(BookParameters.PAGES)).thenReturn("180");
        when(request.getParameter(BookParameters.PUBLICATION_DATE)).thenReturn("1925-04-10");

        doReturn(session).when(request).getSession();


        Optional<Book> bookContainer = new RequestBookBuilder().buildBookForUpdate(request);

        assertThat(bookContainer).isPresent();

        Book book = bookContainer.get();

        doReturn(Optional.of(book)).when(bookService).find(1L);

        doReturn(true).when(bookService).alreadyExists(eq(book));
        doReturn(10).when(bookService).getQuantity(book.getBookId());


        CommandResult result = updateBookCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(String.format(RedirectToPage.BOOKS_UPDATE_PAGE_WITH_PARAMETER, "1"));
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);


        verify(request).getSession();
        verify(session).setAttribute(BookParameters.BOOK_ALREADY_EXISTS, BookParameters.BOOK_ALREADY_EXISTS);
    }

    @Test
    void execute_ValidBookData_ShouldReturnToShouldReturnToUpdateBookPage() throws CommandException, ServiceException {
        when(request.getParameter(Parameters.BOOK_ID)).thenReturn("1");
        when(request.getParameter(BookParameters.COPIES)).thenReturn("10");
        when(request.getParameter(BookParameters.TITLE)).thenReturn("The Great Gatsby");
        when(request.getParameter(BookParameters.AUTHOR_FIRST_NAME)).thenReturn("Scott");
        when(request.getParameter(BookParameters.AUTHOR_SECOND_NAME)).thenReturn("Fitzgerald");
        when(request.getParameter(BookParameters.GENRE)).thenReturn("Novel");
        when(request.getParameter(BookParameters.PUBLISHER)).thenReturn("Scribner");
        when(request.getParameter(BookParameters.PAGES)).thenReturn("180");
        when(request.getParameter(BookParameters.PUBLICATION_DATE)).thenReturn("1925-04-10");

        doReturn(session).when(request).getSession();


        Optional<Book> bookContainer = new RequestBookBuilder().buildBookForUpdate(request);

        assertThat(bookContainer).isPresent();

        Book book = bookContainer.get();

        doReturn(Optional.of(book)).when(bookService).find(1L);

        doReturn(false).when(bookService).alreadyExists(eq(book));


        CommandResult result = updateBookCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(String.format(RedirectToPage.BOOKS_UPDATE_PAGE_WITH_PARAMETER, "1"));
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);


        verify(request).getSession();
        verify(bookService).update(eq(book), eq(10), eq(authorService), eq(transactionManager));
        verify(session).setAttribute(BookParameters.SUCCESSFULLY_UPDATED, BookParameters.SUCCESSFULLY_UPDATED);
    }

    @Test
    void execute_BookServiceThrowsException_ShouldThrowCommandException() throws ServiceException {
        when(request.getParameter(Parameters.BOOK_ID)).thenReturn("1");
        when(request.getParameter(BookParameters.COPIES)).thenReturn("10");
        when(request.getParameter(BookParameters.TITLE)).thenReturn("The Great Gatsby");
        when(request.getParameter(BookParameters.AUTHOR_FIRST_NAME)).thenReturn("Scott");
        when(request.getParameter(BookParameters.AUTHOR_SECOND_NAME)).thenReturn("Fitzgerald");
        when(request.getParameter(BookParameters.GENRE)).thenReturn("Novel");
        when(request.getParameter(BookParameters.PUBLISHER)).thenReturn("Scribner");
        when(request.getParameter(BookParameters.PAGES)).thenReturn("180");
        when(request.getParameter(BookParameters.PUBLICATION_DATE)).thenReturn("1925-04-10");

        doReturn(session).when(request).getSession();

        Optional<Book> bookContainer = new RequestBookBuilder().buildBookForUpdate(request);

        assertThat(bookContainer).isPresent();

        Book book = bookContainer.get();

        doThrow(ServiceException.class).when(bookService).find(anyLong());

        assertThatThrownBy(()->updateBookCommand.execute(request))
                .isExactlyInstanceOf(CommandException.class)
                .hasCauseExactlyInstanceOf(ServiceException.class);

        verify(request).getSession();
        verify(bookService, times(0)).update(eq(book), eq(10), eq(authorService), eq(transactionManager));
    }
}