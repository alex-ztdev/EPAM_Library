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
    private static final int RECORDS_PER_PAGE = 14;

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        BookService bookService = ServiceFactory.getBookService();
        int currPage = 1;

        BooksOrderDir orderDir = BooksOrderDir.ASC;
        BooksOrderTypes orderBy = BooksOrderTypes.BY_TITLE;

        var reqCurrPage = request.getParameter(Parameters.BOOKS_LIST_CURR_PAGE);
        var reqOrderDir = request.getParameter(Parameters.ORDER_DIRECTION);
        var reqOrderBy = request.getParameter(Parameters.ORDER_BY);

        if (reqCurrPage != null && !reqCurrPage.isBlank()) {
            currPage = Integer.parseInt(reqCurrPage);
        }
        if (reqOrderDir != null && !reqOrderDir.isBlank()) {
            orderDir = BooksOrderDir.valueOf(reqOrderDir.toUpperCase());
        }
        if (reqOrderBy != null && !reqOrderBy.isBlank()) {
            orderBy = BooksOrderTypes.valueOf(reqOrderBy.toUpperCase());
        }

        try {
            List<Book> booksList = bookService.findAll((currPage - 1) * RECORDS_PER_PAGE,
                    RECORDS_PER_PAGE, orderBy, orderDir
            );
            int totalRecords = bookService.countBooks();
            int totalPages = (int) Math.ceil((double) totalRecords / RECORDS_PER_PAGE);

            request.setAttribute(Parameters.BOOKS_LIST_CURR_PAGE, currPage);
            request.setAttribute(Parameters.ORDER_DIRECTION, orderDir.toString());
            request.setAttribute(Parameters.ORDER_BY, orderBy.toString());
            request.setAttribute(Parameters.BOOKS_TOTAL_PAGES, totalPages);
            request.setAttribute(Parameters.BOOKS_LIST, booksList);
            request.setAttribute(Parameters.BOOKS_PER_PAGE, RECORDS_PER_PAGE);

        } catch (ServiceException e) {
            throw new CommandException(e);
        }

        return new CommandResult(Pages.BOOKS_LIST, CommandDirection.FORWARD);
    }
}
