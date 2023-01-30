package com.my.library.controller.command.impl.admin;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.BookParameters;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.entities.User;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.AuthorService;
import com.my.library.services.BookService;
import com.my.library.utils.Pages;
import com.my.library.utils.builder.RequestBookBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UpdateBookCommand implements Command {
    private final static Logger logger = LogManager.getLogger();
    private final BookService bookService;
    private final AuthorService authorService;


    public UpdateBookCommand(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        var bookContainer = new RequestBookBuilder().buildBookForUpdate(request);

        HttpSession session = request.getSession();
        var resPage = String.format(RedirectToPage.BOOKS_EDIT_PAGE_WITH_PARAMETER, session.getAttribute(Parameters.BOOK_ID));

        if (bookContainer.isEmpty()) {
            logger.log(Level.INFO, "UpdateBookCommand was called, but Book data is invalid");
            session.setAttribute(BookParameters.BOOK_INVALID_DATA, BookParameters.BOOK_INVALID_DATA);
        } else {
            var book = bookContainer.get();
            try {
                //if there is no such author, then save it to DataBase
                if (authorService.findByNames(book.getAuthor().getFirstName(), book.getAuthor().getSecondName()).isEmpty()) {
                    logger.log(Level.INFO, "UpdateBookCommand was called for book_id: "+ book.getBookId() + " with new Author data:" + book.getAuthor());
                    authorService.save(book.getAuthor());
                }
                if (bookService.alreadyExists(book)) {
                    logger.log(Level.INFO, "UpdateBookCommand book_id:" + book.getBookId() + " book with such parameters already exists");
                    request.getSession().setAttribute(BookParameters.BOOK_ALREADY_EXISTS, BookParameters.BOOK_ALREADY_EXISTS);
                }
            } catch (ServiceException e) {
                throw new CommandException("Error while executing UpdateBookCommand",e);
            }
        }
        return new CommandResult(resPage, CommandDirection.REDIRECT);
    }
}
