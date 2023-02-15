package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;

public class ChangeLanguageCommand implements Command {
    private final static String LANGUAGE_PARAMETER = "language";

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        String language = request.getParameter(LANGUAGE_PARAMETER);
        var session = request.getSession();
        session.setAttribute(LANGUAGE_PARAMETER, language);

        var prev_page = (String)session.getAttribute(Parameters.PREVIOUS_PAGE);

        return new CommandResult(prev_page == null || prev_page.isBlank() ? RedirectToPage.HOME : prev_page, CommandDirection.REDIRECT);
    }
}
