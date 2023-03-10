package com.my.library.controller.command.impl.admin;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.UserService;
import com.my.library.utils.LongParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UnblockUserCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final UserService userService;

    public UnblockUserCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        logger.log(Level.DEBUG, "UnblockUserCommand invoked");
        HttpSession session = request.getSession();
        var userIdStr = request.getParameter(Parameters.USER_ID);

        var userIdContainer = LongParser.parseLong(userIdStr);
        if (userIdContainer.isEmpty()) {
            logger.log(Level.DEBUG, "UnblockUserCommand user_id is null or empty! Redirect to ");
            return new CommandResult(RedirectToPage.UNSUPPORTED_OPERATION, CommandDirection.REDIRECT);
        }
        long userId = userIdContainer.get();
        try {
            if (!userService.unblockUser(userId)) {
                return new CommandResult(RedirectToPage.UNSUPPORTED_OPERATION, CommandDirection.REDIRECT);
            }

            var prev_page = (String) session.getAttribute(Parameters.PREVIOUS_PAGE);

            return new CommandResult(prev_page == null || prev_page.isBlank() ? RedirectToPage.DISPLAY_USERS : prev_page, CommandDirection.REDIRECT);

        } catch (ServiceException e) {
            throw new CommandException("Error while executing UnblockUserCommand", e);
        }
    }
}
