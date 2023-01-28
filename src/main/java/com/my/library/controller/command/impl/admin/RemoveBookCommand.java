package com.my.library.controller.command.impl.admin;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.services.ServiceFactory;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RemoveBookCommand implements Command {
    private final static Logger logger = LogManager.getLogger();

    private final BookService bookService = ServiceFactory.getBookService();

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        var reqBookId = request.getParameter(Parameters.BOOK_ID);

        logger.log(Level.DEBUG,"RemoveBookCommand: request book_id: " + reqBookId);

        Long bookId = null;
        if (reqBookId != null && !reqBookId.isBlank()) {
            bookId = Long.parseLong(reqBookId);
        }

        if (bookId == null) {
            logger.log(Level.DEBUG,"RemoveBookCommand: empty book_id: " + reqBookId);

            return new CommandResult(Pages.ERROR_PAGE, CommandDirection.REDIRECT);
        }
        try {
            bookService.deleteById(bookId);
        } catch (ServiceException e) {
            throw new CommandException("Error occurred while executing RemoveBookCommand!",e);
        }

        return new CommandResult(RedirectToPage.BOOKS_PAGE);
    }
}
