package com.my.library.controller.command.impl.user;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.OrderParameters;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dao.TransactionManager;
import com.my.library.dao.constants.OrderStatus;
import com.my.library.entities.Order;
import com.my.library.entities.User;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.services.OrderService;
import com.my.library.utils.Pages;
import com.my.library.utils.LongParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OrderBookCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final OrderService orderService;
    private final BookService bookService;
    private final TransactionManager transactionManager;

    public OrderBookCommand(OrderService orderService, BookService bookService, TransactionManager transactionManager) {
        this.orderService = orderService;
        this.bookService = bookService;
        this.transactionManager = transactionManager;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();

        String bookIdStr = request.getParameter(Parameters.BOOK_ID);


        var onSubscriptionStr = request.getParameter(OrderParameters.SUBSCRIPTION_TYPE);

        logger.log(Level.DEBUG, "OrderBookCommand: book_id: " + bookIdStr);

        var bookIdContainer = LongParser.parseLong(bookIdStr);
        if (bookIdContainer.isEmpty() || (!"true".equalsIgnoreCase(onSubscriptionStr) && !"false".equalsIgnoreCase(onSubscriptionStr))) {
            logger.log(Level.DEBUG, "OrderBookCommand: book_id is null: redirect to error page");
            return new CommandResult(RedirectToPage.UNSUPPORTED_OPERATION, CommandDirection.REDIRECT);
        }
        long bookId = bookIdContainer.get();

        var onSubscription = Boolean.parseBoolean(onSubscriptionStr);

        var user = (User) session.getAttribute(UserParameters.USER_IN_SESSION);
        logger.log(Level.DEBUG, "OrderBookCommand: user_id: " + user);

        try {
            var orderToSave = new Order();
            orderToSave.setBookId(bookId);
            orderToSave.setUserId(user.getUserId());
            orderToSave.setOnSubscription(onSubscription);
            orderToSave.setOrderStatus(OrderStatus.PROCESSING);

            if (bookService.getQuantity(bookId) <= 0) {
                logger.log(Level.DEBUG, "OrderBookCommand: books quantity is 0 or less: redirect to error page");
                return new CommandResult(RedirectToPage.ERROR_PAGE, CommandDirection.REDIRECT);
            }
            orderService.save(orderToSave, bookService, transactionManager);

            return new CommandResult(RedirectToPage.MY_ORDERS_PAGE_WITH_SUCCESSFUL_MSG, CommandDirection.REDIRECT);
        } catch (ServiceException e) {
            throw new CommandException("Error while executing OrderBookCommand", e);
        }
    }
}
