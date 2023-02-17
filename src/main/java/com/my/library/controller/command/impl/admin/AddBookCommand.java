package com.my.library.controller.command.impl.admin;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.BookParameters;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.dao.TransactionManager;
import com.my.library.dto.mapper.BookMapper;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.AuthorService;
import com.my.library.services.BookService;
import com.my.library.utils.builder.RequestBookBuilder;
import com.my.library.utils.validator.MessagesRemover;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddBookCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    private final BookService bookService;
    private final AuthorService authorService;
    private final TransactionManager transactionManager;

    public AddBookCommand(BookService bookService, AuthorService authorService, TransactionManager transactionManager) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.transactionManager = transactionManager;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();

        var bookContainer = new RequestBookBuilder().buildBookForSave(request);


        new MessagesRemover().removeBookErrors(session);

        if (bookContainer.isEmpty()) {
            logger.log(Level.INFO, "AddBookCommand was called, but Book data is invalid");
            session.setAttribute(BookParameters.BOOK_INVALID_DATA, BookParameters.BOOK_INVALID_DATA);
        } else {

            int copies = Integer.parseInt(request.getParameter(BookParameters.COPIES));
            var book = bookContainer.get();
            var bookDTO = new BookMapper(bookService).toDTO(book, copies, false);
            session.setAttribute(Parameters.BOOKS_DTO, bookDTO);

            try {
                if (bookService.alreadyExists(book)) {
                    logger.log(Level.INFO, "AddBookCommand book_id:" + book.getBookId() + " book with such parameters already exists");
                    request.getSession().setAttribute(BookParameters.BOOK_ALREADY_EXISTS, BookParameters.BOOK_ALREADY_EXISTS);
                } else {
                    book = bookService.save(book, copies, authorService, transactionManager);

                    logger.log(Level.INFO, "AddBookCommand book_id:" + book.getBookId());

                    session.setAttribute(BookParameters.SUCCESSFULLY_ADDED, BookParameters.SUCCESSFULLY_ADDED);
                }

            } catch (ServiceException e) {
                throw new CommandException("Error while executing AddBookCommand", e);
            }
        }
        return new CommandResult(RedirectToPage.BOOKS_ADD_PAGE, CommandDirection.REDIRECT);
    }

}
