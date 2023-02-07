package com.my.library.controller.command.impl.user;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.dao.TransactionManager;
import com.my.library.exceptions.CommandException;
import com.my.library.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CancelOrderCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    private final OrderService orderService;
    private final TransactionManager transactionManager;

    public CancelOrderCommand(OrderService orderService, TransactionManager transactionManager) {
        this.orderService = orderService;
        this.transactionManager = transactionManager;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        return null;
    }
}
