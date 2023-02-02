package com.my.library.controller.command.impl.admin;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.utils.LongParser;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RestoreBookCommand implements Command {

    private final static Logger logger = LogManager.getLogger();

    private final BookService bookService;

    public RestoreBookCommand(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        var reqBookId = request.getParameter(Parameters.BOOK_ID);

        logger.log(Level.DEBUG,"RestoreBookCommand: request book_id: " + reqBookId);


        var bookIdContainer = new LongParser().parseLong(reqBookId);

        if (bookIdContainer.isEmpty()) {
            logger.log(Level.DEBUG,"RestoreBookCommand: empty book_id: " + reqBookId);
            throw new CommandException("RestoreBookCommand book_id is empty! Failed to execute command!");
        }
        long bookId = bookIdContainer.get();
        try {
            bookService.restore(bookId);
        } catch (ServiceException e) {
            throw new CommandException("Error occurred while executing RestoreBookCommand!",e);
        }

        return new CommandResult(RedirectToPage.BOOKS_PAGE);
    }
}
