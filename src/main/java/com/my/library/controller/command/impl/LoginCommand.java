package com.my.library.controller.command.impl;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.UserConstants;
import com.my.library.dao.constants.columns.UsersColumns;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.UserService;
import com.my.library.services.impl.UserServiceImpl;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LoginCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        String login = request.getParameter(UsersColumns.LOGIN);
        String password = request.getParameter(UsersColumns.PASSWORD);
        UserService userService = UserServiceImpl.getInstance();
        CommandResult res;
        try {
            if (userService.authenticate(login, password)) {
                res = new CommandResult(Pages.MAIN_PAGE);
                logger.log(Level.INFO, "User: " + login + " logged successfully");
            }
            //FIXME: add error pages
            else {
                res = new CommandResult(Pages.LOGIN_PAGE);
                request.setAttribute(UserConstants.INVALID_LOGIN_PASSWORD, "Logging failed!"); // FIXME: bundle msg to property file
                logger.log(Level.INFO, "User: " + login + " logging failed");
            }
        } catch (ServiceException e) {
            throw new CommandException("Error while executing LoginCommand.", e);
        }
        return res;
    }
}
