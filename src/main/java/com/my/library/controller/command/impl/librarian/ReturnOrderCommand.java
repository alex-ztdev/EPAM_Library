package com.my.library.controller.command.impl.librarian;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.dao.TransactionManager;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.services.OrderService;
import com.my.library.services.UserService;
import com.my.library.utils.Pages;
import com.my.library.utils.LongParser;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReturnOrderCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    private final BookService bookService;
    private final OrderService orderService;
    private final TransactionManager transactionManager;

    public ReturnOrderCommand(BookService bookService, OrderService orderService, TransactionManager transactionManager) {
        this.bookService = bookService;
        this.orderService = orderService;
        this.transactionManager = transactionManager;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        logger.log(Level.DEBUG, "ReturnOrderCommand invoked");

        var orderIdStr = request.getParameter(Parameters.ORDER_ID);


        var orderIdContainer = LongParser.parseLong(orderIdStr);

        if (orderIdContainer.isEmpty()) {
            return new CommandResult(RedirectToPage.UNSUPPORTED_OPERATION, CommandDirection.REDIRECT);
        }
        var orderId = orderIdContainer.get();

        try{
            orderService.returnOrder(orderId, bookService, transactionManager);
            return new CommandResult(RedirectToPage.DISPLAY_USERS_ORDERS);
        } catch (ServiceException e) {
            throw new CommandException("Error while executing ReturnOrderCommand",e);
        }
    }
}
