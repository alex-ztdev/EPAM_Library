package com.my.library.controller.command.impl.admin;

import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.entities.Author;
import com.my.library.entities.Book;
import com.my.library.entities.Genre;
import com.my.library.entities.Publisher;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.services.GenreService;
import com.my.library.services.PublisherService;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateBookRedirectCommandTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private BookService bookService;
    @Mock
    private GenreService genreService;
    @Mock
    private PublisherService publisherService;

    @InjectMocks
    private UpdateBookRedirectCommand updateBookRedirectCommand;

    @Test
    @DisplayName("sets in request sorted genres and publishers excluding books genre and publisher")
    void execute_ShouldSetGenresAndPublishersInto() throws ServiceException, CommandException {
        List<Publisher> publishersList = List.of(
                new Publisher(1L, "Publisher4"),
                new Publisher(2L, "Publisher3"),
                new Publisher(3L, "Publisher2"),
                new Publisher(4L, "Publisher1")
        );
        List<Genre> genresList = List.of(
                new Genre(1L, "genre4"),
                new Genre(2L, "genre3"),
                new Genre(3L, "genre2"),
                new Genre(4L, "genre1")
        );

        Book book = new Book(1L, "title",
                "Publisher3",
                "other",
                100,
                LocalDate.parse("2023-02-02"),
                new Author("First", "Second")
        );

        doReturn(String.valueOf(book.getBookId())).when(request).getParameter(Parameters.BOOK_ID);


        doReturn(Optional.of(book)).when(bookService).find(book.getBookId());
        doReturn(10).when(bookService).getQuantity(book.getBookId());
        doReturn(false).when(bookService).isRemoved(book.getBookId());

        doReturn(session).when(request).getSession();

        doReturn(publishersList).when(publisherService).findAll();
        doReturn(genresList).when(genreService).findAll();

        CommandResult result = updateBookRedirectCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(Pages.BOOK_EDIT);
        assertThat(result.getAction()).isEqualTo(CommandDirection.FORWARD);

        verify(session).setAttribute(Parameters.PREVIOUS_PAGE, String.format(RedirectToPage.BOOKS_UPDATE_PAGE_WITH_PARAMETER, book.getBookId()));
        verify(session).setAttribute(Parameters.OPERATION_TYPE, Parameters.UPDATE_BOOK);

        genresList = genresList.stream()
                .filter(genre -> !genre.getTitle().equals(book.getGenre()))
                .sorted(Comparator.comparing(Genre::getTitle))
                .toList();

        publishersList = publishersList.stream()
                .filter(publisher -> !publisher.getTitle().equals(book.getPublisherTitle()))
                .sorted(Comparator.comparing(Publisher::getTitle))
                .toList();

        verify(session).setAttribute(Parameters.GENRES_LIST, genresList);
        verify(session).setAttribute(Parameters.PUBLISHERS_LIST, publishersList);
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"invalidId", "NaN", "RandomInput"})
    @DisplayName("invalid book id returns to UnsupportedOperation page")
    void execute_InvalidBookId_ShouldReturnToUnsupportedOperationPage(String bookId) throws CommandException {
        doReturn(bookId).when(request).getParameter(Parameters.BOOK_ID);

        doReturn(session).when(request).getSession();

        CommandResult result = updateBookRedirectCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.UNSUPPORTED_OPERATION);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "20", "3000"})
    @DisplayName("no such book found returns to UnsupportedOperation page")
    void execute_NoSuchBookFound_ShouldReturnToUnsupportedOperationPage(String bookIdStr) throws ServiceException, CommandException {
        doReturn(bookIdStr).when(request).getParameter(Parameters.BOOK_ID);

        long bookId = Long.parseLong(bookIdStr);

        doReturn(Optional.empty()).when(bookService).find(bookId);

        doReturn(session).when(request).getSession();


        CommandResult result = updateBookRedirectCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.UNSUPPORTED_OPERATION);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);
    }


    @Test
    @DisplayName("service exception thrown, should throw CommandException")
    void execute_ServiceThrowsException_ShouldThrowCommandException() throws ServiceException {
        doReturn(session).when(request).getSession();

        doReturn("1").when(request).getParameter(Parameters.BOOK_ID);

        doThrow(ServiceException.class).when(bookService).find(anyLong());


        assertThatThrownBy(() -> updateBookRedirectCommand.execute(request))
                .isExactlyInstanceOf(CommandException.class)
                .hasCauseExactlyInstanceOf(ServiceException.class);

    }
}