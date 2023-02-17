package com.my.library.utils.builder;

import com.my.library.controller.command.constant.parameters.BookParameters;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.entities.Author;
import com.my.library.entities.Book;
import com.my.library.utils.IntegerParser;
import com.my.library.utils.LocalDateParser;
import com.my.library.utils.LongParser;
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

        Optional<Long> bookIdContainer = LongParser.parseLong(request.getParameter(Parameters.BOOK_ID));
        Optional<Integer> copiesContainer = IntegerParser.parseInt(request.getParameter(BookParameters.COPIES));

        if (bookIdContainer.isEmpty() || copiesContainer.isEmpty()) {
            return Optional.empty();
        }
        var bookId = bookIdContainer.get();
        var copies = copiesContainer.get();

        if (bookId < 0 || copies < 0) {
            return Optional.empty();
        }

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

        Optional<Integer> copiesContainer = IntegerParser.parseInt(request.getParameter(BookParameters.COPIES));
        Optional<Integer> pagesContainer = IntegerParser.parseInt(request.getParameter(BookParameters.PAGES));
        Optional<LocalDate> publicationDateContainer = LocalDateParser.parseLocalDate(request.getParameter(BookParameters.PUBLICATION_DATE));

        if (copiesContainer.isEmpty() || pagesContainer.isEmpty() || publicationDateContainer.isEmpty()) {
            return Optional.empty();
        }

        int copies = copiesContainer.get();
        int pages = pagesContainer.get();

        LocalDate publicationDate = publicationDateContainer.get();

        Book book = new Book(title, publisher, genre, pages, publicationDate, new Author(firstName, secondName));
        logger.log(Level.INFO, "buildBookForSave was called. Received data: " + book + " copies: " + copies);

        if (new BookValidator().validateBook(book) && copies >= 0) {
            return Optional.of(book);
        } else {
            return Optional.empty();
        }
    }

}
