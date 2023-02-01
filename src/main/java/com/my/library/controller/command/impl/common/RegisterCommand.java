package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.entities.User;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.ServiceFactory;
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

import static com.my.library.utils.validator.UserValidator.setParameters;

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

        //TODO: Make transition to registration page after language swap
        // (add regForm parameter to session than remove it when logged successfully,
        // probably wouldn't work, because of transition to other pages... )
        session.setAttribute(Parameters.PREVIOUS_PAGE, Pages.LOGIN_PAGE);


        CommandResult res;
        try {
            Optional<User> userOptional = new UserBuilder().buildNewUser(request);

            logger.log(Level.DEBUG, "User builder return: " + userOptional);

            if (userOptional.isPresent()) {
                var user = userOptional.get();
                List<String> validation = userService.canBeRegistered(user);

                logger.log(Level.DEBUG, "User validator return: " + validation);

                if (validation.isEmpty()) {
                    userService.save(user);
                    request.setAttribute(UserParameters.REG_FORM, "");
                    res = new CommandResult(Pages.LOGIN_PAGE, CommandDirection.REDIRECT);
                } else {
                    //TODO: Save all validation errors in session!
                    request.setAttribute(UserParameters.VALIDATION_LIST, validation);
                    setParameters(request, validation);
                    request.setAttribute(UserParameters.REG_FORM, UserParameters.REG_FORM);
                    res = new CommandResult(Pages.LOGIN_PAGE, CommandDirection.FORWARD);
                }
            }
            else {
                request.setAttribute(UserParameters.REG_FORM, UserParameters.REG_FORM);
                res = new CommandResult(Pages.LOGIN_PAGE, CommandDirection.REDIRECT); //TODO: redirect to forward?
            }

        } catch (ServiceException e) {
            throw new CommandException("Error while executing RegisterCommand.", e);
        }
        logger.log(Level.DEBUG, "Register command result" + res);
        return res;
    }
}
