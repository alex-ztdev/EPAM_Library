package com.my.library.controller.command.impl.user;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.dao.TransactionManager;
import com.my.library.exceptions.CommandException;
import com.my.library.services.AuthorService;
import com.my.library.services.BookService;
import com.my.library.services.UserService;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DisplayMyOrdersCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    private final BookService bookService;
    private final UserService userService;

    public DisplayMyOrdersCommand(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        return new CommandResult(Pages.DISPLAY_ORDERS_PAGE, CommandDirection.FORWARD);
    }
}
