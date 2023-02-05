package com.my.library.controller.command.impl.librarian;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.dao.constants.OrderStatus;
import com.my.library.dto.OrderDTO;
import com.my.library.dto.UserDTO;
import com.my.library.dto.mapper.OrderMapper;
import com.my.library.dto.mapper.UserMapper;
import com.my.library.entities.Order;
import com.my.library.entities.User;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.services.OrderService;
import com.my.library.services.UserService;
import com.my.library.utils.IntegerParser;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class DisplayUsersRequestedOrdersCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final int RECORDS_PER_PAGE = 10;

    private final OrderService orderService;
    private final BookService bookService;
    private final UserService userService;

    public DisplayUsersRequestedOrdersCommand(OrderService orderService, BookService bookService, UserService userService) {
        this.orderService = orderService;
        this.bookService = bookService;
        this.userService = userService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        logger.log(Level.DEBUG, "DisplayUsersRequestedOrdersCommand invoked");
        HttpSession session = request.getSession();



        int currPage = 1;

        var reqCurrPage = request.getParameter(Parameters.GENERAL_CURR_PAGE);
        logger.log(Level.DEBUG, "DisplayUsersRequestedOrdersCommand/ current page: " + reqCurrPage);

        if (reqCurrPage != null) {
            var pageContainer = IntegerParser.parseInt(reqCurrPage);
            if (pageContainer.isPresent()) {
                currPage = pageContainer.get();
            }
        }
        session.setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.DISPLAY_USERS_REQUESTS_WITH_PARAMETERS.formatted(currPage));

        try {
            List<Order> orderList = orderService.findAllByStatus(
                    (currPage - 1) * RECORDS_PER_PAGE,
                    RECORDS_PER_PAGE,
                    OrderStatus.PROCESSING,
                    OrderStatus.REJECTED);

            int totalRecords = orderService.countOrdersByStatus(OrderStatus.PROCESSING, OrderStatus.REJECTED);

            logger.log(Level.DEBUG, "DisplayUsersRequestedOrdersCommand/ total orders: " + totalRecords);

            var totalPages = (int) Math.ceil((double) totalRecords / RECORDS_PER_PAGE);

            List<OrderDTO> orderDTOList = new OrderMapper(bookService, userService, orderService).toDTOList(orderList);

            request.setAttribute(Parameters.GENERAL_CURR_PAGE, currPage);
            request.setAttribute(Parameters.GENERAL_TOTAL_PAGES, totalPages);
            request.setAttribute(Parameters.ORDERS_LIST, orderDTOList);
            request.setAttribute(Parameters.ORDERS_PER_PAGE, RECORDS_PER_PAGE);

            return new CommandResult(Pages.USERS_REQUESTS, CommandDirection.FORWARD);
        } catch (ServiceException e) {
            throw new CommandException("Error while executing DisplayUsersRequestedOrdersCommand", e);
        }
    }
}
