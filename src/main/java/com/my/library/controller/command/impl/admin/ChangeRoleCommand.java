package com.my.library.controller.command.impl.admin;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dao.constants.UserRole;
import com.my.library.dao.constants.UserStatus;
import com.my.library.entities.User;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.UserService;
import com.my.library.utils.LongParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Optional;

public class ChangeRoleCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final UserService userService;

    public ChangeRoleCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        logger.log(Level.DEBUG, "ChangeRoleCommand invoked");
        HttpSession session = request.getSession();

        var newRoleStr = request.getParameter(UserParameters.ROLE);
        var userIdStr = request.getParameter(Parameters.USER_ID);

        var userIdContainer = LongParser.parseLong(userIdStr);
        if (userIdContainer.isEmpty()) {
            logger.log(Level.DEBUG, "user id is invalid. Redirect to error page");
            return new CommandResult(RedirectToPage.UNSUPPORTED_OPERATION, CommandDirection.REDIRECT);
        }

        boolean isValidRole = Arrays.stream(UserRole.values())
                .filter(userRole -> userRole != UserRole.ADMIN)
                .anyMatch(userRole -> userRole.toString().equalsIgnoreCase(newRoleStr));

        if (!isValidRole) {
            logger.log(Level.DEBUG, "Such role doesn't found. Redirect to error page");
            return new CommandResult(RedirectToPage.UNSUPPORTED_OPERATION, CommandDirection.REDIRECT);
        }

        UserRole newUserRole = UserRole.valueOf(newRoleStr.toUpperCase());

        long userId = userIdContainer.get();

        try {
            Optional<User> userContainer = userService.find(userId);

            if (userContainer.isEmpty()) {
                logger.log(Level.DEBUG, "User with id: %s doesnt exist!".formatted(userId));
                return new CommandResult(RedirectToPage.UNSUPPORTED_OPERATION, CommandDirection.REDIRECT);
            }
            User user = userContainer.get();

            if (user.getRole() == UserRole.ADMIN ) {
                logger.log(Level.DEBUG, "Cannot change admin role! For user_id: %s".formatted(userId));
                return new CommandResult(RedirectToPage.UNSUPPORTED_OPERATION, CommandDirection.REDIRECT);
            }
            if (user.getStatus() == UserStatus.BLOCKED ) {
                logger.log(Level.DEBUG, "Cannot change user role, user is blocked! For user_id: %s".formatted(userId));
                return new CommandResult(RedirectToPage.UNSUPPORTED_OPERATION, CommandDirection.REDIRECT);
            }
            userService.setUserRole(userId, newUserRole);

            var prev_page = (String) session.getAttribute(Parameters.PREVIOUS_PAGE);

            return new CommandResult(prev_page == null || prev_page.isBlank() ? RedirectToPage.DISPLAY_USERS : prev_page, CommandDirection.REDIRECT);
        } catch (ServiceException e) {
            throw new CommandException("Error while executing ChangeRoleCommand", e);
        }
    }
}
