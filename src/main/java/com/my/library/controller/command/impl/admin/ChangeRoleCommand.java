package com.my.library.controller.command.impl.admin;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dao.constants.UserRole;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.UserService;
import com.my.library.utils.Pages;
import com.my.library.utils.LongParser;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class ChangeRoleCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final UserService userService;

    public ChangeRoleCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        logger.log(Level.DEBUG, "ChangeRoleCommand invoked");
        var newRoleStr = request.getParameter(UserParameters.ROLE);
        var userIdStr = request.getParameter(Parameters.USER_ID);


        var userIdContainer = LongParser.parseLong(userIdStr);
        if (userIdContainer.isEmpty()) {
            logger.log(Level.DEBUG, "user id is null or blank. Redirect to error page");
            return new CommandResult(RedirectToPage.UNSUPPORTED_OPERATION, CommandDirection.REDIRECT);
        }
        if (newRoleStr == null || Arrays.stream(UserRole.values()).map(Enum::toString).noneMatch(newRoleStr::equalsIgnoreCase)) {
            logger.log(Level.DEBUG, "Such role doesn't found. Redirect to error page");
            return new CommandResult(RedirectToPage.UNSUPPORTED_OPERATION, CommandDirection.REDIRECT);
        }

        UserRole newUserRole = UserRole.valueOf(newRoleStr.toUpperCase());

        long userId = userIdContainer.get();

        try {
            userService.setUserRole(userId, newUserRole);

//
//            return new CommandResult(RedirectToPage.DISPLAY_USERS, CommandDirection.REDIRECT);


            var prev_page = (String)request.getAttribute(Parameters.PREVIOUS_PAGE);

            return new CommandResult(prev_page == null || prev_page.isBlank() ? RedirectToPage.DISPLAY_USERS : prev_page, CommandDirection.REDIRECT);
        } catch (ServiceException e) {
            throw new CommandException("Error while executing ChangeRoleCommand", e);
        }
    }
}
