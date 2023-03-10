package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.OrderDir;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.BookParameters;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dao.constants.BooksOrderTypes;
import com.my.library.dao.constants.UserRole;
import com.my.library.dto.BookDTO;
import com.my.library.dto.UserDTO;
import com.my.library.dto.mapper.BookMapper;
import com.my.library.entities.Book;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.utils.IntegerParser;
import com.my.library.utils.Pages;
import com.my.library.utils.validator.MessagesRemover;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.List;

public class DisplayBooksListCommand implements Command {
    private static final int RECORDS_PER_PAGE = 15;

    private final BookService bookService;

    public DisplayBooksListCommand(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        new MessagesRemover().removeBookErrors(session);
        new MessagesRemover().removeBook(session);

        int currPage = 1;

        OrderDir orderDir = OrderDir.ASC;
        BooksOrderTypes orderBy = BooksOrderTypes.BY_TITLE;

        var reqCurrPage = request.getParameter(Parameters.GENERAL_CURR_PAGE);
        var reqOrderDir = request.getParameter(Parameters.ORDER_DIRECTION);
        var reqOrderBy = request.getParameter(Parameters.ORDER_BY);

        var currPageContainer = IntegerParser.parseInt(reqCurrPage);
        if (currPageContainer.isPresent()) {
            currPage = currPageContainer.get();
        }
        if ("ASC".equalsIgnoreCase(reqOrderDir) || "DESC".equalsIgnoreCase(reqOrderDir)) {
            orderDir = OrderDir.valueOf(reqOrderDir.toUpperCase());
        }
        if (Arrays.stream(BooksOrderTypes.values()).anyMatch(orderType -> orderType.toString().equalsIgnoreCase(reqOrderBy))) {
            orderBy = BooksOrderTypes.valueOf(reqOrderBy.toUpperCase());
        }


        var user = (UserDTO) session.getAttribute(UserParameters.USER_IN_SESSION);

        boolean includeRemoved = user != null && user.getRole() == UserRole.ADMIN;

        try {
            List<Book> booksList = bookService.findAll(
                    (currPage - 1) * RECORDS_PER_PAGE,
                    RECORDS_PER_PAGE, orderBy,
                    orderDir, includeRemoved
            );

            List<BookDTO> bookDTOList = new BookMapper(bookService).toDTOList(booksList);

            int totalRecords = bookService.countBooks(includeRemoved);
            int totalPages = (int) Math.ceil((double) totalRecords / RECORDS_PER_PAGE);

            request.setAttribute(Parameters.GENERAL_CURR_PAGE, currPage);
            request.setAttribute(Parameters.ORDER_DIRECTION, orderDir.toString());
            request.setAttribute(Parameters.ORDER_BY, orderBy.toString());
            request.setAttribute(Parameters.GENERAL_TOTAL_PAGES, totalPages);
            request.setAttribute(Parameters.BOOKS_LIST, bookDTOList);
            request.setAttribute(Parameters.BOOKS_PER_PAGE, RECORDS_PER_PAGE);

        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        session.setAttribute(Parameters.PREVIOUS_PAGE,
                String.format(RedirectToPage.BOOKS_PAGE_WITH_PARAMETERS, orderBy, orderDir, currPage));

        return new CommandResult(Pages.BOOKS_LIST, CommandDirection.FORWARD);
    }

}
