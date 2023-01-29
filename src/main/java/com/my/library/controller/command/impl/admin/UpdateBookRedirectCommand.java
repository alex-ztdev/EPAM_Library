package com.my.library.controller.command.impl.admin;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.dto.BookMapper;
import com.my.library.entities.Book;
import com.my.library.entities.Genre;
import com.my.library.entities.Publisher;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.services.ServiceFactory;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.Optional;

public class UpdateBookRedirectCommand implements Command {
    private final static Logger logger = LogManager.getLogger();

    private final BookService bookService = ServiceFactory.getBookService();

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        var reqBookId = request.getParameter(Parameters.BOOK_ID);

        logger.log(Level.DEBUG,"UpdateBookCommand: request book_id: " + reqBookId);

        Long bookId = null;
        if (reqBookId != null && !reqBookId.isBlank()) {
            bookId = Long.parseLong(reqBookId);
        }

        if (bookId == null ) {
            logger.log(Level.DEBUG,"UpdateBookCommand: empty book_id: " + reqBookId);
            return new CommandResult(request.getContextPath() + Pages.ERROR_PAGE, CommandDirection.REDIRECT);
        }
        Optional<Book> book;
        try {
            book = bookService.find(bookId);

            boolean isPresentBook = book.isPresent();
            if (!isPresentBook) {
                logger.log(Level.DEBUG,"UpdateBookCommand: Book with such book_id: " + reqBookId + " doesn't found!");
                return new CommandResult(request.getContextPath() + Pages.ERROR_PAGE, CommandDirection.REDIRECT);
            }

            var bookDTO = new BookMapper().toDTO(book.get(), bookService.getQuantity(bookId), bookService.isRemoved(bookId));
            var genresList = ServiceFactory.getGenreService().findAll();
            var publishersList = ServiceFactory.getPublisherService().findAll();

            publishersList = publishersList.stream()
                    .filter(publisher -> !publisher.getTitle().equals(bookDTO.getPublisherTitle()))
                    .sorted(Comparator.comparing(Publisher::getTitle))
                    .toList();

            genresList = genresList.stream()
                    .filter(genre -> !genre.getTitle().equals(bookDTO.getGenre()))
                    .sorted(Comparator.comparing(Genre::getTitle))
                    .toList();

            //TODO: change to session scope?
            request.setAttribute(Parameters.BOOKS_DTO, bookDTO);
            request.setAttribute(Parameters.GENRES_LIST, genresList);
            request.setAttribute(Parameters.PUBLISHERS_LIST, publishersList);

            return new CommandResult(Pages.BOOK_EDIT);
        } catch (ServiceException e) {
            throw new CommandException("Error while finding book by id while executing UpdateBookRedirectCommand",e);
        }
    }
}
