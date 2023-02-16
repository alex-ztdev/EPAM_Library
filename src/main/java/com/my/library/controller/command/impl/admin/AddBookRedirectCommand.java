package com.my.library.controller.command.impl.admin;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.entities.Genre;
import com.my.library.entities.Publisher;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.GenreService;
import com.my.library.services.PublisherService;
import com.my.library.utils.Pages;
import com.my.library.utils.validator.MessagesRemover;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.List;

public class AddBookRedirectCommand implements Command {
    private final static Logger logger = LogManager.getLogger();
    private final static String TRUE = "true";
    private final GenreService genreService;
    private final PublisherService publisherService;

    public AddBookRedirectCommand(GenreService genreService, PublisherService publisherService) {
        this.genreService = genreService;
        this.publisherService = publisherService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {

        HttpSession session = request.getSession();
        request.setAttribute(Parameters.OPERATION_TYPE, Parameters.ADD_BOOK);
        session.setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.BOOKS_ADD_PAGE);

        if (TRUE.equals(request.getParameter(Parameters.ADD_NEW_BUTTON_PRESSED))) {
            var messagesRemover = new MessagesRemover();
            messagesRemover.removeBookErrors(session);
            messagesRemover.removeBook(session);
        }


        try {
            List<Genre> genresList = genreService.findAll();
            List<Publisher> publishersList = publisherService.findAll();
            publishersList = publishersList.stream()
                    .sorted(Comparator.comparing(Publisher::getTitle))
                    .toList();

            genresList = genresList.stream()
                    .sorted(Comparator.comparing(Genre::getTitle))
                    .toList();

            session.setAttribute(Parameters.GENRES_LIST, genresList);
            session.setAttribute(Parameters.PUBLISHERS_LIST, publishersList);

            return new CommandResult(Pages.BOOK_EDIT, CommandDirection.FORWARD);
        } catch (ServiceException e) {
            throw new CommandException("Error while executing AddBookRedirectCommand", e);
        }
    }
}

