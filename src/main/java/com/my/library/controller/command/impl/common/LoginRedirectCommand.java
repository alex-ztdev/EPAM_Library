package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.entities.User;
import com.my.library.exceptions.CommandException;
import com.my.library.utils.Pages;
import com.my.library.utils.validator.ValidationErrorsRemover;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class LoginRedirectCommand implements Command {

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();

        session.setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.LOGIN_PAGE);

        if (request.getParameter(Parameters.LOGIN_INVOCATION) != null) {
            new ValidationErrorsRemover().removeLoginErrors(session);
        }

        if (session.getAttribute(UserParameters.USER_IN_SESSION) != null) {
            return new CommandResult(RedirectToPage.HOME);
        }

        if (request.getParameter(UserParameters.REG_FORM) != null) {
            request.setAttribute(UserParameters.REG_FORM, UserParameters.REG_FORM);
        }
        return new CommandResult(Pages.LOGIN_PAGE);
    }

}
