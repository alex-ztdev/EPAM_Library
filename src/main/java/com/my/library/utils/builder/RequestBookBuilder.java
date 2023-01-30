package com.my.library.utils.builder;

import com.my.library.controller.command.constant.parameters.BookParameters;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.entities.Author;
import com.my.library.entities.Book;
import com.my.library.utils.validator.BookValidator;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.Optional;

public class RequestBookBuilder {

    public Optional<Book> buildBookForUpdate(HttpServletRequest request) {

        Long bookId = Long.valueOf(request.getParameter(Parameters.BOOK_ID));
        String title = request.getParameter(BookParameters.TITLE);


        String firstName = request.getParameter(BookParameters.AUTHOR_FIRST_NAME);
        String secondName = request.getParameter(BookParameters.AUTHOR_SECOND_NAME);



        String genre = request.getParameter(BookParameters.GENRE);
        String publisher = request.getParameter(BookParameters.PUBLISHER);
        String copies = request.getParameter(BookParameters.COPIES);
        int pages = Integer.parseInt(request.getParameter(BookParameters.PAGES));
        LocalDate publicationDate = LocalDate.parse(request.getParameter(BookParameters.PUBLICATION_DATE));

        Book book = new Book(bookId, title, publisher, genre, pages, publicationDate, new Author(firstName, secondName));

        if (new BookValidator().validateBook(book)) {
            return Optional.empty();
        } else return Optional.of(book);

//        System.out.println();
//        System.out.println(title);
//        System.out.println(firstName);
//        System.out.println(secondName);
//        System.out.println(publisher);
//        System.out.println(copies);
//        System.out.println(pages);
//        System.out.println(publicationDate);
//
//


//        if (true) {
//            return Optional.empty();
//        }
//        return Optional.of(new User(login, password, UserRole.USER, UserStatus.NORMAL, email, phoneNumber.isEmpty() ? null : phoneNumber, firstName, secondName));\
//        return Optional.empty();
    }
}
