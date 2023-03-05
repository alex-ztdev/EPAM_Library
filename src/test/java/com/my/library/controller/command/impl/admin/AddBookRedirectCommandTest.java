package com.my.library.controller.command.impl.admin;

import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.BookParameters;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.entities.Genre;
import com.my.library.entities.Publisher;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.GenreService;
import com.my.library.services.PublisherService;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddBookRedirectCommandTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private GenreService genreService;

    @Mock
    private PublisherService publisherService;

    @InjectMocks
    private AddBookRedirectCommand addBookRedirectCommand;


    @Test
    @DisplayName("execute sets sorted genres and publishers")
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


        doReturn(session).when(request).getSession();

        doReturn(publishersList).when(publisherService).findAll();
        doReturn(genresList).when(genreService).findAll();

        CommandResult result = addBookRedirectCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(Pages.BOOK_EDIT);
        assertThat(result.getAction()).isEqualTo(CommandDirection.FORWARD);

        verify(request).setAttribute(Parameters.OPERATION_TYPE, Parameters.ADD_BOOK);
        verify(session).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.BOOKS_ADD_PAGE);


        genresList = genresList.stream()
                .sorted(Comparator.comparing(Genre::getTitle))
                .toList();

        publishersList = publishersList.stream()
                .sorted(Comparator.comparing(Publisher::getTitle))
                .toList();

        verify(session).setAttribute(Parameters.GENRES_LIST, genresList);
        verify(session).setAttribute(Parameters.PUBLISHERS_LIST, publishersList);
    }

    @Test
    @DisplayName("when add new book button pressed, should remove error messages")
    void execute_AddNewButtonPressed_ShouldRemoveErrors() throws ServiceException, CommandException {
        List<Publisher> publishersList = List.of();
        List<Genre> genresList = List.of();

        doReturn(session).when(request).getSession();
        doReturn("true").when(request).getParameter(Parameters.ADD_NEW_BUTTON_PRESSED);

        doReturn(publishersList).when(publisherService).findAll();
        doReturn(genresList).when(genreService).findAll();

        addBookRedirectCommand.execute(request);

        verify(request).setAttribute(Parameters.OPERATION_TYPE, Parameters.ADD_BOOK);
        verify(session).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.BOOKS_ADD_PAGE);

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
    @Test
    @DisplayName("service exception thrown")
    void execute_ServiceThrowsException_ShouldThrowCommandException() throws ServiceException {
        doReturn(session).when(request).getSession();
        doReturn("true").when(request).getParameter(Parameters.ADD_NEW_BUTTON_PRESSED);

        doThrow(ServiceException.class).when(publisherService).findAll();

        assertThatThrownBy(() -> addBookRedirectCommand.execute(request))
                .isExactlyInstanceOf(CommandException.class)
                .hasCauseExactlyInstanceOf(ServiceException.class);


        verify(request).setAttribute(Parameters.OPERATION_TYPE, Parameters.ADD_BOOK);
        verify(session).setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.BOOKS_ADD_PAGE);
    }

}