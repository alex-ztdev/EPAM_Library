package com.my.library.controller.command.impl.user;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.OrderParameters;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dao.TransactionManager;
import com.my.library.entities.Book;
import com.my.library.entities.Order;
import com.my.library.entities.User;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.services.OrderService;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.tags.shaded.org.apache.xpath.operations.Or;

import java.time.LocalDateTime;

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

        if (bookIdStr == null || onSubscriptionStr==null) {
            logger.log(Level.DEBUG, "OrderBookCommand: book_id is null: redirect to error page");
            return new CommandResult(Pages.ERROR_PAGE, CommandDirection.REDIRECT);
        }
        var bookId = Long.parseLong(bookIdStr);
        var onSubscription = Boolean.parseBoolean(onSubscriptionStr);

        var user = (User) session.getAttribute(UserParameters.USER_IN_SESSION);
        logger.log(Level.DEBUG, "OrderBookCommand: user_id: " + user);

        try {
            var orderToSave = new Order();
            orderToSave.setBookId(bookId);
            orderToSave.setUserId(user.getUserId());
            orderToSave.setOnSubscription(onSubscription);

            if (bookService.getQuantity(bookId) <= 0) {
                logger.log(Level.DEBUG, "OrderBookCommand: books quantity is 0 or less: redirect to error page");
                return new CommandResult(Pages.ERROR_PAGE, CommandDirection.REDIRECT);
            }
            orderService.save(orderToSave, bookService, transactionManager);

            return new CommandResult(RedirectToPage.MY_ORDERS_PAGE);
        } catch (ServiceException e) {
            throw new CommandException("Error while executing OrderBookCommand", e);
        }
    }
}
