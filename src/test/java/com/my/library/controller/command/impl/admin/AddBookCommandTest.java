package com.my.library.controller.command.impl.admin;

import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.BookParameters;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.dao.TransactionManager;
import com.my.library.dto.mapper.BookMapper;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.AuthorService;
import com.my.library.services.BookService;
import com.my.library.utils.builder.RequestBookBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddBookCommandTest {
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

    private AddBookCommand addBookCommand;

    @BeforeEach
    void setUp() {
        this.addBookCommand = new AddBookCommand(bookService, authorService, transactionManager);
    }

    @Test
    @DisplayName("Valid book data, should save book")
    void execute_ValidBookDataNewBook_ShouldSetSuccessMsgToSessionSaveBookAndReturnCommandResultWithRedirectToBookAddPage() throws CommandException, ServiceException {
        when(request.getSession()).thenReturn(session);

        when(request.getParameter(BookParameters.TITLE)).thenReturn("The Great Gatsby");
        when(request.getParameter(BookParameters.AUTHOR_FIRST_NAME)).thenReturn("Scott");
        when(request.getParameter(BookParameters.AUTHOR_SECOND_NAME)).thenReturn("Fitzgerald");
        when(request.getParameter(BookParameters.GENRE)).thenReturn("Novel");
        when(request.getParameter(BookParameters.PUBLISHER)).thenReturn("Scribner");
        when(request.getParameter(BookParameters.COPIES)).thenReturn("2");
        when(request.getParameter(BookParameters.PAGES)).thenReturn("180");
        when(request.getParameter(BookParameters.PUBLICATION_DATE)).thenReturn("1925-04-10");


        var bookContainer = new RequestBookBuilder().buildBookForSave(request);

        assertThat(bookContainer).isPresent();
        var book = bookContainer.get();
        var bookDTO = new BookMapper(bookService).toDTO(book, 2, false);

        when(bookService.save(eq(book), eq(2), eq(authorService), eq(transactionManager))).thenReturn(book);

        CommandResult result = addBookCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.BOOKS_ADD_PAGE);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(bookService).save(eq(book), eq(2), eq(authorService), eq(transactionManager));
        verify(session).setAttribute(eq(Parameters.BOOKS_DTO), eq(bookDTO));
    }

    @Test
    @DisplayName("Book already exists")
    void execute_BookAlreadyExists_ShouldSetErrorMsgToSessionAndReturnCommandResultWithRedirectToBookAddPage() throws CommandException, ServiceException {
        when(request.getSession()).thenReturn(session);

        when(request.getParameter(BookParameters.TITLE)).thenReturn("The Great Gatsby");
        when(request.getParameter(BookParameters.AUTHOR_FIRST_NAME)).thenReturn("Scott");
        when(request.getParameter(BookParameters.AUTHOR_SECOND_NAME)).thenReturn("Fitzgerald");
        when(request.getParameter(BookParameters.GENRE)).thenReturn("Novel");
        when(request.getParameter(BookParameters.PUBLISHER)).thenReturn("Scribner");
        when(request.getParameter(BookParameters.COPIES)).thenReturn("2");
        when(request.getParameter(BookParameters.PAGES)).thenReturn("180");
        when(request.getParameter(BookParameters.PUBLICATION_DATE)).thenReturn("1925-04-10");

        when(bookService.alreadyExists(any())).thenReturn(true);

        CommandResult result = addBookCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.BOOKS_ADD_PAGE);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(bookService, times(0)).save(any(),anyInt(), any(),any());
        verify(session).setAttribute(BookParameters.BOOK_ALREADY_EXISTS, BookParameters.BOOK_ALREADY_EXISTS);
    }

    @ParameterizedTest
    @DisplayName("Invalid book copies")
    @EmptySource
    @NullSource
    @ValueSource(strings = {"-123", "-10", "NaN", "randomInput"})
    void execute_InvalidBookCopies_ShouldSetErrorMsgToSessionAndReturnCommandResultWithRedirectToBookAddPage(String copies) throws CommandException, ServiceException {
        when(request.getSession()).thenReturn(session);

        when(request.getParameter(BookParameters.TITLE)).thenReturn("The Great Gatsby");
        when(request.getParameter(BookParameters.AUTHOR_FIRST_NAME)).thenReturn("Scott");
        when(request.getParameter(BookParameters.AUTHOR_SECOND_NAME)).thenReturn("Fitzgerald");
        when(request.getParameter(BookParameters.GENRE)).thenReturn("Novel");
        when(request.getParameter(BookParameters.PUBLISHER)).thenReturn("Scribner");
        when(request.getParameter(BookParameters.COPIES)).thenReturn(copies);
        when(request.getParameter(BookParameters.PAGES)).thenReturn("180");
        when(request.getParameter(BookParameters.PUBLICATION_DATE)).thenReturn("1925-04-10");

        CommandResult result = addBookCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.BOOKS_ADD_PAGE);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(bookService, times(0)).save(any(), eq(2), eq(authorService), eq(transactionManager));
        verify(session, times(0)).setAttribute(eq(Parameters.BOOKS_DTO), any());
        verify(session, times(1)).setAttribute(BookParameters.BOOK_INVALID_DATA, BookParameters.BOOK_INVALID_DATA);
    }


    @ParameterizedTest
    @DisplayName("Invalid page number")
    @EmptySource
    @NullSource
    @ValueSource(strings = {"-123", "-10", "0", "NaN", "randomInput"})
    void execute_InvalidPageNumber_ShouldSetErrorMsgToSessionAndReturnCommandResultWithRedirectToBookAddPage(String pageNumbers) throws CommandException, ServiceException {
        when(request.getSession()).thenReturn(session);

        when(request.getParameter(BookParameters.TITLE)).thenReturn("The Great Gatsby");
        when(request.getParameter(BookParameters.AUTHOR_FIRST_NAME)).thenReturn("Scott");
        when(request.getParameter(BookParameters.AUTHOR_SECOND_NAME)).thenReturn("Fitzgerald");
        when(request.getParameter(BookParameters.GENRE)).thenReturn("Novel");
        when(request.getParameter(BookParameters.PUBLISHER)).thenReturn("Scribner");
        when(request.getParameter(BookParameters.COPIES)).thenReturn("1");
        when(request.getParameter(BookParameters.PAGES)).thenReturn(pageNumbers);
        when(request.getParameter(BookParameters.PUBLICATION_DATE)).thenReturn("1925-04-10");

        CommandResult result = addBookCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.BOOKS_ADD_PAGE);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(bookService, times(0)).save(any(), eq(2), eq(authorService), eq(transactionManager));
        verify(session, times(0)).setAttribute(eq(Parameters.BOOKS_DTO), any());
        verify(session, times(1)).setAttribute(BookParameters.BOOK_INVALID_DATA, BookParameters.BOOK_INVALID_DATA);
    }

    @ParameterizedTest
    @DisplayName("Invalid publication date")
    @EmptySource
    @NullSource
    @ValueSource(strings = {"notADate", "RandomInput", "2500-10-02", "3000-02-23"})
    void execute_InvalidPublicationDate_ShouldSetErrorMsgToSessionAndReturnCommandResultWithRedirectToBookAddPage(String publicationDate) throws CommandException, ServiceException {
        when(request.getSession()).thenReturn(session);

        when(request.getParameter(BookParameters.TITLE)).thenReturn("The Great Gatsby");
        when(request.getParameter(BookParameters.AUTHOR_FIRST_NAME)).thenReturn("Scott");
        when(request.getParameter(BookParameters.AUTHOR_SECOND_NAME)).thenReturn("Fitzgerald");
        when(request.getParameter(BookParameters.GENRE)).thenReturn("Novel");
        when(request.getParameter(BookParameters.PUBLISHER)).thenReturn("Scribner");
        when(request.getParameter(BookParameters.COPIES)).thenReturn("1");
        when(request.getParameter(BookParameters.PAGES)).thenReturn("180");
        when(request.getParameter(BookParameters.PUBLICATION_DATE)).thenReturn(publicationDate);

        CommandResult result = addBookCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.BOOKS_ADD_PAGE);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(bookService, times(0)).save(any(), eq(2), eq(authorService), eq(transactionManager));
        verify(session, times(0)).setAttribute(eq(Parameters.BOOKS_DTO), any());
        verify(session, times(1)).setAttribute(BookParameters.BOOK_INVALID_DATA, BookParameters.BOOK_INVALID_DATA);
    }

    @Test
    void execute_BookServiceThrowsException_ShouldThrowCommandException() throws ServiceException {
        when(request.getSession()).thenReturn(session);

        when(request.getParameter(BookParameters.TITLE)).thenReturn("The Great Gatsby");
        when(request.getParameter(BookParameters.AUTHOR_FIRST_NAME)).thenReturn("Scott");
        when(request.getParameter(BookParameters.AUTHOR_SECOND_NAME)).thenReturn("Fitzgerald");
        when(request.getParameter(BookParameters.GENRE)).thenReturn("Novel");
        when(request.getParameter(BookParameters.PUBLISHER)).thenReturn("Scribner");
        when(request.getParameter(BookParameters.COPIES)).thenReturn("1");
        when(request.getParameter(BookParameters.PAGES)).thenReturn("180");
        when(request.getParameter(BookParameters.PUBLICATION_DATE)).thenReturn("1925-04-10");

        when(bookService.alreadyExists(any())).thenThrow(ServiceException.class);

        assertThatThrownBy(() -> addBookCommand.execute(request))
                .isExactlyInstanceOf(CommandException.class)
                .hasCauseExactlyInstanceOf(ServiceException.class);


        verify(bookService, times(0)).save(any(),anyInt(),any(),any());
    }
}