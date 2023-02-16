package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.OrderDir;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.SearchBy;
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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SearchBookCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    private final static int RECORDS_PER_PAGE = 10;
    private final BookService bookService;


    public SearchBookCommand(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        new MessagesRemover().removeBookErrors(session);

        int currPage = 1;

        OrderDir orderDir = OrderDir.ASC;
        BooksOrderTypes orderBy = BooksOrderTypes.BY_TITLE;


        var searchByStr = request.getParameter(Parameters.SEARCH_BY);
        var searchContent = request.getParameter(Parameters.SEARCH_CONTENT);

        logger.log(Level.DEBUG, "SearchBookCommand invoked with parameters search_by: " + searchByStr + " search_content: " + searchContent);

        if (searchContent == null || searchContent.isEmpty()) {
            logger.log(Level.DEBUG, "Search content is null or empty");
            return new CommandResult(RedirectToPage.BOOKS_PAGE, CommandDirection.REDIRECT);
        }


        if (Arrays.stream(SearchBy.values()).noneMatch(orderType -> orderType.toString().equalsIgnoreCase(searchByStr))) {
            logger.log(Level.ERROR, "Error received illegal SEARCH_BY parameter");
            return new CommandResult(RedirectToPage.UNSUPPORTED_OPERATION, CommandDirection.REDIRECT);
        }

        SearchBy searchBy = SearchBy.valueOf(searchByStr.toUpperCase());



        String reqCurrPage = request.getParameter(Parameters.GENERAL_CURR_PAGE);
        String reqOrderDir = request.getParameter(Parameters.ORDER_DIRECTION);
        String reqOrderBy = request.getParameter(Parameters.ORDER_BY);

        Optional<Integer> currPageContainer = IntegerParser.parseInt(reqCurrPage);

        if (currPageContainer.isPresent()) {
            currPage = currPageContainer.get();
        }
        if ("ASC".equalsIgnoreCase(reqOrderDir) || "DESC".equalsIgnoreCase(reqOrderDir)) {
            orderDir = OrderDir.valueOf(reqOrderDir.toUpperCase());
        }
        if (Arrays.stream(BooksOrderTypes.values()).anyMatch(orderType -> orderType.toString().equalsIgnoreCase(reqOrderBy))) {
            orderBy = BooksOrderTypes.valueOf(reqOrderBy.toUpperCase());
        }

        UserDTO user = (UserDTO) session.getAttribute(UserParameters.USER_IN_SESSION);

        boolean includeRemoved = user != null && user.getRole() == UserRole.ADMIN;

        logger.log(Level.DEBUG, "Search with parameters: searchBy=" + searchByStr + " searchContent=" + searchContent);
        try {
            List<Book> booksList;
            int totalRecords;
            if (searchBy == SearchBy.BY_TITLE) {
                booksList = bookService.findByTitle(searchContent,
                        (currPage - 1) * RECORDS_PER_PAGE,
                        RECORDS_PER_PAGE, orderBy,
                        orderDir, includeRemoved
                );
                totalRecords = bookService.countFoundByTitle(searchContent, includeRemoved);
            } else {
                booksList = bookService.findByAuthor(searchContent,
                        (currPage - 1) * RECORDS_PER_PAGE,
                        RECORDS_PER_PAGE, orderBy,
                        orderDir, includeRemoved
                );
                totalRecords = bookService.countFoundByAuthor(searchContent, includeRemoved);
            }

            logger.log(Level.DEBUG, "Found books: " + booksList);
            logger.log(Level.DEBUG, "Found books count: " + totalRecords);

            List<BookDTO> bookDTOList = new BookMapper(bookService).toDTOList(booksList);

            int totalPages = (int) Math.ceil((double) totalRecords / RECORDS_PER_PAGE);

            request.setAttribute(Parameters.GENERAL_CURR_PAGE, currPage);
            request.setAttribute(Parameters.ORDER_DIRECTION, orderDir.toString());
            request.setAttribute(Parameters.ORDER_BY, orderBy.toString());
            request.setAttribute(Parameters.GENERAL_TOTAL_PAGES, totalPages);
            request.setAttribute(Parameters.BOOKS_LIST, bookDTOList);
            request.setAttribute(Parameters.BOOKS_PER_PAGE, RECORDS_PER_PAGE);
            request.setAttribute(Parameters.SEARCH_CONTENT, searchContent);
            request.setAttribute(Parameters.SEARCH_BY, searchBy.toString().toLowerCase());

        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        session.setAttribute(Parameters.PREVIOUS_PAGE,
                String.format(RedirectToPage.SEARCH_BOOK_WITH_PARAMETERS, searchBy.toString().toLowerCase(), searchContent, currPage, orderBy.toString().toLowerCase(), orderDir.toString().toLowerCase()));

        return new CommandResult(Pages.BOOKS_LIST, CommandDirection.FORWARD);
    }
}
