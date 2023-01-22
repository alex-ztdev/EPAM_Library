package com.my.library.controller.command.impl;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.UserConstants;
import com.my.library.dao.constants.UserStatus;
import com.my.library.dao.constants.columns.UsersColumns;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.UserService;
import com.my.library.services.impl.UserServiceImpl;
import com.my.library.utils.Pages;
import com.my.library.utils.builder.UserBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegisterCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
//        UserService userService = UserServiceImpl.getInstance();
//        HttpSession session = request.getSession();
//
//        CommandResult res;
//        try {
//            var userContainer = userService.authenticate(login, password);
//
//            if (userContainer.isEmpty()) {
//                res = new CommandResult(Pages.LOGIN_PAGE);
//                request.setAttribute(UserConstants.INVALID_LOGIN_PASSWORD, UserConstants.CONTENT_FROM_RESOURCES); // FIXME: bundle msg to property file
//                logger.log(Level.INFO, "User: " + login + " logging failed");
//            } else {
//                var user = userContainer.get();
//                if (user.getStatus() == UserStatus.BLOCKED) {
//                    res = new CommandResult(Pages.LOGIN_PAGE);
//                    request.setAttribute(UserConstants.USER_IS_BLOCKED, UserConstants.CONTENT_FROM_RESOURCES); // FIXME: bundle msg to property file
//                    logger.log(Level.INFO, "User: " + login + " is blocked!");
//                }
//                //TODO: add error pages
//                else {
//                    res = new CommandResult(Pages.MAIN_PAGE);
//                    session.setAttribute(UserConstants.USER_IN_SESSION, user);
//                    logger.log(Level.INFO, "User: " + login + " logged successfully");
//                }
//            }
//        } catch (ServiceException e) {
//            throw new CommandException("Error while executing LoginCommand.", e);
//        }
//        return res;
        return null;
    }
}
