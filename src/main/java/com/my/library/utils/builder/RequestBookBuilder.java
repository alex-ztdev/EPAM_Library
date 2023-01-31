package com.my.library.utils.builder;

import com.my.library.controller.command.constant.parameters.BookParameters;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.entities.Author;
import com.my.library.entities.Book;
import com.my.library.utils.validator.BookValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.Optional;

public class RequestBookBuilder {

    private static final Logger logger = LogManager.getLogger();

    public Optional<Book> buildBookForUpdate(HttpServletRequest request) {

        Long bookId = Long.valueOf(request.getParameter(Parameters.BOOK_ID));
        int copies = Integer.parseInt(request.getParameter(BookParameters.COPIES));
        var bookContainer = buildBookForSave(request);

        bookContainer.ifPresent(book -> book.setBookId(bookId));

        logger.log(Level.INFO, "buildBookForUpdate was called. Received data: " + bookContainer + " copies: " + copies);

        return bookContainer;
    }

    public Optional<Book> buildBookForSave(HttpServletRequest request) {
        String title = request.getParameter(BookParameters.TITLE);
        String firstName = request.getParameter(BookParameters.AUTHOR_FIRST_NAME);
        String secondName = request.getParameter(BookParameters.AUTHOR_SECOND_NAME);
        String genre = request.getParameter(BookParameters.GENRE);
        String publisher = request.getParameter(BookParameters.PUBLISHER);
        int copies = Integer.parseInt(request.getParameter(BookParameters.COPIES));
        int pages = Integer.parseInt(request.getParameter(BookParameters.PAGES));
        LocalDate publicationDate = LocalDate.parse(request.getParameter(BookParameters.PUBLICATION_DATE));

        Book book = new Book(title, publisher, genre, pages, publicationDate, new Author(firstName, secondName));
        logger.log(Level.INFO, "buildBookForSave was called. Received data: " + book + " copies: " + copies);

        if (new BookValidator().validateBook(book)) {
            return Optional.empty();
        } else return Optional.of(book);
    }


}
