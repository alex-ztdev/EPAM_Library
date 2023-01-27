package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.BooksOrderDir;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.Parameters;
import com.my.library.dao.constants.BooksOrderTypes;
import com.my.library.entities.Book;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.services.ServiceFactory;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public class DisplayBooksListCommand implements Command {
    private static final int RECORDS_PER_PAGE = 10;

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        BookService bookService = ServiceFactory.getBookService();
        int currPage = 1;

        var reqCurrPage = request.getParameter(Parameters.BOOKS_LIST_CURR_PAGE);

        if (reqCurrPage != null && !reqCurrPage.isBlank()) {
            currPage = Integer.parseInt(reqCurrPage);
        }

        try {
            List<Book> booksList = bookService.findAll((currPage - 1) * RECORDS_PER_PAGE,
                    RECORDS_PER_PAGE,
                    BooksOrderTypes.BY_TITLE,
                    BooksOrderDir.ASC);
            request.setAttribute(Parameters.BOOKS_LIST, booksList);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }

        return new CommandResult(Pages.BOOKS_LIST, CommandDirection.FORWARD);
    }
}
