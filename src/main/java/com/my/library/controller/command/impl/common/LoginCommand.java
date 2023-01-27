package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.UserParameters;
import com.my.library.dao.constants.UserStatus;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.ServiceFactory;
import com.my.library.services.UserService;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LoginCommand implements Command {
    private static final Logger logger = LogManager.getLogger();


    //TODO: Make case insensitive
    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        logger.log(Level.DEBUG, "login command invoked");
        String login = request.getParameter(UserParameters.LOGIN);
        String password = request.getParameter(UserParameters.PASSWORD);

        UserService userService = ServiceFactory.getUserService();
        HttpSession session = request.getSession();

        CommandResult res;
        try {
            var userContainer = userService.authenticate(login, password);

            if (userContainer.isEmpty()) {
                res = new CommandResult(Pages.LOGIN_PAGE);
                request.setAttribute(UserParameters.INVALID_LOGIN_PASSWORD, UserParameters.CONTENT_FROM_RESOURCES);
                logger.log(Level.INFO, "User: " + login + " logging failed");
            } else {
                var user = userContainer.get();
                if (user.getStatus() == UserStatus.BLOCKED) {
                    res = new CommandResult(Pages.LOGIN_PAGE);
                    request.setAttribute(UserParameters.USER_IS_BLOCKED, UserParameters.CONTENT_FROM_RESOURCES);
                    logger.log(Level.INFO, "User: " + login + " is blocked!");
                }
                //TODO: add error pages
                else {
                    res = new CommandResult(Pages.MAIN_PAGE, CommandDirection.REDIRECT);
                    session.setAttribute(UserParameters.USER_IN_SESSION, user);
                    logger.log(Level.INFO, "User: " + login + " logged successfully");
                }
            }
        } catch (ServiceException e) {
            throw new CommandException("Error while executing LoginCommand.", e);
        }
        return res;
    }
}
