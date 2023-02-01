package com.my.library.controller.command.impl.admin;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.BookParameters;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.impl.common.LoginCommand;
import com.my.library.dao.TransactionManager;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.AuthorService;
import com.my.library.services.BookService;
import com.my.library.utils.builder.RequestBookBuilder;
import com.my.library.utils.validator.ValidationErrorsRemover;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UpdateBookCommand implements Command {
    private final static Logger logger = LogManager.getLogger();
    private final BookService bookService;
    private final AuthorService authorService;

    private final TransactionManager transactionManager;

    public UpdateBookCommand(BookService bookService, AuthorService authorService, TransactionManager transactionManager) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.transactionManager = transactionManager;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        var bookContainer = new RequestBookBuilder().buildBookForUpdate(request);
        int copies = Integer.parseInt(request.getParameter(BookParameters.COPIES));

        HttpSession session = request.getSession();
        var resPage = String.format(RedirectToPage.BOOKS_UPDATE_PAGE_WITH_PARAMETER, session.getAttribute(Parameters.BOOK_ID));

        session.setAttribute(Parameters.PREVIOUS_PAGE, resPage);
        new ValidationErrorsRemover().removeBookErrors(session);

        if (bookContainer.isEmpty()) {
            logger.log(Level.INFO, "UpdateBookCommand was called, but Book data is invalid");
            session.setAttribute(BookParameters.BOOK_INVALID_DATA, BookParameters.BOOK_INVALID_DATA);
        } else {
            var book = bookContainer.get();
            try {
                if (bookService.alreadyExists(book) && bookService.getQuantity(book.getBookId()) == copies) {
                    logger.log(Level.INFO, "UpdateBookCommand book_id:" + book.getBookId() + " book with such parameters already exists");
                    request.getSession().setAttribute(BookParameters.BOOK_ALREADY_EXISTS, BookParameters.BOOK_ALREADY_EXISTS);
                } else {
                    logger.log(Level.INFO, "UpdateBookCommand book_id:" + book.getBookId());
                    bookService.update(book, copies, authorService, transactionManager);

                    session.setAttribute(BookParameters.SUCCESSFULLY_UPDATED, BookParameters.SUCCESSFULLY_UPDATED);
                }

            } catch (ServiceException e) {
                throw new CommandException("Error while executing UpdateBookCommand", e);
            }
        }
        return new CommandResult(resPage, CommandDirection.REDIRECT);
    }


}
