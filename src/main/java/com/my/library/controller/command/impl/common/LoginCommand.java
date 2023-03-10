package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dao.constants.UserStatus;
import com.my.library.dto.UserDTO;
import com.my.library.dto.mapper.UserMapper;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.UserService;
import com.my.library.utils.Pages;
import com.my.library.utils.validator.MessagesRemover;
import com.my.library.utils.validator.UserValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LoginCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final UserService userService;

    public LoginCommand(UserService userService) {
        this.userService = userService;
    }
    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        logger.log(Level.DEBUG, "LoginCommand invoked");
        String login = request.getParameter(UserParameters.LOGIN);
        String password = request.getParameter(UserParameters.PASSWORD);

        HttpSession session = request.getSession();
        session.setAttribute(UserParameters.LOGIN_VALUE, login);
        logger.log(Level.DEBUG, "LoginCommand: set curr page: " + Pages.LOGIN_PAGE);
        session.setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.LOGIN_PAGE);

        new MessagesRemover().removeLoginErrors(session);

        UserValidator userValidator = new UserValidator();
        if (!userValidator.isValidLogin(login) || !userValidator.isValidPassword(password)) {
            return new CommandResult(RedirectToPage.LOGIN_PAGE, CommandDirection.REDIRECT);
        }

        CommandResult res;
        try {
            var userContainer = userService.authenticate(login, password);

            if (userContainer.isEmpty()) {
                res = new CommandResult(RedirectToPage.LOGIN_PAGE, CommandDirection.REDIRECT);
                session.setAttribute(UserParameters.INVALID_LOGIN_PASSWORD, UserParameters.INVALID_LOGIN_PASSWORD);
                logger.log(Level.INFO, "User: " + login + " logging failed");
            } else {
                var user = userContainer.get();
                if (user.getStatus() == UserStatus.BLOCKED) {
                    res = new CommandResult(RedirectToPage.LOGIN_PAGE, CommandDirection.REDIRECT);
                    session.setAttribute(UserParameters.USER_IS_BLOCKED, UserParameters.USER_IS_BLOCKED);
                    logger.log(Level.INFO, "User: " + login + " is blocked!");
                } else {
                    res = new CommandResult(RedirectToPage.HOME, CommandDirection.REDIRECT);

                    UserDTO userDTO = new UserMapper().toDTO(user);
                    session.setAttribute(UserParameters.USER_IN_SESSION, userDTO);
                    logger.log(Level.INFO, "User: " + login + " logged successfully");
                }
            }
        } catch (ServiceException e) {
            throw new CommandException("Error while executing LoginCommand.", e);
        }
        return res;
    }


}
