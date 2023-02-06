package com.my.library.controller.command.impl.user;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.dto.mapper.BookMapper;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.utils.Pages;
import com.my.library.utils.LongParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OrderBookRedirectCommand implements Command {
    private final static Logger logger = LogManager.getLogger();
    private final BookService bookService;

    public OrderBookRedirectCommand(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        logger.log(Level.DEBUG, "OrderBookRedirectCommand invoked");

        var bookIdString = request.getParameter(Parameters.BOOK_ID);
        HttpSession session = request.getSession();

        var bookIdContainer = LongParser.parseLong(bookIdString);
        if (bookIdContainer.isEmpty()) {
            logger.log(Level.DEBUG, "book id is not a number! Redirect to error page");
            return new CommandResult(RedirectToPage.UNSUPPORTED_OPERATION, CommandDirection.REDIRECT);
        }
        long bookId = bookIdContainer.get();

        try {
            var bookContainer = bookService.find(bookId);

            if (bookContainer.isEmpty()) {
                return new CommandResult(RedirectToPage.UNSUPPORTED_OPERATION, CommandDirection.REDIRECT);
            }
            var book = bookContainer.get();

            var bookDTO = new BookMapper(bookService).toDTO(book, bookService.getQuantity(bookId), bookService.isRemoved(bookId));

            session.setAttribute(Parameters.BOOKS_DTO, bookDTO);

            return new CommandResult(Pages.ORDER_PAGE);
        } catch (ServiceException e) {
            throw new CommandException("Error occurred while executing OrderBookRedirect command", e);
        }
    }
}
