package com.my.library.controller.command.impl.user;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.entities.Order;
import com.my.library.entities.User;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.services.OrderService;
import com.my.library.services.UserService;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class DisplayMyOrdersCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    //TODO: FIXME
    private static final int RECORDS_PER_PAGE = 2;
    private final BookService bookService;
    private final UserService userService;
    private final OrderService orderService;

    public DisplayMyOrdersCommand(BookService bookService, UserService userService, OrderService orderService) {
        this.bookService = bookService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();

        int currPage = 1;

        var reqCurrPage = request.getParameter(Parameters.GENERAL_CURR_PAGE);

        if (reqCurrPage != null && !reqCurrPage.isBlank()) {
            currPage = Integer.parseInt(reqCurrPage);
        }
        var user = (User) session.getAttribute(UserParameters.USER_IN_SESSION);

        if (user == null) {
            return new CommandResult(Pages.NOT_AUTHORIZED, CommandDirection.REDIRECT);
        }
        var userId = user.getUserId();

        try{
            List<Order> orderList = orderService.findAllUsersOrders(userId,
                    (currPage - 1) * RECORDS_PER_PAGE,
                    RECORDS_PER_PAGE);

        } catch (ServiceException e) {
            throw new CommandException("Error while executing DisplayMyOrdersCommand", e);
        }


        request.setAttribute(Parameters.GENERAL_CURR_PAGE, currPage);

//        request.setAttribute(Parameters.GENERAL_TOTAL_PAGES, totalPages);
//        request.setAttribute(Parameters.BOOKS_LIST, bookDTOList);
        request.setAttribute(Parameters.BOOKS_PER_PAGE, RECORDS_PER_PAGE);

        return new CommandResult(Pages.DISPLAY_ORDERS_PAGE, CommandDirection.FORWARD);
    }
}
