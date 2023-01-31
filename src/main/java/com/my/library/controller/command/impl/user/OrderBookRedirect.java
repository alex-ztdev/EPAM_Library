package com.my.library.controller.command.impl.user;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.dto.BookMapper;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OrderBookRedirect implements Command {
    private final static Logger logger = LogManager.getLogger();
    private final BookService bookService;

    public OrderBookRedirect(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        var bookIdString = request.getParameter(Parameters.BOOK_ID);
        HttpSession session = request.getSession();

        if (bookIdString == null) {
            return new CommandResult(Pages.UNSUPPORTED_COMMAND, CommandDirection.REDIRECT);
        }
        var bookId = Long.parseLong(bookIdString);

        try {
            var bookContainer = bookService.find(bookId);

            if (bookContainer.isEmpty()) {
                return new CommandResult(Pages.UNSUPPORTED_COMMAND, CommandDirection.REDIRECT);
            }
            var book = bookContainer.get();

            var bookDTO = new BookMapper(bookService).toDTO(book, bookService.getQuantity(bookId), bookService.isRemoved(bookId));

            session.setAttribute(Parameters.BOOKS_DTO, bookDTO);

            return new CommandResult(Pages.ORDER_PAGE, CommandDirection.REDIRECT);
        } catch (ServiceException e) {
            throw new CommandException("Error occurred while executing OrderBookRedirect command", e);
        }
    }
}
