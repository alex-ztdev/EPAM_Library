package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.*;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dao.constants.BooksOrderTypes;
import com.my.library.dao.constants.UserRole;
import com.my.library.dto.BookDTO;
import com.my.library.dto.BookMapper;
import com.my.library.entities.Book;
import com.my.library.entities.User;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.services.ServiceFactory;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public class DisplayBooksListCommand implements Command {
    private static final int RECORDS_PER_PAGE = 15;

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
        var user = (User) request.getSession().getAttribute(UserParameters.USER_IN_SESSION);

        boolean includeRemoved = user != null && user.getRole() == UserRole.ADMIN;

        try {
            List<Book> booksList = bookService.findAll(
                    (currPage - 1) * RECORDS_PER_PAGE,
                    RECORDS_PER_PAGE, orderBy,
                    orderDir, includeRemoved
            );

            List<BookDTO> bookDTOList = new BookMapper().getDTOList(booksList);

            int totalRecords = bookService.countBooks(includeRemoved);
            int totalPages = (int) Math.ceil((double) totalRecords / RECORDS_PER_PAGE);

            request.setAttribute(Parameters.BOOKS_LIST_CURR_PAGE, currPage);
            request.setAttribute(Parameters.ORDER_DIRECTION, orderDir.toString());
            request.setAttribute(Parameters.ORDER_BY, orderBy.toString());
            request.setAttribute(Parameters.BOOKS_TOTAL_PAGES, totalPages);
            request.setAttribute(Parameters.BOOKS_LIST, bookDTOList);
            request.setAttribute(Parameters.BOOKS_PER_PAGE, RECORDS_PER_PAGE);

        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        request.getSession().setAttribute(Parameters.PREVIOUS_PAGE,
                String.format(RedirectToPage.BOOKS_PAGE_WITH_PARAMETERS, orderBy, orderDir, currPage));

        return new CommandResult(Pages.BOOKS_LIST, CommandDirection.FORWARD);
    }
}
