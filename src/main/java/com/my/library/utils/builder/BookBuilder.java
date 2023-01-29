package com.my.library.utils.builder;

import com.my.library.controller.command.constant.parameters.BookParameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dao.constants.UserRole;
import com.my.library.dao.constants.UserStatus;
import com.my.library.entities.Book;
import com.my.library.entities.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class BookBuilder {

    public Optional<Book> buildNewBook(HttpServletRequest request) {

        String title = request.getParameter(BookParameters.TITLE);
        String firstName = request.getParameter(BookParameters.AUTHOR_FIRST_NAME);
        String secondName = request.getParameter(BookParameters.AUTHOR_SECOND_NAME);
        String publisher = request.getParameter(BookParameters.PUBLISHER);
        String copies = request.getParameter(BookParameters.COPIES);
        String pages = request.getParameter(BookParameters.PAGES);
        String publicationDate = request.getParameter(BookParameters.PUBLICATION_DATE);

        System.out.println(request.getParameter("book_id"));
        System.out.println(title);
        System.out.println(firstName);
        System.out.println(secondName);
        System.out.println(publisher);
        System.out.println(copies);
        System.out.println(pages);
        System.out.println(publicationDate);

//        if (true) {
//            return Optional.empty();
//        }
//        return Optional.of(new User(login, password, UserRole.USER, UserStatus.NORMAL, email, phoneNumber.isEmpty() ? null : phoneNumber, firstName, secondName));\
        return Optional.empty();
    }
}
