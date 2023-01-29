package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.exceptions.CommandException;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;

//TODO: Make changeLanguageCommand redirect to the same page
public class ChangeLanguageCommand implements Command {
    private final static String LANGUAGE_PARAMETER = "language";

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        String language = request.getParameter(LANGUAGE_PARAMETER);
        var session = request.getSession();
        session.setAttribute(LANGUAGE_PARAMETER, language);

        var prev_page = (String)session.getAttribute(Parameters.PREVIOUS_PAGE);
        //TODO:implement default page?
//        session.removeAttribute(Parameters.PREVIOUS_PAGE);

        return new CommandResult(prev_page == null || prev_page.isBlank() ? Pages.MAIN_PAGE : prev_page, CommandDirection.REDIRECT);
    }
}
