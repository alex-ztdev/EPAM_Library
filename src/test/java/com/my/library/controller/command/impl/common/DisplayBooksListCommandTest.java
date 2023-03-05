package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.OrderDir;
import com.my.library.controller.command.constant.parameters.BookParameters;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DisplayBooksListCommandTest {

    private static final int RECORDS_PER_PAGE = 15;
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private BookService bookService;

    private DisplayBooksListCommand displayBooksListCommand;

    @BeforeEach
    public void setUp() {
        displayBooksListCommand = new DisplayBooksListCommand(bookService);
    }

    @Test
    public void execute_WithValidData_ShouldReturnCommandWithRedirectionToPreviousPage() throws CommandException, ServiceException {
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

        doReturn(session).when(request).getSession();
        doReturn(userDTO).when(session).getAttribute(UserParameters.USER_IN_SESSION);

        doReturn(booksList).when(bookService).findAll(0, RECORDS_PER_PAGE, booksOrderTypes, orderDir, includeRemoved);
        doReturn(totalRecords).when(bookService).countBooks(includeRemoved);

        doReturn(String.valueOf(currPage)).when(request).getParameter(Parameters.GENERAL_CURR_PAGE);
        doReturn(orderDir.toString()).when(request).getParameter(Parameters.ORDER_DIRECTION);
        doReturn(booksOrderTypes.toString()).when(request).getParameter(Parameters.ORDER_BY);

        CommandResult result = displayBooksListCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(Pages.BOOKS_LIST);
        assertThat(result.getAction()).isEqualTo(CommandDirection.FORWARD);

        verify(request).setAttribute(Parameters.GENERAL_CURR_PAGE, currPage);
        verify(request).setAttribute(Parameters.ORDER_DIRECTION, orderDir.toString());
        verify(request).setAttribute(Parameters.ORDER_BY, booksOrderTypes.toString());
        verify(request).setAttribute(Parameters.GENERAL_TOTAL_PAGES, 1);
        verify(request).setAttribute(eq(Parameters.BOOKS_LIST), any());
        verify(request).setAttribute(Parameters.BOOKS_PER_PAGE, RECORDS_PER_PAGE);
    }

    @Test
    public void execute_WithInvalidOrderId_ShouldReturnCommandResultRedirectToFirstPage() throws CommandException, ServiceException {
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

        doReturn(session).when(request).getSession();
        doReturn(userDTO).when(session).getAttribute(UserParameters.USER_IN_SESSION);

        doReturn(booksList).when(bookService).findAll(0, RECORDS_PER_PAGE, booksOrderTypes, orderDir, includeRemoved);
        doReturn(totalRecords).when(bookService).countBooks(includeRemoved);

        doReturn("NaN").when(request).getParameter(Parameters.GENERAL_CURR_PAGE);
        doReturn(orderDir.toString()).when(request).getParameter(Parameters.ORDER_DIRECTION);
        doReturn(booksOrderTypes.toString()).when(request).getParameter(Parameters.ORDER_BY);

        CommandResult result = displayBooksListCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(Pages.BOOKS_LIST);
        assertThat(result.getAction()).isEqualTo(CommandDirection.FORWARD);

        verify(request).setAttribute(Parameters.GENERAL_CURR_PAGE, currPage);
        verify(request).setAttribute(Parameters.ORDER_DIRECTION, orderDir.toString());
        verify(request).setAttribute(Parameters.ORDER_BY, booksOrderTypes.toString());
        verify(request).setAttribute(Parameters.GENERAL_TOTAL_PAGES, 1);
        verify(request).setAttribute(eq(Parameters.BOOKS_LIST), any());
        verify(request).setAttribute(Parameters.BOOKS_PER_PAGE, RECORDS_PER_PAGE);

    }


    @Test
    public void execute_WithInvalidOrderDir_ShouldReturnToDefaultDisplayBooksPage() throws CommandException, ServiceException {
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

        doReturn(session).when(request).getSession();
        doReturn(userDTO).when(session).getAttribute(UserParameters.USER_IN_SESSION);

        doReturn(booksList).when(bookService).findAll(0, RECORDS_PER_PAGE, booksOrderTypes, orderDir, includeRemoved);
        doReturn(totalRecords).when(bookService).countBooks(includeRemoved);

        doReturn(String.valueOf(currPage)).when(request).getParameter(Parameters.GENERAL_CURR_PAGE);
        doReturn("InvalidDir").when(request).getParameter(Parameters.ORDER_DIRECTION);
        doReturn(booksOrderTypes.toString()).when(request).getParameter(Parameters.ORDER_BY);

        CommandResult result = displayBooksListCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(Pages.BOOKS_LIST);
        assertThat(result.getAction()).isEqualTo(CommandDirection.FORWARD);

        verify(request).setAttribute(Parameters.GENERAL_CURR_PAGE, currPage);
        verify(request).setAttribute(Parameters.ORDER_DIRECTION, orderDir.toString());
        verify(request).setAttribute(Parameters.ORDER_BY, booksOrderTypes.toString());
        verify(request).setAttribute(Parameters.GENERAL_TOTAL_PAGES, 1);
        verify(request).setAttribute(eq(Parameters.BOOKS_LIST), any());
        verify(request).setAttribute(Parameters.BOOKS_PER_PAGE, RECORDS_PER_PAGE);

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
                new Book(3L, "title3", "publisher23", "other", 103, LocalDate.now(), new Author("FirstName", "SecondName"))
        );

        int totalRecords = booksList.size();

        doReturn(session).when(request).getSession();
        doReturn(userDTO).when(session).getAttribute(UserParameters.USER_IN_SESSION);

        doReturn(booksList).when(bookService).findAll(0, RECORDS_PER_PAGE, BooksOrderTypes.valueOf(booksOrderTypes.toUpperCase()), orderDir, includeRemoved);
        doReturn(totalRecords).when(bookService).countBooks(includeRemoved);

        doReturn(String.valueOf(currPage)).when(request).getParameter(Parameters.GENERAL_CURR_PAGE);
        doReturn(orderDir.toString()).when(request).getParameter(Parameters.ORDER_DIRECTION);
        doReturn(booksOrderTypes).when(request).getParameter(Parameters.ORDER_BY);

        CommandResult result = displayBooksListCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(Pages.BOOKS_LIST);
        assertThat(result.getAction()).isEqualTo(CommandDirection.FORWARD);

        verify(request).setAttribute(Parameters.GENERAL_CURR_PAGE, currPage);
        verify(request).setAttribute(Parameters.ORDER_DIRECTION, orderDir.toString());
        verify(request).setAttribute(Parameters.ORDER_BY, booksOrderTypes.toUpperCase());
        verify(request).setAttribute(Parameters.GENERAL_TOTAL_PAGES, 1);
        verify(request).setAttribute(eq(Parameters.BOOKS_LIST), any());
        verify(request).setAttribute(Parameters.BOOKS_PER_PAGE, RECORDS_PER_PAGE);

    }

    @Test
    public void execute_WithInvalidOrderBy_ShouldReturnToDefaultDisplayBooksPage() throws CommandException, ServiceException {
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

        doReturn(session).when(request).getSession();
        doReturn(userDTO).when(session).getAttribute(UserParameters.USER_IN_SESSION);

        doReturn(booksList).when(bookService).findAll(0, RECORDS_PER_PAGE, booksOrderTypes, orderDir, includeRemoved);

        doReturn(totalRecords).when(bookService).countBooks(includeRemoved);

        doReturn(String.valueOf(currPage)).when(request).getParameter(Parameters.GENERAL_CURR_PAGE);
        doReturn(orderDir.toString()).when(request).getParameter(Parameters.ORDER_DIRECTION);
        doReturn("InvalidOrderBy").when(request).getParameter(Parameters.ORDER_BY);

        CommandResult result = displayBooksListCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(Pages.BOOKS_LIST);
        assertThat(result.getAction()).isEqualTo(CommandDirection.FORWARD);

        verify(request).setAttribute(Parameters.GENERAL_CURR_PAGE, currPage);
        verify(request).setAttribute(Parameters.ORDER_DIRECTION, orderDir.toString());
        verify(request).setAttribute(Parameters.ORDER_BY, BooksOrderTypes.BY_TITLE.toString());
        verify(request).setAttribute(Parameters.GENERAL_TOTAL_PAGES, 1);
        verify(request).setAttribute(eq(Parameters.BOOKS_LIST), any());
        verify(request).setAttribute(Parameters.BOOKS_PER_PAGE, RECORDS_PER_PAGE);

    }

    @Test
    void execute_OrderServiceThrowsServiceException_ShouldThrowCommandException() throws ServiceException {
        UserDTO userDTO = mock(UserDTO.class);
        boolean includeRemoved = false;
        int currPage = 1;
        OrderDir orderDir = OrderDir.ASC;
        BooksOrderTypes booksOrderTypes = BooksOrderTypes.BY_AUTHOR;

        doReturn(session).when(request).getSession();
        doReturn(userDTO).when(session).getAttribute(UserParameters.USER_IN_SESSION);

        doThrow(ServiceException.class).when(bookService).findAll(0, RECORDS_PER_PAGE, booksOrderTypes, orderDir, includeRemoved);

        doReturn(String.valueOf(currPage)).when(request).getParameter(Parameters.GENERAL_CURR_PAGE);
        doReturn("InvalidDir").when(request).getParameter(Parameters.ORDER_DIRECTION);
        doReturn(booksOrderTypes.toString()).when(request).getParameter(Parameters.ORDER_BY);

        assertThatThrownBy(() -> displayBooksListCommand.execute(request))
                .isExactlyInstanceOf(CommandException.class)
                .hasCauseExactlyInstanceOf(ServiceException.class);
    }

    @Test
    void execute_ShouldRemoveBooksErrorMessages() throws CommandException {
        UserDTO userDTO = mock(UserDTO.class);


        doReturn(session).when(request).getSession();
        doReturn(userDTO).when(session).getAttribute(UserParameters.USER_IN_SESSION);


        displayBooksListCommand.execute(request);

        verify(session).removeAttribute(Parameters.BOOK_ID);
        verify(session).removeAttribute(Parameters.BOOKS_DTO);
        verify(session).removeAttribute(Parameters.GENRES_LIST);
        verify(session).removeAttribute(Parameters.PUBLISHERS_LIST);
        verify(session).removeAttribute(Parameters.OPERATION_TYPE);

        verify(session).removeAttribute(BookParameters.BOOK_INVALID_DATA);
        verify(session).removeAttribute(BookParameters.BOOK_ALREADY_EXISTS);
        verify(session).removeAttribute(BookParameters.SUCCESSFULLY_UPDATED);
        verify(session).removeAttribute(BookParameters.SUCCESSFULLY_ADDED);

        verify(session).removeAttribute(Parameters.UPDATE_BOOK);
        verify(session).removeAttribute(Parameters.ADD_BOOK);
    }
}