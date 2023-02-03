package com.my.library.controller.command.impl.admin;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.UserService;
import com.my.library.utils.Pages;
import com.my.library.utils.LongParser;
import jakarta.servlet.http.HttpServletRequest;
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
        var userIdStr = request.getParameter(Parameters.USER_ID);

        var userIdContainer = new LongParser().parseLong(userIdStr);
        if (userIdContainer.isEmpty()) {
            logger.log(Level.DEBUG, "BlockUserCommand user_id is null or empty! Redirect to ");
            return new CommandResult(Pages.UNSUPPORTED_COMMAND, CommandDirection.REDIRECT);
        }
        long userId = userIdContainer.get();
        try {
            userService.blockUser(userId);

            var prev_page = (String)request.getAttribute(Parameters.PREVIOUS_PAGE);

            return new CommandResult(prev_page == null || prev_page.isBlank() ? RedirectToPage.DISPLAY_USERS : prev_page, CommandDirection.REDIRECT);

//            return new CommandResult(RedirectToPage.DISPLAY_USERS, CommandDirection.REDIRECT);
        } catch (ServiceException e) {
            throw new CommandException("Error while executing BlockUserCommand", e);
        }
    }
}
