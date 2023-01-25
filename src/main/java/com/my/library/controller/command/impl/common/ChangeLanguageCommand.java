package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.exceptions.CommandException;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;

//TODO: Make changeLanguageCommand redirect to the same page
public class ChangeLanguageCommand implements Command {
    private final static String LANGUAGE_PARAMETER = "language";

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        String language = request.getParameter(LANGUAGE_PARAMETER);
        request.getSession().setAttribute(LANGUAGE_PARAMETER, language);
        return new CommandResult(Pages.MAIN_PAGE, CommandDirection.REDIRECT);
    }
}
