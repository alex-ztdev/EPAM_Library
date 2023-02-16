package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.OrderDir;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dao.constants.BooksOrderTypes;
import com.my.library.dto.UserDTO;
import com.my.library.entities.Author;
import com.my.library.entities.Book;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchBookCommandTest {

    private static final int RECORDS_PER_PAGE = 10;
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private BookService bookService;

    private SearchBookCommand searchBookCommand;

    @BeforeEach
    public void setUp() {
        searchBookCommand = new SearchBookCommand(bookService);
    }

    @Test
    public void execute_WithValidDataSearchByAuthor_ShouldReturnCommandWithRedirectionToBooksPage() throws CommandException, ServiceException {
        UserDTO userDTO = mock(UserDTO.class);
        boolean includeRemoved = false;
        int currPage = 1;
        OrderDir orderDir = OrderDir.ASC;
        BooksOrderTypes booksOrderTypes = BooksOrderTypes.BY_AUTHOR;

        List<Book> booksList = List.of(
                new Book(1L, "title1", "publisher1", "other", 101, LocalDate.now(), new Author("FirstName", "SecondName")),
                new Book(2L, "title2", "publisher2", "other", 102, LocalDate.now(), new Author("FirstName", "SecondName")),
                new Book(3L, "title3", "publisher23", "other", 103, LocalDate.now(), new Author("FirstName", "SecondName"))
        );

        int totalRecords = booksList.size();

        String searchByStr = "by_author";
        String searchContent = "title";

        doReturn(searchByStr).when(request).getParameter(Parameters.SEARCH_BY);
        doReturn(searchContent).when(request).getParameter(Parameters.SEARCH_CONTENT);
        doReturn(session).when(request).getSession();

        doReturn(String.valueOf(currPage)).when(request).getParameter(Parameters.GENERAL_CURR_PAGE);
        doReturn(orderDir.toString()).when(request).getParameter(Parameters.ORDER_DIRECTION);
        doReturn(booksOrderTypes.toString()).when(request).getParameter(Parameters.ORDER_BY);
        doReturn(userDTO).when(session).getAttribute(UserParameters.USER_IN_SESSION);

        doReturn(booksList).when(bookService).findByAuthor(searchContent, 0, RECORDS_PER_PAGE, booksOrderTypes, orderDir, includeRemoved);
        doReturn(totalRecords).when(bookService).countFoundByAuthor(searchContent, includeRemoved);

        CommandResult result = searchBookCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(Pages.BOOKS_LIST);
        assertThat(result.getAction()).isEqualTo(CommandDirection.FORWARD);

        verify(request).setAttribute(Parameters.GENERAL_CURR_PAGE, currPage);
        verify(request).setAttribute(Parameters.ORDER_DIRECTION, orderDir.toString());
        verify(request).setAttribute(Parameters.ORDER_BY, booksOrderTypes.toString().toUpperCase());
        verify(request).setAttribute(eq(Parameters.GENERAL_TOTAL_PAGES), any());
        verify(request).setAttribute(eq(Parameters.BOOKS_LIST), any());
        verify(request).setAttribute(Parameters.BOOKS_PER_PAGE, RECORDS_PER_PAGE);
        verify(request).setAttribute(Parameters.SEARCH_CONTENT, searchContent);
        verify(request).setAttribute(Parameters.SEARCH_BY, searchByStr.toLowerCase());

        verify(bookService).countFoundByAuthor(searchContent, includeRemoved);
        verify(bookService).findByAuthor(searchContent, 0, RECORDS_PER_PAGE, booksOrderTypes, orderDir, includeRemoved);
        verify(bookService, times(3)).isRemoved(anyLong());
        verify(bookService, times(3)).getQuantity(anyLong());
    }


    @Test
    public void execute_WithInvalidSearchBy_ShouldReturnCommandWithRedirectionToUnsupportedOperationPage() throws CommandException {
        String searchByStr = "INVALID_SEARCH_BY";
        String searchContent = "title";

        doReturn(searchByStr).when(request).getParameter(Parameters.SEARCH_BY);
        doReturn(searchContent).when(request).getParameter(Parameters.SEARCH_CONTENT);
        doReturn(session).when(request).getSession();

        CommandResult result = searchBookCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.UNSUPPORTED_OPERATION);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void execute_WithNullOrEmptySearchContent_ShouldReturnCommandWithRedirectionToDefaultBooksPage(String searchContent) throws CommandException {
        String searchByStr = "INVALID_SEARCH_BY";

        doReturn(searchByStr).when(request).getParameter(Parameters.SEARCH_BY);
        doReturn(searchContent).when(request).getParameter(Parameters.SEARCH_CONTENT);
        doReturn(session).when(request).getSession();

        CommandResult result = searchBookCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.BOOKS_PAGE);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);
    }

    @Test
    public void execute_WithValidDataSearchByTitle_ShouldReturnCommandWithRedirectionToPreviousPage() throws CommandException, ServiceException {
        UserDTO userDTO = mock(UserDTO.class);
        boolean includeRemoved = false;
        int currPage = 1;
        OrderDir orderDir = OrderDir.ASC;
        BooksOrderTypes booksOrderTypes = BooksOrderTypes.BY_TITLE;

        List<Book> booksList = List.of(
                new Book(1L, "title1", "publisher1", "other", 101, LocalDate.now(), new Author("FirstName", "SecondName")),
                new Book(2L, "title2", "publisher2", "other", 102, LocalDate.now(), new Author("FirstName", "SecondName")),
                new Book(3L, "title3", "publisher23", "other", 103, LocalDate.now(), new Author("FirstName", "SecondName"))
        );

        int totalRecords = booksList.size();

        String searchByStr = "by_title";
        String searchContent = "title";

        doReturn(searchByStr).when(request).getParameter(Parameters.SEARCH_BY);
        doReturn(searchContent).when(request).getParameter(Parameters.SEARCH_CONTENT);
        doReturn(session).when(request).getSession();

        doReturn(String.valueOf(currPage)).when(request).getParameter(Parameters.GENERAL_CURR_PAGE);
        doReturn(orderDir.toString()).when(request).getParameter(Parameters.ORDER_DIRECTION);
        doReturn(booksOrderTypes.toString()).when(request).getParameter(Parameters.ORDER_BY);
        doReturn(userDTO).when(session).getAttribute(UserParameters.USER_IN_SESSION);

        doReturn(booksList).when(bookService).findByTitle(searchContent, 0, RECORDS_PER_PAGE, booksOrderTypes, orderDir, includeRemoved);
        doReturn(totalRecords).when(bookService).countFoundByTitle(searchContent, includeRemoved);

        CommandResult result = searchBookCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(Pages.BOOKS_LIST);
        assertThat(result.getAction()).isEqualTo(CommandDirection.FORWARD);

        verify(request).setAttribute(Parameters.GENERAL_CURR_PAGE, currPage);
        verify(request).setAttribute(Parameters.ORDER_DIRECTION, orderDir.toString());
        verify(request).setAttribute(Parameters.ORDER_BY, booksOrderTypes.toString().toUpperCase());
        verify(request).setAttribute(eq(Parameters.GENERAL_TOTAL_PAGES), any());
        verify(request).setAttribute(eq(Parameters.BOOKS_LIST), any());
        verify(request).setAttribute(Parameters.BOOKS_PER_PAGE, RECORDS_PER_PAGE);
        verify(request).setAttribute(Parameters.SEARCH_CONTENT, searchContent);
        verify(request).setAttribute(Parameters.SEARCH_BY, searchByStr.toLowerCase());

        verify(bookService).countFoundByTitle(searchContent, includeRemoved);
        verify(bookService).findByTitle(searchContent, 0, RECORDS_PER_PAGE, booksOrderTypes, orderDir, includeRemoved);
        verify(bookService, times(3)).isRemoved(anyLong());
    }

    @Test
    public void execute_InvalidCurrPage_ShouldUseFirstPageAsDefaultValue() throws CommandException, ServiceException {
        UserDTO userDTO = mock(UserDTO.class);
        boolean includeRemoved = false;
        int currPage = 1;
        OrderDir orderDir = OrderDir.ASC;
        BooksOrderTypes booksOrderTypes = BooksOrderTypes.BY_AUTHOR;

        List<Book> booksList = List.of(
                new Book(1L, "title1", "publisher1", "other", 101, LocalDate.now(), new Author("FirstName", "SecondName")),
                new Book(2L, "title2", "publisher2", "other", 102, LocalDate.now(), new Author("FirstName", "SecondName")),
                new Book(3L, "title3", "publisher3", "other", 103, LocalDate.now(), new Author("FirstName", "SecondName"))
        );

        int totalRecords = booksList.size();

        String searchByStr = "by_author";
        String searchContent = "title";

        doReturn(searchByStr).when(request).getParameter(Parameters.SEARCH_BY);
        doReturn(searchContent).when(request).getParameter(Parameters.SEARCH_CONTENT);
        doReturn(session).when(request).getSession();

        doReturn("NaN").when(request).getParameter(Parameters.GENERAL_CURR_PAGE);
        doReturn(orderDir.toString()).when(request).getParameter(Parameters.ORDER_DIRECTION);
        doReturn(booksOrderTypes.toString()).when(request).getParameter(Parameters.ORDER_BY);
        doReturn(userDTO).when(session).getAttribute(UserParameters.USER_IN_SESSION);

        doReturn(booksList).when(bookService).findByAuthor(searchContent, 0, RECORDS_PER_PAGE, booksOrderTypes, orderDir, includeRemoved);
        doReturn(totalRecords).when(bookService).countFoundByAuthor(searchContent, includeRemoved);

        CommandResult result = searchBookCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(Pages.BOOKS_LIST);
        assertThat(result.getAction()).isEqualTo(CommandDirection.FORWARD);

        verify(request).setAttribute(Parameters.GENERAL_CURR_PAGE, currPage);
        verify(request).setAttribute(Parameters.ORDER_DIRECTION, orderDir.toString());
        verify(request).setAttribute(Parameters.ORDER_BY, booksOrderTypes.toString().toUpperCase());
        verify(request).setAttribute(eq(Parameters.GENERAL_TOTAL_PAGES), any());
        verify(request).setAttribute(eq(Parameters.BOOKS_LIST), any());
        verify(request).setAttribute(Parameters.BOOKS_PER_PAGE, RECORDS_PER_PAGE);
        verify(request).setAttribute(Parameters.SEARCH_CONTENT, searchContent);
        verify(request).setAttribute(Parameters.SEARCH_BY, searchByStr.toLowerCase());

        verify(bookService).countFoundByAuthor(searchContent, includeRemoved);
        verify(bookService).findByAuthor(searchContent, 0, RECORDS_PER_PAGE, booksOrderTypes, orderDir, includeRemoved);
        verify(bookService, times(3)).isRemoved(anyLong());
        verify(bookService, times(3)).getQuantity(anyLong());
    }

    @Test
    public void execute_InvalidOrderDir_ShouldUseDefaultOrderDirValue() throws CommandException, ServiceException {
        UserDTO userDTO = mock(UserDTO.class);
        boolean includeRemoved = false;
        int currPage = 1;
        OrderDir orderDir = OrderDir.ASC;
        BooksOrderTypes booksOrderTypes = BooksOrderTypes.BY_AUTHOR;

        List<Book> booksList = List.of(
                new Book(1L, "title1", "publisher1", "other", 101, LocalDate.now(), new Author("FirstName", "SecondName")),
                new Book(2L, "title2", "publisher2", "other", 102, LocalDate.now(), new Author("FirstName", "SecondName")),
                new Book(3L, "title3", "publisher3", "other", 103, LocalDate.now(), new Author("FirstName", "SecondName"))
        );

        int totalRecords = booksList.size();

        String searchByStr = "by_author";
        String searchContent = "title";

        doReturn(searchByStr).when(request).getParameter(Parameters.SEARCH_BY);
        doReturn(searchContent).when(request).getParameter(Parameters.SEARCH_CONTENT);
        doReturn(session).when(request).getSession();

        doReturn(String.valueOf(currPage)).when(request).getParameter(Parameters.GENERAL_CURR_PAGE);
        doReturn("InvalidOrderDir").when(request).getParameter(Parameters.ORDER_DIRECTION);
        doReturn(booksOrderTypes.toString()).when(request).getParameter(Parameters.ORDER_BY);
        doReturn(userDTO).when(session).getAttribute(UserParameters.USER_IN_SESSION);

        doReturn(booksList).when(bookService).findByAuthor(searchContent, 0, RECORDS_PER_PAGE, booksOrderTypes, orderDir, includeRemoved);
        doReturn(totalRecords).when(bookService).countFoundByAuthor(searchContent, includeRemoved);

        CommandResult result = searchBookCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(Pages.BOOKS_LIST);
        assertThat(result.getAction()).isEqualTo(CommandDirection.FORWARD);

        verify(request).setAttribute(Parameters.GENERAL_CURR_PAGE, currPage);
        verify(request).setAttribute(Parameters.ORDER_DIRECTION, orderDir.toString());
        verify(request).setAttribute(Parameters.ORDER_BY, booksOrderTypes.toString().toUpperCase());
        verify(request).setAttribute(eq(Parameters.GENERAL_TOTAL_PAGES), any());
        verify(request).setAttribute(eq(Parameters.BOOKS_LIST), any());
        verify(request).setAttribute(Parameters.BOOKS_PER_PAGE, RECORDS_PER_PAGE);
        verify(request).setAttribute(Parameters.SEARCH_CONTENT, searchContent);
        verify(request).setAttribute(Parameters.SEARCH_BY, searchByStr.toLowerCase());

        verify(bookService).countFoundByAuthor(searchContent, includeRemoved);
        verify(bookService).findByAuthor(searchContent, 0, RECORDS_PER_PAGE, booksOrderTypes, orderDir, includeRemoved);
        verify(bookService, times(3)).isRemoved(anyLong());
        verify(bookService, times(3)).getQuantity(anyLong());
    }

    @Test
    public void execute_InvalidOrderBy_ShouldUseDefaultOrderByValue() throws CommandException, ServiceException {
        UserDTO userDTO = mock(UserDTO.class);
        boolean includeRemoved = false;
        int currPage = 1;
        OrderDir orderDir = OrderDir.ASC;
        BooksOrderTypes booksOrderTypes = BooksOrderTypes.BY_TITLE;

        List<Book> booksList = List.of(
                new Book(1L, "title1", "publisher1", "other", 101, LocalDate.now(), new Author("FirstName", "SecondName")),
                new Book(2L, "title2", "publisher2", "other", 102, LocalDate.now(), new Author("FirstName", "SecondName")),
                new Book(3L, "title3", "publisher3", "other", 103, LocalDate.now(), new Author("FirstName", "SecondName"))
        );

        int totalRecords = booksList.size();

        String searchByStr = "by_author";
        String searchContent = "title";

        doReturn(searchByStr).when(request).getParameter(Parameters.SEARCH_BY);
        doReturn(searchContent).when(request).getParameter(Parameters.SEARCH_CONTENT);
        doReturn(session).when(request).getSession();

        doReturn(String.valueOf(currPage)).when(request).getParameter(Parameters.GENERAL_CURR_PAGE);
        doReturn(orderDir.toString()).when(request).getParameter(Parameters.ORDER_DIRECTION);
        doReturn("invalidOrderBy").when(request).getParameter(Parameters.ORDER_BY);
        doReturn(userDTO).when(session).getAttribute(UserParameters.USER_IN_SESSION);

        doReturn(booksList).when(bookService).findByAuthor(searchContent, 0, RECORDS_PER_PAGE, booksOrderTypes, orderDir, includeRemoved);
        doReturn(totalRecords).when(bookService).countFoundByAuthor(searchContent, includeRemoved);

        CommandResult result = searchBookCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(Pages.BOOKS_LIST);
        assertThat(result.getAction()).isEqualTo(CommandDirection.FORWARD);

        verify(request).setAttribute(Parameters.GENERAL_CURR_PAGE, currPage);
        verify(request).setAttribute(Parameters.ORDER_DIRECTION, orderDir.toString());
        verify(request).setAttribute(Parameters.ORDER_BY, booksOrderTypes.toString().toUpperCase());
        verify(request).setAttribute(eq(Parameters.GENERAL_TOTAL_PAGES), any());
        verify(request).setAttribute(eq(Parameters.BOOKS_LIST), any());
        verify(request).setAttribute(Parameters.BOOKS_PER_PAGE, RECORDS_PER_PAGE);
        verify(request).setAttribute(Parameters.SEARCH_CONTENT, searchContent);
        verify(request).setAttribute(Parameters.SEARCH_BY, searchByStr.toLowerCase());

        verify(bookService).countFoundByAuthor(searchContent, includeRemoved);
        verify(bookService).findByAuthor(searchContent, 0, RECORDS_PER_PAGE, booksOrderTypes, orderDir, includeRemoved);
        verify(bookService, times(3)).isRemoved(anyLong());
        verify(bookService, times(3)).getQuantity(anyLong());
    }

    @Test
    public void execute_ShouldIgnoreOrderTypeCase() throws CommandException, ServiceException {

        UserDTO userDTO = mock(UserDTO.class);
        boolean includeRemoved = false;
        int currPage = 1;
        OrderDir orderDir = OrderDir.ASC;
        String booksOrderTypes = "by_author";

        List<Book> booksList = List.of(
                new Book(1L, "title1", "publisher1", "other", 101, LocalDate.now(), new Author("FirstName", "SecondName")),
                new Book(2L, "title2", "publisher2", "other", 102, LocalDate.now(), new Author("FirstName", "SecondName")),
                new Book(3L, "title3", "publisher3", "other", 103, LocalDate.now(), new Author("FirstName", "SecondName"))
        );

        int totalRecords = booksList.size();

        String searchByStr = "by_author";
        String searchContent = "title";

        doReturn(searchByStr).when(request).getParameter(Parameters.SEARCH_BY);
        doReturn(searchContent).when(request).getParameter(Parameters.SEARCH_CONTENT);
        doReturn(session).when(request).getSession();

        doReturn(String.valueOf(currPage)).when(request).getParameter(Parameters.GENERAL_CURR_PAGE);
        doReturn(orderDir.toString()).when(request).getParameter(Parameters.ORDER_DIRECTION);
        doReturn(booksOrderTypes).when(request).getParameter(Parameters.ORDER_BY);
        doReturn(userDTO).when(session).getAttribute(UserParameters.USER_IN_SESSION);

        doReturn(booksList).when(bookService)
                .findByAuthor(
                        searchContent,
                        0,
                        RECORDS_PER_PAGE,
                        BooksOrderTypes.valueOf(booksOrderTypes.toUpperCase()),
                        orderDir,
                        includeRemoved
                );

        doReturn(totalRecords).when(bookService).countFoundByAuthor(searchContent, includeRemoved);

        CommandResult result = searchBookCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(Pages.BOOKS_LIST);
        assertThat(result.getAction()).isEqualTo(CommandDirection.FORWARD);

        verify(request).setAttribute(Parameters.GENERAL_CURR_PAGE, currPage);
        verify(request).setAttribute(Parameters.ORDER_DIRECTION, orderDir.toString());
        verify(request).setAttribute(Parameters.ORDER_BY, booksOrderTypes.toUpperCase());
        verify(request).setAttribute(eq(Parameters.GENERAL_TOTAL_PAGES), any());
        verify(request).setAttribute(eq(Parameters.BOOKS_LIST), any());
        verify(request).setAttribute(Parameters.BOOKS_PER_PAGE, RECORDS_PER_PAGE);
        verify(request).setAttribute(Parameters.SEARCH_CONTENT, searchContent);
        verify(request).setAttribute(Parameters.SEARCH_BY, searchByStr.toLowerCase());

        verify(bookService).countFoundByAuthor(searchContent, includeRemoved);
        verify(bookService).findByAuthor(searchContent, 0, RECORDS_PER_PAGE, BooksOrderTypes.valueOf(booksOrderTypes.toUpperCase()), orderDir, includeRemoved);
        verify(bookService, times(3)).isRemoved(anyLong());
        verify(bookService, times(3)).getQuantity(anyLong());

    }

    @Test
    void execute_OrderServiceThrowsServiceException_ShouldThrowCommandException() throws ServiceException {
        UserDTO userDTO = mock(UserDTO.class);
        boolean includeRemoved = false;
        int currPage = 1;
        OrderDir orderDir = OrderDir.ASC;
        String booksOrderTypes = "by_author";

        List<Book> booksList = List.of(
                new Book(1L, "title1", "publisher1", "other", 101, LocalDate.now(), new Author("FirstName", "SecondName")),
                new Book(2L, "title2", "publisher2", "other", 102, LocalDate.now(), new Author("FirstName", "SecondName")),
                new Book(3L, "title3", "publisher3", "other", 103, LocalDate.now(), new Author("FirstName", "SecondName"))
        );

        String searchByStr = "by_author";
        String searchContent = "title";

        doReturn(searchByStr).when(request).getParameter(Parameters.SEARCH_BY);
        doReturn(searchContent).when(request).getParameter(Parameters.SEARCH_CONTENT);
        doReturn(session).when(request).getSession();

        doReturn(String.valueOf(currPage)).when(request).getParameter(Parameters.GENERAL_CURR_PAGE);
        doReturn(orderDir.toString()).when(request).getParameter(Parameters.ORDER_DIRECTION);
        doReturn(booksOrderTypes).when(request).getParameter(Parameters.ORDER_BY);
        doReturn(userDTO).when(session).getAttribute(UserParameters.USER_IN_SESSION);

        doReturn(booksList).when(bookService)
                .findByAuthor(
                        searchContent,
                        0,
                        RECORDS_PER_PAGE,
                        BooksOrderTypes.valueOf(booksOrderTypes.toUpperCase()),
                        orderDir,
                        includeRemoved
                );

        doThrow(ServiceException.class).when(bookService).countFoundByAuthor(searchContent, includeRemoved);

        assertThatThrownBy(() -> searchBookCommand.execute(request))
                .isExactlyInstanceOf(CommandException.class)
                .hasCauseExactlyInstanceOf(ServiceException.class);

        verify(bookService).countFoundByAuthor(searchContent, includeRemoved);
        verify(bookService).findByAuthor(searchContent, 0, RECORDS_PER_PAGE, BooksOrderTypes.valueOf(booksOrderTypes.toUpperCase()), orderDir, includeRemoved);
    }
}