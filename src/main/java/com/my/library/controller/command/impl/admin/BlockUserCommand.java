package com.my.library.controller.command.impl.admin;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.UserService;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlockUserCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final UserService userService;

    public BlockUserCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        logger.log(Level.DEBUG, "BlockUserCommand invoked");
        HttpSession session = request.getSession();

        var userIdStr = request.getParameter(Parameters.USER_ID);

        if (userIdStr == null || userIdStr.isBlank()) {
            logger.log(Level.DEBUG, "BlockUserCommand user_id is null or empty! Redirect to ");
            return new CommandResult(request.getContextPath() + Pages.ERROR_PAGE, CommandDirection.REDIRECT);
        }

        long userId = Long.parseLong(userIdStr);
        try {
            userService.blockUser(userId);
            return new CommandResult(RedirectToPage.DISPLAY_USERS, CommandDirection.REDIRECT);
        } catch (ServiceException e) {
            throw new CommandException("Error while executing BlockUserCommand", e);
        }
    }
}
