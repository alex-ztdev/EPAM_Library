package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.exceptions.CommandException;
import com.my.library.utils.Pages;
import com.my.library.utils.validator.MessagesRemover;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginRedirectCommand implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        logger.log(Level.DEBUG, "LoginRedirectCommand invoked");
        var messagesRemover = new MessagesRemover();
        session.setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.LOGIN_PAGE);

        if (request.getParameter(Parameters.LOGIN_INVOCATION) != null) {
            logger.log(Level.DEBUG, "LoginRedirectCommand/ removed login validation errors");
            messagesRemover.removeLoginErrors(session);
            messagesRemover.removeRegistrationErrors(session);
        }

        if (session.getAttribute(UserParameters.USER_IN_SESSION) != null) {
            logger.log(Level.DEBUG, "LoginRedirectCommand/ user already logged. Redirect to home page");
            return new CommandResult(RedirectToPage.HOME, CommandDirection.REDIRECT);
        }

        if (request.getParameter(Parameters.REG_INVOCATION) != null) {
            messagesRemover.removeRegistrationErrors(session);
            messagesRemover.removeLoginErrors(session);
        }

        if (request.getParameter(UserParameters.REG_FORM) != null) {
            session.setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.REGISTRATION_PAGE);
            logger.log(Level.DEBUG, "LoginRedirectCommand/ request to open registration form");
            request.setAttribute(UserParameters.REG_FORM, UserParameters.REG_FORM);
        }

        if (request.getParameter(Parameters.REG_SUCCESS_MSG) != null) {
            request.setAttribute(Parameters.REG_SUCCESS_MSG, Parameters.REG_SUCCESS_MSG);
            messagesRemover.removeRegistrationErrors(session);
        }
        return new CommandResult(Pages.LOGIN_PAGE);
    }
}
