package com.my.library.controller.command.impl.librarian;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.dao.constants.OrderStatus;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.services.OrderService;
import com.my.library.services.UserService;
import com.my.library.utils.LongParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AcceptOrderCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    private final OrderService orderService;

    public AcceptOrderCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        logger.log(Level.DEBUG, "AcceptOrderCommand invoked");
        HttpSession session = request.getSession();

        var orderIdContainer = LongParser.parseLong(request.getParameter(Parameters.ORDER_ID));

        if (orderIdContainer.isEmpty()) {
            return new CommandResult(RedirectToPage.UNSUPPORTED_OPERATION, CommandDirection.REDIRECT);
        }
        try {
            orderService.setOrderStatus(orderIdContainer.get(), OrderStatus.ACCEPTED);

            var prev_page = (String)session.getAttribute(Parameters.PREVIOUS_PAGE);

            return new CommandResult(prev_page == null || prev_page.isBlank() ? RedirectToPage.DISPLAY_USERS_REQUESTS : prev_page, CommandDirection.REDIRECT);

        } catch (ServiceException e) {
            throw new CommandException("Error in AcceptOrderCommand",e);
        }

    }
}
