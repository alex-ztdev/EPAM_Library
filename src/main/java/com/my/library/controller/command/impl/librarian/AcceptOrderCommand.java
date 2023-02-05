package com.my.library.controller.command.impl.librarian;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.exceptions.CommandException;
import com.my.library.utils.LongParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AcceptOrderCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        logger.log(Level.DEBUG, "AcceptOrderCommand invoked");

        var orderIdContainer = LongParser.parseLong(request.getParameter(Parameters.ORDER_ID));

        if (orderIdContainer.isEmpty()) {
            return new CommandResult(RedirectToPage.UNSUPPORTED_OPERATION, CommandDirection.REDIRECT);
        }


        return null;
    }
}
