package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.entities.User;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.UserService;
import com.my.library.utils.Pages;
import com.my.library.utils.builder.UserBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;



public class RegisterCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final UserService userService;

    public RegisterCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        logger.log(Level.DEBUG, "Registration command invoked");

        HttpSession session = request.getSession();

        logger.log(Level.DEBUG, "RegisterCommand: set curr page: " + Pages.LOGIN_PAGE);

        session.setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.REGISTRATION_PAGE);

        CommandResult res;
        try {

            Optional<User> userOptional = new UserBuilder().buildNewUser(request);

            logger.log(Level.DEBUG, "RegisterCommand/User builder return: " + userOptional);

            if (userOptional.isPresent()) {
                session.setAttribute(UserParameters.REG_LOGIN_VAL, request.getParameter(UserParameters.REG_LOGIN));
                session.setAttribute(UserParameters.REG_EMAIL_VAL, request.getParameter(UserParameters.REG_EMAIL));
                session.setAttribute(UserParameters.REG_PHONE_VAL, request.getParameter(UserParameters.REG_PHONE));
                session.setAttribute(UserParameters.REG_FIRST_NAME_VAL, request.getParameter(UserParameters.REG_FIRST_NAME));
                session.setAttribute(UserParameters.REG_SECOND_NAME_VAL, request.getParameter(UserParameters.REG_SECOND_NAME));


                var user = userOptional.get();
                List<String> validation = userService.canBeRegistered(user);

                logger.log(Level.DEBUG, "RegisterCommand/User validator return: " + validation);

                if (validation.isEmpty()) {
                    user = userService.save(user);
                    logger.log(Level.DEBUG, "RegisterCommand/registration successful! User_id: " + user.getUserId());
                    res = new CommandResult(RedirectToPage.LOGIN_PAGE_WITH_SUCCESS, CommandDirection.REDIRECT);
                } else {
                    logger.log(Level.DEBUG, "RegisterCommand/unique check failed! Errors list: " + validation);
                    setParameters(session, validation);
                    res = new CommandResult(RedirectToPage.REGISTRATION_PAGE, CommandDirection.REDIRECT);
                }
            }
            else {
                logger.log(Level.DEBUG, "RegisterCommand/validation failed!");
                res = new CommandResult(RedirectToPage.REGISTRATION_PAGE, CommandDirection.REDIRECT);
            }

        } catch (ServiceException e) {
            throw new CommandException("Error while executing RegisterCommand.", e);
        }
        logger.log(Level.DEBUG, "Register command result" + res);
        return res;
    }
    private void setParameters(HttpSession session, List<String> validationList) {
        if (validationList.contains(UserParameters.USER_EMAIL_ALREADY_EXISTS)) {
            session.setAttribute(UserParameters.USER_EMAIL_ALREADY_EXISTS, UserParameters.USER_EMAIL_ALREADY_EXISTS);
        }
        if (validationList.contains(UserParameters.USER_LOGIN_ALREADY_EXISTS)) {
            session.setAttribute(UserParameters.USER_LOGIN_ALREADY_EXISTS, UserParameters.USER_LOGIN_ALREADY_EXISTS);
        }
        if (validationList.contains(UserParameters.USER_PHONE_ALREADY_EXISTS)) {
            session.setAttribute(UserParameters.USER_PHONE_ALREADY_EXISTS, UserParameters.USER_PHONE_ALREADY_EXISTS);
        }
    }
}
