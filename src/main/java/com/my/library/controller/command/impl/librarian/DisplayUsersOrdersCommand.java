package com.my.library.controller.command.impl.librarian;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.dao.constants.OrderStatus;
import com.my.library.dto.OrderDTO;
import com.my.library.dto.mapper.OrderMapper;
import com.my.library.entities.Order;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.services.OrderService;
import com.my.library.services.UserService;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class DisplayUsersOrdersCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final int RECORDS_PER_PAGE = 8;
    private final BookService bookService;
    private final UserService userService;
    private final OrderService orderService;

    public DisplayUsersOrdersCommand(BookService bookService, UserService userService, OrderService orderService) {
        this.bookService = bookService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        logger.log(Level.DEBUG, "DisplayUsersOrdersCommand/ invoked");
        session.setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.DISPLAY_USERS_ORDERS);

        int currPage = 1;

        var reqCurrPage = request.getParameter(Parameters.GENERAL_CURR_PAGE);
        logger.log(Level.DEBUG, "DisplayUsersOrdersCommand/ current page: " + reqCurrPage);
        if (reqCurrPage != null && !reqCurrPage.isBlank()) {
            currPage = Integer.parseInt(reqCurrPage);
        }

        try {
            List<Order> orderList = orderService.findAllByStatus(
                    (currPage - 1) * RECORDS_PER_PAGE,
                    RECORDS_PER_PAGE,
                    OrderStatus.ACCEPTED
            );

            int totalRecords = orderService.countOrdersByStatus(OrderStatus.ACCEPTED);

            logger.log(Level.DEBUG, "DisplayUsersOrdersCommand/ total orders: " + totalRecords);

            var totalPages = (int) Math.ceil((double) totalRecords / RECORDS_PER_PAGE);

            List<OrderDTO> orderDTOList = new OrderMapper(bookService, userService, orderService).toDTOList(orderList);

            request.setAttribute(Parameters.GENERAL_CURR_PAGE, currPage);
            request.setAttribute(Parameters.GENERAL_TOTAL_PAGES, totalPages);
            request.setAttribute(Parameters.ORDERS_LIST, orderDTOList);
            request.setAttribute(Parameters.ORDERS_PER_PAGE, RECORDS_PER_PAGE);

            return new CommandResult(Pages.DISPLAY_ORDERS_PAGE, CommandDirection.FORWARD);
        } catch (ServiceException e) {
            throw new CommandException("Error while executing DisplayUsersOrdersCommand", e);
        }
    }
}
